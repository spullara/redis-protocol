package redis;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.IOException;

/**
 * Using JNA to start Redis
 */
public class Redis {

  public interface RedisLibrary extends Library {
    RedisLibrary INSTANCE = (RedisLibrary) Native.loadLibrary("redis-server", RedisLibrary.class);

    int main(int argc, String[] argv);

    int createClient(int fd);

  }

  public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
    RedisLibrary redis = RedisLibrary.INSTANCE;
    redis.main(0, new String[0]);
    redis.createClient(-1);
  }
}
