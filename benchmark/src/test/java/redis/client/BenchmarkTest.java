package redis.client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.embedded.RedisServer;

/**
 * Run the benchmark with defaults
 * <p/>
 * User: sam Date: 3/18/12 Time: 3:53 PM
 */
public class BenchmarkTest {

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
  public void runBenchmark() throws IOException, ExecutionException, InterruptedException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null)
      return;
    String[] args = { "-p", Integer.toString(redisServer.getPort()) };
    // Benchmark.main(new String[0]);
    Benchmark.main(args);
  }
}
