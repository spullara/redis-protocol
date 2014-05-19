package redis.netty.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.Command;
import redis.embedded.RedisServer;
import redis.netty.BulkReply;
import redis.netty.IntegerReply;
import redis.netty.StatusReply;
import spullara.util.concurrent.Promise;
import spullara.util.functions.Block;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test the base redis client. Default redis required.
 */
public class RedisClientBaseTest {

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
  public void testConnect() throws Exception {
    final CountDownLatch done = new CountDownLatch(1);
    final AtomicBoolean success = new AtomicBoolean();
    final AtomicReference<RedisClientBase> client = new AtomicReference<>();
    Promise<RedisClientBase> connect = RedisClientBase.connect("localhost", redisServer.getPort());
    connect.onSuccess(new Block<RedisClientBase>() {
      @Override
      public void apply(RedisClientBase redisClientBase) {
        success.set(true);
        client.set(redisClientBase);
        done.countDown();
      }
    }).onFailure(new Block<Throwable>() {
      @Override
      public void apply(Throwable throwable) {
        success.set(false);
        done.countDown();
      }
    });
    done.await(5000, TimeUnit.MILLISECONDS);
    final CountDownLatch done2 = new CountDownLatch(1);
    assertTrue(success.get());
    client.get().close().onSuccess(new Block<Void>() {
      @Override
      public void apply(Void aVoid) {
        success.set(true);
        done2.countDown();
      }
    }).onFailure(new Block<Throwable>() {
      @Override
      public void apply(Throwable throwable) {
        success.set(false);
        done2.countDown();
      }
    });
    done2.await(5000, TimeUnit.MILLISECONDS);
    assertTrue(success.get());
  }

  @Test
  public void testConnectFailure() throws Exception {
    final CountDownLatch done = new CountDownLatch(1);
    final AtomicBoolean success = new AtomicBoolean();
    final AtomicReference<Throwable> failure = new AtomicReference<>();
    Promise<RedisClientBase> connect = RedisClientBase.connect("localhost", 6380);
    connect.onSuccess(new Block<RedisClientBase>() {
      @Override
      public void apply(RedisClientBase redisClientBase) {
        success.set(true);
        done.countDown();
      }
    }).onFailure(new Block<Throwable>() {
      @Override
      public void apply(Throwable throwable) {
        success.set(false);
        failure.set(throwable);
        done.countDown();
      }
    });
    done.await(5000, TimeUnit.MILLISECONDS);
    assertFalse(success.get());
    assertTrue("Connection not refused - message is: " + failure.get().getMessage(), failure.get().getMessage().startsWith("Connection refused")
        || failure.get().getMessage().startsWith("Connexion ref"));
  }

  @Test
  public void testError() throws ExecutionException, InterruptedException {
    try {
      RedisClient client = RedisClient.connect("localhost", redisServer.getPort()).get();
      client.set("test", "value").get();
      client.hgetall("test").get();
      fail("Should have failed");
    } catch (ExecutionException ee) {
      assertTrue(ee.getCause() instanceof RedisException);
    }
  }

  @Test
  public void testExecute() throws Exception {
    final CountDownLatch done = new CountDownLatch(1);
    final AtomicBoolean success = new AtomicBoolean();
    RedisClientBase.connect("localhost", redisServer.getPort()).onSuccess(new Block<RedisClientBase>() {
      @Override
      public void apply(final RedisClientBase redisClientBase) {
        redisClientBase.execute(StatusReply.class, new Command("set", "test", "test")).onSuccess(new Block<StatusReply>() {
          @Override
          public void apply(StatusReply reply) {
            if (reply.data().equals("OK")) {
              redisClientBase.execute(BulkReply.class, new Command("get", "test")).onSuccess(new Block<BulkReply>() {
                @Override
                public void apply(BulkReply reply) {
                  if (reply.asAsciiString().equals("test")) {
                    success.set(true);
                  }
                  done.countDown();
                  redisClientBase.close();
                }
              });
            } else {
              done.countDown();
              redisClientBase.close();
            }
          }
        });
      }
    });
    done.await(5000, TimeUnit.MILLISECONDS);
    assertTrue(success.get());
  }

  @Test
  public void testCommands() throws InterruptedException {
    final CountDownLatch done = new CountDownLatch(1);
    final AtomicReference<StatusReply> setOK = new AtomicReference<>();
    final AtomicReference<BulkReply> getTest2 = new AtomicReference<>();
    RedisClient.connect("localhost", redisServer.getPort()).onSuccess(new Block<RedisClient>() {
      @Override
      public void apply(final RedisClient redisClient) {
        redisClient.set("test", "test2").onSuccess(new Block<StatusReply>() {
          @Override
          public void apply(StatusReply statusReply) {
            setOK.set(statusReply);
            redisClient.get("test").onSuccess(new Block<BulkReply>() {
              @Override
              public void apply(BulkReply bulkReply) {
                getTest2.set(bulkReply);
                redisClient.close().onSuccess(new Block<Void>() {
                  @Override
                  public void apply(Void aVoid) {
                    done.countDown();
                  }
                });
              }
            });
          }
        });
      }
    }).onFailure(new Block<Throwable>() {
      @Override
      public void apply(Throwable throwable) {
        throwable.printStackTrace();
      }
    });
    done.await(5000, TimeUnit.MILLISECONDS);
    assertEquals("OK", setOK.get().data());
    assertEquals("test2", getTest2.get().asAsciiString());
  }

  @Test
  public void testSerialPerformance() throws InterruptedException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null)
      return;
    final CountDownLatch done = new CountDownLatch(1);
    final int[] i = new int[1];
    RedisClient.connect("localhost", redisServer.getPort()).onSuccess(new Block<RedisClient>() {
      long start;
      private Block<StatusReply> setBlock;

      void again(RedisClient redisClient) {
        apply(redisClient);
      }

      @Override
      public void apply(final RedisClient redisClient) {
        if (start == 0) {
          setBlock = new Block<StatusReply>() {
            @Override
            public void apply(StatusReply statusReply) {
              again(redisClient);
            }
          };
          start = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - start < 5000) {
          redisClient.set(String.valueOf(i[0]++), "test2").onSuccess(setBlock);
        } else {
          redisClient.close().onSuccess(new Block<Void>() {
            @Override
            public void apply(Void aVoid) {
              done.countDown();
            }
          });
        }
      }
    });
    done.await(6000, TimeUnit.MILLISECONDS);
    System.out.println("Completed " + i[0] / 5 + " per second");
  }

  @Test
  public void testPipelinePerformance() throws InterruptedException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null)
      return;
    final CountDownLatch done = new CountDownLatch(1);
    final AtomicInteger total = new AtomicInteger();
    RedisClient.connect("localhost", redisServer.getPort()).onSuccess(new Block<RedisClient>() {

      @Override
      public void apply(final RedisClient redisClient) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              final Semaphore semaphore = new Semaphore(100);
              Runnable release = new Runnable() {
                @Override
                public void run() {
                  semaphore.release();
                }
              };
              long start = System.currentTimeMillis();
              while (System.currentTimeMillis() - start < 5000) {
                semaphore.acquire();
                String current = String.valueOf(total.getAndIncrement());
                redisClient.set(current, current).ensure(release);
              }
              semaphore.acquire(100);
              done.countDown();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }).start();
      }
    });
    done.await(6000, TimeUnit.MILLISECONDS);
    if (total.get() == 100) {
      fail("Failed to complete any requests");
    }
    System.out.println("Completed " + total.get() / 5 + " per second");
  }

  @Test
  public void testPipelineConcurrency() throws InterruptedException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null)
      return;
    final CountDownLatch done = new CountDownLatch(1);
    final AtomicInteger total = new AtomicInteger();
    final AtomicInteger errors = new AtomicInteger();
    RedisClient.connect("localhost", redisServer.getPort()).onSuccess(new Block<RedisClient>() {
      @Override
      public void apply(final RedisClient redisClient) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              final Semaphore semaphore = new Semaphore(100);
              Runnable release = new Runnable() {
                @Override
                public void run() {
                  semaphore.release();
                }
              };
              long start = System.currentTimeMillis();
              while (System.currentTimeMillis() - start < 5000) {
                semaphore.acquire();
                final String current = String.valueOf(total.getAndIncrement());
                redisClient.set(current, current).ensure(release).onSuccess(new Block<StatusReply>() {
                  @Override
                  public void apply(StatusReply statusReply) {
                    redisClient.get(current).onSuccess(new Block<BulkReply>() {
                      @Override
                      public void apply(BulkReply bulkReply) {
                        String s = bulkReply.asAsciiString();
                        if (!s.equals(current)) {
                          System.out.println(s + " != " + current);
                          errors.incrementAndGet();
                        }
                      }
                    }).onFailure(new Block<Throwable>() {
                      @Override
                      public void apply(Throwable throwable) {
                        errors.incrementAndGet();
                      }
                    });
                  }
                });
              }
              semaphore.acquire(100);
              done.countDown();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }).start();
      }
    });
    done.await(6000, TimeUnit.MILLISECONDS);
    System.out.println("Completed " + total.get() / 5 + " per second");
    assertEquals(0, errors.get());
    if (total.get() == 100) {
      fail("Failed to complete any requests");
    }
  }

  @Test
  public void testPubSub() throws InterruptedException, ExecutionException {
    final CountDownLatch done = new CountDownLatch(2);
    final Promise<Void> wassubscribed = new Promise<>();
    final AtomicReference<byte[]> gotmessage = new AtomicReference<>();
    final AtomicLong listeners = new AtomicLong(0);
    final AtomicBoolean failed = new AtomicBoolean();
    RedisClient.connect("localhost", redisServer.getPort()).onSuccess(new Block<RedisClient>() {
      @Override
      public void apply(final RedisClient redisClient) {
        redisClient.addListener(new ReplyListener() {
          @Override
          public void subscribed(byte[] name, int channels) {
            wassubscribed.set(null);
          }

          @Override
          public void psubscribed(byte[] name, int channels) {
            failed.set(true);
          }

          @Override
          public void unsubscribed(byte[] name, int channels) {
            failed.set(true);
          }

          @Override
          public void punsubscribed(byte[] name, int channels) {
            failed.set(true);
          }

          @Override
          public void message(byte[] channel, byte[] message) {
            gotmessage.set(message);
            redisClient.close();
            done.countDown();
          }

          @Override
          public void pmessage(byte[] pattern, byte[] channel, byte[] message) {
            failed.set(true);
          }
        });
        redisClient.subscribe("test").onSuccess(new Block<Void>() {
          @Override
          public void apply(Void aVoid) {
            RedisClient.connect("localhost", redisServer.getPort()).onSuccess(new Block<RedisClient>() {
              @Override
              public void apply(final RedisClient redisClient) {
                wassubscribed.onSuccess(new Block<Void>() {
                  @Override
                  public void apply(Void aVoid) {
                    redisClient.publish("test", "hello").onSuccess(new Block<IntegerReply>() {
                      @Override
                      public void apply(IntegerReply integerReply) {
                        listeners.set(integerReply.data());
                        redisClient.close();
                        done.countDown();
                      }
                    });
                  }
                });
              }
            });
          }
        });
      }
    });
    done.await(10000, TimeUnit.MILLISECONDS);
    assertTrue(wassubscribed.get() == null);
    assertEquals("hello", new String(gotmessage.get()));
    assertEquals(1, listeners.get());
    assertFalse(failed.get());
  }

  @Test
  public void testPubSubPerformance() throws InterruptedException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null)
      return;
    final CountDownLatch done = new CountDownLatch(1);
    final Semaphore semaphore = new Semaphore(100);
    final AtomicInteger total = new AtomicInteger();
    Promise<RedisClient> redisClient = RedisClient.connect("localhost", redisServer.getPort()).onSuccess(new Block<RedisClient>() {
      @Override
      public void apply(RedisClient redisClient) {
        redisClient.addListener(new MessageListener() {
          @Override
          public void message(byte[] channel, byte[] message) {
            semaphore.release();
            total.incrementAndGet();
          }

          @Override
          public void pmessage(byte[] pattern, byte[] channel, byte[] message) {
          }
        });
        redisClient.subscribe("test").onSuccess(new Block<Void>() {
          @Override
          public void apply(Void aVoid) {
            RedisClient.connect("localhost", redisServer.getPort()).onSuccess(new Block<RedisClient>() {
              @Override
              public void apply(final RedisClient redisClient) {
                new Thread(new Runnable() {
                  @Override
                  public void run() {
                    long start = System.currentTimeMillis();
                    while (System.currentTimeMillis() - start < 5000) {
                      semaphore.acquireUninterruptibly();
                      redisClient.publish("test", "hello");
                    }
                    redisClient.close();
                    done.countDown();
                  }
                }).start();
              }
            });
          }
        });
      }
    });
    done.await(6000, TimeUnit.MILLISECONDS);
    redisClient.onSuccess(new Block<RedisClient>() {
      @Override
      public void apply(RedisClient redisClient) {
        redisClient.close();
      }
    });
    System.out.println(total.get() / 5 + " per second");
  }
}