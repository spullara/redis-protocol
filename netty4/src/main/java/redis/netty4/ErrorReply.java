package redis.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.io.IOException;

public class ErrorReply extends AbstarctReply<String> {
  public static final char MARKER = '-';
  public static final ErrorReply NYI_REPLY = new ErrorReply("Not yet implemented");

  public ErrorReply(String error) {
    super(error);
  }

  @Override
  public void write(ByteBuf os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(data().getBytes(CharsetUtil.UTF_8));
    os.writeBytes(CRLF);
  }

  public String toString() {
    return data();
  }
}
