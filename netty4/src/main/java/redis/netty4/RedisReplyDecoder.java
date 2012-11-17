package redis.netty4;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufIndexFinder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.IOException;

/**
 * Netty codec for Redis
 */

public class RedisReplyDecoder extends ReplayingDecoder<Reply<?>, Void> {

  // We track the current multibulk reply in the case
  // where we do not get a complete reply in a single
  // decode invocation.
  private MultiBulkReply reply;

  public ByteBuf readBytes(ByteBuf is) throws IOException {
    long l = Decoders.readLong(is);
    if (l > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Java only supports arrays up to " + Integer.MAX_VALUE + " in size");
    }
    int size = (int) l;
    if (size == -1) {
      return null;
    }
    ByteBuf buffer = is.readSlice(size);
    int cr = is.readByte();
    int lf = is.readByte();
    if (cr != Decoders.CR || lf != Decoders.LF) {
      throw new IOException("Improper line ending: " + cr + ", " + lf);
    }
    return buffer;
  }

  public Reply receive(final ByteBuf is) throws IOException {
    int code = is.readByte();
    switch (code) {
      case StatusReply.MARKER: {
        String status = is.readBytes(is.bytesBefore(ByteBufIndexFinder.CRLF)).toString(Charsets.UTF_8);
        is.skipBytes(2);
        return new StatusReply(status);
      }
      case ErrorReply.MARKER: {
        String error = is.readBytes(is.bytesBefore(ByteBufIndexFinder.CRLF)).toString(Charsets.UTF_8);
        is.skipBytes(2);
        return new ErrorReply(error);
      }
      case IntegerReply.MARKER: {
        return new IntegerReply(Decoders.readLong(is));
      }
      case BulkReply.MARKER: {
        return new BulkReply(readBytes(is));
      }
      case MultiBulkReply.MARKER: {
        return decodeMultiBulkReply(is);
      }
      default: {
        throw new IOException("Unexpected character in stream: " + code);
      }
    }
  }

  @Override
  public void checkpoint() {
    if (internalBuffer() != null) {
      super.checkpoint();
    }
  }

  public MultiBulkReply decodeMultiBulkReply(ByteBuf is) throws IOException {
    try {
      if (reply == null) {
        reply = new MultiBulkReply(this, is);
      } else {
        reply.read(this, is);
      }
      return reply;
    } finally {
      reply = null;
    }
  }

  @Override
  public Reply<?> decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
    return receive(byteBuf);
  }
}
