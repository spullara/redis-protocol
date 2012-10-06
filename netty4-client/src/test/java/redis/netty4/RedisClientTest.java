package redis.netty4;

import org.junit.Test;
import redis.util.Encoding;
import spullara.util.functions.Block;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertTrue;
import static redis.netty4.RedisClientBase.connect;

/**
 * Some tests for the client.
 */
public class RedisClientTest {

  public static final long CALLS = 100000;

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

  final CountDownLatch countDownLatch = new CountDownLatch(1);
  final AtomicInteger calls = new AtomicInteger(0);

  Block<RedisClientBase> benchmark = new Block<RedisClientBase>() {
    @Override
    public void apply(final RedisClientBase client) {
      int i = calls.getAndIncrement();
      if (i == CALLS) {
        countDownLatch.countDown();
      } else {
        client.send(new Command("SET", Encoding.numToBytes(i), "value")).onSuccess(new Block<Reply>() {
          @Override
          public void apply(Reply reply) {
            benchmark.apply(client);
          }
        });
      }
    }
  };

  @Test
  public void testBenchmark() throws InterruptedException {
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
}
