package redis.netty;

import org.junit.Test;

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
}
