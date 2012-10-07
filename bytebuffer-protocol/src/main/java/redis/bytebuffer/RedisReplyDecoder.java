package redis.bytebuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

import static redis.bytebuffer.Decoders.decodeUTF8;

/**
 * Netty codec for Redis
 */

public class RedisReplyDecoder {

  // We track the current multibulk reply in the case
  // where we do not get a complete reply in a single
  // decode invocation.
  private MultiBulkReply reply;

  public ByteBuffer readBytes(ByteBuffer is) throws IOException {
    long size = Decoders.readLong(is);
    if (size == -1) {
      return null;
    } else if (size > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Java only supports arrays up to " + Integer.MAX_VALUE + " in size");
    }
    ByteBuffer buffer = is.slice();
    buffer.limit((int) size);
    is.position(is.position() + (int) size);
    int cr = is.get();
    int lf = is.get();
    if (cr != Decoders.CR || lf != Decoders.LF) {
      throw new IOException("Improper line ending: " + cr + ", " + lf);
    }
    return buffer;
  }

  public Reply receive(final ByteBuffer is) throws IOException {
    int code = is.get();
    switch (code) {
      case StatusReply.MARKER: {
        ByteBuffer slice = skipCRLF(is);
        String status = decodeUTF8(slice);
        return new StatusReply(status);
      }
      case ErrorReply.MARKER: {
        ByteBuffer slice = skipCRLF(is);
        String error = decodeUTF8(slice);
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

  private ByteBuffer skipCRLF(ByteBuffer is) {
    ByteBuffer slice = is.slice();
    int index = 0;
    for (int i = 0; i < slice.remaining(); i++) {
      if (slice.get(i) == Decoders.CR && slice.get(i + 1) == Decoders.LF) {
        index = i;
        break;
      }
    }
    slice = is.slice();
    slice.limit(index);
    is.position(is.position() + index + 2);
    return slice;
  }

  public void checkpoint() {
  }

  public MultiBulkReply decodeMultiBulkReply(ByteBuffer is) throws IOException {
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
}
