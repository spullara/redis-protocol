package redis.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import redis.Command;
import redis.RedisProtocol;
import redis.reply.Reply;

/**
 * Start up a proxy.
 */
public class BlockingProxy {
  @Argument(alias = "p", description = "Port for this server to run")
  private static Integer port = 63790;

  @Argument(alias = "r", description = "Redis server host:port")
  private static String redisServer = "localhost:6379";

  private static ExecutorService es = Executors.newCachedThreadPool();

  public static void main(String[] args) throws IOException {
    final String redisHost;
    final int redisPort;
    try {
      Args.parse(BlockingProxy.class, args);
      String[] split = redisServer.split(":");
      if (split.length != 2) {
        throw new IllegalArgumentException("host:port");
      }
      redisHost = split[0];
      redisPort = Integer.parseInt(split[1]);
    } catch (IllegalArgumentException e) {
      Args.usage(BlockingProxy.class);
      System.exit(1);
      return;
    }

    ServerSocket ss = new ServerSocket(port);

    while(true) {
      final Socket accept = ss.accept();
      es.submit(new Runnable() {
        @Override
        public void run() {
          try {
            final Socket redis = new Socket(redisHost, redisPort);
            es.submit(new Runnable() {
              @Override
              public void run() {
                try {
                  InputStream is = redis.getInputStream();
                  OutputStream os = accept.getOutputStream();
                  Reply read;
                  while ((read = RedisProtocol.receive(is)) != null) {
                    read.write(os);
                  }
                  accept.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            });
            InputStream is = accept.getInputStream();
            OutputStream os = redis.getOutputStream();
            Command read;
            while ((read = Command.read(is)) != null) {
              read.write(os);
            }
            redis.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
    }
  }

}
