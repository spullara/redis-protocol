package redis.reply;

import java.io.IOException;
import java.io.OutputStream;

import com.google.common.base.Charsets;

import org.jboss.netty.buffer.ChannelBuffer;

/**
* Created by IntelliJ IDEA.
* User: sam
* Date: 7/29/11
* Time: 10:23 AM
* To change this template use File | Settings | File Templates.
*/
public class ErrorReply implements Reply<String> {
  public static final char MARKER = '-';
  private static final byte[] ERR = "ERR ".getBytes();
  private final String error;

  public ErrorReply(String error) {
    this.error = error;
  }

  @Override
  public String data() {
    return error;
  }

  @Override
  public void write(ChannelBuffer os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(error.getBytes(Charsets.UTF_8));
    os.writeBytes(CRLF);
  }

  public String toString() {
    return error;
  }
}
