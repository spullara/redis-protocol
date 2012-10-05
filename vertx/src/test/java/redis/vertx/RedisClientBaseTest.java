package redis.vertx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.base.Charsets;

import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import redis.Command;
import redis.netty.BulkReply;
import redis.netty.MultiBulkReply;
import redis.netty.Reply;
import redis.netty.StatusReply;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static redis.util.Encoding.numToBytes;

public class RedisClientBaseTest {
  @Test
  public void testSyncSend() throws Exception {
    final AtomicReference<StatusReply> setResult = new AtomicReference<StatusReply>();
    final AtomicReference<BulkReply> getResult = new AtomicReference<BulkReply>();
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    RedisClientBase.connect("localhost", 6379, Vertx.newVertx(), new Handler<RedisClientBase>() {
      @Override
      public void handle(final RedisClientBase redisClientBase) {
        redisClientBase.send(new Command("SET", "test", "value"), new Handler<Reply>() {
          @Override
          public void handle(Reply reply) {
            setResult.set((StatusReply) reply);
            redisClientBase.send(new Command("GET", "test"), new Handler<Reply>() {
              @Override
              public void handle(Reply reply) {
                getResult.set((BulkReply) reply);
                countDownLatch.countDown();
              }
            });
          }
        });
      }
    });
    countDownLatch.await();
    assertEquals("OK", setResult.get().data());
    assertEquals("value", getResult.get().asAsciiString());
  }

  @Test
  public void testPipelinedSend() throws Exception {
    final AtomicReference<StatusReply> setResult = new AtomicReference<StatusReply>();
    final AtomicReference<BulkReply> getResult = new AtomicReference<BulkReply>();
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    RedisClientBase.connect("localhost", 6379, Vertx.newVertx(), new Handler<RedisClientBase>() {
      @Override
      public void handle(final RedisClientBase redisClientBase) {
        redisClientBase.send(new Command("SET", "test", "value"), new Handler<Reply>() {
          @Override
          public void handle(Reply reply) {
            setResult.set((StatusReply) reply);
          }
        });
        redisClientBase.send(new Command("GET", "test"), new Handler<Reply>() {
          @Override
          public void handle(Reply reply) {
            getResult.set((BulkReply) reply);
            countDownLatch.countDown();
          }
        });
      }
    });
    countDownLatch.await();
    assertEquals("OK", setResult.get().data());
    assertEquals("value", getResult.get().asAsciiString());
  }

  @Test
  public void testBigBuffer() throws Exception {
    final AtomicReference<MultiBulkReply> getResult = new AtomicReference<MultiBulkReply>();
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    RedisClientBase.connect("localhost", 6379, Vertx.newVertx(), new Handler<RedisClientBase>() {
      @Override
      public void handle(final RedisClientBase redisClientBase) {
        redisClientBase.send(new Command("KEYS", "100*"), new Handler<Reply>() {
          @Override
          public void handle(Reply reply) {
            getResult.set((MultiBulkReply) reply);
            countDownLatch.countDown();
          }
        });
      }
    });
    countDownLatch.await();
    assertNotNull(getResult.get());
  }

  private static final byte[] VALUE = "value".getBytes(Charsets.UTF_8);
  private static final long CALLS = 100000l;

  @Test
  public void testBenchmark() throws InterruptedException {
    long start = System.currentTimeMillis();
    final byte[] SET = "SET".getBytes();
    final AtomicInteger num = new AtomicInteger(0);
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    RedisClientBase.connect("localhost", 6379, Vertx.newVertx(), new Handler<RedisClientBase>() {
      @Override
      public void handle(final RedisClientBase redisClient) {
        Handler<Reply> replyHandler = new Handler<Reply>() {
          @Override
          public void handle(Reply statusReply) {
            int current = num.getAndIncrement();
            if (current == CALLS) {
              countDownLatch.countDown();
            } else {
              redisClient.send(new Command(SET, numToBytes(current), VALUE), this);            }
          }
        };
        replyHandler.handle(new StatusReply("GO"));
      }
    });
    countDownLatch.await();
    long end = System.currentTimeMillis();
    System.out.println("Vertx: " + (CALLS * 1000) / (end - start) + " calls per second");
  }
}

