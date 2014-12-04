package redis.netty4;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public abstract class AbstractRedisDecoderState implements IRedisDecoderState {

  public static final char CR = '\r';
  public static final char LF = '\n';
  private static final char ZERO = '0';

  private IRedisDecoderState nextState;
  private Reply decodedReply;

  protected void setNextState(IRedisDecoderState nextState) {
    this.nextState = nextState;
  }

  public IRedisDecoderState getNextState() {
    return nextState;
  }

  protected void setDecodedReply(Reply decodedReply) {
    this.decodedReply = decodedReply;
  }

  public Reply getDecodedReply() {
    return decodedReply;
  }

  public Integer readBulkReplySize(ByteBuf is, int indexCrLf) throws IOException {
    Long l = readLong(is, indexCrLf);
    if (l == null) {
      return null;
    }
    if (l > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Java only supports arrays up to " + Integer.MAX_VALUE + " in size");
    }
    int size = (int) l.intValue();
    return size;

  }

  public ByteBuf readBytes(ByteBuf is, int indexCrLf, int size) throws IOException {
    if (is.readableBytes() < size + 2) {
      return null;
    }
    ByteBuf buffer = is.readSlice(size);
    int cr = is.readByte();
    int lf = is.readByte();
    if (cr != CR || lf != LF) {
      throw new IOException("Improper line ending: " + cr + ", " + lf);
    }
    return buffer;
    // return buffer.retain();// TOD check : retain added by gael
  }

  public static Long readLong(ByteBuf is, int indexCrLf) throws IOException {
    long size = 0;
    int sign = 1;
    // if(is.readableBytes() < 1){ return null;}
    int read = is.readByte();
    if (read == '-') {
      // if(is.readableBytes() < 1){ return null;}
      read = is.readByte();
      sign = -1;
    }
    do {
      if (read == CR) {
        // if(is.readableBytes() < 1){ return null;}
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
      // if(is.readableBytes() < 1){ return null;}
      read = is.readByte();
    } while (true);
    return size * sign;
  }

  public static Long readLong(ByteBuf is) throws IOException {
    return readLong(is, findCrLf(is));
  }

  public static int findCrLf(ByteBuf bb) {
    int ri = bb.readerIndex();
    int rb = bb.readableBytes();
    if (rb < 2) {
      return -1;
    }

    int i = bb.indexOf(ri, ri + rb, (byte) LF);
    if (i > -1 && i > ri && (bb.getByte(i - 1) == (byte) CR)) {
      return i - 1;
    }
    return -1;

  }
}
