import org.junit.Test;
import redis.RedisProtocol;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/19/11
 * Time: 12:32 AM
 */
public class RedisProtocolTest {
  @Test
  public void testSetGet() throws IOException {
    RedisProtocol redisProtocol = new RedisProtocol("localhost", 6379);
    RedisProtocol.Reply setReply = redisProtocol.send(new RedisProtocol.Message("SET", "test", "value"));
    assertTrue(setReply instanceof RedisProtocol.Reply.StatusReply);
    assertEquals("OK", ((RedisProtocol.Reply.StatusReply) setReply).status);
    RedisProtocol.Reply getReply = redisProtocol.send(new RedisProtocol.Message("GET", "test"));
    assertTrue(getReply instanceof RedisProtocol.Reply.BulkReply);
    assertEquals("value", new String(((RedisProtocol.Reply.BulkReply) getReply).bytes));
  }
}
