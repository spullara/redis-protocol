package redis.reply;

import com.google.common.base.Charsets;

import java.nio.charset.Charset;

/**
* Created by IntelliJ IDEA.
* User: sam
* Date: 7/29/11
* Time: 10:23 AM
* To change this template use File | Settings | File Templates.
*/
public class BulkReply implements Reply<byte[]> {
  public static final char MARKER = '$';
  private final byte[] bytes;

  public BulkReply(byte[] bytes) {
    this.bytes = bytes;
  }

  @Override
  public byte[] data() {
    return bytes;
  }

  public String asAsciiString() {
    return new String(bytes, Charsets.US_ASCII);
  }

  public String asUTF8String() {
    return new String(bytes, Charsets.UTF_8);
  }

  public String asString(Charset charset) {
    return new String(bytes, charset);
  }
}
