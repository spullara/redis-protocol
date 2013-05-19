package redis;

import com.google.common.base.Charsets;
import org.junit.Test;
import redis.reply.BulkReply;
import redis.reply.MultiBulkReply;

import java.io.ByteArrayInputStream;
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
        byte[] bytes = Long.toString(i).getBytes(Charsets.UTF_8);
        byte[] bytes1 = numToBytes(i, false);
        assertEquals(new String(bytes), new String(bytes1));
      }
    }
    {
      long start = System.currentTimeMillis();
      for (int i = 0; i < 100000000; i++) {
        Long.toString(i).getBytes(Charsets.UTF_8);
      }
      total = diff = System.currentTimeMillis() - start;
    }
    {
      long start = System.currentTimeMillis();
      for (int i = 0; i < 100000000; i++) {
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
    for (int i = 0; i < 10; i++) {
      ByteArrayInputStream is = new ByteArrayInputStream(multiBulkReply);
      for (int j = 0; j < 100000; j++) {
        RedisProtocol.receive(is);
        is.reset();
      }
      long end = System.currentTimeMillis();
      long diff = end - start;
      System.out.println(diff + " " + ((double)diff)/100000);
      start = end;
    }
  }
}
