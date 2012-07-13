package redis.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.base.Charsets;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.queue.BlockingReadHandler;
import redis.Command;
import redis.util.Encoding;

import static redis.util.Encoding.numToBytes;

public class RedisClient {
  private static final byte[] VALUE = "value".getBytes(Charsets.UTF_8);

  public static void main(String[] args) throws Exception {
    ExecutorService executor = Executors.newCachedThreadPool();
    final ClientBootstrap cb = new ClientBootstrap(new NioClientSocketChannelFactory(executor, executor));
    final BlockingReadHandler<Reply> blockingReadHandler = new BlockingReadHandler<Reply>();
    cb.setPipelineFactory(new ChannelPipelineFactory() {

      @Override
      public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("redisEncoder", new RedisEncoder());
        pipeline.addLast("redisDecoder", new RedisDecoder());
        pipeline.addLast("result", blockingReadHandler);
        return pipeline;
      }
    });
    ChannelFuture redis = cb.connect(new InetSocketAddress("localhost", 6379));
    redis.await().syncUninterruptibly();
    Channel channel = redis.getChannel();

    channel.write(new Command("set", "1", "value"));
    System.out.println(blockingReadHandler.read());
    channel.write(new Command("get", "1"));
    System.out.println(blockingReadHandler.read());

    int CALLS = 100000;
    long start = System.currentTimeMillis();
    byte[] set = "SET".getBytes(Charsets.UTF_8);
    for (int i = 0; i < CALLS; i++) {
      channel.write(new Command(set, numToBytes(i, false), VALUE));
      blockingReadHandler.read();
    }
    long end = System.currentTimeMillis();
    System.out.println(CALLS * 1000 / (end - start) + " calls per second");

    channel.close();
    cb.releaseExternalResources();
  }
}
