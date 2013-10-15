package redis.netty4;

import com.google.common.base.Charsets;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import static redis.util.Encoding.numToBytes;

public class RedisClient {
  private static final byte[] VALUE = "value".getBytes(Charsets.UTF_8);
  private static byte[] set = "SET".getBytes(Charsets.UTF_8);
  private static final int CALLS = 1000000;

  private static int i = 0;

  private static void write(Channel ch) {
    i++;
    ch.write(new Command(set, numToBytes(i, false), VALUE));
  }

  public static void main(String[] args) throws Exception {
    final SocketChannel ch = new NioSocketChannel();
    new NioEventLoopGroup().register(ch);
    final long start = System.currentTimeMillis();
    ch.pipeline().addLast(new RedisCommandEncoder(), new RedisReplyDecoder());
    ch.connect(new InetSocketAddress("localhost", 6379)).addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        write(ch);
      }
    });
  }
}
