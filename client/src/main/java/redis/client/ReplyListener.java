package redis.client;

import redis.reply.MultiBulkReply;

/**
 * Callback interface for subscribe commands.
 */
public interface ReplyListener {
  void subscribed(byte[] name, int channels);
  void psubscribed(byte[] name, int channels);
  void unsubscribed(byte[] name, int channels);
  void punsubscribed(byte[] name, int channels);
  void message(byte[] channel, byte[] message);
  void pmessage(byte[] pattern, byte[] channel, byte[] message);
}
