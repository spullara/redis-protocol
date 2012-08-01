package redis.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import redis.netty4.Command;
import redis.netty4.ErrorReply;
import redis.netty4.Reply;
import redis.netty4.StatusReply;
import redis.util.BytesKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Charsets;

import static redis.netty4.ErrorReply.NYI_REPLY;
import static redis.netty4.Reply.CRLF;
import static redis.netty4.StatusReply.QUIT;
import static redis.util.Encoding.numToBytes;

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
          try {
            command.toArguments(objects, types);
            return (Reply) method.invoke(rs, objects);
          } catch (IllegalAccessException e) {
            throw new RedisException("Invalid server implementation");
          } catch (InvocationTargetException e) {
            Throwable te = e.getTargetException();
            if (!(te instanceof RedisException)) {
              te.printStackTrace();
            }
            return new ErrorReply("ERR " + te.getMessage());
          } catch (Exception e) {
            return new ErrorReply("ERR " + e.getMessage());
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
    ByteBuf os = ctx.nextOutboundByteBuffer();
    Wrapper wrapper = methods.get(new BytesKey(name));
    Reply reply;
    if (wrapper == null) {
      reply = new ErrorReply("unknown command '" + new String(name, Charsets.US_ASCII) + "'");
    } else {
      reply = wrapper.execute(msg);
    }
    if (reply == QUIT) {
      ctx.close();
    } else if (msg.isInline()) {
      if (reply == null) {
        os.writeBytes(CRLF);
      } else {
        Object o = reply.data();
        if (o instanceof String) {
          os.writeByte('+');
          os.writeBytes(((String) o).getBytes(Charsets.US_ASCII));
          os.writeBytes(CRLF);
        } else if (o instanceof byte[]) {
          os.writeByte('+');
          os.writeBytes((byte[]) o);
          os.writeBytes(CRLF);
        } else if (o instanceof Long) {
          os.writeByte(':');
          os.writeBytes(numToBytes((Long) o, true));
        } else {
          os.writeBytes("ERR invalid inline response".getBytes(Charsets.US_ASCII));
          os.writeBytes(CRLF);
        }
      }
    } else {
      if (reply == null) {
        NYI_REPLY.write(os);
      } else {
        reply.write(os);
      }
    }
    ctx.flush();
  }
}
