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
public class ErrorReply extends Reply {
  public static final char MARKER = '-';
  private static final byte[] ERR = "ERR ".getBytes(Charsets.UTF_8);
  public final String error;

  public ErrorReply(String error) {
    this.error = error;
  }

  public void write(OutputStream os) throws IOException {
    os.write(MARKER);
    os.write(ERR);
    os.write(error.getBytes(Charsets.UTF_8));
    os.write(Command.CRLF);
  }
}
