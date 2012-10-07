package redis.bytebuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static redis.bytebuffer.Decoders.decode;
import static redis.bytebuffer.Decoders.decodeAscii;
import static redis.bytebuffer.Decoders.decodeUTF8;
import static redis.util.Encoding.numToBytes;

public class BulkReply implements Reply<ByteBuffer> {
  public static final BulkReply NIL_REPLY = new BulkReply();

  public static final byte MARKER = '$';
  private final ByteBuffer bytes;
  private final int size;

  private BulkReply() {
    bytes = null;
    size = -1;
  }

  public BulkReply(byte[] bytes) {
    this.bytes = ByteBuffer.wrap(bytes);
    size = bytes.length;
  }

  protected BulkReply(ByteBuffer bytes) {
    this.bytes = bytes;
    size = bytes.limit() - bytes.remaining();
  }

  @Override
  public ByteBuffer data() {
    return bytes;
  }

  public String asAsciiString() {
    if (bytes == null) return null;
    return decodeAscii(bytes);
  }

  public String asUTF8String() {
    if (bytes == null) return null;
    return decodeUTF8(bytes);
  }

  public String asString(Charset charset) {
    if (bytes == null) return null;
    return decode(charset, bytes);
  }

  @Override
  public void write(DynamicByteBuffer os) throws IOException {
    os.put(MARKER);
    os.put(numToBytes(size, true));
    if (size > 0) {
      os.put(bytes);
    }
    os.put(CRLF);
  }

  public String toString() {
    return asUTF8String();
  }
}
