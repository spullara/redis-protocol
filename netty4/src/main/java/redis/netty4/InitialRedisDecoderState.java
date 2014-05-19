package redis.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitialRedisDecoderState extends AbstractRedisDecoderState {

  private static final Logger LOG = LoggerFactory.getLogger(InitialRedisDecoderState.class);

  @Override
  public void decode(ByteBuf in) throws IOException {

    int initialReaderIndex = in.readerIndex();

    setDecodedReply(null);
    setNextState(new InitialRedisDecoderState());// default //TODO improve

    if (in.readableBytes() < 1) {
      return;
    }
    int code = in.readByte();

    int indexCrLf = findCrLf(in);// in.forEachByte(ByteBufProcessor.FIND_CRLF) - 1;
    if (indexCrLf < 0) {
      in.readerIndex(initialReaderIndex);
      return;
    }
    Reply res;

    switch (code) {
    case StatusReply.MARKER: {
      String status = readString(in, indexCrLf);
      res = new StatusReply(status);
      break;
    }
    case ErrorReply.MARKER: {
      String error = readString(in, indexCrLf);
      res = new ErrorReply(error);
      break;
    }
    case IntegerReply.MARKER: {
      Long l = readLong(in, indexCrLf);
      if (l == null) {
        in.readerIndex(initialReaderIndex);
        return;
      }
      res = new IntegerReply(l);
      break;
    }
    case BulkReply.MARKER: {
      Integer size = readBulkReplySize(in, indexCrLf);
      if (size == null) {
        in.readerIndex(initialReaderIndex);
        return;
      }

      if (size.intValue() == -1) {
        res = BulkReply.NIL_REPLY;
      } else {
        ByteBuf b = readBytes(in, indexCrLf, size.intValue());
        if (b == null) {
          in.readerIndex(initialReaderIndex);
          return;
        }
        res = new BulkReply(b);
      }
      break;
    }
    case MultiBulkReply.MARKER: {
      Long l = readLong(in, indexCrLf);
      if (l == null) {
        in.readerIndex(initialReaderIndex);
        return;
      }
      if (l > Integer.MAX_VALUE) {
        in.readerIndex(initialReaderIndex);
        throw new IllegalArgumentException("Java only supports arrays up to " + Integer.MAX_VALUE + " in size");
      }

      InAMultiBulkDecoderState ns = new InAMultiBulkDecoderState(l.intValue());
      if (l.intValue() < 1) {
        res = ns.getDecodedReply();
      } else {
        setNextState(ns);
        res = null;
      }

      // ns.decode(in);
      // res = ns.getDecodedReply();
      // if (res == null) {
      // setNextState(ns);
      // } // else mbr decoded completely => initial state

      break;

    }
    default: {
      throw new IOException("Unexpected character in stream: " + code);
    }
    }
    LOG.trace("decoded => {} ", res);
    setDecodedReply(res);

  }

  private String readString(ByteBuf is, int index) throws IOException {
    String res = is.readBytes(index - is.readerIndex()).toString(CharsetUtil.UTF_8);
    is.skipBytes(2);
    return res;
  }

}
