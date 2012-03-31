package redis.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import redis.Command;
import redis.RedisProtocol;
import redis.reply.BulkReply;
import redis.reply.ErrorReply;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;
import redis.reply.StatusReply;

/**
 * The lowest layer that talks directly with the redis protocol.
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 10:24 PM
 */
public class RedisClientBase {
  // Single threaded pipelining
  private ListeningExecutorService es;
  protected RedisProtocol redisProtocol;
  private static final Pattern versionMatcher = Pattern.compile("([0-9]+)\\.([0-9]+)(\\.([0-9]+))?");
  protected AtomicInteger pipelined = new AtomicInteger(0);
  protected int version;

  protected RedisClientBase(String host, int port, ExecutorService executorService) throws RedisException {
    try {
      redisProtocol = new RedisProtocol(new Socket(host, port));
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
    es = MoreExecutors.listeningDecorator(executorService);
  }

  public static int parseVersion(String value) {
    int version = 0;
    Matcher matcher = versionMatcher.matcher(value);
    if (matcher.matches()) {
      String major = matcher.group(1);
      String minor = matcher.group(2);
      String patch = matcher.group(4);
      version = 100 * Integer.parseInt(minor) + 10000 * Integer.parseInt(major);
      if (patch != null) {
        version += Integer.parseInt(patch);
      }
    }
    return version;
  }

  private Queue<SettableFuture<Reply>> txReplies = new ConcurrentLinkedQueue<SettableFuture<Reply>>();

  public synchronized ListenableFuture<? extends Reply> pipeline(String name, Command command) throws RedisException {
    try {
      redisProtocol.sendAsync(command);
    } catch (IOException e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
    pipelined.incrementAndGet();
    if (tx) {
      final SettableFuture<Reply> set = SettableFuture.create();
      es.submit(new Runnable() {
        @Override
        public void run() {
          try {
            Reply reply = redisProtocol.receiveAsync();
            if (reply instanceof ErrorReply) {
              set.setException(new RedisException(((ErrorReply) reply).error));
            }
            if (reply instanceof StatusReply) {
              if ("QUEUED".equals(((StatusReply) reply).status)) {
                txReplies.offer(set);
                return;
              }
            }
            set.set(reply);
          } catch (IOException e) {
            throw new RedisException("Failed to receive queueing result");
          } finally {
            pipelined.decrementAndGet();
          }
        }
      });
      return set;
    } else {
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
  }

  public synchronized Reply execute(String name, Command command) throws RedisException {
    if (tx) {
      throw new RedisException("Use the pipeline API when using transactions");
    }
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

  public RedisProtocol getRedisProtocol() {
    return redisProtocol;
  }

  public void close() throws IOException {
    redisProtocol.close();
    es.shutdownNow();
  }

  /**
   * Transaction support
   */

  private static final Command MULTI = new Command("MULTI".getBytes());
  private static final Command EXEC = new Command("EXEC".getBytes());
  private static final Command DISCARD = new Command("DISCARD".getBytes());

  private boolean tx;

  public synchronized StatusReply multi() {
    if (tx) {
      throw new RedisException("Already in a transaction");
    }
    StatusReply multi = (StatusReply) execute("MULTI", MULTI);
    tx = true;
    return multi;
  }

  public synchronized StatusReply discard() {
    tx = false;
    for (SettableFuture<Reply> txReply : txReplies) {
      txReply.setException(new RedisException("Discarded"));
    }
    return (StatusReply) execute("DISCARD", DISCARD);
  }

  public synchronized Future<Boolean> exec() {
    tx = false;
    try {
      redisProtocol.sendAsync(EXEC);
      return es.submit(new Callable<Boolean>() {
        @Override
        public Boolean call() throws Exception {
          MultiBulkReply execReply = (MultiBulkReply) redisProtocol.receiveAsync();
          if (execReply.byteArrays == null) {
            for (SettableFuture<Reply> txReply : txReplies) {
              txReply.setException(new RedisException("Transaction failed"));
            }
            return false;
          }
          for (Object reply : execReply.byteArrays) {
            SettableFuture<Reply> poll = txReplies.poll();
            if (reply instanceof ErrorReply) {
              poll.setException(new RedisException(((ErrorReply) reply).error));
            } else if (reply instanceof Reply) {
              poll.set((Reply) reply);
            } else if (reply instanceof Integer) {
              Integer i = (Integer) reply;
              poll.set(new IntegerReply(i));
            } else if (reply instanceof byte[]) {
              byte[] bytes = (byte[]) reply;
              poll.set(new BulkReply(bytes));
            } else {
              throw new RedisException("Unexpected result: " + reply);
            }
          }
          return true;
        }
      });
    } catch (IOException e) {
      throw new RedisException(e);
    }
  }

}
