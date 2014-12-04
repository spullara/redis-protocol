package redis.netty4;

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
    RedisReplyDecoder redisDecoder = new RedisReplyDecoder();
    {
      os = Unpooled.buffer();
      String message = "OK";
      new StatusReply(message).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof StatusReply);
      assertEquals(message, ((StatusReply) receive).data());
    }
    {
      os = Unpooled.buffer();
      String message = "OK";
      new ErrorReply(message).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof ErrorReply);
      assertEquals(message, ((ErrorReply) receive).data());
    }
    {
      os = Unpooled.buffer();
      String message = "OK";
      new BulkReply(Unpooled.wrappedBuffer(message.getBytes())).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof BulkReply);
      assertEquals(message, ((BulkReply) receive).data().toString(Charsets.US_ASCII));
    }
    {
      os = Unpooled.buffer();
      long integer = 999;
      new IntegerReply(integer).write(os);
      receive = redisDecoder.receive(os);
      assertTrue(receive instanceof IntegerReply);
      assertEquals(integer, ((IntegerReply) receive).data().longValue());
    }
    {
      os = Unpooled.buffer();
      String message = "OK";
      long integer = 999;
      new MultiBulkReply(new Reply[] { new StatusReply(message), new ErrorReply(message), new MultiBulkReply(new Reply[] { new StatusReply(message) }),
          new BulkReply(Unpooled.wrappedBuffer(message.getBytes())), new IntegerReply(integer) }).write(os);
      receive = redisDecoder.receive(os);
      assertTrue("received should be MultiBulkReply but is " + receive,receive instanceof MultiBulkReply);
      Reply[] data = (Reply[]) ((MultiBulkReply) receive).data();
      assertTrue(data[0] instanceof StatusReply);
      assertTrue(data[1] instanceof ErrorReply);
      assertEquals(message, ((StatusReply) data[0]).data());
      assertEquals(message, ((ErrorReply) data[1]).data());
      assertTrue(data[2] instanceof MultiBulkReply);
      Reply[] data2 = ((MultiBulkReply) data[2]).data();
      assertTrue(data2[0] instanceof StatusReply);
      assertEquals(message, ((StatusReply) data2[0]).data());
      assertTrue(data[3] instanceof BulkReply);
      assertEquals(message, ((ByteBuf)data[3].data()).toString(Charsets.US_ASCII));
      //assertEquals(message, ((BulkReply) data[3]).data());
      assertTrue(data[4] instanceof IntegerReply);
      assertEquals(integer, ((IntegerReply) data[4]).data().longValue());
    }
  }
}
