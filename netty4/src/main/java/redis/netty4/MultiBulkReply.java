package redis.netty4;

import static redis.util.Encoding.NEG_ONE_WITH_CRLF;
import static redis.util.Encoding.numToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Nested replies.
 */
public class MultiBulkReply extends AbstarctReply<Reply[]> {
  public static final char MARKER = '*';
  public static final MultiBulkReply EMPTY = new MultiBulkReply(new AbstarctReply[0]);

  // private final static Logger LOG = LoggerFactory.getLogger(MultiBulkReplyNR.class);

  // private AbstarctReply[] replies;
  private final int size;
  private int index = 0;

  public MultiBulkReply(Reply[] replies) {
    // this.replies = replies;
    super(replies);
    size = replies.length;
  }

  public MultiBulkReply(int size2) {
    super(null);
    this.size = size2;

    if (size == -1) {
      setData(null);
    } else {
      if (size < 0) {
        throw new IllegalArgumentException("Invalid size: " + size);
      }
      setData(new Reply[size]);
    }
  }

  // public void read(ByteBuf is) throws IOException {
  //
  // for (int i = index; i < size; i++) {
  // replies[i] = rd.readReply(is);
  // index = i + 1;
  // //rd.checkpoint();
  // }
  // }

  public void add(Reply r) throws IOException {
    if (index > data().length - 1) {
      throw new IOException("cannot add, MultiBulkReply is full !");
    }
    data()[index] = r;
    index++;
  }

  public boolean isFull() {
    return index == data().length;
  }

  @Override
  public void write(ByteBuf os) throws IOException {
    os.writeByte(MARKER);
    if (data() == null) {
      os.writeBytes(NEG_ONE_WITH_CRLF);
    } else {
      os.writeBytes(numToBytes(data().length, true));
      for (Reply reply : data()) {
        reply.write(os);
      }
    }
  }

  public List<String> asStringList(Charset charset) {
    if (data() == null)
      return null;
    List<String> strings = new ArrayList<String>(data().length);
    for (Reply reply : data()) {
      if (reply instanceof BulkReply) {
        strings.add(((BulkReply) reply).asString(charset));
      } else {
        // throw new IllegalArgumentException("Could not convert " + reply + " to a string");
        strings.add(reply.toString());
      }
    }
    return strings;
  }

  public List<byte[]> asByteArrayList() {
    if (data() == null)
      return null;
    List<byte[]> byteArrays = new ArrayList<byte[]>(data().length);
    for (Reply reply : data()) {
      if (reply instanceof BulkReply) {
        byteArrays.add(((BulkReply) reply).asByteArray());
      } else {
        throw new IllegalArgumentException("Could not convert " + reply + " to a byteArray");
        // strings.add(reply.toString());
      }
    }
    return byteArrays;
  }

  public Set<String> asStringSet(Charset charset) {
    if (data() == null)
      return null;
    Set<String> strings = new HashSet<String>(data().length);
    for (Reply reply : data()) {
      if (reply instanceof BulkReply) {
        strings.add(((BulkReply) reply).asString(charset));
      } else {
        // throw new IllegalArgumentException("Could not convert " + reply + " to a string");
        strings.add(reply.toString());
      }
    }
    return strings;
  }

  public Map<String, String> asStringMap(Charset charset) {
    if (data() == null)
      return null;
    int length = data().length;
    Map<String, String> map = new HashMap<String, String>(length);
    if (length % 2 != 0) {
      throw new IllegalArgumentException("Odd number of replies");
    }
    for (int i = 0; i < length; i += 2) {
      Reply key = data()[i];
      Reply value = data()[i + 1];
      if (key instanceof BulkReply) {
        if (value instanceof BulkReply) {
          map.put(((BulkReply) key).asString(charset), ((BulkReply) value).asString(charset));
        } else {
          throw new IllegalArgumentException("Could not convert value: " + value + " to a string");
        }
      } else {
        throw new IllegalArgumentException("Could not convert key: " + key + " to a string");
      }
    }
    return map;
  }

  public String toString() {
    return asStringList(CharsetUtil.UTF_8).toString();
  }

}
