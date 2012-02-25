package redis.netty;

import java.io.IOException;

import com.google.common.base.Charsets;

import org.jboss.netty.buffer.ChannelBuffer;

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

  public void write(ChannelBuffer os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(status.getBytes(Charsets.UTF_8));
    os.writeBytes(Command.CRLF);
  }
}
