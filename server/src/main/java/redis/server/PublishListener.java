package redis.server;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/31/11
 * Time: 2:23 PM
 */
public interface PublishListener {
  boolean publish(byte[] target, byte[] message);
}
