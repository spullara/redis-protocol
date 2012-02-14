package redis;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.common.base.Charsets;

/**
 * Command serialization.
 * User: sam
 * Date: 7/27/11
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Command {
  public static final byte[] ARGS_PREFIX = "*".getBytes();
  public static final byte[] CRLF = "\r\n".getBytes();
  public static final byte[] BYTES_PREFIX = "$".getBytes();
  public static final byte[] EMPTY_BYTES = new byte[0];
  public static final byte[] NEG_ONE = Command.numToBytes(-1);

  private byte[][] arguments;
  private Object[] objects;

  public Command(byte[]... arguments) {
    this.arguments = arguments;
  }

  public byte[][] getArguments() {
    return arguments;
  }

  public Command(Object... objects) {
    this.objects = objects;
  }

  public void write(OutputStream os) throws IOException {
    writeDirect(os, objects);
  }

  public static void writeDirect(OutputStream os, Object... objects) throws IOException {
    int length = objects.length;
    byte[][] arguments = new byte[length][];
    for (int i = 0; i < length; i++) {
      Object object = objects[i];
      if (object == null) {
        arguments[i] = EMPTY_BYTES;
      } else if (object instanceof byte[]) {
        arguments[i] = (byte[]) object;
      } else {
        arguments[i] = object.toString().getBytes(Charsets.UTF_8);
      }
    }
    writeDirect(os, arguments);
  }

  private static void writeDirect(OutputStream os, byte[][] arguments) throws IOException {
    os.write(ARGS_PREFIX);
    os.write(Command.numToBytes(arguments.length));
    os.write(CRLF);
    for (byte[] argument : arguments) {
      os.write(BYTES_PREFIX);
      os.write(Command.numToBytes(argument.length));
      os.write(CRLF);
      os.write(argument);
      os.write(CRLF);
    }
  }

  public static Command read(DataInputStream is) throws IOException {
    int read = is.read();
    if (read == ARGS_PREFIX[0]) {
      int numArgs = Integer.parseInt(is.readLine());
      byte[][] byteArrays = new byte[numArgs][];
      for (int i = 0; i < numArgs; i++) {
        if (is.read() == BYTES_PREFIX[0]) {
          byteArrays[i] = RedisProtocol.readBytes(is);
        } else {
          throw new IOException("Unexpected character");
        }
      }
      return new Command(byteArrays);
    } else {
      // Special case MONITOR & PING & QUIT command
      if (read == 'M' || read == 'm') {
        String command = ("m" + is.readLine()).toLowerCase();
        if (command.equals("monitor")) {
          byte[][] byteArrays = new byte[1][];
          byteArrays[0] = "monitor".getBytes();
          return new Command(byteArrays);
        }
      } else if (read == 'Q' || read == 'q') {
        String command = ("q" + is.readLine()).toLowerCase();
        if (command.equals("quit")) {
          byte[][] byteArrays = new byte[1][];
          byteArrays[0] = "quit".getBytes();
          return new Command(byteArrays);
        }
      } else if (read == 'P' || read == 'p') {
        String command = ("p" + is.readLine()).toLowerCase();
        if (command.equals("ping")) {
          byte[][] byteArrays = new byte[1][];
          byteArrays[0] = "ping".getBytes();
          return new Command(byteArrays);
        }
      } else if (read == -1) {
        return null;
      }
      throw new IOException("Unexpected character");
    }
  }

  // Optimized for the direct to ASCII bytes case
  // Could be even more optimized but it is already
  // about twice as fast as using Long.toString().getBytes()
  public static byte[] numToBytes(long value) {
    boolean negative = value < 0;
    int index = 1 + (negative ? 1 : 0);
    long current = negative ? -value : value;
    while ((current /= 10) > 0) {
      index++;
    }
    byte[] bytes = new byte[index];
    if (negative) {
      bytes[0] = '-';
    }
    current = negative ? -value : value;
    long tmp = current;
    while ((tmp /= 10) > 0) {
      bytes[--index] = (byte) ('0' + (current % 10));
      current = tmp;
    }
    bytes[--index] = (byte) ('0' + current);
    return bytes;
  }
}
