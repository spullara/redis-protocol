package redis.reply;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/31/11
 * Time: 3:32 PM
 */
public class SubscribeReply implements Reply<byte[][]> {

  private final byte[][] patterns;

  public SubscribeReply(byte[][] patterns) {
    this.patterns = patterns;
  }

  @Override
  public byte[][] data() {
    return patterns;
  }
}
