package redis.client;

import junit.framework.Assert;
import org.junit.Test;
import redis.reply.Reply;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.*;

public class Issue29Test {
  @Test
  public void testBRPOPLPUSHHangup() throws IOException, InterruptedException {
    final RedisClient client = new RedisClient("localhost", 6379);
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
        } catch(Exception e) {
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
