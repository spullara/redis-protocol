package redis;

import redis.reply.BulkReply;
import redis.reply.ErrorReply;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;
import redis.reply.StatusReply;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Implements the Redis Universal Protocol. Send a command, receive a command, send a reply
 * and receive a reply.
 */
public class RedisProtocol {

  private DataInputStream is;
  private OutputStream os;

  public RedisProtocol(Socket socket) throws IOException {
    is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    os = new BufferedOutputStream(socket.getOutputStream());
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
      case StatusReply.MARKER: {
        return new StatusReply(is.readLine());
      }
      case ErrorReply.MARKER: {
        return new ErrorReply(is.readLine());
      }
      case IntegerReply.MARKER: {
        return new IntegerReply(Integer.parseInt(is.readLine()));
      }
      case BulkReply.MARKER: {
        byte[] bytes = readBytes(is);
        return new BulkReply(bytes);
      }
      case MultiBulkReply.MARKER: {
        int size = Integer.parseInt(is.readLine());
        byte[][] byteArrays = new byte[size][];
        for (int i = 0; i < size; i++) {
          if (is.read() == BulkReply.MARKER) {
            byteArrays[i] = readBytes(is);
          } else {
            throw new IOException("Unexpected character in stream");
          }
        }
        return new MultiBulkReply(byteArrays);
      }
      default: {
        throw new IOException("Unexpected character in stream: " + code);
      }
    }
  }

  public Reply send(Command command) throws IOException {
    command.write(os);
    os.flush();
    return receive(is);
  }

  public Command receive() throws IOException {
    return Command.read(is);
  }

  public void send(Reply reply) throws IOException {
    reply.write(os);
    os.flush();
  }

  public void write(byte[] bytes) throws IOException {
    os.write(bytes);
    os.flush();
  }
}
