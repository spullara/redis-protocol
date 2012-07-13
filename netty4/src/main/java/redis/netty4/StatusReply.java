package redis.netty4;

import java.io.IOException;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;

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
  public void write(ByteBuf os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(status.getBytes(Charsets.UTF_8));
    os.writeBytes(CRLF);
  }

  public String toString() {
    return status;
  }
}
