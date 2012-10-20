package redis.netty4;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Reads an ASCII encoded long from a ByteBuf.
 */
public class Decoders {
  public static final char CR = '\r';
  public static final char LF = '\n';
  private static final char ZERO = '0';

  public static long readLong(ByteBuf is) throws IOException {
    long size = 0;
    int sign = 1;
    int read = is.readByte();
    if (read == '-') {
      read = is.readByte();
      sign = -1;
    }
    do {
      if (read == CR) {
        if (is.readByte() == LF) {
          break;
        }
      }
      int value = read - ZERO;
      if (value >= 0 && value < 10) {
        size *= 10;
        size += value;
      } else {
        throw new IOException("Invalid character in integer");
      }
      read = is.readByte();
    } while (true);
    return size * sign;
  }
}
