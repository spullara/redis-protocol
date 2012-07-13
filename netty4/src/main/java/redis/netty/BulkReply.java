package redis.netty;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBuf;
import redis.util.Encoding;

import static redis.util.Encoding.numToBytes;

public class BulkReply implements Reply<ByteBuf> {
  public static final char MARKER = '$';
  private final ByteBuf bytes;

  public BulkReply(ByteBuf bytes) {
    this.bytes = bytes;
  }

  @Override
  public ByteBuf data() {
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
  public void write(ByteBuf os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(numToBytes(bytes.capacity(), true));
    os.writeBytes(bytes);
    os.writeBytes(CRLF);
  }

  public String toString() {
    return asUTF8String();
  }
}
