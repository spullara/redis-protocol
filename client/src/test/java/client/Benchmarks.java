package client;

import org.jredis.RedisException;

import java.io.IOException;

/**
 * Head to head.
 */
public class Benchmarks {
  public static void main(String[] args) throws IOException, RedisException {
    RedisClientTest test = new RedisClientTest();
    test.benchmark();
    test.jredisBenchmark();
    test.jedisBenchmark();
    test.benchmark();
    test.jredisBenchmark();
    test.jedisBenchmark();
    test.benchmark();
    test.jredisBenchmark();
    test.jedisBenchmark();
  }
}
