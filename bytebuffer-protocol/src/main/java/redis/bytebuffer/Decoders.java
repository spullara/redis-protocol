package redis.bytebuffer;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * More decoding.
 */
public class Decoders {
  public static final char CR = '\r';
  public static final char LF = '\n';
  private static final char ZERO = '0';

  public static long readLong(ByteBuffer is) throws IOException {
    long size = 0;
    int sign = 1;
    int read = is.get();
    if (read == '-') {
      read = is.get();
      sign = -1;
    }
    do {
      if (read == CR) {
        if (is.get() == LF) {
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
      read = is.get();
    } while (true);
    return size * sign;
  }

  public static String decodeUTF8(ByteBuffer bb) {
    try {
      return Charsets.UTF_8.newDecoder().decode(bb).toString();
    } catch (CharacterCodingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static String decodeAscii(ByteBuffer bb) {
    try {
      return Charsets.US_ASCII.newDecoder().decode(bb).toString();
    } catch (CharacterCodingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static String decode(Charset charset, ByteBuffer bb) {
    try {
      return charset.newDecoder().decode(bb).toString();
    } catch (CharacterCodingException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
