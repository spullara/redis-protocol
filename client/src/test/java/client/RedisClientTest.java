package client;

import com.google.common.base.Charsets;
import org.junit.Test;
import redis.Command;
import redis.client.RedisClient;
import redis.client.SocketPool;
import redis.reply.BulkReply;
import redis.reply.StatusReply;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertEquals;

/**
 * Test the boilerplate
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 10:20 PM
 */
public class RedisClientTest {

  private static final byte[] VALUE = "value".getBytes(Charsets.UTF_8);

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
      redisClient.set(Command.numToBytes(i, false), VALUE);
    }
    long end = System.currentTimeMillis();
    System.out.println(CALLS * 1000 / (end - start) + " calls per second");
  }

  @Test
  public void benchmarkPipeline() throws IOException, ExecutionException, InterruptedException {
    int CALLS = 1000000;
    long start = System.currentTimeMillis();
    RedisClient redisClient = new RedisClient(new SocketPool("localhost", 6379));
    RedisClient.Pipeline pipeline = redisClient.pipeline();
    int PIPELINE_CALLS = 50;
    Future<StatusReply>[] replies = new Future[PIPELINE_CALLS];
    for (int i = 0; i < CALLS / PIPELINE_CALLS; i++) {
      for (int j = 0; j < PIPELINE_CALLS; j++) {
        replies[j] = pipeline.set(Command.numToBytes(i, false), VALUE);
      }
      for (int j = 0; j < PIPELINE_CALLS; j++) {
        replies[j].get();
      }
    }
    long end = System.currentTimeMillis();
    System.out.println(CALLS * 1000 / (end - start) + " calls per second");
  }
}
