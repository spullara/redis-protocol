import org.junit.Test;
import redis.reply.BulkReply;
import redis.Command;
import redis.RedisProtocol;
import redis.reply.Reply;
import redis.SocketPool;
import redis.reply.StatusReply;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/19/11
 * Time: 12:32 AM
 */
public class RedisProtocolTest {
  @Test
  public void testSetGet() throws IOException {
    SocketPool socketPool = new SocketPool("localhost", 6379);
    RedisProtocol redisProtocol = new RedisProtocol(socketPool.get());
    Reply setReply = redisProtocol.send(new Command("SET", "test", "value"));
    assertTrue(setReply instanceof StatusReply);
    assertEquals("OK", ((StatusReply) setReply).status);
    Reply getReply = redisProtocol.send(new Command("GET", "test"));
    assertTrue(getReply instanceof BulkReply);
    assertEquals("value", new String(((BulkReply) getReply).bytes));
  }

  @Test
  public void testClientServer() throws IOException, BrokenBarrierException, InterruptedException {
    final ServerSocket serverSocket = new ServerSocket(0);
    Thread thread = new Thread(new Runnable() {
      public void run() {
        try {
          Socket accept = serverSocket.accept();
          RedisProtocol rp = new RedisProtocol(accept);
          Command receive = rp.receive();
          assertEquals("SET", new String(receive.getArguments()[0]));
          assertEquals("test", new String(receive.getArguments()[1]));
          assertEquals("value", new String(receive.getArguments()[2]));
          rp.send(new StatusReply("OK"));
          receive = rp.receive();
          assertEquals("GET", new String(receive.getArguments()[0]));
          assertEquals("test", new String(receive.getArguments()[1]));
          rp.send(new BulkReply("value".getBytes()));
          accept.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
    RedisProtocol rp = new RedisProtocol(new Socket("localhost", serverSocket.getLocalPort()));
    Reply setReply = rp.send(new Command("SET", "test", "value"));
    assertTrue(setReply instanceof StatusReply);
    assertEquals("OK", ((StatusReply) setReply).status);
    Reply getReply = rp.send(new Command("GET", "test"));
    assertTrue(getReply instanceof BulkReply);
    assertEquals("value", new String(((BulkReply) getReply).bytes));
  }

  volatile boolean running = true;
  final int TOTAL = 10000;

  @Test
  public void testEchoBench() throws IOException {
    final ServerSocket serverSocket = new ServerSocket(0);
    Thread thread = new Thread(new Runnable() {
      public void run() {
        try {
          Socket accept = serverSocket.accept();
          RedisProtocol rp = new RedisProtocol(accept);
          while (running) {
            Command receive = rp.receive();
            if (receive != null) {
              rp.send(new StatusReply("OK"));
            }
          }
          accept.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
    RedisProtocol rp = new RedisProtocol(new Socket("localhost", serverSocket.getLocalPort()));
    long start = System.currentTimeMillis();
    for (int i = 0; i < TOTAL; i++) {
      Reply setReply = rp.send(new Command("SET", "test", "value"));
    }
    long diff = System.currentTimeMillis() - start;
    System.out.println("Echo: " + (TOTAL / diff));
    running = false;
    serverSocket.close();
  }

  @Test
  public void testRedisBench() throws IOException {
    RedisProtocol rp = new RedisProtocol(new Socket("localhost", 6379));
    long start = System.currentTimeMillis();
    for (int i = 0; i < TOTAL; i++) {
      Reply setReply = rp.send(new Command("SET", "test", "value"));
    }
    long diff = System.currentTimeMillis() - start;
    System.out.println("Redis: " + (TOTAL / diff));
  }

  final ExecutorService es = Executors.newCachedThreadPool();

  @Test
  public void testMultiEchoBench() throws IOException, ExecutionException, InterruptedException {
    final ServerSocket serverSocket = new ServerSocket(0);
    Thread thread = new Thread(new Runnable() {
      public void run() {
        try {
          while (running) {
            final Socket accept = serverSocket.accept();
            es.execute(new Runnable() {
              public void run() {
                try {
                  RedisProtocol rp = new RedisProtocol(accept);
                  while (running) {
                    Command receive = rp.receive();
                    if (receive != null) {
                      rp.send(new StatusReply("OK"));
                    }
                  }
                  accept.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            });
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
    long start = System.currentTimeMillis();
    List<Callable<Void>> clients = new ArrayList<>();
    final int CLIENTS = 50;
    for (int j = 0; j < CLIENTS; j++) {
      clients.add(new Callable<Void>() {
        public Void call() throws Exception {
          RedisProtocol rp = new RedisProtocol(new Socket("localhost", serverSocket.getLocalPort()));
          for (int i = 0; i < TOTAL; i++) {
            Reply setReply = rp.send(new Command("SET", "test", "value"));
          }
          return null;
        }
      });
    }
    for (Future<Void> future : es.invokeAll(clients)){
      future.get();
    }
    long diff = System.currentTimeMillis() - start;
    System.out.println("Echo: " + (TOTAL * CLIENTS / diff));
    running = false;
    serverSocket.close();
  }

  @Test
  public void testMultiRedisBench() throws IOException, InterruptedException, ExecutionException {
    long start = System.currentTimeMillis();
    List<Callable<Void>> clients = new ArrayList<>();
    final int CLIENTS = 50;
    for (int j = 0; j < CLIENTS; j++) {
      final int finalJ = j;
      clients.add(new Callable<Void>() {
        public Void call() throws Exception {
          RedisProtocol rp = new RedisProtocol(new Socket("192.168.1.90", 6379));
          for (int i = 0; i < TOTAL; i++) {
            Reply setReply = rp.send(new Command("SET", "test" + finalJ, "valuevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevalue"));
          }
          return null;
        }
      });
    }
    for (Future<Void> future : es.invokeAll(clients)){
      future.get();
    }
    long diff = System.currentTimeMillis() - start;
    System.out.println("Redis: " + (TOTAL * CLIENTS / diff));
  }
}

