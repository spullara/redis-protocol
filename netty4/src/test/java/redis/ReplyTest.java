package redis;

import java.io.IOException;

import com.google.common.base.Charsets;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;
import redis.netty.Reply;
import redis.netty.RedisDecoder;
import redis.netty.BulkReply;
import redis.netty.ErrorReply;
import redis.netty.IntegerReply;
import redis.netty.MultiBulkReply;
import redis.netty.StatusReply;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test reading and writing replies.
 */
public class ReplyTest {
  @Test
  public void testReadWrite() throws IOException {
    ChannelBuffer os;
    Reply receive;
    RedisDecoder redisDecoder = new RedisDecoder();
    {
      os = ChannelBuffers.dynamicBuffer();
      String message = "OK";
      new StatusReply(message).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof StatusReply);
      assertEquals(message, receive.data());
    }
    {
      os = ChannelBuffers.dynamicBuffer();
      String message = "OK";
      new ErrorReply(message).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof ErrorReply);
      assertEquals(message, receive.data());
    }
    {
      os = ChannelBuffers.dynamicBuffer();
      String message = "OK";
      new BulkReply(ChannelBuffers.wrappedBuffer(message.getBytes())).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof BulkReply);
      assertEquals(message, ((ChannelBuffer)receive.data()).toString(Charsets.US_ASCII));
    }
    {
      os = ChannelBuffers.dynamicBuffer();
      long integer = 999;
      new IntegerReply(integer).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof IntegerReply);
      assertEquals(integer, receive.data());
    }
    {
      os = ChannelBuffers.dynamicBuffer();
      String message = "OK";
      long integer = 999;
      new MultiBulkReply(new Reply[] {
              new StatusReply(message),
              new ErrorReply(message),
              new MultiBulkReply(new Reply[] { new StatusReply(message)}),
              new BulkReply(ChannelBuffers.wrappedBuffer(message.getBytes())),
              new IntegerReply(integer)}).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof MultiBulkReply);
      Reply[] data = (Reply[]) receive.data();
      assertEquals(message, data[0].data());
      assertEquals(message, data[1].data());
      assertTrue(data[2] instanceof MultiBulkReply);
      Reply[] data2 = (Reply[]) data[2].data();
      assertEquals(message, data2[0].data());
      assertEquals(message, ((ChannelBuffer)data[3].data()).toString(Charsets.US_ASCII));
      assertEquals(integer, data[4].data());
    }
  }
}
