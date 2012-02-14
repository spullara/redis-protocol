package redis.server;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import redis.Command;
import redis.RedisProtocol;
import redis.reply.ErrorReply;
import redis.reply.MultiBulkReply;
import redis.reply.PSubscribeReply;
import redis.reply.PUnsubscribeReply;
import redis.reply.Reply;
import redis.reply.StatusReply;
import redis.reply.SubscribeReply;
import redis.reply.UnsubscribeReply;
import redis.util.BytesKey;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static mojava.Abbrev.n;

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

  private static Map<String, Method> commands = new HashMap<String, Method>();
  private static final byte[] MESSAGE = "message".getBytes();
  private static final byte[] PMESSAGE = "pmessage".getBytes();
  private static final byte[] SUBSCRIBE = "subscribe".getBytes();
  private static final byte[] PSUBSCRIBE = "psubscribe".getBytes();
  private static final byte[] UNSUBSCRIBE = "unsubscribe".getBytes();
  private static final byte[] PUNSUBSCRIBE = "unpsubscribe".getBytes();

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

  private static Map<String, Database> databases = new ConcurrentHashMap<String, Database>();

  private static void init() throws IllegalAccessException {
    for (Method method : Database.class.getMethods()) {
      commands.put(method.getName(), method);
    }
  }

  private static List<RedisListener> serverListeners = new ArrayList<RedisListener>();

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
      Method code = commands.get(verb);
      if (code == null) {
        return new ErrorReply("Command not implemented or invalid arguments: " + verb);
      }
      try {
        return (Reply) code.invoke(database, new Object[] { arguments });
      } catch (Throwable throwable) {
        logger.log(Level.SEVERE, "Failed", throwable);
        return new ErrorReply("Failed: " + throwable);
      }
    }

    public void run() {
      final Multimap<BytesKey, PublishListener> subs = HashMultimap.create();
      final Multimap<BytesKey, PublishListener> psubs = HashMultimap.create();
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
                  rp.write(Command.CRLF);
                } catch (IOException e) {
                  serverListeners.remove(this);
                }
              }
            };
            serverListeners.add(redisListener);
            while (true) {
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
              pubsub(rp, subs, psubs, execute);
            }
          }
        }
      } catch (IOException e) {
        logger.log(Level.WARNING, "Disconnected abnormally");
      } finally {
        for (PublishListener publishListener : subs.values()) {
          database.removePublishListener(publishListener);
        }
        for (PublishListener publishListener : psubs.values()) {
          database.removePublishListener(publishListener);
        }
        logger.info("Client disconnected");
        try {
          accept.close();
        } catch (IOException e1) {
          // ignore
        }
      }
    }

    private void pubsub(final RedisProtocol rp, Multimap<BytesKey, PublishListener> subs, Multimap<BytesKey, PublishListener> psubs, Reply execute) throws IOException {
      Command command;
      int subscriptions = 0;
      do {
        if (execute instanceof SubscribeReply) {
          if (execute instanceof PSubscribeReply) {
            PSubscribeReply sr = (PSubscribeReply) execute;
            for (byte[] bytes : sr.getPatterns()) {
              final Pattern match = Pattern.compile(Database.makeRegex(bytes));
              PublishListener pl = new PublishListener() {
                public boolean publish(byte[] target, byte[] message) {
                  if (match.matcher(Database.makeAscii(target)).matches()) {
                    send(target, message, rp);
                    return true;
                  }
                  return false;
                }
              };
              database.addPublishListener(pl);
              BytesKey key = new BytesKey(bytes);
              subs.put(key, pl);
              Object[] r = new Object[3];
              r[0] = PSUBSCRIBE;
              r[1] = bytes;
              r[2] = subscriptions = subs.size() + psubs.size();
              rp.send(new MultiBulkReply(r));
            }
          } else {
            SubscribeReply sr = (SubscribeReply) execute;
            for (byte[] bytes : sr.getPatterns()) {
              final BytesKey match = new BytesKey(bytes);
              PublishListener pl = new PublishListener() {
                public boolean publish(byte[] target, byte[] message) {
                  if (match.equals(new BytesKey(target))) {
                    send(target, message, rp);
                    return true;
                  }
                  return false;
                }
              };
              database.addPublishListener(pl);
              subs.put(match, pl);
              Object[] r = new Object[3];
              r[0] = SUBSCRIBE;
              r[1] = bytes;
              r[2] = subscriptions = subs.size() + psubs.size();
              rp.send(new MultiBulkReply(r));
            }
          }
        } else if (execute instanceof UnsubscribeReply) {
          if (execute instanceof PUnsubscribeReply) {
            PUnsubscribeReply pr = (PUnsubscribeReply) execute;
            for (byte[] pattern : getPatterns(psubs, pr)) {
              BytesKey key = new BytesKey(pattern);
              for (PublishListener pl : n(psubs.get(key))) {
                database.removePublishListener(pl);
              }
              psubs.removeAll(key);
              Object[] r = new Object[3];
              r[0] = PUNSUBSCRIBE;
              r[1] = pattern;
              r[2] = subscriptions = subs.size() + psubs.size();
              rp.send(new MultiBulkReply(r));
            }
          } else {
            UnsubscribeReply pr = (UnsubscribeReply) execute;
            for (byte[] pattern : getPatterns(psubs, pr)) {
              BytesKey key = new BytesKey(pattern);
              for (PublishListener pl : n(subs.get(key))) {
                database.removePublishListener(pl);
              }
              subs.removeAll(key);
              Object[] r = new Object[3];
              r[0] = UNSUBSCRIBE;
              r[1] = pattern;
              r[2] = subscriptions = subs.size() + psubs.size();
              rp.send(new MultiBulkReply(r));
            }
          }
        } else {
          // This is a non-subscription command
          if (subscriptions > 0) {
            // And you are subscribed
            rp.send(new ErrorReply("Invalid command while subscribed"));
          }
          break;
        }
        if (subscriptions == 0) {
          break;
        }
        command = rp.receive();
        if (command == null) {
          break;
        }
        execute = execute(command);
      } while (true);
    }

    private byte[][] getPatterns(Multimap<BytesKey, PublishListener> psubs, UnsubscribeReply pr) {
      byte[][] patterns = pr.getPatterns();
      if (patterns == null) {
        Set<BytesKey> bytesKeys = psubs.keySet();
        patterns = new byte[bytesKeys.size()][];
        int i = 0;
        for (BytesKey bytesKey : bytesKeys) {
          patterns[i++] = bytesKey.getBytes();
        }
      }
      return patterns;
    }

    private void send(byte[] target, byte[] message, RedisProtocol rp) {
      Object[] r = new Object[3];
      r[0] = MESSAGE;
      r[1] = target;
      r[2] = message;
      try {
        rp.send(new MultiBulkReply(r));
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Failed to send message", e);
      }
    }
  }
}
