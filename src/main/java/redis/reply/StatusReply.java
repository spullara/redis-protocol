package redis.reply;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.io.OutputStream;

/**
* Created by IntelliJ IDEA.
* User: sam
* Date: 7/29/11
* Time: 10:22 AM
* To change this template use File | Settings | File Templates.
*/
public class StatusReply extends Reply {
  public static final char MARKER = '+';
  public final String status;

  public StatusReply(String status) {
    this.status = status;
  }

  public void write(OutputStream os) throws IOException {
    os.write(MARKER);
    os.write(status.getBytes(Charsets.UTF_8));
    os.write("\r\n".getBytes(Charsets.UTF_8));
  }
}
