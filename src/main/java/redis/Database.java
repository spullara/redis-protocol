package redis;

import com.google.common.base.Charsets;
import mojava.F;
import redis.reply.BulkReply;
import redis.reply.ErrorReply;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;
import redis.reply.StatusReply;
import redis.util.BytesKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * TODO: Edit this
 * <p/>
 * User: sam
 * Date: 7/28/11
 * Time: 7:50 PM
 */
public class Database {

  private static final BulkReply EMPTY = new BulkReply(new byte[0]);
  private static final StatusReply OK = new StatusReply("OK");
  private static final MultiBulkReply MEMPTY = mbytes(null);
  private static final IntegerReply ZERO = new IntegerReply(0);

  // Every database has a map
  private volatile Map<BytesKey, Object> map = Collections.synchronizedMap(new HashMap<BytesKey, Object>());
  private volatile Map<BytesKey, Long> expireMap = new HashMap<>();

  public Reply append(byte[][] a) {
    if (a.length != 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object v = map.get(key);
      final byte[] s = a[2];
      if (v instanceof byte[]) {
        byte[] b = (byte[]) v;
        byte[] bytes = new byte[s.length + b.length];
        System.arraycopy(b, 0, bytes, 0, b.length);
        System.arraycopy(s, 0, bytes, b.length, s.length);
        put(key, bytes);
        return OK;
      } else if (v == null) {
        put(key, s);
        return OK;
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply auth(byte[][] a) {
    if (a.length < 2) return argerr();
    RedisServer.auth = new String(a[1], Charsets.UTF_8);
    return OK;
  }

  // BGREWRITEAOF
  // BGSAVE

  public Reply blpop(byte[][] a) {
    return pop(a, new F<List, Object>() {
      public Object apply(List input) {
        return input.remove(0);
      }
    });
  }

  public Reply brpop(byte[][] a) {
    return pop(a, new F<List, Object>() {
      public Object apply(List input) {
        return input.remove(input.size() - 1);
      }
    });
  }

  public Reply brpoppush(byte[][] a) {
    if (a.length != 4) argerr();
    byte[][] popArguments = new byte[3][];
    popArguments[0] = a[0];
    popArguments[1] = a[1];
    popArguments[2] = a[3];
    Reply brpop = brpop(popArguments);
    byte[][] pushArguments = new byte[3][];
    pushArguments[0] = a[0];
    pushArguments[1] = a[2];
    pushArguments[2] = ((MultiBulkReply) brpop).byteArrays[1];
    return lpush(pushArguments);
  }

  // CONFIG GET
  // CONFIG SET
  // CONFIG RESETSTAT

  public Reply dbsize(byte[][] a) {
    if (a.length != 1) return argerr();
    synchronized (this) {
      return num(map.size());
    }
  }

  // DEBUG OBJECT
  // DEBUG SEGFAULT

  public Reply decr(byte[][] a) {
    if (a.length != 2) return argerr();
    return change(a[1], -1);
  }

  public Reply decrby(byte[][] a) {
    if (a.length != 3) return argerr();
    return change(a[1], -tonum(a[2]));
  }

  public Reply del(byte[][] a) {
    if (a.length < 2) return argerr();
    int total = 0;
    for (int i = 1; i < a.length; i++) {
      synchronized (this) {
        if (map.remove(new BytesKey(a[i])) != null) {
          total++;
        }
      }
    }
    return num(total);
  }

  // DISCARD

  public Reply echo(byte[][] a) {
    if (a.length != 2) return argerr();
    return new BulkReply(a[1]);
  }

  // EXEC

  public Reply exists(byte[][] a) {
    if (a.length != 2) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      return num(map.containsKey(key) ? 1 : 0);
    } finally {
      lock.unlock();
    }
  }

  // EXPIRE
  // EXPIREAT

  public Reply flushall(byte[][] a) {
    if (a.length != 1) return argerr();
    map = new HashMap<>();
    return OK;
  }

  public Reply flushdb(byte[][] a) {
    return flushall(a);
  }

  public Reply get(byte[][] a) {
    if (a.length != 2) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object value = get(key);
      if (value instanceof byte[] || value == null) {
        return bytes((byte[]) value);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply getbit(byte[][] a) {
    if (a.length != 3) return argerr();
    int offset = (int) tonum(a[2]);
    int byt = offset / 8;
    int bit = offset % 8;
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object v = get(key);
      if (v instanceof byte[]) {
        byte[] bytes = (byte[]) v;
        if (bytes.length <= byt) {
          return num(0);
        }
        int set = bytes[byt] & (1 << bit);
        return num(set > 0 ? 1 : 0);
      } else if (v == null) {
        return num(0);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply getrange(byte[][] a) {
    if (a.length != 4) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object o = get(key);
      if (o instanceof byte[]) {
        byte[] b = (byte[]) o;
        int l = b.length;
        int start = (int) tonum(a[2]);
        if (start < 0) start = l + start;
        start = Math.min(start, l);
        int end = (int) tonum(a[3]);
        if (end < 0) end = l + end;
        end = Math.min(end, l);
        if (end <= start) {
          return EMPTY;
        }
        byte[] r = new byte[end - start];
        System.arraycopy(b, start, r, 0, end - start);
        return new BulkReply(r);
      } else if (o == null) {
        return EMPTY;
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply getset(byte[][] a) {
    if (a.length != 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = get(key);
      put(key, a[2]);
      if (o instanceof byte[]) {
        return bytes((byte[]) o);
      }
      return new BulkReply(null);
    } finally {
      lock.unlock();
    }
  }

  public Reply hdel(byte[][] a) {
    if (a.length < 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map) {
        Map hash = (Map) o;
        synchronized (hash) {
          int total = 0;
          for (int i = 2; i < a.length; i++) {
            if (hash.remove(new BytesKey(a[i])) != null) {
              total++;
            }
          }
          return num(total);
        }
      } else if (o == null) {
        return num(0);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  private Lock writeLock(BytesKey key) {
    Lock lock = key.getLock().writeLock();
    lock.lock();
    return lock;
  }

  public Reply hexists(byte[][] a) {
    if (a.length != 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map) {
        Map hash = (Map) o;
        synchronized (hash) {
          return num(hash.containsKey(new BytesKey(a[2])) ? 1 : 0);
        }
      } else if (o == null) {
        return num(0);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply hget(byte[][] a) {
    if (a.length != 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map) {
        Map hash = (Map) o;
        return bytes((byte[]) hash.get(key));
      } else if (o == null) {
        return bytes(null);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply hgetall(byte[][] a) {
    if (a.length != 2) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map) {
        Map<BytesKey, byte[]> hash = (Map) o;
        byte[][] r = new byte[hash.size()][];
        int i = 0;
        for (Map.Entry<BytesKey, byte[]> entry : hash.entrySet()) {
          r[i++] = entry.getKey().getBytes();
          r[i++] = entry.getValue();
        }
        return mbytes(r);
      } else if (o == null) {
        return mbytes(new byte[0][]);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  // HINCRBY

  public Reply hkeys(byte[][] a) {
    if (a.length != 2) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map) {
        Map<BytesKey, byte[]> hash = (Map) o;
        byte[][] r = new byte[hash.size() * 2][];
        int i = 0;
        for (BytesKey entry : hash.keySet()) {
          r[i++] = entry.getBytes();
        }
        return mbytes(r);
      } else if (o == null) {
        return mbytes(new byte[0][]);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply hlen(byte[][] a) {
    if (a.length != 2) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map) {
        Map<BytesKey, byte[]> hash = (Map) o;
        return new IntegerReply(hash.size());
      } else if (o == null) {
        return ZERO;
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply hmget(byte[][] a) {
    if (a.length < 3) return argerr();
    int total = a.length - 2;
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map) {
        Map<BytesKey, byte[]> hash = (Map<BytesKey, byte[]>) o;
        byte[][] r = new byte[total][];
        for (int i = 0; i < total; i++) {
          r[i] = hash.get(new BytesKey(a[3 + i]));
        }
        return mbytes(r);
      } else if (o == null) {
        return mbytes(new byte[total][]);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply hmset(byte[][] a) {
    if (a.length < 4 || a.length % 2 == 1) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map || o == null) {
        Map hash = (Map) o;
        if (hash == null) {
          synchronized (this) {
            hash = (Map) get(key);
            if (hash == null) {
              hash = new HashMap();
              put(key, hash);
            }
          }
        }
        for (int i = 2; i < a.length; i += 2) {
          hash.put(new BytesKey(a[i]), a[i + 1]);
        }
        return OK;
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply hset(byte[][] a) {
    if (a.length != 4) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map || o == null) {
        Map hash = (Map) o;
        if (hash == null) {
          synchronized (this) {
            hash = (Map) get(key);
            if (hash == null) {
              hash = new HashMap();
              put(key, hash);
            }
          }
        }
        hash.put(new BytesKey(a[2]), a[3]);
        return OK;
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply hsetnx(byte[][] a) {
    if (a.length != 4) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map || o == null) {
        Map hash = (Map) o;
        if (hash == null) {
          synchronized (this) {
            hash = (Map) get(key);
            if (hash == null) {
              hash = new HashMap();
              put(key, hash);
            }
          }
        }
        if (!hash.containsKey(new BytesKey(a[2]))) {
          hash.put(new BytesKey(a[2]), a[3]);
          return num(1);
        }
        return num(0);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply hvals(byte[][] a) {
    if (a.length != 2) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = readLock(key);
    try {
      Object o = get(key);
      if (o instanceof Map) {
        Map<BytesKey, byte[]> hash = (Map) o;
        byte[][] r = new byte[hash.size() * 2][];
        int i = 0;
        for (byte[] entry : hash.values()) {
          r[i++] = entry;
        }
        return mbytes(r);
      } else if (o == null) {
        return mbytes(new byte[0][]);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply incr(byte[][] a) {
    if (a.length != 2) return argerr();
    return change(a[1], 1);
  }

  public Reply incrby(byte[][] a) {
    if (a.length != 3) return argerr();
    return change(a[1], tonum(a[2]));
  }

  // INFO

  public Reply keys(byte[][] a) {
    if (a.length != 2) return argerr();
    String regex = new String(a[1], Charsets.US_ASCII);
    regex = regex.replaceAll("[*]", ".*");
    regex = regex.replaceAll("[?]", ".");
    List<BytesKey> matches = new ArrayList<>();
    for (BytesKey bytesKey : map.keySet()) {
      if (new String(bytesKey.getBytes(), Charsets.US_ASCII).matches(regex)) {
        matches.add(bytesKey);
      }
    }
    byte[][] r = new byte[matches.size()][];
    int i = 0;
    for (BytesKey match : matches) {
      r[i++] = match.getBytes();
    }
    return mbytes(r);
  }

  // LASTSAVE

  public Reply lindex(byte[][] a) {
    if (a.length != 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = get(key);
      if (o instanceof List) {
        List<byte[]> l = (List<byte[]>) o;
        long index = tonum(a[2]);
        int size = l.size();
        if (index < 0) index = size + index;
        if (index < 0 || index >= size) {
          return bytes(null);
        }
        return bytes(l.get((int) index));
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply linsert(byte[][] a) {
    if (a.length != 4) return argerr();
    String where = new String(a[2], Charsets.US_ASCII).toLowerCase();
    boolean before;
    switch (where) {
      case "before":
        before = true;
        break;
      case "after":
        before = false;
        break;
      default:
        return argerr();
    }
    BytesKey key = $(a[1]);
    BytesKey pivot = $(a[2]);
    Lock lock = writeLock(key);
    try {
      Object o = map.get(key);
      if (o instanceof List) {
        List<byte[]> l = (List<byte[]>) o;
        int i = 0;
        for (byte[] bytes : l) {
          if (pivot.equals(new BytesKey(bytes))) {
            break;
          }
          i++;
        }
        if (i == l.size()) return num(-1);
        if (before) {
          l.add(i, a[3]);
        } else {
          l.add(i + 1, a[3]);
        }
        return num(l.size());
      } else return typeerr();
    } finally {
      lock.unlock();
    }
  }

  public Reply llen(byte[][] a) {
    if (a.length != 2) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = get(key);
      if (o instanceof List) {
        List<byte[]> l = (List<byte[]>) o;
        return num(l.size());
      } else if (o == null) {
        return num(0);
      } else {
        return typeerr();
      }
    } finally {
      lock.unlock();
    }
  }

  public Reply lpop(byte[][] a) {
    if (a.length != 2) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = map.get(key);
      if (o instanceof List) {
        List<byte[]> l = (List<byte[]>) o;
        if (l.size() == 0) return bytes(null);
        return bytes(l.remove(0));
      } else if (o == null) {
        return bytes(null);
      } else return typeerr();
    } finally {
      lock.unlock();
    }
  }

  public Reply lpush(byte[][] a) {
    if (a.length < 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = map.get(key);
      if (o instanceof List || o == null) {
        List<byte[]> l = (List<byte[]>) o;
        if (l == null) {
          l = new ArrayList<>(a.length - 2);
          put(key, l);
        }
        for (int i = 2; i < a.length; i++) {
          l.add(0, a[i]);
          synchronized (this) {
            notifyAll();
          }
        }
        return OK;
      } else return typeerr();
    } finally {
      lock.unlock();
    }
  }

  public Reply lpushx(byte[][] a) {
    if (a.length != 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      Object o = map.get(key);
      if (o instanceof List) {
        List<byte[]> l = (List<byte[]>) o;
        l.add(0, a[2]);
        synchronized (this) {
          notifyAll();
        }
        return num(l.size());
      } else if (o == null) {
        return num(0);
      } else return typeerr();
    } finally {
      lock.unlock();
    }
  }

  // LRANGE
  // LREM
  // LSET
  // LTRIM
  // MGET
  // MONITOR
  // MOVE
  // MSET
  // MSETNX
  // MULTI
  // OBJECT
  // PERSIST
  // PING
  // PSUBSCRIBE
  // PUBLISH
  // PUNSUBSCRIBE
  // QUIT
  // RANDOMKEY
  // RENAME
  // RENAMENX
  // RPOP
  // RPOPLPUSH
  // RPUSH
  // RPUSHX
  // SADD
  // SAVE
  // SCARD
  // SDIFF
  // SDIFFSTORE
  // SELECT

  public Reply set(byte[][] a) {
    if (a.length != 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      put(key, a[2]);
      return OK;
    } finally {
      lock.unlock();
    }
  }

  // SETBIT
  // SETEX
  // SETNX
  // SETRANGE
  // SHUTDOWN
  // SINTER
  // SINTERSTORE
  // SISMEMBER
  // SLAVEOF
  // SLOWLOG
  // SMEMBERS
  // SMOVE
  // SORT
  // SPOP
  // SRANDMEMBER
  // SREM
  // STRLEN
  // SUBSCRIBE
  // SUNION
  // SUNIONSTORE
  // SYNC
  // TTL
  // TYPE
  // UNSUBSCRIBE
  // UNWATCH
  // WATCH
  // ZADD
  // ZCARD
  // ZCOUNT
  // ZINCRBY
  // ZINTERSTORE
  // ZRANGE
  // ZRANGEBYSCORE
  // ZRANK
  // ZREM
  // ZREMRANGEBYSCORE
  // ZREVRANGE
  // ZREVRANGEBYSCORE
  // ZREVRANK
  // ZSCORE
  // ZUNIONSTORE

  private Lock readLock(BytesKey key) {
    Lock lock = key.getLock().readLock();
    lock.lock();
    return lock;
  }

  private Reply change(byte[] k, long num) {
    BytesKey key = $(k);
    Lock lock = writeLock(key);
    try {
      Object o = get(key);
      if (o instanceof byte[]) {
        try {
          long l = tonum((byte[]) o) + num;
          final String value = String.valueOf(l);
          put(key, value.getBytes(Charsets.UTF_8));
          return num(l);
        } catch (NumberFormatException e) {
          return numerr();
        }
      } else {
        return numerr();
      }
    } finally {
      lock.unlock();
    }
  }

  private BytesKey $(byte[] k) {
    return new BytesKey(k);
  }

  private IntegerReply num(long total) {
    return new IntegerReply(total);
  }

  private Reply numerr() {
    return new ErrorReply("value is not an integer");
  }

  private Reply pop(byte[][] a, F<List, Object> f) {
    if (a.length < 3) return argerr();
    BytesKey key = $(a[1]);
    Lock lock = writeLock(key);
    try {
      long timeout = tonum(a[a.length - 1]) * 1000;
      long start = System.currentTimeMillis();
      long since;
      while ((since = System.currentTimeMillis() - start) < timeout) {
        for (int i = 1; i < a.length - 1; i++) {
          Object o = get(key);
          if (o instanceof List) {
            List l = (List) o;
            if (l.size() > 0) {
              Object remove = f.apply(l);
              if (remove instanceof byte[] || remove == null) {
                synchronized (this) {
                  notifyAll();
                }
                return mbytes(new byte[][]{a[i], (byte[]) remove});
              } else {
                return typeerr();
              }
            }
          } else if (o != null) {
            return typeerr();
          }
        }
        try {
          RedisServer.logger.info("Waiting");
          wait(timeout - since);
          RedisServer.logger.info("Done waiting");
        } catch (InterruptedException e) {
          // ignore
        }
      }
      return mbytes(null);
    } finally {
      lock.unlock();
    }
  }

  private Object get(BytesKey key) {
    return map.get(key);
  }

  private void put(BytesKey bytesKey, Object value) {
    map.put(bytesKey, value);
    synchronized (this) {
      notifyAll();
    }
  }

  private ErrorReply argerr() {
    String c = Thread.currentThread().getStackTrace()[2].getMethodName();
    return new ErrorReply("wrong number of arguments for '" + c + "' command");
  }

  private ErrorReply typeerr() {
    return new ErrorReply("Operation against a key holding the wrong kind of value");
  }

  private long tonum(byte[] k) {
    return Long.parseLong(new String(k, Charsets.UTF_8));
  }

  private Reply bytes(byte[] o) {
    return new BulkReply(o);
  }

  private static MultiBulkReply mbytes(byte[][] r) {
    return new MultiBulkReply(r);
  }

}
