package redis.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
* Pooling sockets.
* User: sam
* Date: 7/27/11
* Time: 3:00 PM
* To change this template use File | Settings | File Templates.
*/
public class SocketPool {
  private final String host;
  private final int port;
  private final Queue<Socket> queue = new ArrayBlockingQueue<Socket>(100, true);

  public SocketPool(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public Socket get() throws IOException {
    Socket poll = queue.poll();
    if (poll == null) {
      Socket socket = new Socket(host, port);
      socket.setKeepAlive(true);
      socket.setTcpNoDelay(true);
      socket.setReceiveBufferSize(1024);
      return socket;
    }
    return poll;
  }

  public void put(Socket socket) throws IOException {
    if (socket.isConnected()) {
      queue.offer(socket);
    }
  }
}
