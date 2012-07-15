package redis.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import redis.Command;
import redis.netty4.StatusReply;

/**
 * Handle decoded commands
 */
public class RedisCommandHandler extends ChannelInboundMessageHandlerAdapter<Command> {
  @Override
  public void messageReceived(ChannelHandlerContext ctx, Command msg) throws Exception {
    new StatusReply("OK").write(ctx.nextOutboundByteBuffer());
    ctx.flush();
  }
}
