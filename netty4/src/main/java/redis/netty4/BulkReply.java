package redis.netty4;

import static redis.util.Encoding.numToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.nio.charset.Charset;

public class BulkReply extends AbstarctReply<ByteBuf> {
  public static final BulkReply NIL_REPLY = new BulkReply();

  public static final char MARKER = '$';
  // private final ByteBuf bytes;
  private final int capacity;

  private BulkReply() {
    super(null);
    // bytes = null;
    capacity = -1;
  }

  public BulkReply(byte[] bytes) {
    super(Unpooled.wrappedBuffer(bytes));
    capacity = bytes.length;
  }

  public BulkReply(ByteBuf bytes) {
    super(bytes);
    bytes.retain();// TODO release when ?
    capacity = bytes.capacity();
  }

  public String asAsciiString() {
    if (data() == null)
      return null;
    return data().toString(CharsetUtil.US_ASCII);
  }

  public String asUTF8String() {
    if (data() == null)
      return null;
    return data().toString(CharsetUtil.UTF_8);
  }

  public byte[] asByteArray() {
    if (data() == null)
      return null;
    byte[] res = new byte[data().readableBytes()];
    data().getBytes(data().readerIndex(), res);
    return res;
  }

  public String asString(Charset charset) {
    if (data() == null)
      return null;
    return data().toString(charset);
  }

  @Override
  public void write(ByteBuf os) throws IOException {
    os.writeByte(MARKER);
    os.writeBytes(numToBytes(capacity, true));
    if (capacity > 0) {
      os.writeBytes(data());
      os.writeBytes(CRLF);
    }
  }

  public String toString() {
    return asUTF8String();
  }
}
