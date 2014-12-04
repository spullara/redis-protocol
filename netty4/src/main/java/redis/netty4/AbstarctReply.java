package redis.netty4;

import io.netty.buffer.ByteBuf;
import redis.util.Encoding;

public abstract class AbstarctReply<T> implements Reply {
  public static final byte[] CRLF = new byte[] { Encoding.CR, Encoding.LF };

  private T data;

  public AbstarctReply(T data) {
    this.data = data;
  }

  @Override
  public T data() {
    return data;
  }

  protected void setData(T data) {
    this.data = data;
  }
  
  @Override
  public void releaseAll() {
  }

  // void write(ByteBuf os) throws IOException;
}
