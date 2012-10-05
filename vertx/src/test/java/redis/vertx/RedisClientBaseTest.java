package redis.vertx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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

}

