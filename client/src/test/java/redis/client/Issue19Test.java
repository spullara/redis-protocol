package redis.client;

import com.google.common.base.Charsets;
import org.junit.Test;
import redis.Command;
import redis.reply.Reply;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * https://github.com/spullara/redis-protocol/issues/19
 */
public class Issue19Test {
  @Test
  public void testExecuteSyntaxError() throws IOException, InterruptedException, ExecutionException {
    RedisClient client = new RedisClient("localhost", 6379);
    client.multi();
    String name = "ZADD";
    // Wrong number of arguments for zadd command
    Command cmd = new Command(name.getBytes(Charsets.UTF_8),"foo");
    CompletableFuture<? extends Reply> f = client.pipeline(name, cmd);
    try {
      // Fixed in 2.6.5
      if (client.version < 20605) {
        Future<Boolean> exec = client.exec();
        exec.get();
        f.get();
      } else {
        f.get();
      }
      fail("Should have gotten an error");
    } catch (ExecutionException re) {
      Throwable cause = re.getCause();
      assertTrue(cause instanceof RedisException);
      assertTrue(cause.getMessage().startsWith("ERR wrong number"));
    }
  }
}
