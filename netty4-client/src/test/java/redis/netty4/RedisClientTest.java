package redis.netty4;

import static junit.framework.Assert.assertTrue;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.embedded.RedisServer;
import redis.util.Encoding;
import spullara.util.functions.Block;

/**
 * Some tests for the client.
 */
public class RedisClientTest {

  public static final long CALLS = 1000000;

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
  public void testSetGet() throws InterruptedException {
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final AtomicBoolean success = new AtomicBoolean();
    final AtomicBoolean matches = new AtomicBoolean();
    connect("localhost", redisServer.getPort()).addListener(new GenericFutureListener<Future<RedisClient>>() {

      @Override
      public void operationComplete(Future<RedisClient> future) throws Exception {
        if (future.isSuccess()) {
          final RedisClient client = future.getNow();

          client.set("test", "value").addListener(new GenericFutureListener<Future<StatusReply>>() {

            @Override
            public void operationComplete(Future<StatusReply> future) throws Exception {
              if (future.isSuccess()) {
                StatusReply reply = future.getNow();
                success.set(reply.data().equals("OK"));
                client.get("test").addListener(new GenericFutureListener<Future<BulkReply>>() {
                  @Override
                  public void operationComplete(Future<BulkReply> future) {

                    if (future.isSuccess()) {
                      matches.set(future.getNow().asAsciiString().equals("value"));
                    }

                    countDownLatch.countDown();
                  }
                });
              } else {
                countDownLatch.countDown();
              }

            }
          });

        } else {
          countDownLatch.countDown();

        }

      }
    });

    countDownLatch.await();
    assertTrue(success.get());
    assertTrue(matches.get());
  }

  private Future<RedisClient> connect(String string, int i) {
    EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    final Promise<RedisClient> res = eventLoopGroup.next().newPromise();
    final RedisClient redisClient = new RedisClient(eventLoopGroup);
    redisClient.connect(string, i).addListener(new ChannelFutureListener() {

      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          res.setSuccess(redisClient);
        } else {
          res.setFailure(future.cause());
        }

      }
    });
    return res;
  }

  // TODO port that
  // @Test
  // public void testBenchmark() throws InterruptedException {
  // if (System.getenv().containsKey("CI") || System.getProperty("CI") != null)
  // return;
  // final CountDownLatch countDownLatch = new CountDownLatch(1);
  // final AtomicInteger calls = new AtomicInteger(0);
  //
  // Block<RedisClientBase> benchmark = new Block<RedisClientBase>() {
  // @Override
  // public void apply(final RedisClientBase client) {
  // int i = calls.getAndIncrement();
  // if (i == CALLS) {
  // countDownLatch.countDown();
  // } else {
  // final Block<RedisClientBase> thisBenchmark = this;
  // client.send(new Command("SET", Encoding.numToBytes(i), "value")).onSuccess(new Block<Reply>() {
  // @Override
  // public void apply(Reply reply) {
  // thisBenchmark.apply(client);
  // }
  // });
  // }
  // }
  // };
  //
  // long start = System.currentTimeMillis();
  // connect("localhost", 6379).onSuccess(benchmark).onFailure(new Block<Throwable>() {
  // @Override
  // public void apply(Throwable throwable) {
  // countDownLatch.countDown();
  // }
  // });
  // countDownLatch.await();
  // System.out.println("Netty4: " + CALLS * 1000 / (System.currentTimeMillis() - start));
  // }
  //
  // @Test
  // public void testPipelinedBenchmark() throws ExecutionException, InterruptedException {
  // if (System.getenv().containsKey("CI") || System.getProperty("CI") != null)
  // return;
  // long start = System.currentTimeMillis();
  // RedisClientBase client = connect("localhost", 6379).get();
  // final Semaphore semaphore = new Semaphore(100);
  // for (int i = 0; i < CALLS; i++) {
  // semaphore.acquire();
  // client.send(new Command("SET", Encoding.numToBytes(i), "value")).ensure(new Runnable() {
  // @Override
  // public void run() {
  // semaphore.release();
  // }
  // });
  // }
  // semaphore.acquire(50);
  // System.out.println("Netty4 pipelined: " + CALLS * 1000 / (System.currentTimeMillis() - start));
  // }
}
