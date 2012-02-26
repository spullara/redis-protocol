package redis.netty;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Some low level tests
 */
public class CommandTest {
  @Test
  public void numAndCRLF() {
    assertEquals("-12345678\r\n", new String(Command.numAndCRLF(-12345678)));
    assertEquals("-1\r\n", new String(Command.numAndCRLF(-1)));
    assertEquals("0\r\n", new String(Command.numAndCRLF(0)));
    assertEquals("10\r\n", new String(Command.numAndCRLF(10)));
    assertEquals("12345678\r\n", new String(Command.numAndCRLF(12345678)));
  }
}
