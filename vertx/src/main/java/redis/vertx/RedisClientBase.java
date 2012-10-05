package redis.vertx;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetSocket;
import redis.Command;
import redis.netty.RedisDecoder;
import redis.netty.Reply;

/**
 * Base class for Redix Vertx client. Generated client uses the facilties
 * in this class to implement calls.
 */
public class RedisClientBase {
  private final Queue<Handler<Reply>> replies = new LinkedList<Handler<Reply>>();
  private final NetSocket netSocket;

  private RedisClientBase(NetSocket netSocket) {
    this.netSocket = netSocket;
    final RedisDecoder redisDecoder = new RedisDecoder();
    netSocket.dataHandler(new Handler<Buffer>() {
      @Override
      public void handle(Buffer buffer) {
        // Should only get one callback at a time, no sychronization necessary
        try {
          Reply receive = redisDecoder.receive(buffer.getChannelBuffer());
          synchronized (this) {
            replies.poll().handle(receive);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static void connect(String host, int port, Vertx vertx, final Handler<RedisClientBase> handler) {
    vertx.createNetClient().connect(port, host, new Handler<NetSocket>() {
      @Override
      public void handle(NetSocket netSocket) {
        handler.handle(new RedisClientBase(netSocket));
      }
    });
  }

  public void send(Command command, Handler<Reply> replyHandler) {
    ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
    try {
      command.write(channelBuffer);
    } catch (IOException e) {
      throw new AssertionError("Failed to write to memory");
    }
    Buffer buffer = new Buffer(channelBuffer);
    synchronized (this) {
      netSocket.write(buffer);
      replies.offer(replyHandler);
    }
  }
}
