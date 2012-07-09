package redis.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;
import redis.Command;

public class RedisEncoder extends SimpleChannelDownstreamHandler {

  @Override
  public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    Object o = e.getMessage();
    if (o instanceof Command) {
      ChannelBuffer cb = ChannelBuffers.dynamicBuffer();
      Command command = (Command) o;
      command.write(cb);
      Channels.write(ctx, e.getFuture(), cb, e.getRemoteAddress());
    } else {
      super.writeRequested(ctx, e);
    }
  }
}
