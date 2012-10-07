package redis.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufIndexFinder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import redis.netty4.Command;

import java.io.IOException;

import static redis.netty4.Decoders.readLong;

/**
 * Decode commands.
 */
public class RedisCommandDecoder extends ReplayingDecoder<Command, Void> {

  private byte[][] bytes;
  private int arguments = 0;

  @Override
  public Command decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    if (bytes != null) {
      int numArgs = bytes.length;
      for (int i = arguments; i < numArgs; i++) {
        if (in.readByte() == '$') {
          long l = readLong(in);
          if (l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Java only supports arrays up to " + Integer.MAX_VALUE + " in size");
          }
          int size = (int) l;
          bytes[i] = new byte[size];
          in.readBytes(bytes[i]);
          if (in.bytesBefore(ByteBufIndexFinder.CRLF) != 0) {
            throw new RedisException("Argument doesn't end in CRLF");
          }
          in.skipBytes(2);
          arguments++;
          checkpoint();
        } else {
          throw new IOException("Unexpected character");
        }
      }
      try {
        return new Command(bytes);
      } finally {
        bytes = null;
        arguments = 0;
      }
    }
    if (in.readByte() == '*') {
      long l = readLong(in);
      if (l > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Java only supports arrays up to " + Integer.MAX_VALUE + " in size");
      }
      int numArgs = (int) l;
      if (numArgs < 0) {
        throw new RedisException("Invalid size: " + numArgs);
      }
      bytes = new byte[numArgs][];
      checkpoint();
      return decode(ctx, in);
    } else {
      // Go backwards one
      in.readerIndex(in.readerIndex() - 1);
      // Read command -- can't be interupted
      byte[][] b = new byte[1][];
      b[0] = in.readBytes(in.bytesBefore(ByteBufIndexFinder.CRLF)).array();
      in.skipBytes(2);
      return new Command(b, true);
    }
  }
}
