package redis.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import spullara.util.concurrent.Promise;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Uses netty4 to talk to redis.
 */
public class RedisClientBase {

  private final static NioEventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
  private final SocketChannel socketChannel;
  private final Queue<Promise<Reply>> queue;

  protected RedisClientBase(SocketChannel socketChannel, Queue<Promise<Reply>> queue) {
    this.socketChannel = socketChannel;
    this.queue = queue;
    group.register(socketChannel);
  }

  public static Promise<RedisClientBase> connect(String host, int port) {
    final Queue<Promise<Reply>> queue = new LinkedList<>();
    SocketChannel socketChannel = new NioSocketChannel();
    final RedisClientBase client = new RedisClientBase(socketChannel, queue);
    socketChannel.pipeline().addLast(new RedisCommandEncoder(), new RedisReplyDecoder(),
            new SimpleChannelInboundHandler<Reply<?>>() {
              @Override
              protected void channelRead0(ChannelHandlerContext channelHandlerContext, Reply<?> reply) throws Exception {
                Promise<Reply> poll = queue.poll();
                if (poll == null) {
                  throw new IllegalStateException("Promise queue is empty, received reply");
                }
                poll.set(reply);
              }
            });
    final Promise<RedisClientBase> promise = new Promise<>();
    socketChannel.connect(new InetSocketAddress(host, port)).addListener(new ChannelFutureListenerPromiseAdapter<>(promise, client));
    return promise;
  }

  public Promise<Reply> send(final Command command) {
    final Promise<Reply> reply = new Promise<>();
    socketChannel.eventLoop().execute(new Runnable() {
      @Override
      public void run() {
        queue.add(reply);
        socketChannel.writeAndFlush(command);
      }
    });
    return reply;
  }
}
