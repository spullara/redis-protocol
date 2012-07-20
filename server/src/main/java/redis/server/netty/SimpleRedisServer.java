package redis.server.netty;

import java.util.HashMap;
import java.util.Map;

import redis.netty4.BulkReply;
import redis.netty4.IntegerReply;
import redis.netty4.MultiBulkReply;
import redis.netty4.Reply;
import redis.netty4.StatusReply;
import redis.util.BytesKey;
import redis.util.BytesKeyObjectMap;

import static redis.netty4.BulkReply.NIL_REPLY;
import static redis.netty4.IntegerReply.integer;
import static redis.netty4.StatusReply.OK;
import static redis.util.Encoding.bytesToNum;
import static redis.util.Encoding.numToBytes;

public class SimpleRedisServer implements RedisServer {

  private long started = System.currentTimeMillis();
  private BytesKeyObjectMap<Object> data = new BytesKeyObjectMap<Object>();
  private BytesKeyObjectMap<Long> expires = new BytesKeyObjectMap<Long>();
  private static int[] mask = {128, 64, 32, 16, 8, 4, 2, 1};

  private RedisException invalidValue() {
    return new RedisException("Operation against a key holding the wrong kind of value");
  }

  private RedisException notInteger() {
    return new RedisException("value is not an integer or out of range");
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

  private IntegerReply change(byte[] key0, long delta) throws RedisException {
    Object o = data.get(key0);
    if (o == null) {
      data.put(key0, numToBytes(delta, false));
      return new IntegerReply(delta);
    } else if (o instanceof byte[]) {
      try {
        long integer = bytesToNum((byte[]) o) + delta;
        data.put(key0, numToBytes(integer, false));
        return new IntegerReply(integer);
      } catch (IllegalArgumentException e) {
        throw new RedisException(e.getMessage());
      }
    } else {
      throw notInteger();
    }
  }

  /**
   * Append a value to a key
   * String
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  @Override
  public IntegerReply append(byte[] key0, byte[] value1) throws RedisException {
    Object o = data.get(key0);
    int length1 = value1.length;
    if (o instanceof byte[]) {
      int length0 = ((byte[]) o).length;
      byte[] bytes = new byte[length0 + length1];
      System.arraycopy(o, 0, bytes, 0, length0);
      System.arraycopy(value1, 0, bytes, length0, length1);
      data.put(key0, bytes);
      return new IntegerReply(bytes.length);
    } else if (o == null) {
      data.put(key0, value1);
      return new IntegerReply(length1);
    } else {
      throw invalidValue();
    }
  }

  /**
   * Count set bits in a string
   * String
   *
   * @param key0
   * @param start1
   * @param end2
   * @return IntegerReply
   */
  @Override
  public IntegerReply bitcount(byte[] key0, byte[] start1, byte[] end2) throws RedisException {
    return null;
  }

  /**
   * Perform bitwise operations between strings
   * String
   *
   * @param operation0
   * @param destkey1
   * @param key2
   * @return IntegerReply
   */
  @Override
  public IntegerReply bitop(byte[] operation0, byte[] destkey1, byte[][] key2) throws RedisException {
    return null;
  }

  /**
   * Decrement the integer value of a key by one
   * String
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply decr(byte[] key0) throws RedisException {
    return change(key0, -1);
  }

  /**
   * Decrement the integer value of a key by the given number
   * String
   *
   * @param key0
   * @param decrement1
   * @return IntegerReply
   */
  @Override
  public IntegerReply decrby(byte[] key0, byte[] decrement1) throws RedisException {
    return change(key0, -bytesToNum(decrement1));
  }

  /**
   * Get the value of a key
   * String
   *
   * @param key0
   * @return BulkReply
   */
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

  /**
   * Returns the bit value at offset in the string value stored at key
   * String
   *
   * @param key0
   * @param offset1
   * @return IntegerReply
   */
  @Override
  public IntegerReply getbit(byte[] key0, byte[] offset1) throws RedisException {
    Object o = data.get(key0);
    if (o instanceof byte[]) {
      byte[] bytes = (byte[]) o;
      long offset = bytesToNum(offset1);
      long div = offset / 8;
      if (div > Integer.MAX_VALUE) throw notInteger();
      if (bytes.length < div + 1) return integer(0);
      int mod = (int) (offset % 8);
      int value = bytes[((int) div)] & 0xFF;
      int i = value & mask[mod];
      return i != 0 ? integer(1) : integer(0);
    } else if (o == null) {
      return new IntegerReply(0);
    } else {
      throw invalidValue();
    }
  }

  /**
   * Get a substring of the string stored at a key
   * String
   *
   * @param key0
   * @param start1
   * @param end2
   * @return BulkReply
   */
  @Override
  public BulkReply getrange(byte[] key0, byte[] start1, byte[] end2) throws RedisException {
    return null;
  }

  /**
   * Set the string value of a key and return its old value
   * String
   *
   * @param key0
   * @param value1
   * @return BulkReply
   */
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

  /**
   * Increment the integer value of a key by one
   * String
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply incr(byte[] key0) throws RedisException {
    return change(key0, 1);
  }

  /**
   * Increment the integer value of a key by the given amount
   * String
   *
   * @param key0
   * @param increment1
   * @return IntegerReply
   */
  @Override
  public IntegerReply incrby(byte[] key0, byte[] increment1) throws RedisException {
    return change(key0, bytesToNum(increment1));
  }

  /**
   * Increment the float value of a key by the given amount
   * String
   *
   * @param key0
   * @param increment1
   * @return BulkReply
   */
  @Override
  public BulkReply incrbyfloat(byte[] key0, byte[] increment1) throws RedisException {
    return null;
  }

  /**
   * Get the values of all the given keys
   * String
   *
   * @param key0
   * @return MultiBulkReply
   */
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

  /**
   * Set multiple keys to multiple values
   * String
   *
   * @param key_or_value0
   * @return StatusReply
   */
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

  /**
   * Set multiple keys to multiple values, only if none of the keys exist
   * String
   *
   * @param key_or_value0
   * @return IntegerReply
   */
  @Override
  public IntegerReply msetnx(byte[][] key_or_value0) throws RedisException {
    return null;
  }

  /**
   * Set the value and expiration in milliseconds of a key
   * String
   *
   * @param key0
   * @param milliseconds1
   * @param value2
   * @return Reply
   */
  @Override
  public Reply psetex(byte[] key0, byte[] milliseconds1, byte[] value2) throws RedisException {
    return null;
  }

  /**
   * Set the string value of a key
   * String
   *
   * @param key0
   * @param value1
   * @return StatusReply
   */
  @Override
  public StatusReply set(byte[] key0, byte[] value1) throws RedisException {
    data.put(key0, value1);
    return OK;
  }

  /**
   * Sets or clears the bit at offset in the string value stored at key
   * String
   *
   * @param key0
   * @param offset1
   * @param value2
   * @return IntegerReply
   */
  @Override
  public IntegerReply setbit(byte[] key0, byte[] offset1, byte[] value2) throws RedisException {
    int bit = (int) bytesToNum(value2);
    if (bit != 0 && bit != 1) throw notInteger();
    Object o = data.get(key0);
    if (o instanceof byte[] || o == null) {
      long offset = bytesToNum(offset1);
      long div = offset / 8;
      if (div + 1 > Integer.MAX_VALUE) throw notInteger();

      byte[] bytes = (byte[]) o;
      if (bytes == null || bytes.length < div + 1) {
        byte[] tmp = bytes;
        bytes = new byte[(int) div + 1];
        if (tmp != null) System.arraycopy(tmp, 0, bytes, 0, tmp.length);
        data.put(key0, bytes);
      }
      int mod = (int) (offset % 8);
      int value = bytes[((int) div)] & 0xFF;
      int i = value & mask[mod];
      if (i == 0) {
        if (bit != 0) {
          bytes[((int) div)] += mask[mod];
        }
        return integer(0);
      } else {
        if (bit == 0) {
          bytes[((int) div)] -= mask[mod];
        }
        return integer(1);
      }
    } else {
      throw invalidValue();
    }
  }

  /**
   * Set the value and expiration of a key
   * String
   *
   * @param key0
   * @param seconds1
   * @param value2
   * @return StatusReply
   */
  @Override
  public StatusReply setex(byte[] key0, byte[] seconds1, byte[] value2) throws RedisException {
    return null;
  }

  /**
   * Set the value of a key, only if the key does not exist
   * String
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  @Override
  public IntegerReply setnx(byte[] key0, byte[] value1) throws RedisException {
    return null;
  }

  /**
   * Overwrite part of a string at key starting at the specified offset
   * String
   *
   * @param key0
   * @param offset1
   * @param value2
   * @return IntegerReply
   */
  @Override
  public IntegerReply setrange(byte[] key0, byte[] offset1, byte[] value2) throws RedisException {
    return null;
  }

  /**
   * Get the length of the value stored in a key
   * String
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply strlen(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Authenticate to the server
   * Connection
   *
   * @param password0
   * @return StatusReply
   */
  @Override
  public StatusReply auth(byte[] password0) throws RedisException {
    return null;
  }

  /**
   * Echo the given string
   * Connection
   *
   * @param message0
   * @return BulkReply
   */
  @Override
  public BulkReply echo(byte[] message0) throws RedisException {
    return null;
  }

  /**
   * Ping the server
   * Connection
   *
   * @return StatusReply
   */
  @Override
  public StatusReply ping() throws RedisException {
    return new StatusReply("PONG");
  }

  /**
   * Close the connection
   * Connection
   *
   * @return StatusReply
   */
  @Override
  public StatusReply quit() throws RedisException {
    return null;
  }

  /**
   * Change the selected database for the current connection
   * Connection
   *
   * @param index0
   * @return StatusReply
   */
  @Override
  public StatusReply select(byte[] index0) throws RedisException {
    return null;
  }

  /**
   * Asynchronously rewrite the append-only file
   * Server
   *
   * @return StatusReply
   */
  @Override
  public StatusReply bgrewriteaof() throws RedisException {
    return null;
  }

  /**
   * Asynchronously save the dataset to disk
   * Server
   *
   * @return StatusReply
   */
  @Override
  public StatusReply bgsave() throws RedisException {
    return null;
  }

  /**
   * Get the value of a configuration parameter
   * Server
   *
   * @param parameter0
   * @return Reply
   */
  @Override
  public Reply config_get(byte[] parameter0) throws RedisException {
    return null;
  }

  /**
   * Set a configuration parameter to the given value
   * Server
   *
   * @param parameter0
   * @param value1
   * @return Reply
   */
  @Override
  public Reply config_set(byte[] parameter0, byte[] value1) throws RedisException {
    return null;
  }

  /**
   * Reset the stats returned by INFO
   * Server
   *
   * @return Reply
   */
  @Override
  public Reply config_resetstat() throws RedisException {
    return null;
  }

  /**
   * Return the number of keys in the selected database
   * Server
   *
   * @return IntegerReply
   */
  @Override
  public IntegerReply dbsize() throws RedisException {
    return null;
  }

  /**
   * Get debugging information about a key
   * Server
   *
   * @param key0
   * @return Reply
   */
  @Override
  public Reply debug_object(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Make the server crash
   * Server
   *
   * @return Reply
   */
  @Override
  public Reply debug_segfault() throws RedisException {
    return null;
  }

  /**
   * Remove all keys from all databases
   * Server
   *
   * @return StatusReply
   */
  @Override
  public StatusReply flushall() throws RedisException {
    return null;
  }

  /**
   * Remove all keys from the current database
   * Server
   *
   * @return StatusReply
   */
  @Override
  public StatusReply flushdb() throws RedisException {
    return null;
  }

  /**
   * Get information and statistics about the server
   * Server
   *
   * @return BulkReply
   */
  @Override
  public BulkReply info() throws RedisException {
    StringBuilder sb = new StringBuilder();
    sb.append("redis_version:2.4.0\n");
    sb.append("keys:").append(data.size()).append("\n");
    sb.append("uptime:").append(System.currentTimeMillis() - started).append("\n");
    return new BulkReply(sb.toString().getBytes());
  }

  /**
   * Get the UNIX time stamp of the last successful save to disk
   * Server
   *
   * @return IntegerReply
   */
  @Override
  public IntegerReply lastsave() throws RedisException {
    return null;
  }

  /**
   * Listen for all requests received by the server in real time
   * Server
   *
   * @return Reply
   */
  @Override
  public Reply monitor() throws RedisException {
    return null;
  }

  /**
   * Synchronously save the dataset to disk
   * Server
   *
   * @return Reply
   */
  @Override
  public Reply save() throws RedisException {
    return null;
  }

  /**
   * Synchronously save the dataset to disk and then shut down the server
   * Server
   *
   * @param NOSAVE0
   * @param SAVE1
   * @return StatusReply
   */
  @Override
  public StatusReply shutdown(byte[] NOSAVE0, byte[] SAVE1) throws RedisException {
    return null;
  }

  /**
   * Make the server a slave of another instance, or promote it as master
   * Server
   *
   * @param host0
   * @param port1
   * @return StatusReply
   */
  @Override
  public StatusReply slaveof(byte[] host0, byte[] port1) throws RedisException {
    return null;
  }

  /**
   * Manages the Redis slow queries log
   * Server
   *
   * @param subcommand0
   * @param argument1
   * @return Reply
   */
  @Override
  public Reply slowlog(byte[] subcommand0, byte[] argument1) throws RedisException {
    return null;
  }

  /**
   * Internal command used for replication
   * Server
   *
   * @return Reply
   */
  @Override
  public Reply sync() throws RedisException {
    return null;
  }

  /**
   * Return the current server time
   * Server
   *
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply time() throws RedisException {
    return null;
  }

  /**
   * Remove and integer the first element in a list, or block until one is available
   * List
   *
   * @param key0
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply blpop(byte[][] key0) throws RedisException {
    return null;
  }

  /**
   * Remove and integer the last element in a list, or block until one is available
   * List
   *
   * @param key0
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply brpop(byte[][] key0) throws RedisException {
    return null;
  }

  /**
   * Pop a value from a list, push it to another list and return it; or block until one is available
   * List
   *
   * @param source0
   * @param destination1
   * @param timeout2
   * @return BulkReply
   */
  @Override
  public BulkReply brpoplpush(byte[] source0, byte[] destination1, byte[] timeout2) throws RedisException {
    return null;
  }

  /**
   * Get an element from a list by its index
   * List
   *
   * @param key0
   * @param index1
   * @return BulkReply
   */
  @Override
  public BulkReply lindex(byte[] key0, byte[] index1) throws RedisException {
    return null;
  }

  /**
   * Insert an element before or after another element in a list
   * List
   *
   * @param key0
   * @param where1
   * @param pivot2
   * @param value3
   * @return IntegerReply
   */
  @Override
  public IntegerReply linsert(byte[] key0, byte[] where1, byte[] pivot2, byte[] value3) throws RedisException {
    return null;
  }

  /**
   * Get the length of a list
   * List
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply llen(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Remove and integer the first element in a list
   * List
   *
   * @param key0
   * @return BulkReply
   */
  @Override
  public BulkReply lpop(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Prepend one or multiple values to a list
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  @Override
  public IntegerReply lpush(byte[] key0, byte[][] value1) throws RedisException {
    return null;
  }

  /**
   * Prepend a value to a list, only if the list exists
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  @Override
  public IntegerReply lpushx(byte[] key0, byte[] value1) throws RedisException {
    return null;
  }

  /**
   * Get a range of elements from a list
   * List
   *
   * @param key0
   * @param start1
   * @param stop2
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply lrange(byte[] key0, byte[] start1, byte[] stop2) throws RedisException {
    return null;
  }

  /**
   * Remove elements from a list
   * List
   *
   * @param key0
   * @param count1
   * @param value2
   * @return IntegerReply
   */
  @Override
  public IntegerReply lrem(byte[] key0, byte[] count1, byte[] value2) throws RedisException {
    return null;
  }

  /**
   * Set the value of an element in a list by its index
   * List
   *
   * @param key0
   * @param index1
   * @param value2
   * @return StatusReply
   */
  @Override
  public StatusReply lset(byte[] key0, byte[] index1, byte[] value2) throws RedisException {
    return null;
  }

  /**
   * Trim a list to the specified range
   * List
   *
   * @param key0
   * @param start1
   * @param stop2
   * @return StatusReply
   */
  @Override
  public StatusReply ltrim(byte[] key0, byte[] start1, byte[] stop2) throws RedisException {
    return null;
  }

  /**
   * Remove and integer the last element in a list
   * List
   *
   * @param key0
   * @return BulkReply
   */
  @Override
  public BulkReply rpop(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Remove the last element in a list, append it to another list and return it
   * List
   *
   * @param source0
   * @param destination1
   * @return BulkReply
   */
  @Override
  public BulkReply rpoplpush(byte[] source0, byte[] destination1) throws RedisException {
    return null;
  }

  /**
   * Append one or multiple values to a list
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  @Override
  public IntegerReply rpush(byte[] key0, byte[][] value1) throws RedisException {
    return null;
  }

  /**
   * Append a value to a list, only if the list exists
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  @Override
  public IntegerReply rpushx(byte[] key0, byte[] value1) throws RedisException {
    return null;
  }

  /**
   * Delete a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply del(byte[][] key0) throws RedisException {
    return null;
  }

  /**
   * Return a serialized version of the value stored at the specified key.
   * Generic
   *
   * @param key0
   * @return BulkReply
   */
  @Override
  public BulkReply dump(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Determine if a key exists
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply exists(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Set a key's time to live in seconds
   * Generic
   *
   * @param key0
   * @param seconds1
   * @return IntegerReply
   */
  @Override
  public IntegerReply expire(byte[] key0, byte[] seconds1) throws RedisException {
    return null;
  }

  /**
   * Set the expiration for a key as a UNIX timestamp
   * Generic
   *
   * @param key0
   * @param timestamp1
   * @return IntegerReply
   */
  @Override
  public IntegerReply expireat(byte[] key0, byte[] timestamp1) throws RedisException {
    return null;
  }

  /**
   * Find all keys matching the given pattern
   * Generic
   *
   * @param pattern0
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply keys(byte[] pattern0) throws RedisException {
    return null;
  }

  /**
   * Atomically transfer a key from a Redis instance to another one.
   * Generic
   *
   * @param host0
   * @param port1
   * @param key2
   * @param destination_db3
   * @param timeout4
   * @return StatusReply
   */
  @Override
  public StatusReply migrate(byte[] host0, byte[] port1, byte[] key2, byte[] destination_db3, byte[] timeout4) throws RedisException {
    return null;
  }

  /**
   * Move a key to another database
   * Generic
   *
   * @param key0
   * @param db1
   * @return IntegerReply
   */
  @Override
  public IntegerReply move(byte[] key0, byte[] db1) throws RedisException {
    return null;
  }

  /**
   * Inspect the internals of Redis objects
   * Generic
   *
   * @param subcommand0
   * @param arguments1
   * @return Reply
   */
  @Override
  public Reply object(byte[] subcommand0, byte[][] arguments1) throws RedisException {
    return null;
  }

  /**
   * Remove the expiration from a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply persist(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Set a key's time to live in milliseconds
   * Generic
   *
   * @param key0
   * @param milliseconds1
   * @return IntegerReply
   */
  @Override
  public IntegerReply pexpire(byte[] key0, byte[] milliseconds1) throws RedisException {
    return null;
  }

  /**
   * Set the expiration for a key as a UNIX timestamp specified in milliseconds
   * Generic
   *
   * @param key0
   * @param milliseconds_timestamp1
   * @return IntegerReply
   */
  @Override
  public IntegerReply pexpireat(byte[] key0, byte[] milliseconds_timestamp1) throws RedisException {
    return null;
  }

  /**
   * Get the time to live for a key in milliseconds
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply pttl(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Return a random key from the keyspace
   * Generic
   *
   * @return BulkReply
   */
  @Override
  public BulkReply randomkey() throws RedisException {
    return null;
  }

  /**
   * Rename a key
   * Generic
   *
   * @param key0
   * @param newkey1
   * @return StatusReply
   */
  @Override
  public StatusReply rename(byte[] key0, byte[] newkey1) throws RedisException {
    return null;
  }

  /**
   * Rename a key, only if the new key does not exist
   * Generic
   *
   * @param key0
   * @param newkey1
   * @return IntegerReply
   */
  @Override
  public IntegerReply renamenx(byte[] key0, byte[] newkey1) throws RedisException {
    return null;
  }

  /**
   * Create a key using the provided serialized value, previously obtained using DUMP.
   * Generic
   *
   * @param key0
   * @param ttl1
   * @param serialized_value2
   * @return StatusReply
   */
  @Override
  public StatusReply restore(byte[] key0, byte[] ttl1, byte[] serialized_value2) throws RedisException {
    return null;
  }

  /**
   * Sort the elements in a list, set or sorted set
   * Generic
   *
   * @param key0
   * @param pattern1
   * @param offset_or_count2
   * @param pattern3
   * @return Reply
   */
  @Override
  public Reply sort(byte[] key0, byte[] pattern1, byte[] offset_or_count2, byte[][] pattern3) throws RedisException {
    return null;
  }

  /**
   * Get the time to live for a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply ttl(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Determine the type stored at key
   * Generic
   *
   * @param key0
   * @return StatusReply
   */
  @Override
  public StatusReply type(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Forget about all watched keys
   * Transactions
   *
   * @return StatusReply
   */
  @Override
  public StatusReply unwatch() throws RedisException {
    return null;
  }

  /**
   * Watch the given keys to determine execution of the MULTI/EXEC block
   * Transactions
   *
   * @param key0
   * @return StatusReply
   */
  @Override
  public StatusReply watch(byte[][] key0) throws RedisException {
    return null;
  }

  /**
   * Execute a Lua script server side
   * Scripting
   *
   * @param script0
   * @param numkeys1
   * @param key2
   * @return Reply
   */
  @Override
  public Reply eval(byte[] script0, byte[] numkeys1, byte[][] key2) throws RedisException {
    return null;
  }

  /**
   * Execute a Lua script server side
   * Scripting
   *
   * @param sha10
   * @param numkeys1
   * @param key2
   * @return Reply
   */
  @Override
  public Reply evalsha(byte[] sha10, byte[] numkeys1, byte[][] key2) throws RedisException {
    return null;
  }

  /**
   * Check existence of scripts in the script cache.
   * Scripting
   *
   * @param script0
   * @return Reply
   */
  @Override
  public Reply script_exists(byte[][] script0) throws RedisException {
    return null;
  }

  /**
   * Remove all the scripts from the script cache.
   * Scripting
   *
   * @return Reply
   */
  @Override
  public Reply script_flush() throws RedisException {
    return null;
  }

  /**
   * Kill the script currently in execution.
   * Scripting
   *
   * @return Reply
   */
  @Override
  public Reply script_kill() throws RedisException {
    return null;
  }

  /**
   * Load the specified Lua script into the script cache.
   * Scripting
   *
   * @param script0
   * @return Reply
   */
  @Override
  public Reply script_load(byte[] script0) throws RedisException {
    return null;
  }

  /**
   * Delete one or more hash fields
   * Hash
   *
   * @param key0
   * @param field1
   * @return IntegerReply
   */
  @Override
  public IntegerReply hdel(byte[] key0, byte[][] field1) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, false);
    int total = 0;
    for (byte[] hkey : field1) {
      total += hash.remove(new BytesKey(hkey)) == null ? 0 : 1;
    }
    return new IntegerReply(total);
  }

  /**
   * Determine if a hash field exists
   * Hash
   *
   * @param key0
   * @param field1
   * @return IntegerReply
   */
  @Override
  public IntegerReply hexists(byte[] key0, byte[] field1) throws RedisException {
    return getHash(key0, false).get(new BytesKey(field1)) == null ? integer(0) : integer(1);
  }

  /**
   * Get the value of a hash field
   * Hash
   *
   * @param key0
   * @param field1
   * @return BulkReply
   */
  @Override
  public BulkReply hget(byte[] key0, byte[] field1) throws RedisException {
    byte[] bytes = getHash(key0, false).get(new BytesKey(field1));
    if (bytes == null) {
      return NIL_REPLY;
    } else {
      return new BulkReply(bytes);
    }
  }

  /**
   * Get all the fields and values in a hash
   * Hash
   *
   * @param key0
   * @return MultiBulkReply
   */
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

  /**
   * Increment the integer value of a hash field by the given number
   * Hash
   *
   * @param key0
   * @param field1
   * @param increment2
   * @return IntegerReply
   */
  @Override
  public IntegerReply hincrby(byte[] key0, byte[] field1, byte[] increment2) throws RedisException {
    return null;
  }

  /**
   * Increment the float value of a hash field by the given amount
   * Hash
   *
   * @param key0
   * @param field1
   * @param increment2
   * @return BulkReply
   */
  @Override
  public BulkReply hincrbyfloat(byte[] key0, byte[] field1, byte[] increment2) throws RedisException {
    return null;
  }

  /**
   * Get all the fields in a hash
   * Hash
   *
   * @param key0
   * @return MultiBulkReply
   */
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

  /**
   * Get the number of fields in a hash
   * Hash
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply hlen(byte[] key0) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, false);
    return new IntegerReply(hash.size());
  }

  /**
   * Get the values of all the given hash fields
   * Hash
   *
   * @param key0
   * @param field1
   * @return MultiBulkReply
   */
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

  /**
   * Set multiple hash fields to multiple values
   * Hash
   *
   * @param key0
   * @param field_or_value1
   * @return StatusReply
   */
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

  /**
   * Set the string value of a hash field
   * Hash
   *
   * @param key0
   * @param field1
   * @param value2
   * @return IntegerReply
   */
  @Override
  public IntegerReply hset(byte[] key0, byte[] field1, byte[] value2) throws RedisException {
    BytesKeyObjectMap<byte[]> hash = getHash(key0, true);
    Object put = hash.put(new BytesKey(field1), value2);
    return put == null ? integer(1) : integer(0);
  }

  /**
   * Set the value of a hash field, only if the field does not exist
   * Hash
   *
   * @param key0
   * @param field1
   * @param value2
   * @return IntegerReply
   */
  @Override
  public IntegerReply hsetnx(byte[] key0, byte[] field1, byte[] value2) throws RedisException {
    return null;
  }

  /**
   * Get all the values in a hash
   * Hash
   *
   * @param key0
   * @return MultiBulkReply
   */
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

  /**
   * Post a message to a channel
   * Pubsub
   *
   * @param channel0
   * @param message1
   * @return IntegerReply
   */
  @Override
  public IntegerReply publish(byte[] channel0, byte[] message1) throws RedisException {
    return null;
  }

  /**
   * Add one or more members to a set
   * Set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  @Override
  public IntegerReply sadd(byte[] key0, byte[][] member1) throws RedisException {
    return null;
  }

  /**
   * Get the number of members in a set
   * Set
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply scard(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Subtract multiple sets
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply sdiff(byte[][] key0) throws RedisException {
    return null;
  }

  /**
   * Subtract multiple sets and store the resulting set in a key
   * Set
   *
   * @param destination0
   * @param key1
   * @return IntegerReply
   */
  @Override
  public IntegerReply sdiffstore(byte[] destination0, byte[][] key1) throws RedisException {
    return null;
  }

  /**
   * Intersect multiple sets
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply sinter(byte[][] key0) throws RedisException {
    return null;
  }

  /**
   * Intersect multiple sets and store the resulting set in a key
   * Set
   *
   * @param destination0
   * @param key1
   * @return IntegerReply
   */
  @Override
  public IntegerReply sinterstore(byte[] destination0, byte[][] key1) throws RedisException {
    return null;
  }

  /**
   * Determine if a given value is a member of a set
   * Set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  @Override
  public IntegerReply sismember(byte[] key0, byte[] member1) throws RedisException {
    return null;
  }

  /**
   * Get all the members in a set
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply smembers(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Move a member from one set to another
   * Set
   *
   * @param source0
   * @param destination1
   * @param member2
   * @return IntegerReply
   */
  @Override
  public IntegerReply smove(byte[] source0, byte[] destination1, byte[] member2) throws RedisException {
    return null;
  }

  /**
   * Remove and return a random member from a set
   * Set
   *
   * @param key0
   * @return BulkReply
   */
  @Override
  public BulkReply spop(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Get a random member from a set
   * Set
   *
   * @param key0
   * @return BulkReply
   */
  @Override
  public BulkReply srandmember(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Remove one or more members from a set
   * Set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  @Override
  public IntegerReply srem(byte[] key0, byte[][] member1) throws RedisException {
    return null;
  }

  /**
   * Add multiple sets
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply sunion(byte[][] key0) throws RedisException {
    return null;
  }

  /**
   * Add multiple sets and store the resulting set in a key
   * Set
   *
   * @param destination0
   * @param key1
   * @return IntegerReply
   */
  @Override
  public IntegerReply sunionstore(byte[] destination0, byte[][] key1) throws RedisException {
    return null;
  }

  /**
   * Add one or more members to a sorted set, or update its score if it already exists
   * Sorted_set
   *
   * @param args
   * @return IntegerReply
   */
  @Override
  public IntegerReply zadd(byte[][] args) throws RedisException {
    return null;
  }

  /**
   * Get the number of members in a sorted set
   * Sorted_set
   *
   * @param key0
   * @return IntegerReply
   */
  @Override
  public IntegerReply zcard(byte[] key0) throws RedisException {
    return null;
  }

  /**
   * Count the members in a sorted set with scores within the given values
   * Sorted_set
   *
   * @param key0
   * @param min1
   * @param max2
   * @return IntegerReply
   */
  @Override
  public IntegerReply zcount(byte[] key0, byte[] min1, byte[] max2) throws RedisException {
    return null;
  }

  /**
   * Increment the score of a member in a sorted set
   * Sorted_set
   *
   * @param key0
   * @param increment1
   * @param member2
   * @return BulkReply
   */
  @Override
  public BulkReply zincrby(byte[] key0, byte[] increment1, byte[] member2) throws RedisException {
    return null;
  }

  /**
   * Intersect multiple sorted sets and store the resulting sorted set in a new key
   * Sorted_set
   *
   * @param args
   * @return IntegerReply
   */
  @Override
  public IntegerReply zinterstore(byte[][] args) throws RedisException {
    return null;
  }

  /**
   * Return a range of members in a sorted set, by index
   * Sorted_set
   *
   * @param key0
   * @param start1
   * @param stop2
   * @param withscores3
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply zrange(byte[] key0, byte[] start1, byte[] stop2, byte[] withscores3) throws RedisException {
    return null;
  }

  /**
   * Return a range of members in a sorted set, by score
   * Sorted_set
   *
   * @param key0
   * @param min1
   * @param max2
   * @param withscores3
   * @param offset_or_count4
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply zrangebyscore(byte[] key0, byte[] min1, byte[] max2, byte[] withscores3, byte[] offset_or_count4) throws RedisException {
    return null;
  }

  /**
   * Determine the index of a member in a sorted set
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return Reply
   */
  @Override
  public Reply zrank(byte[] key0, byte[] member1) throws RedisException {
    return null;
  }

  /**
   * Remove one or more members from a sorted set
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  @Override
  public IntegerReply zrem(byte[] key0, byte[][] member1) throws RedisException {
    return null;
  }

  /**
   * Remove all members in a sorted set within the given indexes
   * Sorted_set
   *
   * @param key0
   * @param start1
   * @param stop2
   * @return IntegerReply
   */
  @Override
  public IntegerReply zremrangebyrank(byte[] key0, byte[] start1, byte[] stop2) throws RedisException {
    return null;
  }

  /**
   * Remove all members in a sorted set within the given scores
   * Sorted_set
   *
   * @param key0
   * @param min1
   * @param max2
   * @return IntegerReply
   */
  @Override
  public IntegerReply zremrangebyscore(byte[] key0, byte[] min1, byte[] max2) throws RedisException {
    return null;
  }

  /**
   * Return a range of members in a sorted set, by index, with scores ordered from high to low
   * Sorted_set
   *
   * @param key0
   * @param start1
   * @param stop2
   * @param withscores3
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply zrevrange(byte[] key0, byte[] start1, byte[] stop2, byte[] withscores3) throws RedisException {
    return null;
  }

  /**
   * Return a range of members in a sorted set, by score, with scores ordered from high to low
   * Sorted_set
   *
   * @param key0
   * @param max1
   * @param min2
   * @param withscores3
   * @param offset_or_count4
   * @return MultiBulkReply
   */
  @Override
  public MultiBulkReply zrevrangebyscore(byte[] key0, byte[] max1, byte[] min2, byte[] withscores3, byte[] offset_or_count4) throws RedisException {
    return null;
  }

  /**
   * Determine the index of a member in a sorted set, with scores ordered from high to low
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return Reply
   */
  @Override
  public Reply zrevrank(byte[] key0, byte[] member1) throws RedisException {
    return null;
  }

  /**
   * Get the score associated with the given member in a sorted set
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return BulkReply
   */
  @Override
  public BulkReply zscore(byte[] key0, byte[] member1) throws RedisException {
    return null;
  }

  /**
   * Add multiple sorted sets and store the resulting sorted set in a new key
   * Sorted_set
   *
   * @param destination0
   * @param numkeys1
   * @param key2
   * @return IntegerReply
   */
  @Override
  public IntegerReply zunionstore(byte[] destination0, byte[] numkeys1, byte[][] key2) throws RedisException {
    return null;
  }
}
