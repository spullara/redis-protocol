package redis.client;

import com.google.common.base.Charsets;
import org.junit.Test;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class Issue25Test {

  private RedisClient client;

  public List scriptExists(String... scripts) {
    Reply reply = client.script_exists(scripts);
    if (reply instanceof MultiBulkReply) {
      MultiBulkReply mbr = (MultiBulkReply) reply;
      return mbr.asStringList(Charsets.UTF_8);
    }
    return new ArrayList();
  }

  @Test
  public void testScriptExists() throws IOException {
    client = new RedisClient("localhost", 6379);
    List test = scriptExists("test", "test2");
    assertEquals(2, test.size());
    client.close();
  }
}