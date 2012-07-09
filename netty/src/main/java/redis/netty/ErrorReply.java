package redis.netty;

import java.io.IOException;

import com.google.common.base.Charsets;

import org.jboss.netty.buffer.ChannelBuffer;

public class ErrorReply implements Reply<String> {
  public static final char MARKER = '-';
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
