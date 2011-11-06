package redis.client;

import redis.Command;
import redis.RedisProtocol;
import redis.reply.Reply;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 10:24 PM
 */
public class RedisClientBase {
  // Single threaded pipelining
  private static ExecutorService es = Executors.newFixedThreadPool(1);
  protected RedisProtocol redisProtocol;

  protected RedisClientBase(SocketPool socketPool) throws RedisException {
    try {
      redisProtocol = new RedisProtocol(socketPool.get());
    } catch (IOException e) {
      throw new RedisException("Could not connect", e);
    }
  }

  protected Future<? extends Reply> pipeline(String name, Command command) throws RedisException {
    try {
      redisProtocol.sendAsync(command);
    } catch (IOException e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
    return es.submit(new Callable<Reply>() {
      @Override
      public Reply call() throws Exception {
        return redisProtocol.receiveAsync();
      }
    });
  }

  protected Reply execute(String name, Command command) throws RedisException {
    try {
      return pipeline(name, command).get();
    } catch (Exception e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
  }
}
