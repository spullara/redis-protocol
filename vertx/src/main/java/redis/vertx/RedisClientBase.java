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
 * Base class for Redix Vertx client. Generated client would use the facilties
 * in this class to implement typed commands.
 */
public class RedisClientBase {
  private final Queue<Handler<Reply>> replies = new LinkedList<Handler<Reply>>();
  private final NetSocket netSocket;

  private RedisClientBase(NetSocket netSocket) {
    this.netSocket = netSocket;
    final RedisDecoder redisDecoder = new RedisDecoder();
    netSocket.dataHandler(new Handler<Buffer>() {

      private ChannelBuffer read = null;

      @Override
      public void handle(Buffer buffer) {
        // Should only get one callback at a time, no sychronization necessary
        ChannelBuffer channelBuffer = buffer.getChannelBuffer();
        if (read != null) {
          // Merge the new buffer with the previous buffer
          channelBuffer = ChannelBuffers.copiedBuffer(read, channelBuffer);
          read = null;
        }
        try {
          // Attempt to decode a full reply from the channelbuffer
          Reply receive = redisDecoder.receive(channelBuffer);
          synchronized (this) {
            // If successful, grab the matching handler
            replies.poll().handle(receive);
          }
          // May be more to read
          if (channelBuffer.readable()) {
            // More than one message in the buffer, need to be careful
            handle(new Buffer(ChannelBuffers.copiedBuffer(channelBuffer)));
          }
        } catch (IOException e) {
          e.printStackTrace();
        } catch (IndexOutOfBoundsException th) {
          // Got to catch decoding fails and try it again
          channelBuffer.resetReaderIndex();
          read = ChannelBuffers.copiedBuffer(channelBuffer);
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
    // Serialize the buffer before writing it
    ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
    try {
      command.write(channelBuffer);
    } catch (IOException e) {
      throw new AssertionError("Failed to write to memory");
    }
    Buffer buffer = new Buffer(channelBuffer);
    synchronized (this) {
      // The order read must match the order written
      netSocket.write(buffer);
      replies.offer(replyHandler);
    }
  }
}
