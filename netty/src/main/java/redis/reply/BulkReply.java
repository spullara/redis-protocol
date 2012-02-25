package redis.reply;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import redis.Command;

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
      os.writeBytes(Command.NEG_ONE);
    } else {
      os.writeBytes(Command.numToBytes(bytes.length));
      os.writeBytes(Command.CRLF);
      os.writeBytes(bytes);
    }
    os.writeBytes(Command.CRLF);
  }
}
