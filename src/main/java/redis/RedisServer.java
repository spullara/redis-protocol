package redis;

import com.google.common.base.Charsets;
import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import mojava.F;

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

  private static String auth;

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
    if (!"auth".equals(verb)) {
      if (password != null && !password.equals(auth)) {
        return new Reply.ErrorReply("Not authenticated");
      }
    }
    MethodHandle code = commands.get(verb);
    if (code == null) {
      return new Reply.ErrorReply("Command not implemented or invalid arguments: " + verb);
    }
    try {
      return (Reply) code.invoke(Commands.O, arguments);
    } catch (Throwable throwable) {
      logger.log(Level.SEVERE, "Failed", throwable);
      return new Reply.ErrorReply("Failed: " + throwable);
    }
  }

  public static class Commands {

    private static final Reply.BulkReply EMPTY = new Reply.BulkReply(new byte[0]);

    private Commands() {
    }

    private final static Commands O = new Commands();
    private final Reply.StatusReply OK = new Reply.StatusReply("OK");

    public Reply append(byte[][] a) {
      synchronized (Commands.class) {
        String key = $(a[1]);
        Object v = map.get(key);
        final byte[] s = a[2];
        if (v instanceof byte[]) {
          byte[] b = (byte[]) v;
          byte[] bytes = new byte[s.length + b.length];
          System.arraycopy(b, 0, bytes, 0, b.length);
          System.arraycopy(s, 0, bytes, b.length, s.length);
          put(key, bytes);
          return OK;
        } else if (v == null) {
          put(key, s);
          return OK;
        } else {
          return typeerr();
        }
      }
    }

    public Reply auth(byte[][] a) {
      if (a.length < 2) return argerr();
      auth = $(a[1]);
      return OK;
    }

    // BGREWRITEAOF
    // BGSAVE

    public Reply blpop(byte[][] a) {
      return pop(a, new F<List, Object>() {
        public Object apply(List input) {
          return input.remove(0);
        }
      });
    }

    public Reply brpop(byte[][] a) {
      return pop(a, new F<List, Object>() {
        public Object apply(List input) {
          return input.remove(input.size() - 1);
        }
      });
    }

    public Reply brpoppush(byte[][] a) {
      if (a.length != 4) argerr();
      byte[][] popArguments = new byte[3][];
      popArguments[0] = a[0];
      popArguments[1] = a[1];
      popArguments[2] = a[3];
      Reply brpop = brpop(popArguments);
      byte[][] pushArguments = new byte[3][];
      pushArguments[0] = a[0];
      pushArguments[1] = a[2];
      pushArguments[2] = ((Reply.MultiBulkReply) brpop).byteArrays[1];
      return lpush(pushArguments);
    }

    // CONFIG GET
    // CONFIG SET
    // CONFIG RESETSTAT

    public Reply dbsize(byte[][] a) {
      if (a.length != 1) return argerr();
      synchronized (this) {
        return num(map.size());
      }
    }

    // DEBUG OBJECT
    // DEBUG SEGFAULT

    public Reply decr(byte[][] a) {
      if (a.length != 2) return argerr();
      return decr(a[1], 1);
    }

    public Reply decrby(byte[][] a) {
      if (a.length != 3) return argerr();
      return decr(a[1], Long.parseLong($(a[2])));
    }

    public Reply del(byte[][] a) {
      if (a.length < 2) return argerr();
      int total = 0;
      for (int i = 1; i < a.length; i++) {
        synchronized (this) {
          if (map.remove($(a[i])) != null) {
            total++;
          }
        }
      }
      return num(total);
    }

    // DISCARD

    public Reply echo(byte[][] a) {
      if (a.length != 2) return argerr();
      return new Reply.BulkReply(a[1]);
    }

    // EXEC

    public Reply exists(byte[][] a) {
      if (a.length != 2) return argerr();
      synchronized (this) {
        return num(map.containsKey($(a[0])) ? 1 : 0);
      }
    }

    // EXPIRE
    // EXPIREAT

    public Reply flushall(byte[][] a) {
      if (a.length != 1) return argerr();
      synchronized (this) {
        map.clear();
      }
      return OK;
    }

    public Reply flushdb(byte[][] a) {
      return flushall(a);
    }

    public Reply get(byte[][] a) {
      if (a.length != 2) return argerr();
      Object value = get($(a[1]));
      if (value instanceof byte[] || value == null) {
        return bytes((byte[]) value);
      } else {
        return typeerr();
      }
    }

    public Reply getbit(byte[][] a) {
      if (a.length != 3) return argerr();
      int offset = tonum(a[2]);
      int byt = offset / 8;
      int bit = offset % 8;
      Object v = get($(a[1]));
      if (v instanceof byte[]) {
        byte[] bytes = (byte[]) v;
        if (bytes.length <= byt) {
          return num(0);
        }
        int set = bytes[byt] & (1 << bit);
        return num(set > 0 ? 1 : 0);
      } else if (v == null) {
        return num(0);
      } else {
        return typeerr();
      }
    }

    public Reply getrange(byte[][] a) {
      if (a.length != 4) return argerr();
      Object o = get($(a[1]));
      if (o instanceof byte[]) {
        byte[] b = (byte[]) o;
        int l = b.length;
        int start = tonum(a[2]);
        if (start < 0) start = l + start;
        start = Math.min(start, l);
        int end = tonum(a[3]);
        if (end < 0) end = l + end;
        end = Math.min(end, l);
        if (end <= start) {
          return EMPTY;
        }
        byte[] r = new byte[end - start];
        System.arraycopy(b, start, r, 0, end - start);
        return new Reply.BulkReply(r);
      } else if (o == null) {
        return EMPTY;
      } else {
        return typeerr();
      }
    }

    public Reply getset(byte[][] a) {
      if (a.length != 3) return argerr();
      synchronized (this) {
        String key = $(a[1]);
        Object o = get(key);
        put(key, a[2]);
        if (o instanceof byte[]) {
          return bytes((byte[]) o);
        }
        return new Reply.BulkReply(null);
      }
    }

    public Reply hdel(byte[][] a) {
      if (a.length < 3) return argerr();
      Object o = get($(a[1]));
      if (o instanceof Map) {
        Map hash = (Map) o;
        synchronized (hash) {
          int total = 0;
          for (int i = 2; i < a.length; i++) {
            if (hash.remove($(a[i])) != null) {
              total++;
            }
          }
          return num(total);
        }
      } else if (o == null) {
        return num(0);
      } else {
        return typeerr();
      }
    }

    public Reply hexists(byte[][] a) {
      if (a.length != 3) return argerr();
      Object o = get($(a[1]));
      if (o instanceof Map) {
        Map hash = (Map) o;
        synchronized (hash) {
          return num(hash.containsKey($(a[2])) ? 1 : 0);
        }
      } else if (o == null) {
        return num(0);
      } else {
        return typeerr();
      }
    }

    // HGET
    // HGETALL
    // HINCRBY
    // HKEYS
    // HLEN
    // HMGET
    // HMSET

    public Reply hset(byte[][] a) {
      if (a.length != 4) return argerr();
      String key = $(a[1]);
      Object o = get(key);
      if (o instanceof Map || o == null) {
        Map hash = (Map) o;
        if (hash == null) {
          synchronized (this) {
            hash = (Map) get(key);
            if (hash == null) {
              hash = new HashMap();
              put(key, hash);
            }
          }
        }
        synchronized (hash) {
          hash.put($(a[2]), a[3]);
          return OK;
        }
      } else {
        return typeerr();
      }
    }

    public Reply lpush(byte[][] a) {
      if (a.length < 3) return argerr();
      synchronized (this) {
        String key = $(a[1]);
        Object o = map.get(key);
        if (o instanceof List || o == null) {
          List<byte[]> l = (List<byte[]>) o;
          if (l == null) {
            l = new ArrayList<>(a.length - 2);
            put(key, l);
          }
          for (int i = 1; i < a.length; i++) {
            l.add(0, a[i]);
            notifyAll();
          }
          return OK;
        } else return typeerr();
      }
    }

    public Reply set(byte[][] a) {
      if (a.length != 3) return argerr();
      String key = $(a[1]);
      put(key, a[2]);
      return OK;
    }

    private Reply decr(byte[] k, long num) {
      synchronized (this) {
        String key = $(k);
        Object o = get(key);
        if (o instanceof byte[]) {
          try {
            long l = Long.parseLong($((byte[]) o)) - num;
            final String value = String.valueOf(l);
            put(key, value.getBytes(Charsets.UTF_8));
            return num(l);
          } catch (NumberFormatException e) {
            return numerr();
          }
        } else {
          return numerr();
        }
      }
    }

    private Reply.IntegerReply num(long total) {
      return new Reply.IntegerReply(total);
    }

    private Reply numerr() {
      return new Reply.ErrorReply("value is not an integer");
    }

    private Reply pop(byte[][] a, F<List, Object> f) {
      if (a.length < 3) return argerr();
      int timeout = tonum(a[a.length - 1]) * 1000;
      long start = System.currentTimeMillis();
      long since;
      while ((since = System.currentTimeMillis() - start) < timeout) {
        synchronized (this) {
          for (int i = 1; i < a.length - 1; i++) {
            Object o = get($(a[i]));
            if (o instanceof List) {
              List l = (List) o;
              if (l.size() > 0) {
                Object remove = f.apply(l);
                if (remove instanceof byte[] || remove == null) {
                  notifyAll();
                  return new Reply.MultiBulkReply(new byte[][]{a[i], (byte[]) remove});
                } else {
                  return typeerr();
                }
              }
            } else if (o != null) {
              return typeerr();
            }
          }
          try {
            logger.info("Waiting");
            wait(timeout - since);
            logger.info("Done waiting");
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      return new Reply.MultiBulkReply(null);
    }

    private Object get(String key) {
      return map.get(key);
    }

    private void put(String key, Object value) {
      map.put(key, value);
      synchronized (this) {
        notifyAll();
      }
    }

    private Reply.ErrorReply argerr() {
      String c = Thread.currentThread().getStackTrace()[2].getMethodName();
      return new Reply.ErrorReply("wrong number of arguments for '" + c + "' command");
    }

    private Reply.ErrorReply typeerr() {
      return new Reply.ErrorReply("Operation against a key holding the wrong kind of value");
    }

    private int tonum(byte[] k) {
      return Integer.parseInt($(k));
    }

    private Reply bytes(byte[] o) {
      return new Reply.BulkReply((byte[]) o);
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
