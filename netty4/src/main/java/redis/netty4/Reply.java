package redis.netty4;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public interface Reply<T> {
  byte[] CRLF = new byte[] { RedisDecoder.CR, RedisDecoder.LF };

  T data();
  void write(ByteBuf os) throws IOException;
}
