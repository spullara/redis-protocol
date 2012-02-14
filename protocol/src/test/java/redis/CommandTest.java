package redis;

import com.google.common.base.Charsets;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Some low level tests
 */
public class CommandTest {
  @Test
  public void numToBytes() {
    assertEquals("12345678", new String(Command.numToBytes(12345678)));
    assertEquals("-12345678", new String(Command.numToBytes(-12345678)));
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
}
