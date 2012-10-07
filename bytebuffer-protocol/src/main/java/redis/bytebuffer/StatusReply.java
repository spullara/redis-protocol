package redis.bytebuffer;

import com.google.common.base.Charsets;

import java.io.IOException;

public class StatusReply implements Reply<String> {
  public static final byte MARKER = '+';
  public static final StatusReply OK = new StatusReply("OK");
  public static final StatusReply QUIT = new StatusReply("OK");
  private final String status;
  private final byte[] statusBytes;

  public StatusReply(String status) {
    this.status = status;
    this.statusBytes = status.getBytes(Charsets.UTF_8);
  }

  @Override
  public String data() {
    return status;
  }

  @Override
  public void write(DynamicByteBuffer os) throws IOException {
    os.put(MARKER);
    os.put(statusBytes);
    os.put(CRLF);
  }

  public String toString() {
    return status;
  }
}
