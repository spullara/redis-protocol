package redis.client;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Run the benchmark with defaults
 * <p/>
 * User: sam
 * Date: 3/18/12
 * Time: 3:53 PM
 */
public class BenchmarkTest {
  @Test
  public void runBenchmark() throws IOException, ExecutionException, InterruptedException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null) return;
    Benchmark.main(new String[0]);
  }
}
