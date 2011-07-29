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
public class IntegerReply extends Reply {
  public static final char MARKER = ':';
  public final long integer;

  public IntegerReply(long integer) {
    this.integer = integer;
  }

  public void write(OutputStream os) throws IOException {
    os.write(MARKER);
    os.write(String.valueOf(integer).getBytes(Charsets.UTF_8));
    os.write("\r\n".getBytes(Charsets.UTF_8));
  }
}
