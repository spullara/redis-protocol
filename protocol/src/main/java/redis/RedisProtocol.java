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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Implements the Redis Universal Protocol. Send a command, receive a command, send a reply
 * and receive a reply.
 */
public class RedisProtocol {

  private static final char CR = '\r';
  private static final char LF = '\n';
  private static final char ZERO = '0';
  private final BufferedInputStream is;
  private final OutputStream os;

  public RedisProtocol(Socket socket) throws IOException {
    is = new BufferedInputStream(socket.getInputStream());
    os = new BufferedOutputStream(socket.getOutputStream());
  }

  public static byte[] readBytes(InputStream is) throws IOException {
    int size = readInteger(is);
    int read;
    if (size == -1) {
      return null;
    }
    byte[] bytes = new byte[size];
    int total = 0;
    while (total < bytes.length && (read = is.read(bytes, total, bytes.length - total)) != -1) {
      total += read;
    }
    if (total < bytes.length) {
      throw new IOException("Failed to read enough bytes: " + total);
    }
    int cr = is.read();
    int lf = is.read();
    if (cr != CR || lf != LF) {
      throw new IOException("Improper line ending: " + cr + ", " + lf);
    }
    return bytes;
  }

  public static int readInteger(InputStream is) throws IOException {
    int sign;
    int read = is.read();
    if (read == '-') {
      read = is.read();
      sign = -1;
    } else {
      sign = 1;
    }
    int size = 0;
    do {
      if (read == -1) {
        throw new EOFException("Unexpected end of stream");
      } else if (read == CR) {
        if (is.read() == LF) {
          return size * sign;
        }
      }
      int value = read - ZERO;
      if (value >= 0 && value < 10) {
        size *= 10;
        size += value;
      } else {
        throw new IOException("Invalid character in integer");
      }
      read = is.read();
    } while (true);
  }

  public static Reply receive(InputStream is) throws IOException {
    int code = is.read();
    switch (code) {
      case StatusReply.MARKER: {
        return new StatusReply(new DataInputStream(is).readLine());
      }
      case ErrorReply.MARKER: {
        return new ErrorReply(new DataInputStream(is).readLine());
      }
      case IntegerReply.MARKER: {
        return new IntegerReply(readInteger(is));
      }
      case BulkReply.MARKER: {
        return new BulkReply(readBytes(is));
      }
      case MultiBulkReply.MARKER: {
        return new MultiBulkReply(is);
      }
      default: {
        throw new IOException("Unexpected character in stream: " + code);
      }
    }
  }

  public Reply send(Command command) throws IOException {
    sendAsync(command);
    return receiveAsync();
  }

  public Reply receiveAsync() throws IOException {
    synchronized (is) {
      return receive(is);
    }
  }

  public void sendAsync(Command command) throws IOException {
    synchronized (os) {
      command.write(os);
    }
    os.flush();
  }

  public Command receive() throws IOException {
    synchronized (is) {
      return Command.read(is);
    }
  }

  public void write(byte[] bytes) throws IOException {
    synchronized (os) {
      os.write(bytes);
    }
    os.flush();
  }

  public void close() throws IOException {
    is.close();
    os.close();
  }
}
