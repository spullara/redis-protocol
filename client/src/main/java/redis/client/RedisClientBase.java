package redis.client;

import com.google.common.base.Charsets;
import com.google.common.primitives.SignedBytes;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import redis.Command;
import redis.RedisProtocol;
import redis.reply.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
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

  // Needed for reconnection
  private final String host;
  private final int port;
	private int db = 0;
	private String passwd = null;

  // Single threaded pipelining
  private ListeningExecutorService es;
  protected RedisProtocol redisProtocol;
  private static final Pattern versionMatcher = Pattern.compile(
          "([0-9]+)\\.([0-9]+)(\\.([0-9]+))?");
  protected AtomicInteger pipelined = new AtomicInteger(0);
  protected int version = 9999999;

  protected RedisClientBase(String host, int port, int db, String passwd, ExecutorService executorService) throws RedisException {
    this.host = host;
    this.port = port;
    this.db = db;
    this.passwd = passwd;
    es = MoreExecutors.listeningDecorator(executorService);
    connect();
  }

  private boolean connect() throws RedisException {
    try {
      if (subscribed || tx) {
        return false;
      }
      redisProtocol = new RedisProtocol(new Socket(host, port));
      parseInfo();
      if (passwd != null)
      	auth(passwd);
      if (db != 0)
      	select(db);
      return true;
    } catch (IOException e) {
      throw new RedisException("Could not connect", e);
    } finally {
      subscribed = false;
      tx = false;
      retrying = false;
    }
  }

  private boolean parseAttempted;

  private void parseInfo() {
    if (parseAttempted) return; else parseAttempted = true;
    try {
      BulkReply info = (BulkReply) execute("INFO", new Command("INFO"));
      if (info != null && info.data() != null) {
        BufferedReader br = new BufferedReader(new StringReader(new String(info.data())));
        String line;
        while ((line = br.readLine()) != null) {
          int index = line.indexOf(':');
          if (index != -1) {
            String name = line.substring(0, index);
            String value = line.substring(index + 1);
            if ("redis_version".equals(name)) {
              this.version = parseVersion(value);
            }
          }
        }
      }
    } catch (RedisException re) {
      // Sadly no specific error code for this beyond the text
      if (re.getMessage().equals("ERR operation not permitted")) {
        // Server is either authenticated and we will try again when AUTH command is sent
        parseAttempted = false;
      } else { // or
        // is a non-standard redis implementation, e.g. Twemproxy
        // https://github.com/spullara/redis-protocol/issues/22
        connect();
      }
    } catch (Exception e) {
      // Ignore, don't try again
    }
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

  private Queue<CompletableFuture<Reply>> txReplies = new ConcurrentLinkedQueue<CompletableFuture<Reply>>();

  public synchronized CompletableFuture<? extends Reply> pipeline(String name, Command command) throws RedisException {
    if (subscribed) {
      throw new RedisException("You are subscribed and cannot create a pipeline");
    }
    try {
      redisProtocol.sendAsync(command);
    } catch (IOException e) {
      connect();
      throw new RedisException("Failed to execute: " + name, e);
    }
    pipelined.incrementAndGet();
    if (tx) {
      final CompletableFuture<Reply> set = new CompletableFuture<>();
      es.submit(new Runnable() {
        @Override
        public void run() {
          try {
            Reply reply = redisProtocol.receiveAsync();
            if (reply instanceof ErrorReply) {
              set.completeExceptionally(new RedisException(((ErrorReply) reply).data()));
            } else if (reply instanceof StatusReply) {
              if ("QUEUED".equals(((StatusReply) reply).data())) {
                txReplies.offer(set);
              }
            } else {
              set.complete(reply);
            }
          } catch (IOException e) {
            throw new RedisException("Failed to receive queueing result");
          } finally {
            pipelined.decrementAndGet();
          }
        }
      });
      return set;
    } else {
      return CompletableFuture.supplyAsync(() -> {
        try {
          Reply reply = redisProtocol.receiveAsync();
          if (reply instanceof ErrorReply) {
            throw new RedisException(((ErrorReply) reply).data());
          }
          return reply;
        } catch (IOException e) {
          throw new CompletionException(e);
        } finally {
          pipelined.decrementAndGet();
        }
      }, es);
    }
  }

  private boolean retrying = false;

  public synchronized Reply execute(String name, Command command) throws RedisException {
    if (tx) {
      throw new RedisException("Use the pipeline API when using transactions");
    }
    if (subscribed) {
      throw new RedisException("You are subscribed and must use the original pipeline to execute commands");
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
    } catch (IOException e) {
      if (!retrying && connect()) {
        retrying = true;
        execute(name, command);
      }
      throw new RedisException("I/O Failure: " + name, e);
    } catch (InterruptedException e) {
      throw new RedisException("Interrupted: " + name, e);
    } catch (ExecutionException e) {
      throw new RedisException("Failed to execute: " + name, e);
    }
  }

  public RedisProtocol getRedisProtocol() {
    return redisProtocol;
  }

  public void close() throws IOException {
    redisProtocol.close();
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

  public StatusReply discard() {
    CompletableFuture<StatusReply> discard;
    synchronized (this) {
      if (subscribed) {
        throw new RedisException("You can only issue subscription commands once subscribed");
      }
      if (tx) {
        tx = false;
        discard = CompletableFuture.supplyAsync(() -> {
          synchronized (RedisClientBase.this) {
            CompletableFuture<Reply> txReply;
            while ((txReply = txReplies.poll()) != null) {
              txReply.completeExceptionally(new RedisException("Discarded"));
            }
            return (StatusReply) execute("DISCARD", DISCARD);
          }
        });
      } else {
        throw new RedisException("Not in a transaction");
      }
    }
    try {
      return discard.get();
    } catch (Exception e) {
      throw new RedisException("Failed to discard the transaction", e);
    }
  }

  public synchronized Future<Boolean> exec() {
    if (subscribed) {
      throw new RedisException("You can only issue subscription commands once subscribed");
    }
    if (tx) {
      tx = false;
      try {
        redisProtocol.sendAsync(EXEC);
        return es.submit(new Callable<Boolean>() {
          @Override
          public Boolean call() throws Exception {
            Reply maybeReply = redisProtocol.receiveAsync();
            if (maybeReply instanceof ErrorReply) {
              ErrorReply errorReply = (ErrorReply) maybeReply;
              throw new RedisException(errorReply.data());
            }
            MultiBulkReply execReply = (MultiBulkReply) maybeReply;
            if (execReply.data() == null) {
              for (CompletableFuture<Reply> txReply : txReplies) {
                txReply.completeExceptionally(new RedisException("Transaction failed"));
              }
              return false;
            }
            for (Reply reply : execReply.data()) {
              CompletableFuture<Reply> poll = txReplies.poll();
              if (reply instanceof ErrorReply) {
                poll.completeExceptionally(new RedisException((String) reply.data()));
              } else {
                poll.complete(reply);
              }
            }
            return true;
          }
        });
      } catch (IOException e) {
        connect();
        throw new RedisException(e);
      }
    } else {
      throw new RedisException("Not in a transaction");
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
      connect();
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
      connect();
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
      connect();
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
      connect();
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

  protected static final String AUTH = "AUTH";
  protected static final byte[] AUTH_BYTES = AUTH.getBytes(Charsets.US_ASCII);

  /**
   * Authenticate to the server
   * Connection
   *
   * @param password0
   * @return StatusReply
   */
  public StatusReply auth(Object password0) throws RedisException {
    StatusReply statusReply = (StatusReply) execute(AUTH, new Command(AUTH_BYTES, password0));
    // Now that we are successful, parse the info
    parseInfo();
    return statusReply;
  }
  
  protected static final String SELECT = "SELECT";
  protected static final byte[] SELECT_BYTES = SELECT.getBytes(Charsets.US_ASCII);
  protected static final int SELECT_VERSION = parseVersion("1.0.0");

  /**
   * Change the selected database for the current connection
   * Connection
   *
   * @param index0
   * @return StatusReply
   */
  public StatusReply select(Object index0) throws RedisException {
    if (version < SELECT_VERSION) throw new RedisException("Server does not support SELECT");
    return (StatusReply) execute(SELECT, new Command(SELECT_BYTES, index0));
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
