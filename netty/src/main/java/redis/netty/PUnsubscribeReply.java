package redis.netty;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/31/11
 * Time: 5:13 PM
 */
public class PUnsubscribeReply extends UnsubscribeReply {

  public PUnsubscribeReply(byte[][] patterns) {
    super(patterns);
  }

  @Override
  public void write(ChannelBuffer os) throws IOException {
    
  }
}
