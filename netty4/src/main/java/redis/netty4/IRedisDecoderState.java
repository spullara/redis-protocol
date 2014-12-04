package redis.netty4;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface IRedisDecoderState {

  void decode(ByteBuf in) throws IOException;

  Reply getDecodedReply();

  IRedisDecoderState getNextState();

}
