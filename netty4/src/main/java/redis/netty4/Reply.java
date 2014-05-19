package redis.netty4;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface Reply {

  void write(ByteBuf os) throws IOException;

  Object data();
}
