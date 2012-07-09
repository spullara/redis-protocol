package redis.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import redis.Command;
import redis.util.Encoding;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Charsets;

import static redis.util.Encoding.NEG_ONE_WITH_CRLF;
import static redis.util.Encoding.numToBytes;

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
  private final int size;
  private int index = 0;

  public MultiBulkReply(RedisDecoder rd, ChannelBuffer is) throws IOException {
    size = RedisDecoder.readInteger(is);
    if (size == -1) {
      replies = null;
    } else {
      if (size < 0) {
        throw new IllegalArgumentException("Invalid size: " + size);
      }
      replies = new Reply[size];
      read(rd, is);
    }
  }

  public void read(RedisDecoder rd, ChannelBuffer is) throws IOException {
    for (int i = index; i < size; i++) {
      replies[i] = rd.receive(is);
      index = i + 1;
      rd.checkpoint();
    }
  }

  public MultiBulkReply(Reply[] replies) {
    this.replies = replies;
    size = replies.length;
  }

  @Override
  public Reply[] data() {
    return replies;
  }

  @Override
  public void write(ChannelBuffer os) throws IOException {
    os.writeByte(MARKER);
    if (replies == null) {
      os.writeBytes(NEG_ONE_WITH_CRLF);
    } else {
      os.writeBytes(numToBytes(replies.length, true));
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

  public String toString() {
    return asStringList(Charsets.UTF_8).toString();
  }
}
