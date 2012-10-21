package redis.netty4;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Reads an ASCII encoded long from a ByteBuf that ends with CRLF.
 */
public class Decoders {
  public static final char CR = '\r';
  public static final char LF = '\n';
  private static final char ZERO = '0';

  public static long readLong(ByteBuf is) throws IOException {
    long result = 0;
    int read = is.readByte();
    boolean positive = true;
    if (read == '-') {
      read = is.readByte();
      positive = false;
    }
    do {
      if (read == CR) {
        if (is.readByte() == LF) {
          return positive ? result : -result;
        } else {
          throw new IllegalArgumentException("CR without LF");
        }
      }
      int value = read - ZERO;
      if (value >= 0 && value < 10) {
        result = result * 10 + value;
      } else {
        throw new IOException("Invalid character in integer");
      }
      read = is.readByte();
    } while (true);
  }
}
