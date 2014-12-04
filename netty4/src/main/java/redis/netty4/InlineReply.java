package redis.netty4;

import static redis.util.Encoding.numToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.io.IOException;

/**
 * Return the reply inline when you get an inline message.
 */
public class InlineReply extends AbstarctReply<Object> {

  public InlineReply(Object o) {
    super(o);
  }

  @Override
  public void write(ByteBuf os) throws IOException {
    Object o = data();
    if (o == null) {
      os.writeBytes(CRLF);
    } else if (o instanceof String) {
      os.writeByte('+');
      os.writeBytes(((String) o).getBytes(CharsetUtil.US_ASCII));
      os.writeBytes(CRLF);
    } else if (o instanceof ByteBuf) {
      os.writeByte('+');
      os.writeBytes(((ByteBuf) o).array());
      os.writeBytes(CRLF);
    } else if (o instanceof byte[]) {
      os.writeByte('+');
      os.writeBytes((byte[]) o);
      os.writeBytes(CRLF);
    } else if (o instanceof Long) {
      os.writeByte(':');
      os.writeBytes(numToBytes((Long) o, true));
    } else {
      os.writeBytes("ERR invalid inline response".getBytes(CharsetUtil.US_ASCII));
      os.writeBytes(CRLF);
    }
  }

  @Override
  public void releaseAll() {
    Object o = data();
    if (o instanceof ByteBuf) {
      ((ByteBuf) o).release();
    }
  }
}
