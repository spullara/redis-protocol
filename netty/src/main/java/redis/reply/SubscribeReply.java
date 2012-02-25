package redis.reply;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/31/11
 * Time: 3:32 PM
 */
public class SubscribeReply extends Reply {

  private final byte[][] patterns;

  public SubscribeReply(byte[][] patterns) {
    this.patterns = patterns;
  }

  public byte[][] getPatterns() {
    return patterns;
  }

  @Override
  public void write(ChannelBuffer os) throws IOException {
  }
}
