package redis;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;
import redis.netty4.BulkReply;
import redis.netty4.ErrorReply;
import redis.netty4.IntegerReply;
import redis.netty4.MultiBulkReply;
import redis.netty4.RedisReplyDecoder;
import redis.netty4.Reply;
import redis.netty4.StatusReply;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test reading and writing replies.
 */
public class ReplyTest {
  @Test
  public void testReadWrite() throws IOException {
    ByteBuf os;
    Reply receive;
    RedisReplyDecoder redisDecoder = new RedisReplyDecoder(false);
    {
      os = Unpooled.buffer();
      String message = "OK";
      new StatusReply(message).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof StatusReply);
      assertEquals(message, receive.data());
    }
    {
      os = Unpooled.buffer();
      String message = "OK";
      new ErrorReply(message).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof ErrorReply);
      assertEquals(message, receive.data());
    }
    {
      os = Unpooled.buffer();
      String message = "OK";
      new BulkReply(Unpooled.wrappedBuffer(message.getBytes())).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof BulkReply);
      assertEquals(message, ((ByteBuf)receive.data()).toString(Charsets.US_ASCII));
    }
    {
      os = Unpooled.buffer();
      long integer = 999;
      new IntegerReply(integer).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof IntegerReply);
      assertEquals(integer, receive.data());
    }
    {
      os = Unpooled.buffer();
      String message = "OK";
      long integer = 999;
      new MultiBulkReply(new Reply[] {
              new StatusReply(message),
              new ErrorReply(message),
              new MultiBulkReply(new Reply[] { new StatusReply(message)}),
              new BulkReply(Unpooled.wrappedBuffer(message.getBytes())),
              new IntegerReply(integer)}).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof MultiBulkReply);
      Reply[] data = (Reply[]) receive.data();
      assertEquals(message, data[0].data());
      assertEquals(message, data[1].data());
      assertTrue(data[2] instanceof MultiBulkReply);
      Reply[] data2 = (Reply[]) data[2].data();
      assertEquals(message, data2[0].data());
      assertEquals(message, ((ByteBuf)data[3].data()).toString(Charsets.US_ASCII));
      assertEquals(integer, data[4].data());
    }
  }
}
