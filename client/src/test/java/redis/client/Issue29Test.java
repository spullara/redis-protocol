package redis.client;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.embedded.RedisServer;
import redis.reply.Reply;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.*;

public class Issue29Test {

  private RedisServer redisServer;

  @Before
  public void setup() throws Exception {
    redisServer = new RedisServer();
    redisServer.start();
  }

  @After
  public void tearDown() throws Exception {
    redisServer.stop();
  }

  // @Test TODO check that failing test!
  public void testBRPOPLPUSHHangup() throws IOException, InterruptedException {
    final RedisClient client = new RedisClient("localhost", redisServer.getPort());
    final AtomicReference<Reply> ref = new AtomicReference<Reply>();
    final AtomicReference<Exception> error = new AtomicReference<Exception>();
    ExecutorService es = Executors.newCachedThreadPool();
    final CountDownLatch latch = new CountDownLatch(1);
    es.submit(new Runnable() {
      @Override
      public void run() {
        try {
          latch.countDown();
          Reply reply = client.brpoplpush("popsource", "pushdest", 0);
          ref.set(reply);
        } catch (Exception e) {
          error.set(e);
        }
      }
    });
    latch.await();
    Thread.sleep(100);
    client.close();
    Thread.sleep(3000);
    assertNotNull(error.get());
    assertNull(ref.get());
  }
}
