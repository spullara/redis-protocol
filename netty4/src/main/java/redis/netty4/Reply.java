package redis.netty4;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public interface Reply<T> {
  byte[] CRLF = new byte[] { Decoders.CR, Decoders.LF };

  T data();
  void write(ByteBuf os) throws IOException;
}
