package redis.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
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
            new ChannelInboundMessageHandlerAdapter<Reply<?>>() {
              @Override
              public void messageReceived(ChannelHandlerContext channelHandlerContext, Reply<?> reply) throws Exception {
                Promise<Reply> poll;
                synchronized (client) {
                  poll = queue.poll();
                  if (poll == null) {
                    throw new IllegalStateException("Promise queue is empty, received reply");
                  }
                }
                poll.set(reply);
              }
            });
    final Promise<RedisClientBase> promise = new Promise<>();
    socketChannel.connect(new InetSocketAddress(host, port)).addListener(new ChannelFutureListenerPromiseAdapter<>(promise, client));
    return promise;
  }

  public Promise<Reply> send(Command command) {
    Promise<Reply> reply = new Promise<>();
    synchronized (this) {
      queue.add(reply);
      socketChannel.write(command);
    }
    return reply;
  }
}
