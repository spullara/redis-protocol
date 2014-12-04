package redis.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.io.IOException;

public class StatusReply extends AbstarctReply<String> {
  public static final char MARKER = '+';
  public static final StatusReply OK = new StatusReply("OK");
  public static final StatusReply QUIT = new StatusReply("OK");
  // private final String status;
  private final byte[] statusBytes;

  public StatusReply(String status) {
    super(status);
    this.statusBytes = status.getBytes(CharsetUtil.UTF_8);
  }

  // @Override
  // public String data() {
  // return status;
  // }

  @Override
  public void write(ByteBuf os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(statusBytes);
    os.writeBytes(CRLF);
  }

  public String toString() {
    return data();
  }
}
