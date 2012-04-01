package redis.reply;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/31/11
 * Time: 5:13 PM
 */
public class UnsubscribeReply implements Reply<byte[][]> {

  private final byte[][] patterns;

  public UnsubscribeReply(byte[][] patterns) {
    this.patterns = patterns;
  }

  public byte[][] data() {
    return patterns;
  }
}
