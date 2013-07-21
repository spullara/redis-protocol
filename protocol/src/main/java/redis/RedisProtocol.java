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

  public static final char CR = '\r';
  public static final char LF = '\n';
  private static final char ZERO = '0';
  private final BufferedInputStream is;
  private final OutputStream os;
  private final Socket socket;

  /**
   * Create a new RedisProtocol from a socket connection.
   *
   * @param socket
   * @throws IOException
   */
  public RedisProtocol(Socket socket) throws IOException {
    this.socket = socket;
    is = new BufferedInputStream(socket.getInputStream());
    os = new BufferedOutputStream(socket.getOutputStream());
  }

  /**
   * Create a new RedisProtocol using provided input and output streams.
   *
   * @param is a buffered input stream is required
   * @param os a buffered input stream is required
   * @throws IOException
   */
  public RedisProtocol(BufferedInputStream is, OutputStream os) {
    this.is = is;
    this.os = os;
    socket = null;
  }

  /**
   * Read fixed size field from the stream.
   *
   * @param is
   * @return
   * @throws IOException
   */
  public static byte[] readBytes(InputStream is) throws IOException {
    long size = readLong(is);
    if (size > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Java only supports arrays up to " + Integer.MAX_VALUE + " in size");
    }
    int read;
    if (size == -1) {
      return null;
    }
    if (size < 0) {
      throw new IllegalArgumentException("Invalid size: " + size);
    }
    byte[] bytes = new byte[(int) size];
    int total = 0;
    int length = bytes.length;
    while (total < length && (read = is.read(bytes, total, length - total)) != -1) {
      total += read;
    }
    if (total < length) {
      throw new IOException("Failed to read enough bytes: " + total);
    }
    int cr = is.read();
    int lf = is.read();
    if (cr != CR || lf != LF) {
      throw new IOException("Improper line ending: " + cr + ", " + lf);
    }
    return bytes;
  }

  /**
   * Read a signed ascii integer from the input stream.
   * @param is
   * @return
   * @throws IOException
   */
  public static long readLong(InputStream is) throws IOException {
    int sign;
    int read = is.read();
    if (read == '-') {
      read = is.read();
      sign = -1;
    } else {
      sign = 1;
    }
    long number = 0;
    do {
      if (read == -1) {
        throw new EOFException("Unexpected end of stream");
      } else if (read == CR) {
        if (is.read() == LF) {
          return number * sign;
        }
      }
      int value = read - ZERO;
      if (value >= 0 && value < 10) {
        number *= 10;
        number += value;
      } else {
        throw new IOException("Invalid character in integer");
      }
      read = is.read();
    } while (true);
  }

  /**
   * Read a Reply from an input stream.
   *
   * @param is
   * @return
   * @throws IOException
   */
  public static Reply receive(InputStream is) throws IOException {
    int code = is.read();
    if (code == -1) {
      throw new EOFException();
    }
    switch (code) {
      case StatusReply.MARKER: {
        return new StatusReply(new DataInputStream(is).readLine());
      }
      case ErrorReply.MARKER: {
        return new ErrorReply(new DataInputStream(is).readLine());
      }
      case IntegerReply.MARKER: {
        return new IntegerReply(readLong(is));
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

  public static byte[] toBytes(Number length) {
    return length.toString().getBytes();
  }

  /**
   * Wait for a reply on the input stream.
   *
   * @return
   * @throws IOException
   */
  public Reply receiveAsync() throws IOException {
    synchronized (is) {
      return receive(is);
    }
  }

  /**
   * Send a command over the wire, do not wait for a reponse.
   *
   * @param command
   * @throws IOException
   */
  public void sendAsync(Command command) throws IOException {
    synchronized (os) {
      command.write(os);
    }
    os.flush();
  }

  /**
   * Close the input and output streams. Will also disconnect the socket.
   *
   * @throws IOException
   */
  public void close() throws IOException {
    is.close();
    os.close();
    if (socket != null) {
      socket.close();
    }
  }
}
