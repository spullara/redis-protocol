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
public class MultiBulkReply extends Reply {
  public static final char MARKER = '*';
  public final Object[] byteArrays;

  public MultiBulkReply(Object... values) {
    this.byteArrays = values;
  }

  public void write(OutputStream os) throws IOException {
    os.write(MARKER);
    if (byteArrays == null) {
      os.write(String.valueOf(-1).getBytes(Charsets.UTF_8));
      os.write("\r\n".getBytes(Charsets.UTF_8));
    } else {
      os.write(String.valueOf(byteArrays.length).getBytes(Charsets.UTF_8));
      os.write("\r\n".getBytes(Charsets.UTF_8));
      for (Object value : byteArrays) {
        if (value == null) {
          os.write(BulkReply.MARKER);
          os.write(String.valueOf(-1).getBytes(Charsets.UTF_8));
        } else if (value instanceof byte[]) {
          byte[] bytes = (byte[]) value;
          os.write(BulkReply.MARKER);
          os.write(String.valueOf(bytes.length).getBytes(Charsets.UTF_8));
          os.write("\r\n".getBytes(Charsets.UTF_8));
          os.write(bytes);
        } else if (value instanceof Number) {
          os.write(IntegerReply.MARKER);
          os.write(String.valueOf(value).getBytes(Charsets.UTF_8));
        }
        os.write("\r\n".getBytes(Charsets.UTF_8));
      }
    }
  }
}
