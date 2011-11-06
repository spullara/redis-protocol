package redis.reply;

import com.google.common.base.Charsets;

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
      os.write(String.valueOf(-1).getBytes(Charsets.UTF_8));
    } else {
      os.write(String.valueOf(bytes.length).getBytes(Charsets.UTF_8));
      os.write("\r\n".getBytes(Charsets.UTF_8));
      os.write(bytes);
    }
    os.write("\r\n".getBytes(Charsets.UTF_8));
  }
}
