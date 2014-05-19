package redis.netty4;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InAMultiBulkDecoderState extends AbstractRedisDecoderState implements IRedisDecoderState {

  private final static Logger LOG = LoggerFactory.getLogger(InAMultiBulkDecoderState.class);
  private final MultiBulkReply reply;

  private IRedisDecoderState subState = new InitialRedisDecoderState();

  public InAMultiBulkDecoderState(int size) {
    reply = new MultiBulkReply(size);
    if (reply.isFull()) {
      setDecodedReply(reply);
      setNextState(new InitialRedisDecoderState());
    } else {
      setNextState(this);
      setDecodedReply(null);
    }

  }

  @Override
  public void decode(ByteBuf in) throws IOException {
    subState.decode(in);
    Reply dr = subState.getDecodedReply();
    subState = subState.getNextState();

    if (dr != null) {
      LOG.trace("*addReply {}", dr);
      reply.add(dr);
    }

    if (reply.isFull()) {
      setNextState(new InitialRedisDecoderState());
      setDecodedReply(reply);
    } else {
      setNextState(this);
      setDecodedReply(null);
    }
  }

}
