package redis.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import redis.netty4.Command;
import redis.netty4.ErrorReply;
import redis.netty4.Reply;
import redis.util.BytesKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static redis.netty4.ErrorReply.NYI_REPLY;

/**
 * Handle decoded commands
 */
@ChannelHandler.Sharable
public class RedisCommandHandler extends ChannelInboundMessageHandlerAdapter<Command> {

  private Map<BytesKey, Wrapper> methods = new HashMap<BytesKey, Wrapper>();

  interface Wrapper {
    Reply execute(Command command) throws RedisException;
  }

  public RedisCommandHandler(final RedisServer rs) {
    Class<? extends RedisServer> aClass = rs.getClass();
    for (final Method method : aClass.getMethods()) {
      final Class<?>[] types = method.getParameterTypes();
      methods.put(new BytesKey(method.getName().getBytes()), new Wrapper() {
        @Override
        public Reply execute(Command command) throws RedisException {
          Object[] objects = new Object[types.length];
          command.toArguments(objects, types);
          try {
            return (Reply) method.invoke(rs, objects);
          } catch (IllegalAccessException e) {
            throw new RedisException("Invalid server implementation");
          } catch (InvocationTargetException e) {
            return new ErrorReply(e.getTargetException().getMessage());
          }
        }
      });
    }
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, Command msg) throws Exception {
    byte[] name = msg.getName();
    for (int i = 0; i < name.length; i++) {
      name[i] = (byte) Character.toLowerCase(name[i]);
    }
    Reply reply = methods.get(new BytesKey(name)).execute(msg);
    ByteBuf os = ctx.nextOutboundByteBuffer();
    if (reply == null) {
      NYI_REPLY.write(os);
    } else {
      reply.write(os);
    }
    ctx.flush();
  }
}
