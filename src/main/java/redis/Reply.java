package redis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
* Created by IntelliJ IDEA.
* User: sam
* Date: 7/27/11
* Time: 3:04 PM
* To change this template use File | Settings | File Templates.
*/
public abstract class Reply {

  private static Charset UTF8 = Charset.forName("UTF-8");

  public static Reply read(DataInputStream is) throws IOException {
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
        byte[] bytes = RedisProtocol.readBytes(is);
        return new BulkReply(bytes);
      }
      case MultiBulkReply.MARKER: {
        int size = Integer.parseInt(is.readLine());
        byte[][] byteArrays = new byte[size][];
        for (int i = 0; i < size; i++) {
          if (is.read() == BulkReply.MARKER) {
            byteArrays[i] = RedisProtocol.readBytes(is);
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

  public abstract void write(OutputStream os) throws IOException;

  public static class StatusReply extends Reply {
    public static final char MARKER = '+';
    public final String status;

    public StatusReply(String status) {
      this.status = status;
    }

    public void write(OutputStream os) throws IOException {
      os.write(MARKER);
      os.write(status.getBytes(UTF8));
      os.write("\r\n".getBytes(UTF8));
    }
  }

  public static class ErrorReply extends Reply {
    public static final char MARKER = '-';
    public final String error;

    public ErrorReply(String error) {
      this.error = error;
    }

    public void write(OutputStream os) throws IOException {
      os.write(MARKER);
      os.write(error.getBytes(UTF8));
      os.write("\r\n".getBytes(UTF8));
    }
  }

  public static class IntegerReply extends Reply {
    public static final char MARKER = ':';
    public final int integer;

    public IntegerReply(int integer) {
      this.integer = integer;
    }

    public void write(OutputStream os) throws IOException {
      os.write(MARKER);
      os.write(String.valueOf(integer).getBytes(UTF8));
      os.write("\r\n".getBytes(UTF8));
    }
  }

  public static class BulkReply extends Reply {
    public static final char MARKER = '$';
    public final byte[] bytes;

    public BulkReply(byte[] bytes) {
      this.bytes = bytes;
    }

    public void write(OutputStream os) throws IOException {
      os.write(MARKER);
      os.write(String.valueOf(bytes.length).getBytes(UTF8));
      os.write("\r\n".getBytes(UTF8));
      os.write(bytes);
      os.write("\r\n".getBytes(UTF8));
    }
  }

  public static class MultiBulkReply extends Reply {
    public static final char MARKER = '*';
    public final byte[][] byteArrays;

    public MultiBulkReply(byte[][] byteArrays) {
      this.byteArrays = byteArrays;
    }

    public void write(OutputStream os) throws IOException {
      os.write(MARKER);
      os.write(String.valueOf(byteArrays.length).getBytes(UTF8));
      os.write("\r\n".getBytes(UTF8));
      for (byte[] bytes : byteArrays) {
        os.write(BulkReply.MARKER);
        if (bytes == null) {
          os.write(String.valueOf(-1).getBytes(UTF8));
        } else {
          os.write(String.valueOf(bytes.length).getBytes(UTF8));
          os.write("\r\n".getBytes(UTF8));
          os.write(bytes);
        }
        os.write("\r\n".getBytes(UTF8));
      }
    }
  }
}
