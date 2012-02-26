package redis.reply;

import redis.Command;
import redis.RedisProtocol;

import java.io.IOException;
import java.io.InputStream;
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

  public MultiBulkReply(InputStream is) throws IOException {
    int size = RedisProtocol.readInteger(is);
    byteArrays = new Object[size];
    for (int i = 0; i < size; i++) {
      int read = is.read();
      if (read == BulkReply.MARKER) {
        byteArrays[i] = RedisProtocol.readBytes(is);
      } else if (read == IntegerReply.MARKER) {
        byteArrays[i] = RedisProtocol.readInteger(is);
      } else {
        throw new IOException("Unexpected character in stream: " + read);
      }
    }
  }

  public MultiBulkReply(Object... values) {
    this.byteArrays = values;
  }

  public void write(OutputStream os) throws IOException {
    os.write(MARKER);
    if (byteArrays == null) {
      os.write(Command.NEG_ONE_WITH_CRLF);
    } else {
      os.write(Command.numToBytes(byteArrays.length, true));
      for (Object value : byteArrays) {
        if (value == null) {
          os.write(BulkReply.MARKER);
          os.write(Command.NEG_ONE_WITH_CRLF);
        } else if (value instanceof byte[]) {
          byte[] bytes = (byte[]) value;
          os.write(BulkReply.MARKER);
          int length = bytes.length;
          os.write(Command.numToBytes(length, true));
          os.write(bytes);
          os.write(Command.CRLF);
        } else if (value instanceof Number) {
          os.write(IntegerReply.MARKER);
          os.write(Command.numToBytes(((Number) value).longValue(), true));
        }
      }
    }
  }
}
