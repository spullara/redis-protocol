package redis.client;

import redis.Command;
import redis.RedisProtocol;
import redis.reply.ErrorReply;
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
  private ExecutorService es = Executors.newFixedThreadPool(1);
  protected RedisProtocol redisProtocol;

  protected RedisClientBase(SocketPool socketPool) throws RedisException {
    try {
      redisProtocol = new RedisProtocol(socketPool.get());
    } catch (IOException e) {
      throw new RedisException("Could not connect", e);
    }
  }

  protected synchronized Future<? extends Reply> pipeline(String name, Command command) throws RedisException {
    try {
      redisProtocol.sendAsync(command);
    } catch (IOException e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
    return es.submit(new Callable<Reply>() {
      @Override
      public Reply call() throws Exception {
        Reply reply = redisProtocol.receiveAsync();
        if (reply instanceof ErrorReply) {
          throw new RedisException(((ErrorReply) reply).error);
        }
        return reply;
      }
    });
  }

  protected synchronized Future<? extends Reply> pipeline(String name, Object... objects) throws RedisException {
    try {
      redisProtocol.sendAsync(objects);
    } catch (IOException e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
    return es.submit(new Callable<Reply>() {
      @Override
      public Reply call() throws Exception {
        Reply reply = redisProtocol.receiveAsync();
        if (reply instanceof ErrorReply) {
          throw new RedisException(((ErrorReply) reply).error);
        }
        return reply;
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

  protected Reply execute(String name, Object... objects) throws RedisException {
    try {
      return pipeline(name, objects).get();
    } catch (Exception e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
  }
}
