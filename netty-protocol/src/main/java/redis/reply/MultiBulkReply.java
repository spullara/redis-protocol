package redis.reply;

import com.google.common.base.Charsets;
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
public class MultiBulkReply extends Reply {
  public static final char MARKER = '*';
  public final Object[] byteArrays;

  public MultiBulkReply(Object... values) {
    this.byteArrays = values;
  }

  public void write(OutputStream os) throws IOException {
    os.write(MARKER);
    if (byteArrays == null) {
      os.write(Command.NEG_ONE);
      os.write(Command.CRLF);
    } else {
      os.write(Command.numToBytes(byteArrays.length));
      os.write(Command.CRLF);
      for (Object value : byteArrays) {
        if (value == null) {
          os.write(BulkReply.MARKER);
          os.write(Command.NEG_ONE);
        } else if (value instanceof byte[]) {
          byte[] bytes = (byte[]) value;
          os.write(BulkReply.MARKER);
          int length = bytes.length;
          os.write(Command.numToBytes(length));
          os.write(Command.CRLF);
          os.write(bytes);
        } else if (value instanceof Number) {
          os.write(IntegerReply.MARKER);
          os.write(Command.numToBytes(((Number) value).longValue()));
        }
        os.write(Command.CRLF);
      }
    }
  }
}
