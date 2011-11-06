package redis.client;

import com.google.common.base.Charsets;
import redis.Command;
import redis.reply.Reply;

import java.io.IOException;
import java.util.concurrent.Future;

public class RedisClient extends RedisClientBase {
  protected Pipeline pipeline = new Pipeline();

  public RedisClient(SocketPool socketPool) throws IOException {
    super(socketPool);
  }

  public Pipeline pipeline() {
    return pipeline;
  }

  private static final String SET = "SET";
  private static final byte[] SET_BYTES = SET.getBytes(Charsets.US_ASCII);

  public Reply set(Object key, Object value) throws RedisException {
    return execute(SET, new Command(SET_BYTES, key, value));
  }

  public class Pipeline {
    public Future<Reply> set(Object key, Object value) throws RedisException {
      return pipeline(SET, new Command(SET_BYTES, key, value));
    }
  }
}
