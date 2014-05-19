package redis.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisReplyDecoder extends ByteToMessageDecoder {

  private static final Logger LOG = LoggerFactory.getLogger(RedisReplyDecoder.class);

  private IRedisDecoderState state = new InitialRedisDecoderState();

  @Override
  protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    Object decoded = decode(ctx, in);
    if (decoded != null) {
      out.add(decoded);
    }
  }

  /**
   * Create a frame out of the {@link ByteBuf} and return it.
   * 
   * @param ctx
   *          the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
   * @param in
   *          the {@link ByteBuf} from which to read data
   * @return frame the {@link ByteBuf} which represent the frame or {@code null} if no frame could be created.
   */
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

    if (LOG.isDebugEnabled()) {
      int readableBytes = in.readableBytes();
      // int readableBytes = 1;
      LOG.trace("readableBytes={}", readableBytes);
      byte[] dst = new byte[readableBytes];
      in.getBytes(in.readerIndex(), dst, 0, readableBytes);
      if (LOG.isTraceEnabled()) {
        LOG.trace("*decoder received : {}", new String(dst, CharsetUtil.UTF_8).replaceAll("\r\n", " "));
      }
    }
    return receive(in);
  }

  protected Reply receive(ByteBuf in) throws IOException {

    Reply res;
    state.decode(in);
    res = state.getDecodedReply();
    state = state.getNextState();

    LOG.trace("*decoder res={}", res);

    return res;
  }

}
