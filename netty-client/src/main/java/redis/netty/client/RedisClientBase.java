package redis.netty.client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import redis.Command;
import redis.netty.RedisDecoder;
import redis.netty.RedisEncoder;
import redis.netty.Reply;
import spullara.util.concurrent.Promise;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used by the generated code to execute commands.
 */
public class RedisClientBase {

  private Channel channel;
  private Queue<Promise> queue;

  public static Promise<? extends RedisClientBase> connect(String hostname, int port) {
    return connect(hostname, port, new RedisClientBase());
  }

  private static final Pattern versionMatcher = Pattern.compile(
          "([0-9]+)\\.([0-9]+)(\\.([0-9]+))?");
  protected int version = 9999999;

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

  protected static Promise<? extends RedisClientBase> connect(String hostname, int port, final RedisClientBase redisClientBase) {
    ExecutorService executor = Executors.newCachedThreadPool();
    final ClientBootstrap cb = new ClientBootstrap(new NioClientSocketChannelFactory(executor, executor));
    final Queue<Promise> queue = new LinkedTransferQueue<>();
    final SimpleChannelUpstreamHandler handler = new SimpleChannelUpstreamHandler() {
      @Override
      public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (queue.isEmpty()) {
          // Needed for pub/sub?
        } else {
          Promise poll = queue.poll();
          poll.set(e.getMessage());
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
    cb.setPipelineFactory(new ChannelPipelineFactory() {

      @Override
      public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("redisEncoder", new RedisEncoder());
        pipeline.addLast("redisDecoder", new RedisDecoder());
        pipeline.addLast("result", handler);
        return pipeline;
      }
    });
    ChannelFuture redis = cb.connect(new InetSocketAddress(hostname, port));
    final Promise<RedisClientBase> redisClientBasePromise = new Promise<>();
    redis.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          redisClientBase.init(channelFuture.getChannel(), queue);
          redisClientBasePromise.set(redisClientBase);
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

  protected synchronized Promise<? extends Reply> execute(Command command) {
    Promise<Reply> reply = new Promise<>();
    queue.add(reply);
    channel.write(command);
    return reply;
  }
}
