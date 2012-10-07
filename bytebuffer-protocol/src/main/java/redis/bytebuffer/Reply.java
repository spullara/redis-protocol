package redis.bytebuffer;

import java.io.IOException;

public interface Reply<T> {
  byte[] CRLF = new byte[] { Decoders.CR, Decoders.LF };

  T data();
  void write(DynamicByteBuffer os) throws IOException;
}
