package redis.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.embedded.RedisServer;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * https://github.com/spullara/redis-protocol/issues/21
 */
public class Issue21Test {
  private RedisServer redisServer;

  @Before
  public void setup() throws Exception {
    redisServer = new RedisServer();
    redisServer.start();
    // redisServer.getPort()
  }

  @After
  public void tearDown() throws Exception {
    redisServer.stop();
  }

  @Test
  public void testBRPOPLPUSH() throws IOException {
    RedisClient client = new RedisClient("localhost", redisServer.getPort());
    Reply brpoplpush = client.brpoplpush("alskdjflksadf", "alksdjflaksdfj", 1);
    assertTrue(brpoplpush instanceof MultiBulkReply);
    assertEquals(null, ((MultiBulkReply) brpoplpush).data());
  }
}
