package redis.server.netty;

import redis.netty4.*;
import redis.util.BytesKey;
import redis.util.BytesKeyObjectMap;
import redis.util.Encoding;

import java.util.HashMap;
import java.util.Map;

import static redis.netty4.BulkReply.NIL_REPLY;
import static redis.netty4.IntegerReply.ONE_REPLY;
import static redis.netty4.IntegerReply.ZERO_REPLY;
import static redis.netty4.StatusReply.OK;
import static redis.util.Encoding.bytesToNum;
import static redis.util.Encoding.numToBytes;

/**
 * Uses java.util.*
 */
public class SimpleRedisServer implements RedisServer {

  private long started = System.currentTimeMillis();
  private BytesKeyObjectMap<Object> data = new BytesKeyObjectMap<Object>();

  @Override
  public IntegerReply append(byte[] key0, byte[] value1) throws RedisException {
    return null;
  }

  @Override
  public StatusReply auth(byte[] password0) throws RedisException {
    return null;
  }

  @Override
  public StatusReply bgrewriteaof() throws RedisException {
    return null;
  }

  @Override
  public StatusReply bgsave() throws RedisException {
    return null;
  }

  @Override
  public IntegerReply bitcount(byte[] key0, byte[] start1, byte[] end2) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply bitop(byte[] operation0, byte[] destkey1, byte[][] key2) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply blpop(byte[][] key0) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply brpop(byte[][] key0) throws RedisException {
    return null;
  }

  @Override
  public BulkReply brpoplpush(byte[] source0, byte[] destination1, byte[] timeout2) throws RedisException {
    return null;
  }

  @Override
  public Reply config_get(byte[] parameter0) throws RedisException {
    return null;
  }

  @Override
  public Reply config_set(byte[] parameter0, byte[] value1) throws RedisException {
    return null;
  }

  @Override
  public Reply config_resetstat() throws RedisException {
    return null;
  }

  @Override
  public IntegerReply dbsize() throws RedisException {
    return null;
  }

  @Override
  public Reply debug_object(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public Reply debug_segfault() throws RedisException {
    return null;
  }

  @Override
  public IntegerReply decr(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply decrby(byte[] key0, byte[] decrement1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply del(byte[][] key0) throws RedisException {
    return null;
  }

  @Override
  public BulkReply dump(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public BulkReply echo(byte[] message0) throws RedisException {
    return null;
  }

  @Override
  public Reply eval(byte[] script0, byte[] numkeys1, byte[][] key2) throws RedisException {
    return null;
  }

  @Override
  public Reply evalsha(byte[] sha10, byte[] numkeys1, byte[][] key2) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply exists(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply expire(byte[] key0, byte[] seconds1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply expireat(byte[] key0, byte[] timestamp1) throws RedisException {
    return null;
  }

  @Override
  public StatusReply flushall() throws RedisException {
    return null;
  }

  @Override
  public StatusReply flushdb() throws RedisException {
    return null;
  }

  @Override
  public BulkReply get(byte[] key0) throws RedisException {
    Object o = data.get(key0);
    if (o instanceof byte[]) {
      return new BulkReply((byte[]) o);
    }
    if (o == null) {
      return NIL_REPLY;
    } else {
      throw invalidValue();
    }
  }

  @Override
  public IntegerReply getbit(byte[] key0, byte[] offset1) throws RedisException {
    return null;
  }

  @Override
  public BulkReply getrange(byte[] key0, byte[] start1, byte[] end2) throws RedisException {
    return null;
  }

  @Override
  public BulkReply getset(byte[] key0, byte[] value1) throws RedisException {
    Object put = data.put(key0, value1);
    if (put == null || put instanceof byte[]) {
      return put == null ? NIL_REPLY : new BulkReply((byte[]) put);
    } else {
      // Put it back
      data.put(key0, put);
      throw invalidValue();
    }
  }

  @Override
  public IntegerReply hdel(byte[] key0, byte[][] field1) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, false);
    int total = 0;
    for (byte[] hkey : field1) {
      total += hash.remove(new BytesKey(hkey)) == null ? 0 : 1;
    }
    return new IntegerReply(total);
  }

  @Override
  public IntegerReply hexists(byte[] key0, byte[] field1) throws RedisException {
    return getHash(key0, false).get(new BytesKey(field1)) == null ? ZERO_REPLY : ONE_REPLY;
  }

  @Override
  public BulkReply hget(byte[] key0, byte[] field1) throws RedisException {
    byte[] bytes = getHash(key0, false).get(new BytesKey(field1));
    if (bytes == null) {
      return NIL_REPLY;
    } else {
      return new BulkReply(bytes);
    }
  }

  @Override
  public MultiBulkReply hgetall(byte[] key0) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, false);
    int size = hash.size();
    Reply[] replies = new Reply[size * 2];
    int i = 0;
    for (Map.Entry<Object, byte[]> entry : hash.entrySet()) {
      replies[i++] = new BulkReply(((BytesKey)entry.getKey()).getBytes());
      replies[i++] = new BulkReply(entry.getValue());
    }
    return new MultiBulkReply(replies);
  }

  @Override
  public IntegerReply hincrby(byte[] key0, byte[] field1, byte[] increment2) throws RedisException {
    return null;
  }

  @Override
  public BulkReply hincrbyfloat(byte[] key0, byte[] field1, byte[] increment2) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply hkeys(byte[] key0) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, false);
    int size = hash.size();
    Reply[] replies = new Reply[size];
    int i = 0;
    for (Object hkey : hash.keySet()) {
      replies[i++] = new BulkReply(((BytesKey)hkey).getBytes());
    }
    return new MultiBulkReply(replies);
  }

  @Override
  public IntegerReply hlen(byte[] key0) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, false);
    return new IntegerReply(hash.size());
  }

  @Override
  public MultiBulkReply hmget(byte[] key0, byte[][] field1) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, false);
    int length = field1.length;
    Reply[] replies = new Reply[length];
    for (int i = 0; i < length; i++) {
      byte[] bytes = hash.get(new BytesKey(field1[i]));
      if (bytes == null) {
        replies[i] = NIL_REPLY;
      } else {
        replies[i] = new BulkReply(bytes);
      }
    }
    return new MultiBulkReply(replies);
  }

  @Override
  public StatusReply hmset(byte[] key0, byte[][] field_or_value1) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, true);
    if (field_or_value1.length % 2 != 0) {
      throw new RedisException("wrong number of arguments for HMSET");
    }
    for (int i = 0; i < field_or_value1.length; i+=2) {
      hash.put(new BytesKey(field_or_value1[i]), field_or_value1[i + 1]);
    }
    return OK;
  }

  @Override
  public IntegerReply hset(byte[] key0, byte[] field1, byte[] value2) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, true);
    Object put = hash.put(new BytesKey(field1), value2);
    return put == null ? ONE_REPLY : ZERO_REPLY;
  }

  @SuppressWarnings("unchecked")
  private BytesKeyObjectMap<byte[]> getHash(byte[] key0, boolean create) throws RedisException {
    Object o = data.get(key0);
    if (o == null) {
      o = new BytesKeyObjectMap();
      if (create) {
        data.put(key0, o);
      }
    }
    if (!(o instanceof HashMap)) {
      throw invalidValue();
    }
    return (BytesKeyObjectMap<byte[]>) o;
  }

  @Override
  public IntegerReply hsetnx(byte[] key0, byte[] field1, byte[] value2) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply hvals(byte[] key0) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, false);
    int size = hash.size();
    Reply[] replies = new Reply[size];
    int i = 0;
    for (byte[] hvalue : hash.values()) {
      replies[i++] = new BulkReply(hvalue);
    }
    return new MultiBulkReply(replies);
  }

  @Override
  public IntegerReply incr(byte[] key0) throws RedisException {
    Object o = data.get(key0);
    if (o == null) {
      data.put(key0, new byte[] { '1' });
      return ONE_REPLY;
    } else if (o instanceof byte[]) {
      try {
        long integer = bytesToNum((byte[]) o) + 1;
        data.put(key0, numToBytes(integer, false));
        return new IntegerReply(integer);
      } catch (IllegalArgumentException e) {
        throw new RedisException(e.getMessage());
      }
    } else {
      throw invalidValue();
    }
  }

  @Override
  public IntegerReply incrby(byte[] key0, byte[] increment1) throws RedisException {
    return null;
  }

  @Override
  public BulkReply incrbyfloat(byte[] key0, byte[] increment1) throws RedisException {
    return null;
  }

  @Override
  public BulkReply info() throws RedisException {
    StringBuilder sb = new StringBuilder();
    sb.append("redis_version:2.4.0\n");
    sb.append("keys:").append(data.size()).append("\n");
    sb.append("uptime:").append(System.currentTimeMillis() - started).append("\n");
    return new BulkReply(sb.toString().getBytes());
  }

  @Override
  public MultiBulkReply keys(byte[] pattern0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply lastsave() throws RedisException {
    return null;
  }

  @Override
  public BulkReply lindex(byte[] key0, byte[] index1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply linsert(byte[] key0, byte[] where1, byte[] pivot2, byte[] value3) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply llen(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public BulkReply lpop(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply lpush(byte[] key0, byte[][] value1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply lpushx(byte[] key0, byte[] value1) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply lrange(byte[] key0, byte[] start1, byte[] stop2) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply lrem(byte[] key0, byte[] count1, byte[] value2) throws RedisException {
    return null;
  }

  @Override
  public StatusReply lset(byte[] key0, byte[] index1, byte[] value2) throws RedisException {
    return null;
  }

  @Override
  public StatusReply ltrim(byte[] key0, byte[] start1, byte[] stop2) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply mget(byte[][] key0) throws RedisException {
    int length = key0.length;
    Reply[] replies = new Reply[length];
    for (int i = 0; i < length; i++) {
      Object o = data.get(key0[i]);
      if (o instanceof byte[]) {
        replies[i] = new BulkReply((byte[]) o);
      } else {
        replies[i] = NIL_REPLY;
      }
    }
    return new MultiBulkReply(replies);
  }

  @Override
  public StatusReply migrate(byte[] host0, byte[] port1, byte[] key2, byte[] destination_db3, byte[] timeout4) throws RedisException {
    return null;
  }

  @Override
  public Reply monitor() throws RedisException {
    return null;
  }

  @Override
  public IntegerReply move(byte[] key0, byte[] db1) throws RedisException {
    return null;
  }

  @Override
  public StatusReply mset(byte[][] key_or_value0) throws RedisException {
    int length = key_or_value0.length;
    if (length % 2 != 0) {
      throw new RedisException("wrong number of arguments for MSET");
    }
    for (int i = 0; i < length; i += 2) {
      data.put(key_or_value0[i], key_or_value0[i + 1]);
    }
    return OK;
  }

  @Override
  public IntegerReply msetnx(byte[][] key_or_value0) throws RedisException {
    return null;
  }

  @Override
  public Reply object(byte[] subcommand0, byte[][] arguments1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply persist(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply pexpire(byte[] key0, byte[] milliseconds1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply pexpireat(byte[] key0, byte[] milliseconds_timestamp1) throws RedisException {
    return null;
  }

  @Override
  public StatusReply ping() throws RedisException {
    return new StatusReply("PONG");
  }

  @Override
  public Reply psetex(byte[] key0, byte[] milliseconds1, byte[] value2) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply pttl(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply publish(byte[] channel0, byte[] message1) throws RedisException {
    return null;
  }

  @Override
  public StatusReply quit() throws RedisException {
    return null;
  }

  @Override
  public BulkReply randomkey() throws RedisException {
    return null;
  }

  @Override
  public StatusReply rename(byte[] key0, byte[] newkey1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply renamenx(byte[] key0, byte[] newkey1) throws RedisException {
    return null;
  }

  @Override
  public StatusReply restore(byte[] key0, byte[] ttl1, byte[] serialized_value2) throws RedisException {
    return null;
  }

  @Override
  public BulkReply rpop(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public BulkReply rpoplpush(byte[] source0, byte[] destination1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply rpush(byte[] key0, byte[][] value1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply rpushx(byte[] key0, byte[] value1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply sadd(byte[] key0, byte[][] member1) throws RedisException {
    return null;
  }

  @Override
  public Reply save() throws RedisException {
    return null;
  }

  @Override
  public IntegerReply scard(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public Reply script_exists(byte[][] script0) throws RedisException {
    return null;
  }

  @Override
  public Reply script_flush() throws RedisException {
    return null;
  }

  @Override
  public Reply script_kill() throws RedisException {
    return null;
  }

  @Override
  public Reply script_load(byte[] script0) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply sdiff(byte[][] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply sdiffstore(byte[] destination0, byte[][] key1) throws RedisException {
    return null;
  }

  @Override
  public StatusReply select(byte[] index0) throws RedisException {
    return null;
  }

  @Override
  public StatusReply set(byte[] key0, byte[] value1) throws RedisException {
    data.put(key0, value1);
    return OK;
  }

  private RedisException invalidValue() {
    return new RedisException("Operation against a key holding the wrong kind of value");
  }

  @Override
  public IntegerReply setbit(byte[] key0, byte[] offset1, byte[] value2) throws RedisException {
    return null;
  }

  @Override
  public StatusReply setex(byte[] key0, byte[] seconds1, byte[] value2) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply setnx(byte[] key0, byte[] value1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply setrange(byte[] key0, byte[] offset1, byte[] value2) throws RedisException {
    return null;
  }

  @Override
  public StatusReply shutdown(byte[] NOSAVE0, byte[] SAVE1) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply sinter(byte[][] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply sinterstore(byte[] destination0, byte[][] key1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply sismember(byte[] key0, byte[] member1) throws RedisException {
    return null;
  }

  @Override
  public StatusReply slaveof(byte[] host0, byte[] port1) throws RedisException {
    return null;
  }

  @Override
  public Reply slowlog(byte[] subcommand0, byte[] argument1) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply smembers(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply smove(byte[] source0, byte[] destination1, byte[] member2) throws RedisException {
    return null;
  }

  @Override
  public Reply sort(byte[] key0, byte[] pattern1, byte[] offset_or_count2, byte[][] pattern3) throws RedisException {
    return null;
  }

  @Override
  public BulkReply spop(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public BulkReply srandmember(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply srem(byte[] key0, byte[][] member1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply strlen(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply sunion(byte[][] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply sunionstore(byte[] destination0, byte[][] key1) throws RedisException {
    return null;
  }

  @Override
  public Reply sync() throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply time() throws RedisException {
    return null;
  }

  @Override
  public IntegerReply ttl(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public StatusReply type(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public StatusReply unwatch() throws RedisException {
    return null;
  }

  @Override
  public StatusReply watch(byte[][] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply zadd(byte[][] args) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply zcard(byte[] key0) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply zcount(byte[] key0, byte[] min1, byte[] max2) throws RedisException {
    return null;
  }

  @Override
  public BulkReply zincrby(byte[] key0, byte[] increment1, byte[] member2) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply zinterstore(byte[][] args) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply zrange(byte[] key0, byte[] start1, byte[] stop2, byte[] withscores3) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply zrangebyscore(byte[] key0, byte[] min1, byte[] max2, byte[] withscores3, byte[] offset_or_count4) throws RedisException {
    return null;
  }

  @Override
  public Reply zrank(byte[] key0, byte[] member1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply zrem(byte[] key0, byte[][] member1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply zremrangebyrank(byte[] key0, byte[] start1, byte[] stop2) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply zremrangebyscore(byte[] key0, byte[] min1, byte[] max2) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply zrevrange(byte[] key0, byte[] start1, byte[] stop2, byte[] withscores3) throws RedisException {
    return null;
  }

  @Override
  public MultiBulkReply zrevrangebyscore(byte[] key0, byte[] max1, byte[] min2, byte[] withscores3, byte[] offset_or_count4) throws RedisException {
    return null;
  }

  @Override
  public Reply zrevrank(byte[] key0, byte[] member1) throws RedisException {
    return null;
  }

  @Override
  public BulkReply zscore(byte[] key0, byte[] member1) throws RedisException {
    return null;
  }

  @Override
  public IntegerReply zunionstore(byte[] destination0, byte[] numkeys1, byte[][] key2) throws RedisException {
    return null;
  }

}
