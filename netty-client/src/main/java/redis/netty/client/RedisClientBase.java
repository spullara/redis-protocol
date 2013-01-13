package redis.netty.client;

import org.jboss.netty.bootstrap.ClientBootstrap;
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
import redis.netty.RedisDecoder;
import redis.netty.RedisEncoder;
import spullara.util.concurrent.Promise;
import spullara.util.functions.Block;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used by the generated code to execute commands.
 */
public class RedisClientBase {

  private Channel channel;
  private Queue<Promise> queue;

  public static <T extends RedisClientBase> Promise<T> connect(String hostname, int port) {
    return connect(hostname, port, (T) new RedisClientBase());
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

  protected static <T extends RedisClientBase> Promise<T> connect(String hostname, int port, final T redisClient) {
    ExecutorService executor = Executors.newCachedThreadPool();
    final ClientBootstrap cb = new ClientBootstrap(new NioClientSocketChannelFactory(executor, executor));
    final Queue<Promise> queue = new LinkedTransferQueue<>();
    final SimpleChannelUpstreamHandler handler = new SimpleChannelUpstreamHandler() {
      @Override
      public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (queue.isEmpty()) {
          // Needed for pub/sub?
          throw new AssertionError("Queue was empty");
        } else {
          Promise poll = queue.poll();
          poll.set(e.getMessage());
        }
      }

      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        if (queue.isEmpty()) {
          // Needed for pub/sub?
          throw new AssertionError("Queue was empty");
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

  protected synchronized <T> Promise<T> execute(Class<T> clazz, Command command) {
    final Promise<T> reply = new Promise<>();
    channel.write(command).addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        // Tacit assumption that netty ensures the order
        if (future.isSuccess()) {
          queue.add(reply);
        } else if (future.isCancelled()) {
          reply.cancel(true);
        } else {
          reply.setException(future.getCause());
        }
      }
    });
    return reply;
  }
}
