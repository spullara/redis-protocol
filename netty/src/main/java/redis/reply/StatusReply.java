package redis.reply;

import java.io.IOException;
import java.io.OutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import redis.RedisProtocol;

import static redis.RedisProtocol.toBytes;

/**
* Created by IntelliJ IDEA.
* User: sam
* Date: 7/29/11
* Time: 10:22 AM
* To change this template use File | Settings | File Templates.
*/
public class StatusReply implements Reply<String> {
  public static final char MARKER = '+';
  private final String status;

  public StatusReply(String status) {
    this.status = status;
  }

  @Override
  public String data() {
    return status;
  }

  @Override
  public void write(ChannelBuffer os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(status.getBytes());
    os.writeBytes(CRLF);
  }
}
