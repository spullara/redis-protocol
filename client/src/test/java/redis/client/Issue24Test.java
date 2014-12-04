package redis.client;

import com.google.common.util.concurrent.ListenableFuture;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import redis.embedded.RedisServer;
import redis.reply.ErrorReply;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Issue 24: https://github.com/spullara/redis-protocol/issues/24
 */
public class Issue24Test {

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
  public void testRestoreBadData() throws Exception {
    RedisClient client = new RedisClient("localhost", redisServer.getPort());
    RedisClient.Pipeline pipeline = client.pipeline();
    client.multi();

    // Use something other than dump-specific serialization to cause an error on restore
    ListenableFuture restoreResults = pipeline.restore("testing".getBytes(), 0, "foo".getBytes());
    Future<Boolean> execResults = client.exec();
    assertTrue(execResults.get());

    // The result of restore is supposed to be a ListenableFuture<StatusReply>, which I can't cast
    // to ErrorReply. Should get() throw an Exception instead?
    try {
      ErrorReply reply = (ErrorReply) restoreResults.get();
      fail("Should have thrown an exception");
    } catch (ExecutionException re) {
      Assert.assertTrue(re.getCause() instanceof RedisException);
    } catch (Exception e) {
      fail("Should have thrown an ExecutionException");
    }
  }
}
