package redis;

import com.google.common.base.Charsets;
import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

import java.io.IOException;
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
  private static Map<String, Code> commands = new HashMap<>();

  public static void main(String[] args) throws IOException {
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
    final Code code = commands.get(verb);
    if (code == null) {
      return new Reply.ErrorReply("Command not implemented: " + verb);
    }
    return code.call(arguments);
  }

  private static void init() {
    commands.put("set", new Code() {
      public Reply call(byte[][] arguments) {
        if (arguments.length != 3) {
          return new Reply.ErrorReply("Invalid number of arguments");
        }
        String key = new String(arguments[1], Charsets.UTF_8);
        map.put(key, arguments[2]);
        return new Reply.StatusReply("OK");
      }
    });
    commands.put("get", new Code() {
      public Reply call(byte[][] arguments) {
        if (arguments.length != 2) {
          return new Reply.ErrorReply("Invalid number of arguments");
        }
        String key = new String(arguments[1], Charsets.UTF_8);
        Object value = map.get(key);
        if (value instanceof byte[]) {
          return new Reply.BulkReply((byte[]) value);
        } else {
          return new Reply.ErrorReply("Invalid value type: " + value.getClass().getName());
        }
      }
    });
  }
}
