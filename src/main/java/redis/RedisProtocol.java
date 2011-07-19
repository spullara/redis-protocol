package redis;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Implements the Redis Universal Protocol
 */
public class RedisProtocol {

  private SocketPool socketPool;

  public RedisProtocol(String host, int port) {
    socketPool = new SocketPool(host, port);
  }

  public static class Message {
    public static byte[] ARGS_PREFIX = "*".getBytes();
    public static byte[] CRLF = "\r\n".getBytes();
    public static byte[] BYTES_PREFIX = "$".getBytes();
    private static final byte[] EMPTY_BYTES = new byte[0];

    private byte[][] arguments;

    public Message(byte[]... arguments) {
      this.arguments = arguments;
    }

    public Message(Object... objects) {
      int length = objects.length;
      arguments = new byte[length][];
      for (int i = 0; i < length; i++) {
        Object object = objects[i];
        if (object == null) {
          arguments[i] = EMPTY_BYTES;
        } else if (object instanceof byte[]) {
          arguments[i] = (byte[]) object;
        } else {
          arguments[i] = object.toString().getBytes();
        }
      }
    }

    public void write(OutputStream os) throws IOException {
      os.write(ARGS_PREFIX);
      os.write(String.valueOf(arguments.length).getBytes());
      os.write(CRLF);
      for (byte[] argument : arguments) {
        os.write(BYTES_PREFIX);
        os.write(String.valueOf(argument.length).getBytes());
        os.write(CRLF);
        os.write(argument);
        os.write(CRLF);
      }
    }
  }

  public static interface Reply {
    public class StatusReply implements Reply {
      public static final char MARKER = '+';
      public final String status;
      public StatusReply(String status) {
        this.status = status;
      }
    }
    public class ErrorReply implements Reply {
      public static final char MARKER = '-';
      public final String error;
      public ErrorReply(String error) {
        this.error = error;
      }
    }
    public class IntegerReply implements Reply {
      public static final char MARKER = ':';
      public final int integer;
      public IntegerReply(int integer) {
        this.integer = integer;
      }
    }
    public class BulkReply implements Reply {
      public static final char MARKER = '$';
      public final byte[] bytes;
      public BulkReply(byte[] bytes) {
        this.bytes = bytes;
      }
    }
    public class MultiBulkReply implements Reply {
      public static final char MARKER = '*';
      public final byte[][] byteArrays;
      public MultiBulkReply(byte[][] byteArrays) {
        this.byteArrays = byteArrays;
      }
    }
  }

  public static class SocketPool {
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
        return new Socket(host, port);
      }
      return poll;
    }

    public void put(Socket socket) throws IOException {
      if (socket.isConnected()) {
        queue.offer(socket);
      }
    }
  }


  public Reply send(Message message) throws IOException {
    Socket socket = socketPool.get();
    try {
      OutputStream os = socket.getOutputStream();
      message.write(os);
      os.flush();
      DataInputStream is = new DataInputStream(socket.getInputStream());
      int code = is.read();
      switch(code) {
        case Reply.StatusReply.MARKER: {
          return new Reply.StatusReply(is.readLine());
        }
        case Reply.ErrorReply.MARKER: {
          return new Reply.ErrorReply(is.readLine());
        }
        case Reply.IntegerReply.MARKER: {
          return new Reply.IntegerReply(Integer.parseInt(is.readLine()));
        }
        case Reply.BulkReply.MARKER: {
          byte[] bytes = readBytes(is);
          return new Reply.BulkReply(bytes);
        }
        case Reply.MultiBulkReply.MARKER: {
          int size = Integer.parseInt(is.readLine());
          byte[][] byteArrays = new byte[size][];
          for (int i = 0; i < size; i++) {
            byteArrays[i] = readBytes(is);
          }
          return new Reply.MultiBulkReply(byteArrays);
        }
        default: {
          throw new IOException("Unexpected character in stream: " + code);
        }
      }
    } catch (IOException e) {
      Socket tmp = socket;
      socket = null;
      tmp.close();
      throw e;
    } finally {
      if (socket != null) {
        socketPool.put(socket);
      }
    }
  }

  private byte[] readBytes(DataInputStream is) throws IOException {
    int size = Integer.parseInt(is.readLine());
    if (size == -1) {
      return null;
    }
    byte[] bytes = new byte[size];
    int total = 0;
    int read;
    while (total < bytes.length &&  (read = is.read(bytes, total, bytes.length - total)) != -1) {
      total += read;
    }
    if (total < bytes.length) {
      throw new IOException("Failed to read enough bytes: " + total);
    }
    int cr = is.read();
    int lf = is.read();
    if (cr != '\r' || lf != '\n') {
      throw new IOException("Improper line ending: " + cr + ", " + lf);
    }
    return bytes;
  }
}
