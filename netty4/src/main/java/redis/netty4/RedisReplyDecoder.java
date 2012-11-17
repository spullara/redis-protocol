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

  public static final char CR = '\r';
  public static final char LF = '\n';
  private static final char ZERO = '0';
  // We track the current multibulk reply in the case
  // where we do not get a complete reply in a single
  // decode invocation.
  private MultiBulkReply reply;

  public ByteBuf readBytes(ByteBuf is) throws IOException {
    long l = readLong(is);
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
    if (cr != CR || lf != LF) {
      throw new IOException("Improper line ending: " + cr + ", " + lf);
    }
    return buffer;
  }

  public static long readLong(ByteBuf is) throws IOException {
    long size = 0;
    int sign = 1;
    int read = is.readByte();
    if (read == '-') {
      read = is.readByte();
      sign = -1;
    }
    do {
      if (read == CR) {
        if (is.readByte() == LF) {
          break;
        }
      }
      int value = read - ZERO;
      if (value >= 0 && value < 10) {
        size *= 10;
        size += value;
      } else {
        throw new IOException("Invalid character in integer");
      }
      read = is.readByte();
    } while (true);
    return size * sign;
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
        return new IntegerReply(readLong(is));
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
