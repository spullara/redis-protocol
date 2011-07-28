package redis;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Implements the Redis Universal Protocol. Send a command, receive a command, send a reply
 * and receive a reply.
 */
public class RedisProtocol {

  private Socket socket;

  public RedisProtocol(Socket socket) {
    this.socket = socket;
  }

  public static byte[] readBytes(DataInputStream is) throws IOException {
    int size = Integer.parseInt(is.readLine());
    if (size == -1) {
      return null;
    }
    byte[] bytes = new byte[size];
    int total = 0;
    int read;
    while (total < bytes.length && (read = is.read(bytes, total, bytes.length - total)) != -1) {
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

  public static Reply receive(DataInputStream is) throws IOException {
    int code = is.read();
    switch (code) {
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
          if (is.read() == Reply.BulkReply.MARKER) {
            byteArrays[i] = readBytes(is);
          } else {
            throw new IOException("Unexpected character in stream");
          }
        }
        return new Reply.MultiBulkReply(byteArrays);
      }
      default: {
        throw new IOException("Unexpected character in stream: " + code);
      }
    }
  }

  public Reply send(Command command) throws IOException {
    OutputStream os = socket.getOutputStream();
    command.write(os);
    os.flush();
    DataInputStream is = new DataInputStream(socket.getInputStream());
    return receive(is);
  }

  public Command receive() throws IOException {
    DataInputStream is = new DataInputStream(socket.getInputStream());
    return Command.read(is);
  }

  public void send(Reply reply) throws IOException {
    OutputStream os = socket.getOutputStream();
    reply.write(os);
    os.flush();
  }
}
