package redis.netty4;

import static redis.util.Encoding.numToBytes;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: sam Date: 7/29/11 Time: 10:23 AM To change this template use File | Settings | File Templates.
 */
public class IntegerReply extends AbstarctReply<Long> {
  public static final char MARKER = ':';
  // private final long integer;

  private static IntegerReply[] replies = new IntegerReply[512];
  static {
    for (int i = -255; i < 256; i++) {
      replies[i + 255] = new IntegerReply(i);
    }
  }

  public static IntegerReply integer(long integer) {
    if (integer > -256 && integer < 256) {
      return replies[((int) (integer + 255))];
    } else {
      return new IntegerReply(integer);
    }
  }

  public IntegerReply(long integer) {
    super(integer);
  }

  @Override
  public void write(ByteBuf os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(numToBytes(data().longValue(), true));
  }

  public String toString() {
    return data().toString();
  }
}
