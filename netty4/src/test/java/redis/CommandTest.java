package redis;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;
import redis.netty4.BulkReply;
import redis.netty4.MultiBulkReply;
import redis.netty4.RedisReplyDecoder;
import redis.netty4.Reply;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static redis.util.Encoding.numToBytes;

/**
 * Some low level tests
 */
public class CommandTest {
  @Test
  public void testNumToBytes() {
    assertEquals("-12345678", new String(numToBytes(-12345678, false)));
    assertEquals("-1", new String(numToBytes(-1, false)));
    assertEquals("0", new String(numToBytes(0, false)));
    assertEquals("10", new String(numToBytes(10, false)));
    assertEquals("12345678", new String(numToBytes(12345678, false)));
    assertEquals("-1\r\n", new String(numToBytes(-1, true)));
    assertEquals("10\r\n", new String(numToBytes(10, true)));
    assertEquals("12345678\r\n", new String(numToBytes(12345678, true)));
  }

  @Test
  public void benchmark() {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null) return;
    long diff;
    long total;
    {
      // Warm them up
      for (int i = 0; i < 10000000; i++) {
        Long.toString(i).getBytes(Charsets.UTF_8);
        numToBytes(i, true);
      }
    }
    {
      long start = System.currentTimeMillis();
      for (int i = 0; i < 10000000; i++) {
        Long.toString(i).getBytes(Charsets.UTF_8);
      }
      total = diff = System.currentTimeMillis() - start;
    }
    {
      long start = System.currentTimeMillis();
      for (int i = 0; i < 10000000; i++) {
        numToBytes(i, true);
      }
      diff -= System.currentTimeMillis() - start;
    }
    System.out.println(total + ", " + diff);
  }

  @Test
  public void freelsBench() throws IOException {
    if (System.getenv().containsKey("CI") || System.getProperty("CI") != null) return;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(MultiBulkReply.MARKER);
    baos.write("100\r\n".getBytes());
    for (int i = 0; i < 100; i++) {
      baos.write(BulkReply.MARKER);
      baos.write("6\r\n".getBytes());
      baos.write("foobar\r\n".getBytes());
    }
    byte[] multiBulkReply = baos.toByteArray();
    long start = System.currentTimeMillis();
    RedisReplyDecoder redisDecoder = new RedisReplyDecoder(false);
    ByteBuf cb = Unpooled.wrappedBuffer(multiBulkReply);
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 100000; j++) {
        Reply receive = redisDecoder.receive(cb);
        cb.resetReaderIndex();
      }
      long end = System.currentTimeMillis();
      long diff = end - start;
      System.out.println(diff + " " + ((double)diff)/100000);
      start = end;
    }
  }
}
