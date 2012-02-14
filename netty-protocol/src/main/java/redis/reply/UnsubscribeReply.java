package redis.reply;

import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/31/11
 * Time: 5:13 PM
 */
public class UnsubscribeReply extends Reply {

  private final byte[][] patterns;

  public UnsubscribeReply(byte[][] patterns) {
    this.patterns = patterns;
  }

  @Override
  public void write(OutputStream os) throws IOException {
    
  }

  public byte[][] getPatterns() {
    return patterns;
  }
}
