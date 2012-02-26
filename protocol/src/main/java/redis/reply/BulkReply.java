package redis.reply;

import redis.Command;

import java.io.IOException;
import java.io.OutputStream;

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

  public void write(OutputStream os) throws IOException {
    os.write(MARKER);
    if (bytes == null) {
      os.write(Command.NEG_ONE_WITH_CRLF);
    } else {
      os.write(Command.numToBytes(bytes.length, true));
      os.write(bytes);
      os.write(Command.CRLF);
    }
  }
}
