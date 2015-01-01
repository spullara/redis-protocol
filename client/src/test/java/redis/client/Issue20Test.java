package redis.client;

import org.junit.Before;
import org.junit.Test;
import redis.reply.StatusReply;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertEquals;

/**
 * https://github.com/spullara/redis-protocol/issues/20
 */
public class Issue20Test {
  private RedisClient client;

  @Before
  public void setUp() throws Exception {
    client = new RedisClient("localhost", 6379);
  }

  @Test
  public void testMultiDiscard() throws Exception {
    StatusReply set = client.set("testitnow", "willdo");
    StatusReply multi = client.multi();
    CompletableFuture<StatusReply> set1 = client.pipeline().set("testitnow", "notok");
    client.discard();
    assertEquals("willdo", new String(client.get("testitnow").data()));
    // Ensure we can run a new tx after discarding previous one
    testMultiExec();
  }

  @Test
  public void testMultiExec() throws Exception {
    StatusReply multi = client.multi();
    CompletableFuture<StatusReply> reply = client.pipeline().set("key", "value");
    Future<Boolean> exec = client.exec();
    assertEquals("OK", reply.get().data());
  }
}
