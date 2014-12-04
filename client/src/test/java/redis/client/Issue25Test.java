package redis.client;

import com.google.common.base.Charsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.embedded.RedisServer;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class Issue25Test {

  private RedisClient client;

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
    client = new RedisClient("localhost", redisServer.getPort());
    List test = scriptExists("test", "test2");
    assertEquals(2, test.size());
    client.close();
  }
}