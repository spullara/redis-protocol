package redis.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import redis.Command;
import redis.netty4.Reply;
import redis.netty4.StatusReply;

/**
 * Take command objects and produce replies.
 */
public class RedisCommandHandler extends MessageToMessageEncoder<Command, Reply<?>> {
  @Override
  public Reply<?> encode(ChannelHandlerContext ctx, Command msg) throws Exception {
    return new StatusReply("OK");
  }
}
