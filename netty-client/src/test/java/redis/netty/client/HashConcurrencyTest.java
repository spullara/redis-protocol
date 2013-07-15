package redis.netty.client;

import org.junit.Test;
import redis.netty.MultiBulkReply;
import spullara.util.functions.Block;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.fail;

/**
 * Test a high concurrent application accessing a large hash table in redis.
 */
public class HashConcurrencyTest {
  @Test
  public void testConcurrency() throws ExecutionException, InterruptedException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null) return;
    RedisClient client = RedisClient.connect("localhost", 6379).get();
    client.del_("hash").get();
    for (int i = 0; i < 100; i++) {
      client.hset("hash", "key" + i, "values" + i).get();
    }
    long start = System.currentTimeMillis();
    final AtomicBoolean failed = new AtomicBoolean();
    int permits = 100;
    final Semaphore semaphore = new Semaphore(permits);
    for (int i = 0; i < 100000; i++) {
      semaphore.acquireUninterruptibly();
      client.hgetall("hash").onSuccess(new Block<MultiBulkReply>() {
        @Override
        public void apply(MultiBulkReply multiBulkReply) {
          semaphore.release();
        }
      }).onFailure(new Block<Throwable>() {
        @Override
        public void apply(Throwable throwable) {
          throwable.printStackTrace();
          semaphore.release();
          failed.set(true);
        }
      });
      if (failed.get()) {
        fail("Concurrency bug");
        return;
      }
    }
    semaphore.acquireUninterruptibly(permits);
    System.out.println(System.currentTimeMillis() - start);
  }

}
