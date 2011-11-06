import org.junit.Test;
import redis.Command;
import redis.RedisProtocol;
import redis.SocketPool;
import redis.reply.MultiBulkReply;

import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 5:59 PM
 */
public class PubSubTest {
  @Test
  public void testPubSub() throws IOException, InterruptedException {
    final SocketPool socketPool = new SocketPool("localhost", 6379);
    final Semaphore semaphore = new Semaphore(1);
    semaphore.acquire();
    Thread thread = new Thread() {
      @Override
      public void run() {
        try {
          final RedisProtocol redisProtocol = new RedisProtocol(socketPool.get());
          Command subscribeTest = new Command("subscribe", "test");
          redisProtocol.send(subscribeTest);
          semaphore.release();
          while (true) {
            MultiBulkReply reply = (MultiBulkReply) redisProtocol.receiveAsync();
            if (new String((byte[]) reply.byteArrays[2]).equals("quit")) {
              break;
            }
            semaphore.release();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
    thread.start();
    RedisProtocol redisProtocol = new RedisProtocol(socketPool.get());
    long start = System.currentTimeMillis();
    int total = 0;
    while (System.currentTimeMillis() - start < 10000) {
      Command publish = new Command("publish", "test", "message" + total);
      total++;
      semaphore.acquire();
      redisProtocol.send(publish);
    }
    redisProtocol.send(new Command("publish", "test", "quit"));
    thread.join();
    System.out.println(total/10);
  }
}
