package redis.client;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Test;
import redis.Command;
import redis.reply.Reply;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    ListenableFuture<? extends Reply> f = client.pipeline(name, cmd);
    Future<Boolean> exec = client.exec();
    exec.get();
    f.get();
  }
}
