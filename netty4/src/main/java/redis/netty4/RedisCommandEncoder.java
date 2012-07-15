package redis.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import redis.Command;

public class RedisCommandEncoder extends MessageToByteEncoder<Command> {

  @Override
  public void encode(ChannelHandlerContext channelHandlerContext, Command command, ByteBuf byteBuf) throws Exception {
    command.write(byteBuf);
  }
}
