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
public class PUnsubscribeReply extends UnsubscribeReply {

  public PUnsubscribeReply(byte[][] patterns) {
    super(patterns);
  }

  @Override
  public void write(OutputStream os) throws IOException {
    
  }
}
