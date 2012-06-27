package socket.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EchoTest {
  public static final int BUFFER_SIZE = 768;

  private static final Logger logger = Logger.getLogger("Proxy");

  private static abstract class Handler<A> implements CompletionHandler<Integer, A> {
    @Override
    public void failed(Throwable exc, A attachment) {
      error(exc, attachment);
    }
  }

  private static void error(Throwable exc, Object attachment) {
    logger.log(Level.WARNING, "IO failure in " + attachment, exc);
  }

  private static Queue<ByteBuffer> queue = new ConcurrentLinkedQueue<>();

  private static ByteBuffer getBuffer() {
    ByteBuffer poll = queue.poll();
    if (poll == null) {
      return ByteBuffer.allocate(BUFFER_SIZE);
    }
    return poll;
  }

  private static AtomicLong count = new AtomicLong(0);
  private static long start;

  private static void read(final AsynchronousSocketChannel reader, AsynchronousSocketChannel writer) {
    final ByteBuffer buffer = getBuffer();
    reader.read(buffer, writer, new Handler<AsynchronousSocketChannel>() {
      @Override
      public void completed(Integer result, AsynchronousSocketChannel writer) {
        if (result == -1) {
          return;
        }
        long count = EchoTest.count.incrementAndGet();
        if (count % 100000 == 0) {
          long end = System.currentTimeMillis();
          long rps = count * 1000 / (end - start);
          System.out.println("RPS: " + rps);
        }
        writer.write((ByteBuffer) buffer.flip(), buffer, new Handler<ByteBuffer>() {
          @Override
          public void completed(Integer result, ByteBuffer attachment) {
            queue.add((ByteBuffer) buffer.clear());
          }
        });
        read(reader, writer);
      }
    });
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    int port = 63790;
    CountDownLatch done = new CountDownLatch(1);

    if (args.length > 0 && args[0].equals("-c")) {
      final AsynchronousSocketChannel client;
      try {
        client = AsynchronousSocketChannel.open();
        client.connect(new InetSocketAddress("localhost", port)).get();
      } catch (Exception e) {
        error(e, "connect failed: " + port);
        System.exit(1);
        return;
      }

      read(client, client);

      final ByteBuffer writeBuffer = getBuffer();
      client.write(writeBuffer, "write", new CompletionHandler<Integer, String>() {
        @Override
        public void completed(Integer result, String attachment) {
          start = System.currentTimeMillis();
          queue.offer(writeBuffer);
        }

        @Override
        public void failed(Throwable exc, String attachment) {
          error(exc, attachment);
          System.exit(1);
        }
      });


    } else {
      final AsynchronousServerSocketChannel listener =
              AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));

      listener.accept(null, new CompletionHandler<AsynchronousSocketChannel,Void>() {
        public void completed(final AsynchronousSocketChannel client, Void att) {
          // accept the next connection
          listener.accept(null, this);
          read(client, client);
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
          error(exc, "accept");
          System.exit(1);
        }
      });
    }

    done.await();
  }
}