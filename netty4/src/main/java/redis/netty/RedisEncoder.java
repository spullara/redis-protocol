package redis.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import redis.Command;

import static io.netty.buffer.Unpooled.dynamicBuffer;

public class RedisEncoder extends SimpleChannelDownstreamHandler {

  @Override
  public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    Object o = e.getMessage();
    if (o instanceof Command) {
      ByteBuf cb = dynamicBuffer();
      Command command = (Command) o;
      command.write(cb);
      Channels.write(ctx, e.getFuture(), cb, e.getRemoteAddress());
    } else {
      super.writeRequested(ctx, e);
    }
  }
}
