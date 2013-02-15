package redis.client;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.SettableFuture;
import org.junit.Test;
import redis.Command;
import redis.reply.BulkReply;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;
import redis.reply.StatusReply;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static redis.util.Encoding.numToBytes;

/**
 * Test the boilerplate
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 10:20 PM
 */
public class RedisClientTest {

  private static final byte[] VALUE = "value".getBytes(Charsets.UTF_8);
  private static final long CALLS = 100000l;

  @Test
  public void testIt() throws IOException, ExecutionException, InterruptedException {
    RedisClient redisClient = new RedisClient("localhost", 6379);
    redisClient.set("test", "value");
    BulkReply test = redisClient.get("test");
    assertEquals("value", test.asAsciiString());
    assertEquals("value", test.asUTF8String());
    RedisClient.Pipeline p = redisClient.pipeline();
    p.set("increment", 0);
    p.incr("increment");
    p.incr("increment");
    assertEquals(3, (long) redisClient.incr("increment").data());

    redisClient.del(new Object[] { "hash" });
    redisClient.hset("hash", "field1", "value1");
    redisClient.hset("hash", "field2", "value2");
    redisClient.hset("hash", "field3", "value1");
    MultiBulkReply hash = redisClient.hgetall("hash");
    assertEquals("value2", hash.asStringMap(Charsets.UTF_8).get("field2"));
    assertEquals(6, hash.asStringList(Charsets.UTF_8).size());
    assertEquals(5, hash.asStringSet(Charsets.UTF_8).size());

    Object[] keys = {"test1", "test2", "test3"};
    redisClient.del(keys);
    redisClient.set("test1", "value1");
    redisClient.set("test2", "value2");
    redisClient.set("test3", "value3");
    MultiBulkReply values = redisClient.mget(keys);
    List<String> strings = values.asStringList(Charsets.UTF_8);
    assertEquals(3, strings.size());
    assertEquals("value2", strings.get(1));
  }

  @Test
  public void testTx() throws IOException, ExecutionException, InterruptedException {
    RedisClient redisClient1 = new RedisClient("localhost", 6379);
    RedisClient redisClient2 = new RedisClient("localhost", 6379);

    {
      redisClient1.set("txincr", 0);
      redisClient1.watch(new Object[]{"txincr"});
      redisClient1.get("txincr");
      redisClient1.multi();
      RedisClient.Pipeline p = redisClient1.pipeline();
      Future<IntegerReply> txincr1 = p.incr("txincr");
      Future<IntegerReply> txincr2 = p.incr("txincr");
      redisClient1.exec();
      assertEquals(2l, (long) txincr2.get().data());
      assertEquals(1l, (long) txincr1.get().data());
    }

    {
      redisClient1.set("txincr", 0);
      redisClient1.watch(new Object[]{"txincr"});
      redisClient1.get("txincr");
      redisClient1.multi();
      redisClient2.set("txincr", 1);
      RedisClient.Pipeline p = redisClient1.pipeline();
      p.incr("txincr");
      p.incr("txincr");
      if (redisClient1.exec().get()) {
        fail("This should have failed");
      }
    }
    redisClient1.close();
    redisClient2.close();
  }

  @Test
  public void testSubscriptions() throws IOException, ExecutionException, InterruptedException {
    RedisClient redisClient1 = new RedisClient("localhost", 6379);
    RedisClient redisClient2 = new RedisClient("localhost", 6379);
    final SettableFuture<String> messaged = SettableFuture.create();
    final SettableFuture<String> pmessaged = SettableFuture.create();
    final SettableFuture<String> subscribed = SettableFuture.create();
    final SettableFuture<String> psubscribed = SettableFuture.create();
    final SettableFuture<String> unsubscribed = SettableFuture.create();
    final SettableFuture<String> punsubscribed = SettableFuture.create();
    redisClient2.addListener(new ReplyListener() {
      public void subscribed(byte[] name, int channels) {
        subscribed.set(new String(name));
      }
      public void psubscribed(byte[] name, int channels) {
        psubscribed.set(new String(name));
      }
      public void unsubscribed(byte[] name, int channels) {
        unsubscribed.set(new String(name));
      }
      public void punsubscribed(byte[] name, int channels) {
        punsubscribed.set(new String(name));
      }
      public void message(byte[] channel, byte[] message) {
        messaged.set(new String(channel) + " " + new String(message));
      }
      public void pmessage(byte[] pattern, byte[] channel, byte[] message) {
        pmessaged.set(new String(pattern) + " " + new String(message));
      }
    });
    redisClient2.subscribe("subscribe");
    redisClient2.psubscribe("subscribe*");
    redisClient1.publish("subscribe", "hello, world!");
    assertEquals("subscribe hello, world!", messaged.get());
    assertEquals("subscribe* hello, world!", pmessaged.get());
    redisClient2.unsubscribe("subscribe");
    redisClient2.punsubscribe("subscribe*");
    assertEquals("subscribe", subscribed.get());
    assertEquals("subscribe", unsubscribed.get());
    assertEquals("subscribe*", psubscribed.get());
    assertEquals("subscribe*", punsubscribed.get());
    try {
      redisClient2.set("test", "fail");
      fail("Should have failed");
    } catch (RedisException e) {
      // fails
    }
    redisClient1.close();
    redisClient2.close();
  }

  @Test
  public void testAPI() throws IOException {
    RedisClient rc = new RedisClient("localhost", 6379);
    rc.set("test1".getBytes(), "value".getBytes());
    rc.set("test2".getBytes(), "value");
    assertEquals("value", ((BulkReply)(rc.mget(new Object[] { "test1".getBytes() }).data()[0])).asAsciiString());
  }
  
  @Test
  public void benchmark() throws IOException {
    long start = System.currentTimeMillis();
    RedisClient redisClient = new RedisClient("localhost", 6379);
    for (int i = 0; i < CALLS; i++) {
      redisClient.set(numToBytes(i, false), VALUE);
    }
    long end = System.currentTimeMillis();
    System.out.println("Blocking: " + (CALLS * 1000) / (end - start) + " calls per second");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void benchmarkPipeline() throws IOException, ExecutionException, InterruptedException {
    long start = System.currentTimeMillis();
    RedisClient redisClient = new RedisClient("localhost", 6379);
    redisClient.execute("INFO", new Command("INFO", "server"));
    RedisClient.Pipeline pipeline = redisClient.pipeline();
    int PIPELINE_CALLS = 50;
    Future<StatusReply>[] replies = new Future[PIPELINE_CALLS];
    for (int i = 0; i < CALLS * 10 / PIPELINE_CALLS; i++) {
      for (int j = 0; j < PIPELINE_CALLS; j++) {
        replies[j] = pipeline.set(numToBytes(i, false), VALUE);
      }
      for (int j = 0; j < PIPELINE_CALLS; j++) {
        replies[j].get();
      }
    }
    long end = System.currentTimeMillis();
    System.out.println("Pipelined: " + (CALLS * 10 * 1000) / (end - start) + " calls per second");
  }

  @Test
  public void benchmarkPubsub() throws IOException, ExecutionException, InterruptedException {
    long start = System.currentTimeMillis();
    byte[] hello = "hello".getBytes();
    byte[] test = "test".getBytes();
    RedisClient subscriberClient = new RedisClient("localhost", 6379);
    final AtomicReference<SettableFuture> futureRef = new AtomicReference<SettableFuture>();
    subscriberClient.addListener(new MessageListener() {
      @SuppressWarnings("unchecked")
      @Override
      public void message(byte[] channel, byte[] message) {
        futureRef.get().set(null);
      }

      @SuppressWarnings("unchecked")
      @Override
      public void pmessage(byte[] pattern, byte[] channel, byte[] message) {
        futureRef.get().set(null);
      }
    });
    subscriberClient.subscribe("test");
    RedisClient publisherClient = new RedisClient("localhost", 6379);
    for (int i = 0; i < CALLS; i++) {
      SettableFuture<Object> future = SettableFuture.create();
      futureRef.set(future);
      publisherClient.publish(test, hello);
      future.get();
    }
    long end = System.currentTimeMillis();
    System.out.println("Pub/sub: " + (CALLS * 1000) / (end - start) + " calls per second");
  }

  @Test
  public void benchmarkPubsubPipelined() throws IOException, ExecutionException, InterruptedException {
    long start = System.currentTimeMillis();
    byte[] hello = "hello".getBytes();
    byte[] test = "test".getBytes();
    RedisClient subscriberClient = new RedisClient("localhost", 6379);
    final Semaphore semaphore = new Semaphore(50);
    final AtomicReference<SettableFuture> futureRef = new AtomicReference<SettableFuture>();
    subscriberClient.addListener(new MessageListener() {
      @Override
      public void message(byte[] channel, byte[] message) {
        semaphore.release();
      }

      @Override
      public void pmessage(byte[] pattern, byte[] channel, byte[] message) {
        semaphore.release();
      }
    });
    subscriberClient.subscribe("test");
    RedisClient.Pipeline publisherClient = new RedisClient("localhost", 6379).pipeline();
    for (int i = 0; i < CALLS * 10; i++) {
      semaphore.acquire();
      SettableFuture<Object> future = SettableFuture.create();
      futureRef.set(future);
      publisherClient.publish(test, hello);
    }
    semaphore.acquire(50);
    long end = System.currentTimeMillis();
    System.out.println("Pub/sub pipelined: " + (CALLS * 10 * 1000) / (end - start) + " calls per second");
  }

}
