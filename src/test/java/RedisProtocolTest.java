import org.junit.Test;
import redis.Command;
import redis.RedisProtocol;
import redis.Reply;
import redis.SocketPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;

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
    assertTrue(setReply instanceof Reply.StatusReply);
    assertEquals("OK", ((Reply.StatusReply) setReply).status);
    Reply getReply = redisProtocol.send(new Command("GET", "test"));
    assertTrue(getReply instanceof Reply.BulkReply);
    assertEquals("value", new String(((Reply.BulkReply) getReply).bytes));
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
          rp.send(new Reply.StatusReply("OK"));
          receive = rp.receive();
          assertEquals("GET", new String(receive.getArguments()[0]));
          assertEquals("test", new String(receive.getArguments()[1]));
          rp.send(new Reply.BulkReply("value".getBytes()));
          accept.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
    RedisProtocol rp = new RedisProtocol(new Socket("localhost", serverSocket.getLocalPort()));
    Reply setReply = rp.send(new Command("SET", "test", "value"));
    assertTrue(setReply instanceof Reply.StatusReply);
    assertEquals("OK", ((Reply.StatusReply) setReply).status);
    Reply getReply = rp.send(new Command("GET", "test"));
    assertTrue(getReply instanceof Reply.BulkReply);
    assertEquals("value", new String(((Reply.BulkReply) getReply).bytes));
  }

  volatile boolean running = true;

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
            rp.send(new Reply.StatusReply("OK"));
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
    final int TOTAL = 10000;
    for (int i = 0; i < TOTAL; i++) {
      Reply setReply = rp.send(new Command("SET", "test", "value"));
    }
    long diff = System.currentTimeMillis() - start;
    System.out.println(TOTAL / diff);
    running = false;
    serverSocket.close();
  }
}

