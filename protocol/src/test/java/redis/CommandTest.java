package redis;

import com.google.common.base.Charsets;
import org.junit.Test;
import redis.reply.MultiBulkReply;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * Some low level tests
 */
public class CommandTest {
  @Test
  public void numToBytes() {
    assertEquals("-12345678", new String(Command.numToBytes(-12345678, false)));
    assertEquals("-1", new String(Command.numToBytes(-1, false)));
    assertEquals("0", new String(Command.numToBytes(0, false)));
    assertEquals("10", new String(Command.numToBytes(10, false)));
    assertEquals("12345678", new String(Command.numToBytes(12345678, false)));
    assertEquals("-1\r\n", new String(Command.numToBytes(-1, true)));
    assertEquals("10\r\n", new String(Command.numToBytes(10, true)));
    assertEquals("12345678\r\n", new String(Command.numToBytes(12345678, true)));
  }

  @Test
  public void benchmark() {
    long diff;
    long total;
    {
      // Warm them up
      for (int i = 0; i < 10000000; i++) {
        Long.toString(i).getBytes(Charsets.UTF_8);
        Command.numToBytes(i, true);
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
        Command.numToBytes(i, true);
      }
      diff -= System.currentTimeMillis() - start;
    }
    System.out.println(total + ", " + diff);
  }

  @Test
  public void freelsBench() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[][] replies = new byte[100][];
    for (int i = 0; i < replies.length; i++) {
      replies[i] = "foobar".getBytes();
    }
    new MultiBulkReply(replies).write(baos);
    byte[] multiBulkReply = baos.toByteArray();
    long start = System.currentTimeMillis();
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 100000; j++) {
        RedisProtocol.receive(new ByteArrayInputStream(multiBulkReply));
      }
      long end = System.currentTimeMillis();
      long diff = end - start;
      System.out.println(diff + " " + ((double)diff)/100000);
      start = end;
    }
  }
}
