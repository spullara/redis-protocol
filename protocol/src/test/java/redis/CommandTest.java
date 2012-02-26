package redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.base.Charsets;

import org.junit.Test;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Some low level tests
 */
public class CommandTest {
  @Test
  public void numToBytes() {
    assertEquals("-12345678", new String(Command.numToBytes(-12345678)));
    assertEquals("-1", new String(Command.numToBytes(-1)));
    assertEquals("0", new String(Command.numToBytes(0)));
    assertEquals("10", new String(Command.numToBytes(10)));
    assertEquals("12345678", new String(Command.numToBytes(12345678)));
  }

  @Test
  public void benchmark() {
    long diff;
    long total;
    {
      // Warm them up
      for (int i = 0; i < 10000000; i++) {
        Long.toString(i).getBytes(Charsets.UTF_8);
        Command.numToBytes(i);
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
        Command.numToBytes(i);
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
    for (int i = 0; i < 100; i++) {
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
