package redis.netty4;

import org.junit.Test;
import redis.util.Encoding;
import spullara.util.functions.Block;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertTrue;
import static redis.netty4.RedisClientBase.connect;

/**
 * Some tests for the client.
 */
public class RedisClientTest {

  public static final long CALLS = 1000000;

  @Test
  public void testSetGet() throws InterruptedException {
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final AtomicBoolean success = new AtomicBoolean();
    final AtomicBoolean matches = new AtomicBoolean();
    connect("localhost", 6379).onSuccess(new Block<RedisClientBase>() {
      @Override
      public void apply(final RedisClientBase client) {
        client.send(new Command("SET", "test", "value")).onSuccess(new Block<Reply>() {
          @Override
          public void apply(Reply reply) {
            success.set(reply.data().equals("OK"));
            client.send(new Command("GET", "test")).onSuccess(new Block<Reply>() {
              @Override
              public void apply(Reply reply) {
                if (reply instanceof BulkReply) {
                  matches.set(((BulkReply) reply).asAsciiString().equals("value"));
                }
                countDownLatch.countDown();
              }
            });
          }
        });
      }
    }).onFailure(new Block<Throwable>() {
      @Override
      public void apply(Throwable throwable) {
        countDownLatch.countDown();
      }
    });
    countDownLatch.await();
    assertTrue(success.get());
    assertTrue(matches.get());
  }

  @Test
  public void testBenchmark() throws InterruptedException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null) return;
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final AtomicInteger calls = new AtomicInteger(0);

    Block<RedisClientBase> benchmark = new Block<RedisClientBase>() {
      @Override
      public void apply(final RedisClientBase client) {
        int i = calls.getAndIncrement();
        if (i == CALLS) {
          countDownLatch.countDown();
        } else {
          final Block<RedisClientBase> thisBenchmark = this;
          client.send(new Command("SET", Encoding.numToBytes(i), "value")).onSuccess(new Block<Reply>() {
            @Override
            public void apply(Reply reply) {
              thisBenchmark.apply(client);
            }
          });
        }
      }
    };

    long start = System.currentTimeMillis();
    connect("localhost", 6379).onSuccess(benchmark).onFailure(new Block<Throwable>() {
      @Override
      public void apply(Throwable throwable) {
        countDownLatch.countDown();
      }
    });
    countDownLatch.await();
    System.out.println("Netty4: " + CALLS  * 1000 / (System.currentTimeMillis() - start));
  }

  @Test
  public void testPipelinedBenchmark() throws ExecutionException, InterruptedException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null) return;
    long start = System.currentTimeMillis();
    RedisClientBase client = connect("localhost", 6379).get();
    final Semaphore semaphore = new Semaphore(100);
    for (int i = 0; i < CALLS; i++) {
      semaphore.acquire();
      client.send(new Command("SET", Encoding.numToBytes(i), "value")).ensure(new Runnable() {
        @Override
        public void run() {
          semaphore.release();
        }
      });
    }
    semaphore.acquire(50);
    System.out.println("Netty4 pipelined: " + CALLS  * 1000 / (System.currentTimeMillis() - start));
  }
}
