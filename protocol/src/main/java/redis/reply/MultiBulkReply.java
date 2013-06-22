package redis.reply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.RedisProtocol;

import static redis.util.Encoding.NEG_ONE_WITH_CRLF;

/**
 * Nested replies.
 * <p/>
 * User: sam
 * Date: 7/29/11
 * Time: 10:23 AM
 */
public class MultiBulkReply implements Reply<Reply[]> {
  public static final char MARKER = '*';
  private final Reply[] replies;

  public MultiBulkReply(InputStream is) throws IOException {
    long size = RedisProtocol.readLong(is);
    if (size == -1) {
      replies = null;
    } else {
      if (size > Integer.MAX_VALUE || size < 0) {
        throw new IllegalArgumentException("Invalid size: " + size);
      }
      replies = new Reply[(int) size];
      for (int i = 0; i < size; i++) {
        replies[i] = RedisProtocol.receive(is);
      }
    }
  }

  public MultiBulkReply(Reply[] replies) {
    this.replies = replies;
  }

  @Override
  public Reply[] data() {
    return replies;
  }

  @Override
  public void write(OutputStream os) throws IOException {
    os.write(MARKER);
    if (replies == null) {
      os.write(NEG_ONE_WITH_CRLF);
    } else {
      os.write(RedisProtocol.toBytes(replies.length));
      os.write(CRLF);
      for (Reply reply : replies) {
        reply.write(os);
      }
    }
  }

  public List<String> asStringList(Charset charset) {
    if (replies == null) return null;
    List<String> strings = new ArrayList<String>(replies.length);
    for (Reply reply : replies) {
      if (reply instanceof BulkReply) {
        strings.add(((BulkReply) reply).asString(charset));
      } else if (reply instanceof IntegerReply) {
        strings.add(reply.data().toString());
      } else {
        throw new IllegalArgumentException("Could not convert " + reply + " to a string");
      }
    }
    return strings;
  }

  public Set<String> asStringSet(Charset charset) {
    if (replies == null) return null;
    Set<String> strings = new HashSet<String>(replies.length);
    for (Reply reply : replies) {
      if (reply instanceof BulkReply) {
        strings.add(((BulkReply) reply).asString(charset));
      } else {
        throw new IllegalArgumentException("Could not convert " + reply + " to a string");
      }
    }
    return strings;
  }

  public Map<String, String> asStringMap(Charset charset) {
    if (replies == null) return null;
    int length = replies.length;
    Map<String, String> map = new HashMap<String, String>(length);
    if (length % 2 != 0) {
      throw new IllegalArgumentException("Odd number of replies");
    }
    for (int i = 0; i < length; i += 2) {
      Reply key = replies[i];
      Reply value = replies[i + 1];
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
}
