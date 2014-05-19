package redis.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.primitives.UnsignedBytes;

public class RedisClient {

  private SocketChannel socketChannel;
  private final Deque<CommandResp> waitingRespQueue;
  private final Queue<CommandResp> waitingToSendQueue;
  private boolean sendInProgess = false;
  private final Object queueSync = new Object();
  // private final List<CommandResp> historyDebugList;

  private final static Logger LOG = LoggerFactory.getLogger(RedisClient.class);
  private static final long RETRY_DELAY = 5000;

  private AtomicBoolean subscribed = new AtomicBoolean();
  protected int redisServerPort;
  private String redisServerHostname;
  private EventLoopGroup eventLoopGroup;

  private List<ReplyListener> replyListeners = new CopyOnWriteArrayList<>();

  public RedisClient(EventLoopGroup eventLoopGroup) {
    this.eventLoopGroup = eventLoopGroup;
    waitingRespQueue = new LinkedList<>();
    waitingToSendQueue = new LinkedList<>();
    // historyDebugList = new LinkedList<>();
  }

  public Channel channel() {
    return socketChannel;
  }

  private void prepareSocket() {
    LOG.debug("{} socket channel was: {}", this, socketChannel);
    socketChannel = new NioSocketChannel();
    LOG.debug("{}, (re)create socket channel: {}", this, socketChannel);

    eventLoopGroup.register(socketChannel);
    socketChannel.pipeline().addLast(new RedisCommandEncoder(), new RedisReplyDecoder(), new ChannelInboundHandlerAdapter() {

      @Override
      public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Reply reply = (Reply) msg;

        LOG.trace("{} ** redis msg received: type={} - val = {} ", RedisClient.this, reply.getClass().getSimpleName(), reply);

        CommandResp poll;
        synchronized (queueSync) {
          poll = waitingRespQueue.poll();
          LOG.trace("{} - after poll - queue size is : {} - sub={}", RedisClient.this, waitingRespQueue.size(), subscribed.get());
        }
        if (poll == null) {
          LOG.trace("{} - poll is null, it is a notif", RedisClient.this);// TODO
          // check
          // that
          // !! we could
          // resend a
          // subscribe...
          if (subscribed.get()) {
            // throw new
            // IllegalStateException("Promise queue is empty, received reply");
            if (reply instanceof MultiBulkReply) {
              handleMessage((MultiBulkReply) reply);
            } else {
              // Need some way to notify
              LOG.warn("cl-id={} - unexperted message received from server: ignored/dropped - {}", System.identityHashCode(RedisClient.this), reply);
            }
          } else {
            LOG.error("cl-id={} - cannot read redis response (poll is null) but it's not a subscribed socket !", System.identityHashCode(RedisClient.this));
          }
        } else {
          // LOG.debug("poll not null - redis msg received2 setResp {}",
          // System.identityHashCode(poll));
          try {
            poll.setReply(reply);
          } catch (Exception e) {
            // StringBuilder sb = new
            // StringBuilder("Dump of command history : \n");
            // for (CommandResp cr : historyDebugList){
            // sb.append(cr).append("\n");
            // }
            // LOG.debug("historyDebugList is" + sb.toString() );
            throw e;
          }

        }
      }

      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.warn("Error on " + ctx, cause);
        socketChannel.close();
      }
    });
  }

  private ChannelFuture internalConnect() {
    prepareSocket();
    socketChannel.closeFuture().addListener(new GenericFutureListener<ChannelFuture>() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        // reconnect TODO manage connection state...
        LOG.debug("{}, socket seems closed socket channel was: {} closeFuture={}", this, socketChannel, future);

        LOG.debug("{}, will Reconnect - socket channel was: {}", this, socketChannel);
        long nextRetryDelay = nextRetryDelay();
        socketChannel.eventLoop().schedule(new Runnable() {

          @Override
          public void run() {
            internalConnect().addListener(new ChannelFutureListener() {
              @Override
              public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                  LOG.info("{} Reconnect completed Sucessfuly {}", this, socketChannel);
                  checkWaitingToSendQueue();
                } else {
                  LOG.warn("{} Reconnect completed in ERROR {}", this, socketChannel);
                  LOG.warn("Reconnect completed in ERROR", future.cause());
                }
              }
            });

          }
        }, nextRetryDelay, TimeUnit.MILLISECONDS);

      }

    });
    LOG.debug("{}, will connect - socket channel : {}", this, socketChannel);
    return socketChannel.connect(new InetSocketAddress(redisServerHostname, redisServerPort));
  }

  private long nextRetryDelay() {
    return RETRY_DELAY;
  }

  public ChannelFuture connect(final String redisServerHostname, final int redisServerPort) {
    this.redisServerPort = redisServerPort;
    this.redisServerHostname = redisServerHostname;
    ChannelFuture f = internalConnect();
    f.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          LOG.info("{} connect completed Sucessfuly {}", this, socketChannel);
          checkWaitingToSendQueue();
        } else {
          LOG.warn("{} connect completed in ERROR {}", this, socketChannel);
          LOG.warn("connect completed in ERROR", future.cause());
        }
      }
    });
    return f;
  }

  private void subscribed() {
    subscribed.set(true);
  }

  protected <T extends Reply> Promise<T> execute(Class<T> clazz, Command command) {
    // LOG.debug("execute {} {}",clazz,command);

    final Promise<T> resPromise = eventLoopGroup.next().<T> newPromise();

    if (subscribed.get()) {
      resPromise.setFailure(new RedisException("Already subscribed, cannot send this command"));
    } else {

      final CommandResp cmdResp;
      final Promise<Reply> replyPromise = eventLoopGroup.next().<Reply> newPromise();
      cmdResp = new CommandResp(command, clazz, replyPromise);
      cmdResp.map(resPromise, clazz);
      // historyDebugList.add(cmdResp);
      synchronized (queueSync) {
        waitingToSendQueue.add(cmdResp);
      }
      checkWaitingToSendQueue();

      // LOG.debug("cl-id={} - after add to queue - queue size is : {} - command is {} - sub={}",System.identityHashCode(RedisClient.this),
      // waitingRespQueue.size(),new
      // String(command.getName()),subscribed.get());

    }
    return resPromise;
  }

  private void checkWaitingToSendQueue() {

    // netty (4.0.15) is intended to write in order on socket, but in some
    // case (lot of pipelined messages it seems false)
    // thus workarround : write only one message and wait for callback

    synchronized (queueSync) {
      if (!sendInProgess && waitingToSendQueue.size() > 0 && socketChannel != null && socketChannel.isActive()) {
        final CommandResp cmdResp = waitingToSendQueue.peek();
        ChannelFuture write;
        write = socketChannel.writeAndFlush(cmdResp.getCommand());
        waitingRespQueue.add(cmdResp);
        sendInProgess = true;
        write.addListener(new ChannelFutureListener() {
          @Override
          public void operationComplete(ChannelFuture future) throws Exception {
            LOG.trace("operationComplete for write {}", cmdResp);
            synchronized (queueSync) {
              sendInProgess = false;
              if (future.isSuccess()) {
                waitingToSendQueue.poll();
              } else if (future.isCancelled()) {
                LOG.error("error canceled");
                waitingRespQueue.pollLast();
                // throw new
                // IcebaseError("write cancel on socket");
                // resPromise.cancel(true);
              } else {
                waitingRespQueue.pollLast();
                // command is kept in waitingToSendQueue queue
                // TODO reinitialize connection ??!!!
                // resPromise.setFailure(future.cause());
                LOG.error("error on write to REDIS socket", future.cause());
              }
            }
            checkWaitingToSendQueue();
          }
        });
      }
    }

  }

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

  protected void handleMessage(Reply message) {
    MultiBulkReply reply = (MultiBulkReply) message;
    Reply[] data = reply.data();
    if (data.length != 3 && data.length != 4) {
      throw new RedisException("Invalid subscription messsage");
    }
    for (ReplyListener replyListener : replyListeners) {
      // LOG.debug("data[0] is {}, sn={}",data[0],data[0].getClass().getSimpleName());
      BulkReply typeReply = (BulkReply) data[0]; // TODO check before cast
      // (it would be a
      // protocol error from
      // redis server...)
      BulkReply data1Reply = (BulkReply) data[1];
      Reply data2Reply = (Reply) data[2];
      byte[] type = getBytes(typeReply.data());
      byte[] data1 = getBytes(data1Reply.data());

      // Object data2 = data2Reply.data();
      switch (type.length) {
      case 7:
        if (BYTES.compare(type, MESSAGE) == 0) {
          BulkReply data2BulkReply = (BulkReply) data2Reply;
          replyListener.message(data1, getBytes(data2BulkReply.data()));
          continue;
        }
        break;
      case 8:
        if (BYTES.compare(type, PMESSAGE) == 0) {
          // replyListener.pmessage(data1, (byte[]) data2, ((ByteBuf)
          // data[3].data()).array());
          BulkReply data2BulkReply = (BulkReply) data2Reply;
          replyListener.pmessage(data1, getBytes(data2BulkReply.data()), getBytes(((BulkReply) data[3]).data()));
          continue;
        }
        break;
      case 9:
        if (BYTES.compare(type, SUBSCRIBE) == 0) {
          IntegerReply data2IntegerReply = (IntegerReply) data2Reply;
          replyListener.subscribed(data1, data2IntegerReply.data().intValue());
          continue;
        }
        break;
      case 10:
        if (BYTES.compare(type, PSUBSCRIBE) == 0) {
          IntegerReply data2IntegerReply = (IntegerReply) data2Reply;
          replyListener.psubscribed(data1, data2IntegerReply.data().intValue());
          continue;
        }
        break;
      case 11:
        if (BYTES.compare(type, UNSUBSCRIBE) == 0) {
          IntegerReply data2IntegerReply = (IntegerReply) data2Reply;
          replyListener.unsubscribed(data1, data2IntegerReply.data().intValue());
          continue;
        }
        break;
      case 12:
        if (BYTES.compare(type, PUNSUBSCRIBE) == 0) {
          IntegerReply data2IntegerReply = (IntegerReply) data2Reply;
          replyListener.punsubscribed(data1, data2IntegerReply.data().intValue());
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
    ByteBuf d = (ByteBuf) data2;
    byte[] bytes = new byte[d.readableBytes()];
    d.getBytes(d.readerIndex(), bytes);
    return bytes;
  }

  public Future<Void> close() {
    LOG.debug("{}, will close socket channel: {}", this, socketChannel);
    final Promise<Void> closed = eventLoopGroup.next().newPromise();
    socketChannel.close().addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          closed.setSuccess(null);
        } else if (channelFuture.isCancelled()) {
          closed.cancel(true);
        } else {
          closed.setFailure(channelFuture.cause());
        }
      }
    });
    return closed;
  }

  // ---------------

  private static final String GET = "GET";
  private static final byte[] GET_BYTES = GET.getBytes(CharsetUtil.US_ASCII);

  /**
   * Get the value of a key String
   * 
   * @param key0
   * @return BulkReply
   */
  public Future<BulkReply> get(Object key0) {
    return execute(BulkReply.class, new Command(GET_BYTES, key0));
  }

  private static final String SET = "SET";
  private static final byte[] SET_BYTES = SET.getBytes(CharsetUtil.US_ASCII);

  /**
   * Set the string value of a key String
   * 
   * @param key0
   * @param value1
   * @return StatusReply
   */
  public Future<StatusReply> set(Object key0, Object value1) {
    return execute(StatusReply.class, new Command(SET_BYTES, key0, value1));
  }

  private static final String PUBLISH = "PUBLISH";
  private static final byte[] PUBLISH_BYTES = PUBLISH.getBytes(CharsetUtil.US_ASCII);

  /**
   * Post a message to a channel Pubsub
   * 
   * @param channel0
   * @param message1
   * @return IntegerReply
   */
  public Future<IntegerReply> publish(Object channel0, Object message1) {
    return execute(IntegerReply.class, new Command(PUBLISH_BYTES, channel0, message1));
  }

  private static final byte[] MESSAGE = "message".getBytes(CharsetUtil.US_ASCII);
  private static final byte[] PMESSAGE = "pmessage".getBytes(CharsetUtil.US_ASCII);
  private static final byte[] SUBSCRIBE = "subscribe".getBytes(CharsetUtil.US_ASCII);
  private static final byte[] PSUBSCRIBE = "psubscribe".getBytes(CharsetUtil.US_ASCII);
  private static final byte[] UNSUBSCRIBE = "unsubscribe".getBytes(CharsetUtil.US_ASCII);
  private static final byte[] PUNSUBSCRIBE = "punsubscribe".getBytes(CharsetUtil.US_ASCII);

  /**
   * Subscribes the client to the specified channels.
   * 
   * @param subscriptions
   */
  public Future<Void> subscribe(Object... subscriptions) {
    subscribed();
    Promise<Void> result = eventLoopGroup.next().newPromise();
    LOG.debug("{} redis msg sent: SUBSCRIBE {}", RedisClient.this, subscriptions);
    socketChannel.write(new Command(SUBSCRIBE, subscriptions)).addListener(wrapSubscribe(result));
    socketChannel.flush();
    return result;
  }

  /**
   * Subscribes the client to the specified patterns.
   * 
   * @param subscriptions
   */
  public Future<Void> psubscribe(Object... subscriptions) {
    subscribed();
    Promise<Void> result = eventLoopGroup.next().newPromise();
    LOG.debug("{} redis msg sent: SUBSCRIBE {}", RedisClient.this, subscriptions);
    socketChannel.write(new Command(PSUBSCRIBE, subscriptions)).addListener(wrapSubscribe(result));
    socketChannel.flush();
    return result;
  }

  private ChannelFutureListener wrapSubscribe(final Promise<Void> result) {
    return new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          result.setSuccess(null);
        } else if (future.isCancelled()) {
          result.cancel(true);
        } else {
          result.setFailure(future.cause());
        }
      }
    };
  }

  public Future<Void> unsubscribe(Object... subscriptions) {
    subscribed();
    Promise<Void> result = eventLoopGroup.next().newPromise();
    socketChannel.write(new Command(UNSUBSCRIBE, subscriptions)).addListener(wrapSubscribe(result));
    socketChannel.flush();
    return result;
  }

  /**
   * Unsubscribes the client to the specified patterns.
   * 
   * @param subscriptions
   */
  public Future<Void> punsubscribe(Object... subscriptions) {
    subscribed();
    Promise<Void> result = eventLoopGroup.next().newPromise();
    socketChannel.write(new Command(PUNSUBSCRIBE, subscriptions)).addListener(wrapSubscribe(result));
    socketChannel.flush();
    return result;
  }

  private static final String RPUSH = "RPUSH";
  private static final byte[] RPUSH_BYTES = RPUSH.getBytes(CharsetUtil.US_ASCII);

  /**
   * Append one or multiple values to a list List
   * 
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public Future<IntegerReply> rpush(Object key0, Object[] value1) {
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, value1);
    return execute(IntegerReply.class, new Command(RPUSH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Future<IntegerReply> rpush_(Object... arguments) {
    return execute(IntegerReply.class, new Command(RPUSH_BYTES, arguments));
  }

  private static final String LRANGE = "LRANGE";
  private static final byte[] LRANGE_BYTES = LRANGE.getBytes(CharsetUtil.US_ASCII);

  /**
   * Get a range of elements from a list List
   * 
   * @param key0
   * @param start1
   * @param stop2
   * @return MultiBulkReplyNR
   */
  public Future<MultiBulkReply> lrange(Object key0, Object start1, Object stop2) {
    return execute(MultiBulkReply.class, new Command(LRANGE_BYTES, key0, start1, stop2));
  }

  private static final String FLUSHALL = "FLUSHALL";
  private static final byte[] FLUSHALL_BYTES = FLUSHALL.getBytes(CharsetUtil.US_ASCII);

  public Future<StatusReply> flushall() {
    return execute(StatusReply.class, new Command(FLUSHALL_BYTES));
  }

  private static final String ZADD = "ZADD";
  private static final byte[] ZADD_BYTES = ZADD.getBytes(CharsetUtil.US_ASCII);

  /**
   * Add one or more members to a sorted set, or update its score if it already exists Sorted_set
   * 
   * @param args
   * @return IntegerReply
   */
  public Future<IntegerReply> zadd(Object[] args) {
    return execute(IntegerReply.class, new Command(ZADD_BYTES, args));
  }

  public Future<IntegerReply> zadd(Object key, Long score, Object member) {
    return execute(IntegerReply.class, new Command(ZADD_BYTES, key, score, member));
  }

  private static final String ZRANGE = "ZRANGE";
  private static final byte[] ZRANGE_BYTES = ZRANGE.getBytes(CharsetUtil.US_ASCII);

  /**
   * Return a range of members in a sorted set, by index Sorted_set
   * 
   * @param key0
   * @param start1
   * @param stop2
   * @param withscores3
   * @return MultiBulkReply
   */
  public Future<MultiBulkReply> zrange(Object key0, Object start1, Object stop2, Object withscores3) {
    List<Object> list = new ArrayList<>();
    list.add(key0);
    list.add(start1);
    list.add(stop2);
    if (withscores3 != null)
      list.add(withscores3);
    return this.<MultiBulkReply> execute(MultiBulkReply.class, new Command(ZRANGE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Future<MultiBulkReply> zrange_(Object... arguments) {
    return execute(MultiBulkReply.class, new Command(ZRANGE_BYTES, arguments));
  }

  private static final String DEL = "DEL";
  private static final byte[] DEL_BYTES = DEL.getBytes(CharsetUtil.US_ASCII);

  /**
   * Delete a key Generic
   * 
   * @param key0
   * @return IntegerReply
   */
  public Future<IntegerReply> del(Object[] key0) {
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(IntegerReply.class, new Command(DEL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Future<IntegerReply> del_(Object... arguments) {
    return execute(IntegerReply.class, new Command(DEL_BYTES, arguments));
  }

  private static final String INCR = "INCR";
  private static final byte[] INCR_BYTES = INCR.getBytes(CharsetUtil.US_ASCII);

  /**
   * Increment the integer value of a key by one String
   * 
   * @param key0
   * @return IntegerReply
   */
  public Future<IntegerReply> incr(Object key0) {
    return execute(IntegerReply.class, new Command(INCR_BYTES, key0));
  }

  private static final String SETNX = "SETNX";
  private static final byte[] SETNX_BYTES = SETNX.getBytes(CharsetUtil.US_ASCII);

  /**
   * Set the value of a key, only if the key does not exist String
   * 
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public Future<IntegerReply> setnx(Object key0, Object value1) {
    return execute(IntegerReply.class, new Command(SETNX_BYTES, key0, value1));
  }

  // --
  private static final String SETEX = "SETEX";
  private static final byte[] SETEX_BYTES = SETEX.getBytes(CharsetUtil.US_ASCII);

  /**
   * Set the value and expiration of a key String
   * 
   * @param key0
   * @param seconds1
   * @param value2
   * @return StatusReply
   */
  public Future<StatusReply> setex(Object key0, long ttlSeconds, Object value2) {
    return execute(StatusReply.class, new Command(SETEX_BYTES, key0, ttlSeconds, value2));
  }

  private static final String TTL = "TTL";
  private static final byte[] TTL_BYTES = TTL.getBytes(CharsetUtil.US_ASCII);

  /**
   * Get the time to live for a key Generic
   * 
   * @param key0
   * @return IntegerReply
   */
  public Future<IntegerReply> ttl(Object key0) {
    return execute(IntegerReply.class, new Command(TTL_BYTES, key0));
  }

  private static final String AUTH = "AUTH";
  private static final byte[] AUTH_BYTES = AUTH.getBytes(CharsetUtil.US_ASCII);

  public Future<StatusReply> auth(Object redisServerPassword) {
    return execute(StatusReply.class, new Command(AUTH_BYTES, redisServerPassword));
  }

  private static final String KEYS = "KEYS";
  private static final byte[] KEYS_BYTES = KEYS.getBytes(CharsetUtil.US_ASCII);

  public Future<MultiBulkReply> keys(Object pattern0) {
    return execute(MultiBulkReply.class, new Command(KEYS_BYTES, pattern0));
  }

  public Future<MultiBulkReply> mget(Collection<String> keys) {
    return execute(MultiBulkReply.class, new Command(MGET_BYTES, keys.toArray(new Object[keys.size()])));
  }

  private static final String MGET = "MGET";
  private static final byte[] MGET_BYTES = MGET.getBytes(CharsetUtil.US_ASCII);

  public Future<MultiBulkReply> mget(Object[] key0) {
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(MultiBulkReply.class, new Command(MGET_BYTES, list.toArray(new Object[list.size()])));
  }

  public Future<MultiBulkReply> mget_(Object... arguments) {
    return execute(MultiBulkReply.class, new Command(MGET_BYTES, arguments));
  }

  private static final String EVAL = "EVAL";
  private static final byte[] EVAL_BYTES = EVAL.getBytes(Charsets.US_ASCII);

  /**
   * Execute a Lua script server side Scripting
   * 
   * @param script0
   * @param numkeys1
   * @param key2
   * @return Reply
   */
  public Future<Reply> eval(Object script0, Object numkeys1, Object[] key2) {
    List<Object> list = new ArrayList<>();
    list.add(script0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return execute(Reply.class, new Command(EVAL_BYTES, list.toArray(new Object[list.size()])));
  }

  public Future<Reply> eval2(Object script, List<Object> keys, List<Object> args) {
    List<Object> list = new ArrayList<>();
    list.add(script);
    list.add(keys.size());
    list.addAll(keys);
    list.addAll(args);
    return execute(Reply.class, new Command(EVAL_BYTES, list.toArray(new Object[list.size()])));
  }

  public Future<Reply> eval3(String script, List<String> keys, List<String> args) {
    List<Object> list = new ArrayList<>();
    list.add(script);
    list.add(keys.size());
    list.addAll(keys);
    list.addAll(args);
    return execute(Reply.class, new Command(EVAL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Future<Reply> eval_(Object... arguments) {
    return execute(Reply.class, new Command(EVAL_BYTES, arguments));
  }

  // ----------------------------------

  @Override
  public String toString() {
    String localAddress = socketChannel == null || socketChannel.localAddress() == null ? " --" : socketChannel.localAddress().toString();
    return "[RedisClient " + Integer.toHexString(hashCode()) + " at socket/localAddress=" + socketChannel + " " + localAddress + "]";
  }

}
