package redis.netty.client;

import org.junit.Test;
import redis.Command;
import redis.netty.BulkReply;
import redis.netty.Reply;
import spullara.util.concurrent.Promise;
import spullara.util.functions.Block;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the base redis client. Default redis required.
 */
public class RedisClientBaseTest {
  @Test
  public void testConnect() throws Exception {
    final CountDownLatch connectLatch = new CountDownLatch(1);
    final AtomicBoolean success = new AtomicBoolean();
    final AtomicReference<RedisClientBase> client = new AtomicReference<>();
    Promise<RedisClientBase> connect = RedisClientBase.connect("localhost", 6379);
    connect.onSuccess(new Block<RedisClientBase>() {
      @Override
      public void apply(RedisClientBase redisClientBase) {
        success.set(true);
        client.set(redisClientBase);
        connectLatch.countDown();
      }
    }).onFailure(new Block<Throwable>() {
      @Override
      public void apply(Throwable throwable) {
        success.set(false);
        connectLatch.countDown();
      }
    });
    connectLatch.await();
    final CountDownLatch closeLatch = new CountDownLatch(1);
    assertTrue(success.get());
    client.get().close().onSuccess(new Block<Void>() {
      @Override
      public void apply(Void aVoid) {
        success.set(true);
        closeLatch.countDown();
      }
    }).onFailure(new Block<Throwable>() {
      @Override
      public void apply(Throwable throwable) {
        success.set(false);
        closeLatch.countDown();
      }
    });
    closeLatch.await();
    assertTrue(success.get());
  }

  @Test
  public void testConnectFailure() throws Exception {
    final CountDownLatch connectLatch = new CountDownLatch(1);
    final AtomicBoolean success = new AtomicBoolean();
    final AtomicReference<Throwable> failure = new AtomicReference<>();
    Promise<RedisClientBase> connect = RedisClientBase.connect("localhost", 6380);
    connect.onSuccess(new Block<RedisClientBase>() {
      @Override
      public void apply(RedisClientBase redisClientBase) {
        success.set(true);
        connectLatch.countDown();
      }
    }).onFailure(new Block<Throwable>() {
      @Override
      public void apply(Throwable throwable) {
        success.set(false);
        failure.set(throwable);
        connectLatch.countDown();
      }
    });
    connectLatch.await();
    assertFalse(success.get());
    assertEquals("Connection refused", failure.get().getMessage());
  }

  @Test
  public void testCommands() throws Exception {
    final CountDownLatch done = new CountDownLatch(1);
    final AtomicBoolean success = new AtomicBoolean();
    RedisClientBase.connect("localhost", 6379).onSuccess(new Block<RedisClientBase>() {
      @Override
      public void apply(final RedisClientBase redisClientBase) {
        redisClientBase.execute(new Command("set", "test", "test")).onSuccess(new Block<Reply>() {
          @Override
          public void apply(Reply reply) {
            if (reply.data().equals("OK")) {
              redisClientBase.execute(new Command("get", "test")).onSuccess(new Block<Reply>() {
                @Override
                public void apply(Reply reply) {
                  if (reply instanceof BulkReply) {
                    if (((BulkReply) reply).asAsciiString().equals("test")) {
                      success.set(true);
                    }
                    done.countDown();
                    redisClientBase.close();
                  }
                }
              });
            } else {
              done.countDown();
              redisClientBase.close();
            }
          }
        });
      }
    });
    done.await();
    assertTrue(success.get());
  }
}
