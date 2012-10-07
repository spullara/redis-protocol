package redis;

import org.junit.Test;
import redis.bytebuffer.BulkReply;
import redis.bytebuffer.Decoders;
import redis.bytebuffer.DynamicByteBuffer;
import redis.bytebuffer.ErrorReply;
import redis.bytebuffer.IntegerReply;
import redis.bytebuffer.MultiBulkReply;
import redis.bytebuffer.RedisReplyDecoder;
import redis.bytebuffer.Reply;
import redis.bytebuffer.StatusReply;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test reading and writing replies.
 */
public class ReplyTest {
  @Test
  public void testReadWrite() throws IOException {
    DynamicByteBuffer os;
    Reply receive;
    RedisReplyDecoder redisDecoder = new RedisReplyDecoder();
    {
      os = new DynamicByteBuffer();
      String message = "OK";
      new StatusReply(message).write(os);
      receive = redisDecoder.receive(os.asByteBuffer());
      assertTrue(receive instanceof StatusReply);
      assertEquals(message, receive.data());
    }
    {
      os = new DynamicByteBuffer();
      String message = "OK";
      new ErrorReply(message).write(os);
      receive = redisDecoder.receive(os.asByteBuffer());
      assertTrue(receive instanceof ErrorReply);
      assertEquals(message, receive.data());
    }
    {
      os = new DynamicByteBuffer();
      String message = "OK";
      new BulkReply(message.getBytes()).write(os);
      receive = redisDecoder.receive(os.asByteBuffer());
      assertTrue(receive instanceof BulkReply);
      assertEquals(message, Decoders.decodeUTF8((ByteBuffer) receive.data()));
    }
    {
      os = new DynamicByteBuffer();
      long integer = 999;
      new IntegerReply(integer).write(os);
      receive = redisDecoder.receive(os.asByteBuffer());
      assertTrue(receive instanceof IntegerReply);
      assertEquals(integer, receive.data());
    }
    {
      os = new DynamicByteBuffer();
      String message = "OK";
      long integer = 999;
      new MultiBulkReply(new Reply[] {
              new StatusReply(message),
              new ErrorReply(message),
              new MultiBulkReply(new Reply[] { new StatusReply(message)}),
              new BulkReply(message.getBytes()),
              new IntegerReply(integer)}).write(os);
      receive = redisDecoder.receive(os.asByteBuffer());
      assertTrue(receive instanceof MultiBulkReply);
      Reply[] data = (Reply[]) receive.data();
      assertEquals(message, data[0].data());
      assertEquals(message, data[1].data());
      assertTrue(data[2] instanceof MultiBulkReply);
      Reply[] data2 = (Reply[]) data[2].data();
      assertEquals(message, data2[0].data());
      assertEquals(message, Decoders.decodeAscii((ByteBuffer)data[3].data()));
      assertEquals(integer, data[4].data());
    }
  }
}
