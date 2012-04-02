package redis.client;

/**
 * Convenience when you don't care about the subscriptions.
 */
public abstract class MessageListener implements ReplyListener {
  @Override
  public void subscribed(byte[] name, int channels) {
  }

  @Override
  public void psubscribed(byte[] name, int channels) {
  }

  @Override
  public void unsubscribed(byte[] name, int channels) {
  }

  @Override
  public void punsubscribed(byte[] name, int channels) {
  }
}
