package redis.bytebuffer;

import com.google.common.base.Charsets;

import java.io.IOException;

public class ErrorReply implements Reply<String> {
  public static final byte MARKER = '-';
  public static final ErrorReply NYI_REPLY = new ErrorReply("Not yet implemented");
  private final String error;

  public ErrorReply(String error) {
    this.error = error;
  }

  @Override
  public String data() {
    return error;
  }

  @Override
  public void write(DynamicByteBuffer os) throws IOException {
    os.put(MARKER);
    os.put(error.getBytes(Charsets.UTF_8));
    os.put(CRLF);
  }

  public String toString() {
    return error;
  }
}
