package redis.netty.client;

import com.google.common.base.Charsets;
import com.google.common.primitives.UnsignedBytes;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import redis.Command;
import redis.netty.BulkReply;
import redis.netty.ErrorReply;
import redis.netty.MultiBulkReply;
import redis.netty.RedisDecoder;
import redis.netty.RedisEncoder;
import redis.netty.Reply;
import spullara.util.concurrent.Promise;
import spullara.util.functions.Block;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used by the generated code to execute commands.
 */
public class RedisClientBase {

  private static final byte[] MESSAGE = "message".getBytes(Charsets.US_ASCII);
  private static final byte[] PMESSAGE = "pmessage".getBytes(Charsets.US_ASCII);
  private static final byte[] SUBSCRIBE = "subscribe".getBytes(Charsets.US_ASCII);
  private static final byte[] PSUBSCRIBE = "psubscribe".getBytes(Charsets.US_ASCII);
  private static final byte[] UNSUBSCRIBE = "unsubscribe".getBytes(Charsets.US_ASCII);
  private static final byte[] PUNSUBSCRIBE = "punsubscribe".getBytes(Charsets.US_ASCII);

  private Channel channel;
  private Queue<Promise> queue;

  public static <T extends RedisClientBase> Promise<T> connect(String hostname, int port) {
    return connect(hostname, port, (T) new RedisClientBase(), Executors.newCachedThreadPool());
  }

  private static final Pattern versionMatcher = Pattern.compile(
          "([0-9]+)\\.([0-9]+)(\\.([0-9]+))?");
  protected int version = 9999999;

  protected void parseInfo(BulkReply info) {
    try {
      BufferedReader br = new BufferedReader(new StringReader(info.asUTF8String()));
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
    } catch (Exception re) {
      // Server requires AUTH, check later
    }
  }

  protected static int parseVersion(String value) {
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

  public static <T extends RedisClientBase> Promise<T> connect(String hostname, int port, final T redisClient, ExecutorService executor) {
    final ClientBootstrap cb = new ClientBootstrap(new NioClientSocketChannelFactory(executor, executor));
    final Queue<Promise> queue = new LinkedTransferQueue<>();
    final SimpleChannelUpstreamHandler handler = new SimpleChannelUpstreamHandler() {
      @Override
      public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object message = e.getMessage();
        if (queue.isEmpty()) {
          if (message instanceof MultiBulkReply) {
            redisClient.handleMessage(message);
          } else {
            // Need some way to notify
          }
        } else {
          Promise poll = queue.poll();
          if (message instanceof ErrorReply) {
            poll.setException(new RedisException(((ErrorReply) message).data()));
          } else {
            poll.set(message);
          }
        }
      }

      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        if (queue.isEmpty()) {
          // Needed for pub/sub?
        } else {
          Promise poll = queue.poll();
          poll.setException(e.getCause());
        }
      }
    };
    final RedisEncoder encoder = new RedisEncoder();
    final RedisDecoder decoder = new RedisDecoder();
    cb.setPipelineFactory(new ChannelPipelineFactory() {
      @Override
      public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("redisEncoder", encoder);
        pipeline.addLast("redisDecoder", decoder);
        pipeline.addLast("result", handler);
        return pipeline;
      }
    });
    final Promise<T> redisClientBasePromise = new Promise<>();
    cb.connect(new InetSocketAddress(hostname, port)).addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          redisClient.init(channelFuture.getChannel(), queue);
          redisClient.execute(BulkReply.class, new Command("INFO")).onSuccess(new Block<BulkReply>() {
            @Override
            public void apply(BulkReply bulkReply) {
              redisClient.parseInfo(bulkReply);
              redisClientBasePromise.set(redisClient);
            }
          }).onFailure(new Block<Throwable>() {
            @Override
            public void apply(Throwable throwable) {
              redisClientBasePromise.setException(throwable);
            }
          });
        } else if (channelFuture.isCancelled()) {
          redisClientBasePromise.cancel(true);
        } else {
          redisClientBasePromise.setException(channelFuture.getCause());
        }
      }
    });
    return redisClientBasePromise;
  }

  protected RedisClientBase() {
  }

  protected void init(Channel channel, Queue<Promise> queue) {
    this.channel = channel;
    this.queue = queue;
  }

  public Promise<Void> close() {
    final Promise<Void> closed = new Promise<>();
    channel.close().addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          closed.set(null);
        } else if (channelFuture.isCancelled()) {
          closed.cancel(true);
        } else {
          closed.setException(channelFuture.getCause());
        }
      }
    });
    return closed;
  }

  private final Semaphore writerLock = new Semaphore(1);

  protected <T> Promise<T> execute(final Class<T> clazz, Command command) {
    final Promise<T> reply = new Promise<T>() {
      @Override
      public void set(T value) {
        // Check the type and fail if the wrong type
        if (!clazz.isInstance(value)) {
          setException(new RedisException("Incorrect type for " + value + " should be " + clazz.getName() + " but is " + value.getClass().getName()));
        } else {
          super.set(value);
        }
      }
    };
    if (subscribed.get()) {
      reply.setException(new RedisException("Already subscribed, cannot send this command"));
    } else {
      ChannelFuture write;
      writerLock.acquireUninterruptibly();
      queue.add(reply);
      write = channel.write(command);
      write.addListener(new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
          writerLock.release();
          if (future.isSuccess()) {
            // Netty doesn't call these in order
          } else if (future.isCancelled()) {
            reply.cancel(true);
          } else {
            reply.setException(future.getCause());
          }
        }
      });
    }
    return reply;
  }

  // Publish/subscribe section

  private AtomicBoolean subscribed = new AtomicBoolean();

  private void subscribed() {
    subscribed.set(true);
  }

  /**
   * Subscribes the client to the specified channels.
   *
   * @param subscriptions
   */
  public Promise<Void> subscribe(Object... subscriptions) {
    subscribed();
    Promise<Void> result = new Promise<>();
    channel.write(new Command(SUBSCRIBE, subscriptions)).addListener(wrapSubscribe(result));
    return result;
  }

  private ChannelFutureListener wrapSubscribe(final Promise<Void> result) {
    return new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          result.set(null);
        } else if (future.isCancelled()) {
          result.cancel(true);
        } else {
          result.setException(future.getCause());
        }
      }
    };
  }

  /**
   * Subscribes the client to the specified patterns.
   *
   * @param subscriptions
   */
  public Promise<Void> psubscribe(Object... subscriptions) {
    subscribed();
    Promise<Void> result = new Promise<>();
    channel.write(new Command(PSUBSCRIBE, subscriptions)).addListener(wrapSubscribe(result));
    return result;
  }

  /**
   * Unsubscribes the client to the specified channels.
   *
   * @param subscriptions
   */
  public Promise<Void> unsubscribe(Object... subscriptions) {
    subscribed();
    Promise<Void> result = new Promise<>();
    channel.write(new Command(UNSUBSCRIBE, subscriptions)).addListener(wrapSubscribe(result));
    return result;
  }

  /**
   * Unsubscribes the client to the specified patterns.
   *
   * @param subscriptions
   */
  public Promise<Void> punsubscribe(Object... subscriptions) {
    subscribed();
    Promise<Void> result = new Promise<>();
    channel.write(new Command(PUNSUBSCRIBE, subscriptions)).addListener(wrapSubscribe(result));
    return result;
  }

  private List<ReplyListener> replyListeners = new CopyOnWriteArrayList<>();

  /**
   * Add a reply listener to this client for subscriptions.
   */
  public void addListener(ReplyListener replyListener) {
    replyListeners.add(replyListener);
  }

  /**
   * Remove a reply listener from this client.
   */
  public boolean removeListener(ReplyListener replyListener) {
    return replyListeners.remove(replyListener);
  }

  private static Comparator<byte[]> BYTES = UnsignedBytes.lexicographicalComparator();

  protected void handleMessage(Object message) {
    MultiBulkReply reply = (MultiBulkReply) message;
    Reply[] data = reply.data();
    if (data.length != 3 && data.length != 4) {
      throw new RedisException("Invalid subscription messsage");
    }
    for (ReplyListener replyListener : replyListeners) {
      byte[] type = getBytes(data[0].data());
      byte[] data1 = getBytes(data[1].data());
      Object data2 = data[2].data();
      switch (type.length) {
        case 7:
          if (BYTES.compare(type, MESSAGE) == 0) {
            replyListener.message(data1, getBytes(data2));
            continue;
          }
          break;
        case 8:
          if (BYTES.compare(type, PMESSAGE) == 0) {
            replyListener.pmessage(data1, (byte[]) data2, ((ChannelBuffer) data[3].data()).array());
            continue;
          }
          break;
        case 9:
          if (BYTES.compare(type, SUBSCRIBE) == 0) {
            replyListener.subscribed(data1, ((Number) data2).intValue());
            continue;
          }
          break;
        case 10:
          if (BYTES.compare(type, PSUBSCRIBE) == 0) {
            replyListener.psubscribed(data1, ((Number) data2).intValue());
            continue;
          }
          break;
        case 11:
          if (BYTES.compare(type, UNSUBSCRIBE) == 0) {
            replyListener.unsubscribed(data1, ((Number) data2).intValue());
            continue;
          }
          break;
        case 12:
          if (BYTES.compare(type, PUNSUBSCRIBE) == 0) {
            replyListener.punsubscribed(data1, ((Number) data2).intValue());
            continue;
          }
          break;
        default:
          break;
      }
      close();
    }
  }

  private byte[] getBytes(Object data2) {
    ChannelBuffer d = (ChannelBuffer) data2;
    byte[] bytes = new byte[d.readableBytes()];
    d.getBytes(d.readerIndex(), bytes);
    return bytes;
  }
}
