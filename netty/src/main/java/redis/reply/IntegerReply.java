package redis.reply;

import java.io.IOException;
import java.io.OutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import redis.RedisProtocol;

/**
* Created by IntelliJ IDEA.
* User: sam
* Date: 7/29/11
* Time: 10:23 AM
* To change this template use File | Settings | File Templates.
*/
public class IntegerReply implements Reply<Long> {
  public static final char MARKER = ':';
  private final long integer;

  public IntegerReply(long integer) {
    this.integer = integer;
  }

  @Override
  public Long data() {
    return integer;
  }

  @Override
  public void write(ChannelBuffer os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(RedisProtocol.toBytes(integer));
    os.writeBytes(CRLF);
  }
}
