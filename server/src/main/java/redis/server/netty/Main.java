package redis.server.netty;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoop;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Redis server
 */
public class Main {
  @Argument(alias = "p")
  private static Integer port = 6380;

  public static void main(String[] args) throws InterruptedException {
    try {
      Args.parse(Main.class, args);
    } catch (IllegalArgumentException e) {
      Args.usage(Main.class);
      System.exit(1);
    }

    // Only execute the command handler in a single thread
    final EventExecutor ee = new DefaultEventExecutor(1);
    final RedisCommandHandler commandHandler = new RedisCommandHandler(new SimpleRedisServer());

    // Configure the server.
    ServerBootstrap b = new ServerBootstrap();
    try {
        b.eventLoop(new NioEventLoop(), new NioEventLoop())
         .channel(new NioServerSocketChannel())
         .option(ChannelOption.SO_BACKLOG, 100)
         .localAddress(port)
         .childOption(ChannelOption.TCP_NODELAY, true)
         .childHandler(new ChannelInitializer<SocketChannel>() {
             @Override
             public void initChannel(SocketChannel ch) throws Exception {
               ChannelPipeline p = ch.pipeline();
               p.addLast(new RedisCommandDecoder());
               p.addLast(new RedisReplyEncoder());
               p.addLast(ee, commandHandler);
             }
         });

        // Start the server.
        ChannelFuture f = b.bind().sync();

        // Wait until the server socket is closed.
        f.channel().closeFuture().sync();
    } finally {
        // Shut down all event loops to terminate all threads.
        b.shutdown();
    }
  }
}
