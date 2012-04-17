package redis.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

public class NonBlockingProxy {
  @Argument(alias = "p", description = "Port for this server to run")
  private static Integer port = 63790;

  @Argument(alias = "r", description = "Redis server host:port")
  private static String redisServer = "localhost:6379";

  private static ExecutorService es = Executors.newCachedThreadPool();
  public static final int REDIS_BUFFER_SIZE = 1024;

  public static void main(String[] args) throws IOException, InterruptedException {
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

    CountDownLatch done = new CountDownLatch(1);

    final AsynchronousServerSocketChannel listener =
            AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
    final Queue<ByteBuffer> queue = new ConcurrentLinkedQueue<>();

    listener.accept(null, new CompletionHandler<AsynchronousSocketChannel,Void>() {
      public void completed(final AsynchronousSocketChannel client, Void att) {
        // accept the next connection
        listener.accept(null, this);

        final AsynchronousSocketChannel redis;
        try {
          redis = AsynchronousSocketChannel.open();
          redis.connect(new InetSocketAddress(redisHost, redisPort)).get();
        } catch (Exception e) {
          e.printStackTrace();
          try {
            listener.close();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
          return;
        }

        read(client, redis);
        read(redis, client);
      }

      private ByteBuffer getBuffer() {
        ByteBuffer poll = queue.poll();
        if (poll == null) {
          return ByteBuffer.allocate(REDIS_BUFFER_SIZE);
        }
        return poll;
      }

      private void read(final AsynchronousSocketChannel reader, AsynchronousSocketChannel writer) {
        final ByteBuffer buffer = getBuffer();
        reader.read(buffer, writer, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

          @Override
          public void completed(Integer result, AsynchronousSocketChannel writer) {
            if (result == -1) {
              return;
            }
            writer.write((ByteBuffer) buffer.flip(), buffer, new CompletionHandler<Integer, ByteBuffer>() {
              @Override
              public void completed(Integer result, ByteBuffer attachment) {
                queue.add((ByteBuffer) buffer.clear());
              }

              @Override
              public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
              }
            });
            read(reader, writer);
          }

          @Override
          public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
            exc.printStackTrace();
            try {
              attachment.close();
              reader.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
      }

      public void failed(Throwable exc, Void att) {
        exc.printStackTrace();
      }
    });

    done.await();
  }
}