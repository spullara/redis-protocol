package client;

import org.junit.Test;
import redis.Command;
import redis.client.RedisClient;
import redis.client.SocketPool;
import redis.reply.BulkReply;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import com.google.common.base.Charsets;

import static junit.framework.Assert.assertEquals;

/**
 * Test the boilerplate
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 10:20 PM
 */
public class RedisClientTest {
  @Test
  public void testIt() throws IOException, ExecutionException, InterruptedException {
    RedisClient redisClient = new RedisClient(new SocketPool("localhost", 6379));
    redisClient.set("test", "value");
    BulkReply test = redisClient.get("test");
    assertEquals("value", new String(test.bytes));
    RedisClient.Pipeline p = redisClient.pipeline();
    p.set("increment", 0);
    p.incr("increment");
    p.incr("increment");
    assertEquals(3, redisClient.incr("increment").integer);
  }
  
  @Test
  public void benchmark() throws IOException {
    int CALLS = 1000000;
    long start = System.currentTimeMillis();
    RedisClient redisClient = new RedisClient(new SocketPool("localhost", 6379));
    for (int i = 0; i < CALLS; i++) {
      redisClient.get(Command.numToBytes(i));
    }
    long end = System.currentTimeMillis();
    System.out.println(CALLS * 1000 / (end - start) + " calls per second");
  }
}
