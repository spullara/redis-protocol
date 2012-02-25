package redis.netty;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;

/**
* Created by IntelliJ IDEA.
* User: sam
* Date: 7/29/11
* Time: 10:23 AM
* To change this template use File | Settings | File Templates.
*/
public class IntegerReply extends Reply {
  public static final char MARKER = ':';
  public final long integer;

  public IntegerReply(long integer) {
    this.integer = integer;
  }

  public void write(ChannelBuffer os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(Command.numToBytes(integer));
    os.writeBytes(Command.CRLF);
  }
}
