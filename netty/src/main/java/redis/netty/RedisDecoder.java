package redis.netty;

import com.google.common.base.Charsets;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferIndexFinder;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;

import java.io.IOException;

/**
 * Netty codec for Redis
 */

public class RedisDecoder extends ReplayingDecoder<VoidEnum> {

  public static final char CR = '\r';
  public static final char LF = '\n';
  private static final char ZERO = '0';

  // We track the current multibulk reply in the case
  // where we do not get a complete reply in a single
  // decode invocation.
  private MultiBulkReply reply;

  public ChannelBuffer readBytes(ChannelBuffer is) throws IOException {
    long size = readLong(is);
    if (size == -1) {
      return null;
    }
    if (size > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Java only supports arrays up to " + Integer.MAX_VALUE + " in size");
    }
    ChannelBuffer buffer = is.readSlice((int) size);
    int cr = is.readByte();
    int lf = is.readByte();
    if (cr != CR || lf != LF) {
      throw new IOException("Improper line ending: " + cr + ", " + lf);
    }
    return buffer;
  }

  public static long readLong(ChannelBuffer is) throws IOException {
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

  public Reply receive(final ChannelBuffer is) throws IOException {
    // We may be in the middle of a large multibulk reply
    if (reply != null) {
      return decodeMultiBulkReply(is);
    }
    return readReply(is);
  }

  public Reply readReply(ChannelBuffer is) throws IOException {
    int code = is.readByte();
    switch (code) {
      case StatusReply.MARKER: {
        String status = is.readBytes(is.bytesBefore(ChannelBufferIndexFinder.CRLF)).toString(Charsets.UTF_8);
        is.skipBytes(2);
        return new StatusReply(status);
      }
      case ErrorReply.MARKER: {
        String error = is.readBytes(is.bytesBefore(ChannelBufferIndexFinder.CRLF)).toString(Charsets.UTF_8);
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
        if (reply == null) {
          return decodeMultiBulkReply(is);
        } else {
          // This is an internal MBR in an MBR.
          return new RedisDecoder().decodeMultiBulkReply(is);
        }
      }
      default: {
        throw new IOException("Unexpected character in stream: " + code);
      }
    }
  }

  @Override
  public void checkpoint() {
    super.checkpoint();
  }

  @Override
  protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer channelBuffer, VoidEnum anEnum) throws Exception {
    return receive(channelBuffer);
  }

  public MultiBulkReply decodeMultiBulkReply(ChannelBuffer is) throws IOException {
    try {
      if (reply == null) {
        reply = new MultiBulkReply();
        checkpoint();
      }
      reply.read(this, is);
      return reply;
    } finally {
      // If we throw a replay exception we won't be
      // done yet and will need to contiue parsing.
      if (reply != null && reply.isDone()) {
        reply = null;
      }
    }
  }
}
