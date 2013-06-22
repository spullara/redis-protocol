package redis;

import com.google.common.base.Charsets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static redis.util.Encoding.numToBytes;

/**
 * Command serialization.
 * User: sam
 * Date: 7/27/11
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Command {
  public static final char ARGS_PREFIX = '*';
  public static final char BYTES_PREFIX = '$';
  public static final byte[] CRLF = "\r\n".getBytes();
  public static final byte[] EMPTY_BYTES = new byte[0];

  private final Object name;
  private final Object[] objects;
  private final Object object1;
  private final Object object2;
  private final Object object3;

  public Command(Object[] objects) {
    this(null, null, null, null, objects);
  }

  public Command(Object name) {
    this(name, null, null, null, null);
  }

  public Command(Object name, Object[] objects) {
    this(name, null, null, null, objects);
  }

  public Command(Object name, Object object1) {
    this(name, object1, null, null, null);
  }

  public Command(Object name, Object object1, Object[] objects) {
    this(name, object1, null, null, objects);
  }

  public Command(Object name, Object object1, Object object2) {
    this(name, object1, object2, null, null);
  }

  public Command(Object name, Object object1, Object object2, Object object3) {
    this(name, object1, object2, object3, null);
  }

  private Command(Object name, Object object1, Object object2, Object object3, Object[] objects) {
    this.name = name;
    this.object1 = object1;
    this.object2 = object2;
    this.object3 = object3;
    this.objects = objects;
  }

  public void write(OutputStream os) throws IOException {
    writeDirect(os, name, object1, object2, object3, objects);
  }

  public static void writeDirect(OutputStream os, Object name, Object object1, Object object2, Object object3, Object[] objects) throws IOException {
    int others = (object1 == null ? 0 : 1) + (object2 == null ? 0 : 1) +
            (object3 == null ? 0 : 1) + (name == null ? 0 : 1);
    int length = objects == null ? 0 : objects.length;
    os.write(ARGS_PREFIX);
    os.write(numToBytes(length + others, true));
    if (name != null) writeObject(os, name);
    if (object1 != null) writeObject(os, object1);
    if (object2 != null) writeObject(os, object2);
    if (object3 != null) writeObject(os, object3);
    if (objects != null) {
      for (Object object : objects) {
        writeObject(os, object);
      }
    }
  }

  private static void writeObject(OutputStream os, Object object) throws IOException {
    byte[] argument;
    if (object == null) {
      argument = EMPTY_BYTES;
    } else if (object instanceof byte[]) {
      argument = (byte[]) object;
    } else {
      argument = object.toString().getBytes(Charsets.UTF_8);
    }
    writeArgument(os, argument);
  }

  private static void writeArgument(OutputStream os, byte[] argument) throws IOException {
    os.write(BYTES_PREFIX);
    os.write(numToBytes(argument.length, true));
    os.write(argument);
    os.write(CRLF);
  }

  public static Command read(InputStream is) throws IOException {
    int read = is.read();
    if (read == ARGS_PREFIX) {
      long numArgs = RedisProtocol.readLong(is);
      if (numArgs < 0 || numArgs > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Invalid size: " + numArgs);
      }
      byte[][] byteArrays = new byte[(int) numArgs][];
      for (int i = 0; i < numArgs; i++) {
        if (is.read() == BYTES_PREFIX) {
          byteArrays[i] = RedisProtocol.readBytes(is);
        } else {
          throw new IOException("Unexpected character");
        }
      }
      return new Command(byteArrays);
    } else {
      DataInputStream dis = new DataInputStream(is);
      // Special case MONITOR & PING & QUIT command
      if (read == 'M' || read == 'm') {
        String command = ("m" + dis.readLine()).toLowerCase();
        if (command.equals("monitor")) {
          byte[][] byteArrays = new byte[1][];
          byteArrays[0] = "monitor".getBytes();
          return new Command(byteArrays);
        }
      } else if (read == 'Q' || read == 'q') {
        String command = ("q" + dis.readLine()).toLowerCase();
        if (command.equals("quit")) {
          byte[][] byteArrays = new byte[1][];
          byteArrays[0] = "quit".getBytes();
          return new Command(byteArrays);
        }
      } else if (read == 'P' || read == 'p') {
        String command = ("p" + dis.readLine()).toLowerCase();
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
}
