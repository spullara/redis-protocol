package redis.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufIndexFinder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import redis.netty4.Command;

import java.io.IOException;

import static redis.netty4.Decoders.readInteger;

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
          int size = readInteger(in);
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
      int numArgs = readInteger(in);
      if (numArgs < 0) {
        throw new RedisException("Invalid size: " + numArgs);
      }
      bytes = new byte[numArgs][];
      checkpoint();
      return decode(ctx, in);
    } else {
      // Go backwards one
      in.readerIndex(in.readerIndex() - 1);
      // Read command
      bytes = new byte[1][];
      bytes[0] = in.readBytes(in.bytesBefore(ByteBufIndexFinder.CRLF)).array();
      return new Command(bytes);
    }
  }
}
