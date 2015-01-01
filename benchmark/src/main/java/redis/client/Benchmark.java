package redis.client;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import redis.Command;
import redis.reply.Reply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clone redis-benchmark
 */
public class Benchmark {
  private static final String help = "" +
      "Usage: redis-benchmark [-h <host>] [-p <port>] [-c <clients>] [-n <requests]> [-P <pipelined>] [-d <data size>]\n" +
      "\n" +
      " -h <hostname>      Server hostname (default 127.0.0.1)\n" +
      " -p <port>          Server port (default 6379)\n" +
      " -c <clients>       Number of parallel connections (default 50)\n" +
      " -n <requests>      Total number of requests (default 10000)\n" +
      " -P <outstanding>   Number of outstanding pipeline requests (defaults 1)\n" +
      " -d <size>          Data size of SET/GET value in bytes (default 3)\n";

  @Argument
  private static String h = "127.0.0.1";
  @Argument
  private static Integer p = 6379;
  @Argument
  private static Integer c = 50;
  @Argument
  private static Integer n = 10000;
  @Argument
  private static Integer d = 3;
  @Argument
  private static Integer P = 1;

  private static final long NANOS_PER_MILLI = 1000000l;
  private static final int MILLIS_PER_SECOND = 1000;
  private static ExecutorService es = Executors.newCachedThreadPool();

  private static void benchmark(final String title, final Command command) throws IOException, InterruptedException, ExecutionException {
    System.out.println("====== " + title + " ======");
   final ArrayList<AtomicInteger> bins = new ArrayList<AtomicInteger>() {
      @Override
      public synchronized AtomicInteger get(int index) {
        if (index > size() - 1) {
          int toadd = index - size() + 1;
          for (int i = 0; i < toadd; i++) {
            add(new AtomicInteger(0));
          }
        }
        return super.get(index);
      }
    };
    List<Callable<Void>> benchmarks = new ArrayList<Callable<Void>>(c);
    for (int j = 0; j < c; j++) {
      benchmarks.add(new Callable<Void>() {
        @Override
        public Void call() throws IOException, InterruptedException {
          RedisClient redisClient = new RedisClient(h, p);
          final Semaphore semaphore = new Semaphore(P);
          for (int i = 0; i < n / c; i++) {
            final long commandstart = System.nanoTime();
            if (P == 1) {
              redisClient.execute(title, command);
              long commandend = System.nanoTime();
              int bin = (int) ((commandend - commandstart) / NANOS_PER_MILLI);
              bins.get(bin).incrementAndGet();
            } else {
              semaphore.acquire(1);
              CompletableFuture<? extends Reply> pipeline = redisClient.pipeline(title, command);
              pipeline.thenAcceptAsync(r -> {
                long commandend = System.nanoTime();
                int bin = (int) ((commandend - commandstart) / NANOS_PER_MILLI);
                bins.get(bin).incrementAndGet();
                semaphore.release();
              }, es);
            }
          }
          semaphore.acquire(P);
          redisClient.close();
          return null;
        }
      });
    }
    long start = System.nanoTime();
    List<Future<Void>> futures = es.invokeAll(benchmarks);
    for (Future<Void> future : futures) {
      future.get();
    }
    long end = System.nanoTime();
    double seconds = ((double) end - start) / NANOS_PER_MILLI / MILLIS_PER_SECOND;
    double rate = n / seconds;
    System.out.printf("  %d requests completed in %.2f seconds\n", n, seconds);
    System.out.printf("  %d parallel clients\n", c);
    System.out.printf("  %d outstanding requests\n", P);
    System.out.printf("  %d bytes payload\n", d);
    System.out.println();
    double total = 0;
    int milli = 0;
    for (AtomicInteger bin : bins) {
      total += bin.intValue();
      if (milli++ == 0) {
        System.out.printf("%.2f%% < 1 millisecond\n", total * 100 / n);
      } else {
        System.out.printf("%.2f%% <= %d milliseconds\n", total * 100 / n, milli);
      }
    }
    System.out.println();
    System.out.printf("%.2f requests per second\n\n", rate);
  }


  public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
    List<String> parse;
    try {
      parse = Args.parse(Benchmark.class, args);

      if (parse.size() > 0) {
        benchmark(parse.get(0), new Command(parse.toArray()));
      } else {
        byte[] key = "foo:rand:000000000000".getBytes();
        byte[] counter = "counter:rand:000000000000".getBytes();
        byte[] list = "mylist".getBytes();
        byte[] set = "myset".getBytes();
        byte[] data = new byte[d];
        Object[] objects = new Object[21];
        objects[0] = "MSET";
        for (int i = 1; i < objects.length - 1; i += 2) {
          objects[i] = key;
          objects[i + 1] = data;
        }
        // Delete it all
        RedisClient redisClient = new RedisClient(h, p);
        redisClient.del(new Object[] { key, counter, list, set });
        redisClient.close();

        benchmark("PING (warmup)", new Command(new Object[]{"PING".getBytes()}));
        benchmark("PING", new Command(new Object[]{"PING".getBytes()}));
        benchmark("MSET", new Command(objects));
        benchmark("SET", new Command("SET".getBytes(), key, data));
        benchmark("GET", new Command("GET".getBytes(), key));
        benchmark("INCR", new Command("INCR".getBytes(), counter));
        benchmark("LPUSH", new Command("LPUSH".getBytes(), list, data));
        benchmark("LPOP", new Command("LPOP".getBytes(), list));
        benchmark("SADD", new Command("SADD".getBytes(), set, counter));
        benchmark("SPOP", new Command("SPOP".getBytes(), set));
        benchmark("LPUSH (again, in order to bench LRANGE)", new Command("LPUSH".getBytes(), list, data));
        benchmark("LRANGE (first 100 elements)", new Command("LRANGE".getBytes(), list, "0".getBytes(), "99".getBytes()));
        benchmark("LRANGE (first 300 elements)", new Command("LRANGE".getBytes(), list, "0".getBytes(), "299".getBytes()));
        benchmark("LRANGE (first 450 elements)", new Command("LRANGE".getBytes(), list, "0".getBytes(), "449".getBytes()));
        benchmark("LRANGE (first 600 elements)", new Command("LRANGE".getBytes(), list, "0".getBytes(), "599".getBytes()));
      }
    } catch (IllegalArgumentException e) {
      System.out.print(help);
      System.exit(1);
    } finally {
      es.shutdown();
    }
  }
}
