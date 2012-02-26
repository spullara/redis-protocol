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
public class BulkReply extends Reply {
  public static final char MARKER = '$';
  public final byte[] bytes;

  public BulkReply(byte[] bytes) {
    this.bytes = bytes;
  }

  public void write(ChannelBuffer os) throws IOException {
    os.writeByte(MARKER);
    if (bytes == null) {
      os.writeBytes(Command.NEG_ONE_AND_CRLF);
    } else {
      os.writeBytes(Command.numAndCRLF(bytes.length));
      os.writeBytes(bytes);
      os.writeBytes(Command.CRLF);
    }
  }
}
