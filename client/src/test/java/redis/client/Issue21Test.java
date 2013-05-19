package redis.client;

import org.junit.Test;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * https://github.com/spullara/redis-protocol/issues/21
 */
public class Issue21Test {
  @Test
  public void testBRPOPLPUSH() throws IOException {
    RedisClient client = new RedisClient("localhost", 6379);
    Reply brpoplpush = client.brpoplpush("alskdjflksadf", "alksdjflaksdfj", 1);
    assertTrue(brpoplpush instanceof MultiBulkReply);
    assertEquals(null, ((MultiBulkReply) brpoplpush).data());
  }
}
