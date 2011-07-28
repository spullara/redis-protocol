package redis;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Command holder.
 * User: sam
 * Date: 7/27/11
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Command {
  public static byte[] ARGS_PREFIX = "*".getBytes();
  public static byte[] CRLF = "\r\n".getBytes();
  public static byte[] BYTES_PREFIX = "$".getBytes();
  private static final byte[] EMPTY_BYTES = new byte[0];

  private byte[][] arguments;

  public Command(byte[]... arguments) {
    this.arguments = arguments;
  }

  public byte[][] getArguments() {
    return arguments;
  }

  public Command(Object... objects) {
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
      if (read == -1) {
        return null;
      }
      throw new IOException("Unexpected character");
    }
  }
}
