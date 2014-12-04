package redis.client;

import com.google.common.util.concurrent.ListenableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.embedded.RedisServer;
import redis.reply.StatusReply;

import java.util.concurrent.Future;

import static junit.framework.Assert.assertEquals;

/**
 * https://github.com/spullara/redis-protocol/issues/20
 */
public class Issue20Test {
  private RedisClient client;

  private RedisServer redisServer;

  @Before
  public void setup() throws Exception {
    redisServer = new RedisServer();
    redisServer.start();
    client = new RedisClient("localhost", redisServer.getPort());
  }

  @After
  public void tearDown() throws Exception {
    redisServer.stop();
  }

  @Test
  public void testMultiDiscard() throws Exception {
    StatusReply set = client.set("testitnow", "willdo");
    StatusReply multi = client.multi();
    ListenableFuture<StatusReply> set1 = client.pipeline().set("testitnow", "notok");
    client.discard();
    assertEquals("willdo", new String(client.get("testitnow").data()));
    // Ensure we can run a new tx after discarding previous one
    testMultiExec();
  }

  @Test
  public void testMultiExec() throws Exception {
    StatusReply multi = client.multi();
    ListenableFuture<StatusReply> reply = client.pipeline().set("key", "value");
    Future<Boolean> exec = client.exec();
    assertEquals("OK", reply.get().data());
  }
}
