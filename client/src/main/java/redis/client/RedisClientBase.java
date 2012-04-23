package redis.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.primitives.SignedBytes;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import redis.Command;
import redis.RedisProtocol;
import redis.reply.BulkReply;
import redis.reply.ErrorReply;
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
  private static final Comparator<byte[]> BYTES = SignedBytes.lexicographicalComparator();

  // Standard values for use with some commands
  public static final byte[] WEIGHTS = "WEIGHTS".getBytes();
  public static final byte[] WITHSCORES = "WITHSCORES".getBytes();
  public static final byte[] ALPHA = "ALPHA".getBytes();
  public static final byte[] LIMIT = "LIMIT".getBytes();
  public static final byte[] DESC = "DESC".getBytes();
  public static final byte[] BY = "BY".getBytes();
  public static final byte[] STORE = "STORE".getBytes();
  public static final byte[] GET = "GET".getBytes();

  // Single threaded pipelining
  private ListeningExecutorService es;
  protected RedisProtocol redisProtocol;
  private static final Pattern versionMatcher = Pattern.compile(
          "([0-9]+)\\.([0-9]+)(\\.([0-9]+))?");
  protected AtomicInteger pipelined = new AtomicInteger(0);
  protected int version;

  protected RedisClientBase(Socket socket, ExecutorService executorService) throws RedisException {
    try {
      redisProtocol = new RedisProtocol(socket);
      BulkReply info = (BulkReply) execute("INFO", new Command("INFO"));
      BufferedReader br = new BufferedReader(new StringReader(new String(info.data())));
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
    if (subscribed) {
      throw new RedisException("You can only issue subscription commands once subscribed");
    }
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
              set.setException(new RedisException(((ErrorReply) reply).data()));
            }
            if (reply instanceof StatusReply) {
              if ("QUEUED".equals(((StatusReply) reply).data())) {
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
              throw new RedisException(((ErrorReply) reply).data());
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
    if (subscribed) {
      throw new RedisException("You can only issue subscription commands once subscribed");
    }
    try {
      if (pipelined.get() == 0) {
        redisProtocol.sendAsync(command);
        Reply reply = redisProtocol.receiveAsync();
        if (reply instanceof ErrorReply) {
          throw new RedisException(((ErrorReply) reply).data());
        }
        return reply;
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
    if (subscribed) {
      throw new RedisException("You can only issue subscription commands once subscribed");
    }
    StatusReply multi = (StatusReply) execute("MULTI", MULTI);
    tx = true;
    return multi;
  }

  public synchronized StatusReply discard() {
    if (subscribed) {
      throw new RedisException("You can only issue subscription commands once subscribed");
    }
    tx = false;
    for (SettableFuture<Reply> txReply : txReplies) {
      txReply.setException(new RedisException("Discarded"));
    }
    return (StatusReply) execute("DISCARD", DISCARD);
  }

  public synchronized Future<Boolean> exec() {
    if (subscribed) {
      throw new RedisException("You can only issue subscription commands once subscribed");
    }
    tx = false;
    try {
      redisProtocol.sendAsync(EXEC);
      return es.submit(new Callable<Boolean>() {
        @Override
        public Boolean call() throws Exception {
          MultiBulkReply execReply = (MultiBulkReply) redisProtocol.receiveAsync();
          if (execReply.data() == null) {
            for (SettableFuture<Reply> txReply : txReplies) {
              txReply.setException(new RedisException("Transaction failed"));
            }
            return false;
          }
          for (Reply reply : execReply.data()) {
            SettableFuture<Reply> poll = txReplies.poll();
            poll.set(reply);
          }
          return true;
        }
      });
    } catch (IOException e) {
      throw new RedisException(e);
    }
  }

  private List<ReplyListener> replyListeners;
  private boolean subscribed;

  /**
   * Add a reply listener to this client for subscriptions.
   */
  public synchronized void addListener(ReplyListener replyListener) {
    if (replyListeners == null) {
      replyListeners = new CopyOnWriteArrayList<ReplyListener>();
    }
    replyListeners.add(replyListener);
  }

  /**
   * Remove a reply listener from this client.
   */
  public synchronized boolean removeListener(ReplyListener replyListener) {
    return replyListeners != null && replyListeners.remove(replyListener);
  }

  private static final byte[] MESSAGE = "message".getBytes();
  private static final byte[] PMESSAGE = "pmessage".getBytes();
  private static final byte[] SUBSCRIBE = "subscribe".getBytes();
  private static final byte[] UNSUBSCRIBE = "unsubscribe".getBytes();
  private static final byte[] PSUBSCRIBE = "psubscribe".getBytes();
  private static final byte[] PUNSUBSCRIBE = "punsubscribe".getBytes();

  /**
   * Subscribes the client to the specified channels.
   *
   * @param subscriptions
   */
  public synchronized void subscribe(Object... subscriptions) {
    subscribe();
    try {
      redisProtocol.sendAsync(new Command(SUBSCRIBE, subscriptions));
    } catch (IOException e) {
      throw new RedisException("Failed to subscribe", e);
    }
  }

  /**
   * Subscribes the client to the specified patterns.
   *
   * @param subscriptions
   */
  public synchronized void psubscribe(Object... subscriptions) {
    subscribe();
    try {
      redisProtocol.sendAsync(new Command(PSUBSCRIBE, subscriptions));
    } catch (IOException e) {
      throw new RedisException("Failed to subscribe", e);
    }
  }

  /**
   * Unsubscribes the client to the specified channels.
   *
   * @param subscriptions
   */
  public synchronized void unsubscribe(Object... subscriptions) {
    subscribe();
    try {
      redisProtocol.sendAsync(new Command(UNSUBSCRIBE, subscriptions));
    } catch (IOException e) {
      throw new RedisException("Failed to subscribe", e);
    }
  }

  /**
   * Unsubscribes the client to the specified patterns.
   *
   * @param subscriptions
   */
  public synchronized void punsubscribe(Object... subscriptions) {
    subscribe();
    try {
      redisProtocol.sendAsync(new Command(PUNSUBSCRIBE, subscriptions));
    } catch (IOException e) {
      throw new RedisException("Failed to subscribe", e);
    }
  }

  private void subscribe() {
    if (!subscribed) {
      subscribed = true;
      // Start up the listener, only subscription commands
      // are accepted past this point
      es.submit(new SubscriptionsDispatcher());
    }
  }

  private class SubscriptionsDispatcher implements Runnable {
    @Override
    public void run() {
      try {
        while (true) {
          MultiBulkReply reply = (MultiBulkReply) redisProtocol.receiveAsync();
          Reply[] data = reply.data();
          if (data.length != 3 && data.length != 4) {
            throw new RedisException("Invalid subscription messsage");
          }
          for (ReplyListener replyListener : replyListeners) {
            byte[] type = (byte[]) data[0].data();
            byte[] data1 = (byte[]) data[1].data();
            Object data2 = data[2].data();
            switch (type.length) {
              case 7:
                if (BYTES.compare(type, MESSAGE) == 0) {
                  replyListener.message(data1, (byte[]) data2);
                  continue;
                }
              case 8:
                if (BYTES.compare(type, PMESSAGE) == 0) {
                  replyListener.pmessage(data1, (byte[]) data2, (byte[]) data[3].data());
                  continue;
                }
              case 9:
                if (BYTES.compare(type, SUBSCRIBE) == 0) {
                  replyListener.subscribed(data1, ((Number) data2).intValue());
                  continue;
                }
              case 10:
                if (BYTES.compare(type, PSUBSCRIBE) == 0) {
                  replyListener.psubscribed(data1, ((Number) data2).intValue());
                  continue;
                }
              case 11:
                if (BYTES.compare(type, UNSUBSCRIBE) == 0) {
                  replyListener.unsubscribed(data1, ((Number) data2).intValue());
                  continue;
                }
              case 12:
                if (BYTES.compare(type, PUNSUBSCRIBE) == 0) {
                  replyListener.punsubscribed(data1, ((Number) data2).intValue());
                  continue;
                }
            }
            close();
            throw new RedisException("Invalid subscription messsage");
          }
        }
      } catch (IOException e) {
        // Ignore, probably closed
      }
    }
  }
}
