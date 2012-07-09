package redis.netty;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import redis.util.Encoding;

import static redis.util.Encoding.numToBytes;

public class BulkReply implements Reply<ChannelBuffer> {
  public static final char MARKER = '$';
  private final ChannelBuffer bytes;

  public BulkReply(ChannelBuffer bytes) {
    this.bytes = bytes;
  }

  @Override
  public ChannelBuffer data() {
    return bytes;
  }

  public String asAsciiString() {
    if (bytes == null) return null;
    return bytes.toString(Charsets.US_ASCII);
  }

  public String asUTF8String() {
    if (bytes == null) return null;
    return bytes.toString(Charsets.UTF_8);
  }

  public String asString(Charset charset) {
    if (bytes == null) return null;
    return bytes.toString(charset);
  }

  @Override
  public void write(ChannelBuffer os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(numToBytes(bytes.capacity(), true));
    os.writeBytes(bytes);
    os.writeBytes(CRLF);
  }

  public String toString() {
    return asUTF8String();
  }
}
