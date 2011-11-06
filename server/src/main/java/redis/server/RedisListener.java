package redis.server;

import redis.Command;

public interface RedisListener {
  public void received(Command command);
}
