package redis;

import com.google.common.base.Charsets;
import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import redis.reply.ErrorReply;
import redis.reply.Reply;
import redis.reply.StatusReply;
import redis.util.BytesKey;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Redis server
 * User: sam
 * Date: 7/28/11
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class RedisServer {

  public static Logger logger = Logger.getLogger("RedisServer");

  @Argument(alias = "p")
  private static Integer port = 6379;

  @Argument(alias = "pw")
  private static String password;

  protected static String auth;

  private static Map<String, MethodHandle> commands = new HashMap<>();

  public static void main(String[] args) throws IOException, IllegalAccessException {
    try {
      Args.parse(RedisServer.class, args);
    } catch (IllegalArgumentException e) {
      Args.usage(RedisServer.class);
      System.exit(1);
    }
    init();

    ExecutorService es = Executors.newCachedThreadPool();
    ServerSocket ss = new ServerSocket(port);
    logger.info("Listening");
    while (true) {
      final Socket accept = ss.accept();
      accept.setTcpNoDelay(true);
      accept.setKeepAlive(true);
      es.execute(new ServerConnection(accept));
      logger.info("Client connected");
    }
  }

  interface Code {
    Reply call(byte[][] arguments);
  }

  private static Map<String, Database> databases = new ConcurrentHashMap<>();

  private static void init() throws IllegalAccessException {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    for (Method method : Database.class.getMethods()) {
      commands.put(method.getName(), lookup.unreflect(method));
    }
  }

  private static List<RedisListener> serverListeners = new ArrayList<>();

  private static class ServerConnection implements Runnable {
    private static final byte[] SELECT = "select".getBytes();
    private static final byte[] QUIT = "quit".getBytes();
    private static final byte[] MONITOR = "monitor".getBytes();
    private final Socket accept;
    private Database database;

    public ServerConnection(Socket accept) {
      this.accept = accept;
      database = databases.get("0");
      if (database == null) {
        databases.put("0", database = new Database());
      }
    }

    private Reply execute(Command command) {
      byte[][] arguments = command.getArguments();
      String verb = new String(arguments[0], Charsets.UTF_8).toLowerCase();
      if (!"auth".equals(verb)) {
        if (password != null && !password.equals(auth)) {
          return new ErrorReply("Not authenticated");
        }
      }
      MethodHandle code = commands.get(verb);
      if (code == null) {
        return new ErrorReply("Command not implemented or invalid arguments: " + verb);
      }
      try {
        return (Reply) code.invoke(database, arguments);
      } catch (Throwable throwable) {
        logger.log(Level.SEVERE, "Failed", throwable);
        return new ErrorReply("Failed: " + throwable);
      }
    }

    public void run() {
      try {
        final RedisProtocol rp = new RedisProtocol(accept);
        REDIS:
        while (true) {
          Command command = rp.receive();
          if (command == null) {
            break;
          }
          byte[][] arguments = command.getArguments();
          if (BytesKey.equals(SELECT, arguments[0])) {
            String name = new String(arguments[1], Charsets.UTF_8);
            database = databases.get(name);
            if (database == null) {
              databases.put(name, database = new Database());
            }
            rp.send(new StatusReply("OK"));
          } else if (BytesKey.equals(MONITOR, arguments[0])) {
            rp.send(new StatusReply("OK"));
            RedisListener redisListener = new RedisListener() {
              public void received(Command command) {
                try {
                  boolean first = true;
                  for (byte[] bytes : command.getArguments()) {
                    if (first) {
                      first = false;
                    } else {
                      rp.write(" ".getBytes());
                    }
                    rp.write(bytes);
                  }
                  rp.write("\r\n".getBytes());
                } catch (IOException e) {
                  serverListeners.remove(this);
                }
              }
            };
            serverListeners.add(redisListener);
            while(true) {
              Command quit = rp.receive();
              if (BytesKey.equals(QUIT, quit.getArguments()[0])) {
                serverListeners.remove(redisListener);
                break REDIS;
              }
            }
          } else {
            if (serverListeners.size() > 0) {
              for (RedisListener serverListener : serverListeners) {
                synchronized (serverListeners) {
                  serverListener.received(command);
                }
              }
            }
            Reply execute = execute(command);
            if (execute == null) {
              break;
            } else {
              rp.send(execute);
            }
          }
        }
      } catch (IOException e) {
        logger.log(Level.WARNING, "Disconnected abnormally");
      } finally {
        logger.info("Client disconnected");
        try {
          accept.close();
        } catch (IOException e1) {
          // ignore
        }
      }
    }
  }
}
