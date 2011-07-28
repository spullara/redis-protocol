package redis;

import com.google.common.base.Charsets;
import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
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

  private static Map<String, Object> map = new ConcurrentHashMap<>();
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
      es.execute(new Runnable() {
        public void run() {
          try {
            RedisProtocol rp = new RedisProtocol(accept);
            while (true) {
              Command command = rp.receive();
              Reply execute = execute(command);
              if (execute == null) {
                break;
              } else {
                rp.send(execute);
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
      });
      logger.info("Client connected");
    }
  }

  interface Code {
    Reply call(byte[][] arguments);
  }

  private static Reply execute(Command command) {
    byte[][] arguments = command.getArguments();
    String verb = new String(arguments[0], Charsets.UTF_8).toLowerCase();
    MethodHandle code = commands.get(verb);
    if (code == null) {
      return new Reply.ErrorReply("Command not implemented or invalid arguments: " + verb);
    }
    try {
      switch(arguments.length) {
        case 1:
          return (Reply) code.invoke(Commands.O);
        case 2:
          return (Reply) code.invoke(Commands.O, arguments[1]);
        case 3:
          return (Reply) code.invoke(Commands.O, arguments[1], arguments[2]);
        default:
          return new Reply.ErrorReply("Invalid number of arguments: " + arguments.length);
      }
    } catch (Throwable throwable) {
      logger.log(Level.SEVERE, "Failed", throwable);
      return new Reply.ErrorReply("Failed: " + throwable);
    }
  }

  public static class Commands {
    private Commands() {}
    private final static Commands O = new Commands();
    private final Reply.StatusReply OK = new Reply.StatusReply("OK");

    public Reply append(byte[] k, byte[] s) {
      synchronized (Commands.class) {
        String key = $(k);
        Object v = map.get(key);
        if (v instanceof byte[]) {
          byte[] b = (byte[]) v;
          byte[] bytes = new byte[s.length + b.length];
          System.arraycopy(b, 0, bytes, 0, b.length);
          System.arraycopy(s, 0, bytes, b.length, s.length);
          map.put(key, bytes);
          return OK;
        } else if (v == null) {
          map.put(key, s);
          return OK;
        } else {
          return new Reply.ErrorReply("Invalid value type: " + v.getClass().getName());
        }
      }
    }

    public Reply set(byte[] k, byte[] v) {
      String key = $(k);
      map.put(key, v);
      return OK;
    }
    public Reply get(byte[] k) {
      String key = $(k);
      Object value = map.get(key);
      if (value instanceof byte[] || value == null) {
        return new Reply.BulkReply((byte[]) value);
      } else {
        return new Reply.ErrorReply("Invalid value type: " + value.getClass().getName());
      }
    }
  }

  private static void init() throws IllegalAccessException {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    for (Method method : Commands.class.getMethods()) {
      commands.put(method.getName(), lookup.unreflect(method));
    }
  }

  private static String $(byte[] k) {
    return new String(k, Charsets.UTF_8);
  }

}
