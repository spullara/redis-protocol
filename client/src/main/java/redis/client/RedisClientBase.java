package redis.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import redis.Command;
import redis.RedisProtocol;
import redis.reply.BulkReply;
import redis.reply.ErrorReply;
import redis.reply.Reply;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The lowest layer that talks directly with the redis protocol.
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 10:24 PM
 */
public class RedisClientBase {
  // Single threaded pipelining
  private ListeningExecutorService es = MoreExecutors.listeningDecorator(
      Executors.newSingleThreadExecutor());
  protected RedisProtocol redisProtocol;
  private static final Pattern versionMatcher = Pattern.compile("([0-9]+)\\.([0-9]+)(\\.([0-9]+))?");
  private AtomicInteger pipelined = new AtomicInteger(0);
  protected int version;

  protected RedisClientBase(SocketPool socketPool) throws RedisException {
    try {
      redisProtocol = new RedisProtocol(socketPool.get());
      BulkReply info = (BulkReply) redisProtocol.send(new Command("info"));
      BufferedReader br = new BufferedReader(new StringReader(new String(info.bytes)));
      String line;
      while ((line = br.readLine()) != null) {
        int index = line.indexOf(':');
        String name = line.substring(0, index);
        String value = line.substring(index + 1);
        if ("redis_version".equals(name)) {
          this.version = parseVersion(value);
        }
      }
    } catch (IOException e) {
      throw new RedisException("Could not connect", e);
    }
  }

  public static int parseVersion(String value) {
    int version = 0;
    Matcher matcher = versionMatcher.matcher(value);
    if (matcher.matches()) {
      String major = matcher.group(1);
      String minor = matcher.group(2);
      String patch = matcher.group(4);
      version  = 100*Integer.parseInt(minor) + 10000*Integer.parseInt(major);
      if (patch != null) {
        version += Integer.parseInt(patch);
      }
    }
    return version;
  }

  public synchronized ListenableFuture<? extends Reply> pipeline(String name, Command command) throws RedisException {
    try {
      redisProtocol.sendAsync(command);
    } catch (IOException e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
    pipelined.incrementAndGet();
    return es.submit(new Callable<Reply>() {
      @Override
      public Reply call() throws Exception {
        try {
          Reply reply = redisProtocol.receiveAsync();
          if (reply instanceof ErrorReply) {
            throw new RedisException(((ErrorReply) reply).error);
          }
          return reply;
        } finally {
          pipelined.decrementAndGet();
        }
      }
    });
  }

  public synchronized ListenableFuture<? extends Reply> pipeline(String name, Object... objects) throws RedisException {
    try {
      redisProtocol.sendAsync(objects);
    } catch (IOException e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
    pipelined.incrementAndGet();
    return es.submit(new Callable<Reply>() {
      @Override
      public Reply call() throws Exception {
        try {
          Reply reply = redisProtocol.receiveAsync();
          if (reply instanceof ErrorReply) {
            throw new RedisException(((ErrorReply) reply).error);
          }
          return reply;
        } finally {
          pipelined.decrementAndGet();
        }
      }
    });
  }

  public synchronized Reply execute(String name, Command command) throws RedisException {
    try {
      if (pipelined.get() == 0) {
        return redisProtocol.send(command);
      } else {
        return pipeline(name, command).get();
      }
    } catch (Exception e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
  }

  protected synchronized Reply execute(String name, Object... objects) throws RedisException {
    try {
      if (pipelined.get() == 0) {
        redisProtocol.sendAsync(objects);
        return redisProtocol.receiveAsync();
      } else {
        return pipeline(name, objects).get();
      }
    } catch (Exception e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
  }

  public RedisProtocol getRedisProtocol() {
    return redisProtocol;
  }

  public void close() throws IOException {
    redisProtocol.close();
    es.shutdown();
  }
}
