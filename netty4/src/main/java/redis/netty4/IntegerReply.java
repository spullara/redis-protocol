package redis.netty4;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

import static redis.util.Encoding.numToBytes;

/**
* Created by IntelliJ IDEA.
* User: sam
* Date: 7/29/11
* Time: 10:23 AM
* To change this template use File | Settings | File Templates.
*/
public class IntegerReply implements Reply<Long> {
  public static final char MARKER = ':';
  public static final IntegerReply ZERO_REPLY = new IntegerReply(0);
  public static final IntegerReply ONE_REPLY = new IntegerReply(1);
  private final long integer;

  public IntegerReply(long integer) {
    this.integer = integer;
  }

  @Override
  public Long data() {
    return integer;
  }

  @Override
  public void write(ByteBuf os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(numToBytes(integer, true));
  }

  public String toString() {
    return data().toString();
  }
}
