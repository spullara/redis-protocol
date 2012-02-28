package redis.client;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ListenableFuture;

import redis.reply.BulkReply;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;
import redis.reply.StatusReply;

public class RedisClient extends RedisClientBase {
  protected Pipeline pipeline = new Pipeline();

  public RedisClient(SocketPool socketPool) throws IOException {
    super(socketPool);
  }

  public Pipeline pipeline() {
    return pipeline;
  }
  
  private static final String APPEND = "APPEND";
  private static final byte[] APPEND_BYTES = APPEND.getBytes(Charsets.US_ASCII);
  private static final int APPEND_VERSION = parseVersion("1.3.3");

  // Append a value to a key
  public IntegerReply append(Object key0, Object value1) throws RedisException {
    if (version < APPEND_VERSION) throw new RedisException("Server does not support APPEND");
    return (IntegerReply) execute(APPEND, APPEND_BYTES, key0, value1);
  }
  
  private static final String AUTH = "AUTH";
  private static final byte[] AUTH_BYTES = AUTH.getBytes(Charsets.US_ASCII);
  private static final int AUTH_VERSION = parseVersion("0.08");

  // Authenticate to the server
  public StatusReply auth(Object password0) throws RedisException {
    if (version < AUTH_VERSION) throw new RedisException("Server does not support AUTH");
    return (StatusReply) execute(AUTH, AUTH_BYTES, password0);
  }
  
  private static final String BGREWRITEAOF = "BGREWRITEAOF";
  private static final byte[] BGREWRITEAOF_BYTES = BGREWRITEAOF.getBytes(Charsets.US_ASCII);
  private static final int BGREWRITEAOF_VERSION = parseVersion("1.07");

  // Asynchronously rewrite the append-only file
  public StatusReply bgrewriteaof() throws RedisException {
    if (version < BGREWRITEAOF_VERSION) throw new RedisException("Server does not support BGREWRITEAOF");
    return (StatusReply) execute(BGREWRITEAOF, BGREWRITEAOF_BYTES);
  }
  
  private static final String BGSAVE = "BGSAVE";
  private static final byte[] BGSAVE_BYTES = BGSAVE.getBytes(Charsets.US_ASCII);
  private static final int BGSAVE_VERSION = parseVersion("0.07");

  // Asynchronously save the dataset to disk
  public StatusReply bgsave() throws RedisException {
    if (version < BGSAVE_VERSION) throw new RedisException("Server does not support BGSAVE");
    return (StatusReply) execute(BGSAVE, BGSAVE_BYTES);
  }
  
  private static final String BLPOP = "BLPOP";
  private static final byte[] BLPOP_BYTES = BLPOP.getBytes(Charsets.US_ASCII);
  private static final int BLPOP_VERSION = parseVersion("1.3.1");

  // Remove and get the first element in a list, or block until one is available
  public MultiBulkReply blpop(Object... key0) throws RedisException {
    if (version < BLPOP_VERSION) throw new RedisException("Server does not support BLPOP");
    return (MultiBulkReply) execute(BLPOP, BLPOP_BYTES, key0);
  }
  
  private static final String BRPOP = "BRPOP";
  private static final byte[] BRPOP_BYTES = BRPOP.getBytes(Charsets.US_ASCII);
  private static final int BRPOP_VERSION = parseVersion("1.3.1");

  // Remove and get the last element in a list, or block until one is available
  public MultiBulkReply brpop(Object... key0) throws RedisException {
    if (version < BRPOP_VERSION) throw new RedisException("Server does not support BRPOP");
    return (MultiBulkReply) execute(BRPOP, BRPOP_BYTES, key0);
  }
  
  private static final String BRPOPLPUSH = "BRPOPLPUSH";
  private static final byte[] BRPOPLPUSH_BYTES = BRPOPLPUSH.getBytes(Charsets.US_ASCII);
  private static final int BRPOPLPUSH_VERSION = parseVersion("2.1.7");

  // Pop a value from a list, push it to another list and return it; or block until one is available
  public BulkReply brpoplpush(Object source0, Object destination1, Object timeout2) throws RedisException {
    if (version < BRPOPLPUSH_VERSION) throw new RedisException("Server does not support BRPOPLPUSH");
    return (BulkReply) execute(BRPOPLPUSH, BRPOPLPUSH_BYTES, source0, destination1, timeout2);
  }
  
  private static final String CONFIG_GET = "CONFIG_GET";
  private static final byte[] CONFIG_GET_BYTES = CONFIG_GET.getBytes(Charsets.US_ASCII);
  private static final int CONFIG_GET_VERSION = parseVersion("2.0");

  // Get the value of a configuration parameter
  public Reply config_get(Object parameter0) throws RedisException {
    if (version < CONFIG_GET_VERSION) throw new RedisException("Server does not support CONFIG_GET");
    return (Reply) execute(CONFIG_GET, CONFIG_GET_BYTES, parameter0);
  }
  
  private static final String CONFIG_SET = "CONFIG_SET";
  private static final byte[] CONFIG_SET_BYTES = CONFIG_SET.getBytes(Charsets.US_ASCII);
  private static final int CONFIG_SET_VERSION = parseVersion("2.0");

  // Set a configuration parameter to the given value
  public Reply config_set(Object parameter0, Object value1) throws RedisException {
    if (version < CONFIG_SET_VERSION) throw new RedisException("Server does not support CONFIG_SET");
    return (Reply) execute(CONFIG_SET, CONFIG_SET_BYTES, parameter0, value1);
  }
  
  private static final String CONFIG_RESETSTAT = "CONFIG_RESETSTAT";
  private static final byte[] CONFIG_RESETSTAT_BYTES = CONFIG_RESETSTAT.getBytes(Charsets.US_ASCII);
  private static final int CONFIG_RESETSTAT_VERSION = parseVersion("2.0");

  // Reset the stats returned by INFO
  public Reply config_resetstat() throws RedisException {
    if (version < CONFIG_RESETSTAT_VERSION) throw new RedisException("Server does not support CONFIG_RESETSTAT");
    return (Reply) execute(CONFIG_RESETSTAT, CONFIG_RESETSTAT_BYTES);
  }
  
  private static final String DBSIZE = "DBSIZE";
  private static final byte[] DBSIZE_BYTES = DBSIZE.getBytes(Charsets.US_ASCII);
  private static final int DBSIZE_VERSION = parseVersion("0.07");

  // Return the number of keys in the selected database
  public IntegerReply dbsize() throws RedisException {
    if (version < DBSIZE_VERSION) throw new RedisException("Server does not support DBSIZE");
    return (IntegerReply) execute(DBSIZE, DBSIZE_BYTES);
  }
  
  private static final String DEBUG_OBJECT = "DEBUG_OBJECT";
  private static final byte[] DEBUG_OBJECT_BYTES = DEBUG_OBJECT.getBytes(Charsets.US_ASCII);
  private static final int DEBUG_OBJECT_VERSION = parseVersion("0.101");

  // Get debugging information about a key
  public Reply debug_object(Object key0) throws RedisException {
    if (version < DEBUG_OBJECT_VERSION) throw new RedisException("Server does not support DEBUG_OBJECT");
    return (Reply) execute(DEBUG_OBJECT, DEBUG_OBJECT_BYTES, key0);
  }
  
  private static final String DEBUG_SEGFAULT = "DEBUG_SEGFAULT";
  private static final byte[] DEBUG_SEGFAULT_BYTES = DEBUG_SEGFAULT.getBytes(Charsets.US_ASCII);
  private static final int DEBUG_SEGFAULT_VERSION = parseVersion("0.101");

  // Make the server crash
  public Reply debug_segfault() throws RedisException {
    if (version < DEBUG_SEGFAULT_VERSION) throw new RedisException("Server does not support DEBUG_SEGFAULT");
    return (Reply) execute(DEBUG_SEGFAULT, DEBUG_SEGFAULT_BYTES);
  }
  
  private static final String DECR = "DECR";
  private static final byte[] DECR_BYTES = DECR.getBytes(Charsets.US_ASCII);
  private static final int DECR_VERSION = parseVersion("0.07");

  // Decrement the integer value of a key by one
  public IntegerReply decr(Object key0) throws RedisException {
    if (version < DECR_VERSION) throw new RedisException("Server does not support DECR");
    return (IntegerReply) execute(DECR, DECR_BYTES, key0);
  }
  
  private static final String DECRBY = "DECRBY";
  private static final byte[] DECRBY_BYTES = DECRBY.getBytes(Charsets.US_ASCII);
  private static final int DECRBY_VERSION = parseVersion("0.07");

  // Decrement the integer value of a key by the given number
  public IntegerReply decrby(Object key0, Object decrement1) throws RedisException {
    if (version < DECRBY_VERSION) throw new RedisException("Server does not support DECRBY");
    return (IntegerReply) execute(DECRBY, DECRBY_BYTES, key0, decrement1);
  }
  
  private static final String DEL = "DEL";
  private static final byte[] DEL_BYTES = DEL.getBytes(Charsets.US_ASCII);
  private static final int DEL_VERSION = parseVersion("0.07");

  // Delete a key
  public IntegerReply del(Object... key0) throws RedisException {
    if (version < DEL_VERSION) throw new RedisException("Server does not support DEL");
    return (IntegerReply) execute(DEL, DEL_BYTES, key0);
  }
  
  private static final String DISCARD = "DISCARD";
  private static final byte[] DISCARD_BYTES = DISCARD.getBytes(Charsets.US_ASCII);
  private static final int DISCARD_VERSION = parseVersion("1.3.3");

  // Discard all commands issued after MULTI
  public StatusReply discard() throws RedisException {
    if (version < DISCARD_VERSION) throw new RedisException("Server does not support DISCARD");
    return (StatusReply) execute(DISCARD, DISCARD_BYTES);
  }
  
  private static final String ECHO = "ECHO";
  private static final byte[] ECHO_BYTES = ECHO.getBytes(Charsets.US_ASCII);
  private static final int ECHO_VERSION = parseVersion("0.07");

  // Echo the given string
  public BulkReply echo(Object message0) throws RedisException {
    if (version < ECHO_VERSION) throw new RedisException("Server does not support ECHO");
    return (BulkReply) execute(ECHO, ECHO_BYTES, message0);
  }
  
  private static final String EXEC = "EXEC";
  private static final byte[] EXEC_BYTES = EXEC.getBytes(Charsets.US_ASCII);
  private static final int EXEC_VERSION = parseVersion("1.1.95");

  // Execute all commands issued after MULTI
  public MultiBulkReply exec() throws RedisException {
    if (version < EXEC_VERSION) throw new RedisException("Server does not support EXEC");
    return (MultiBulkReply) execute(EXEC, EXEC_BYTES);
  }
  
  private static final String EXISTS = "EXISTS";
  private static final byte[] EXISTS_BYTES = EXISTS.getBytes(Charsets.US_ASCII);
  private static final int EXISTS_VERSION = parseVersion("0.07");

  // Determine if a key exists
  public IntegerReply exists(Object key0) throws RedisException {
    if (version < EXISTS_VERSION) throw new RedisException("Server does not support EXISTS");
    return (IntegerReply) execute(EXISTS, EXISTS_BYTES, key0);
  }
  
  private static final String EXPIRE = "EXPIRE";
  private static final byte[] EXPIRE_BYTES = EXPIRE.getBytes(Charsets.US_ASCII);
  private static final int EXPIRE_VERSION = parseVersion("0.09");

  // Set a key's time to live in seconds
  public IntegerReply expire(Object key0, Object seconds1) throws RedisException {
    if (version < EXPIRE_VERSION) throw new RedisException("Server does not support EXPIRE");
    return (IntegerReply) execute(EXPIRE, EXPIRE_BYTES, key0, seconds1);
  }
  
  private static final String EXPIREAT = "EXPIREAT";
  private static final byte[] EXPIREAT_BYTES = EXPIREAT.getBytes(Charsets.US_ASCII);
  private static final int EXPIREAT_VERSION = parseVersion("1.1");

  // Set the expiration for a key as a UNIX timestamp
  public IntegerReply expireat(Object key0, Object timestamp1) throws RedisException {
    if (version < EXPIREAT_VERSION) throw new RedisException("Server does not support EXPIREAT");
    return (IntegerReply) execute(EXPIREAT, EXPIREAT_BYTES, key0, timestamp1);
  }
  
  private static final String FLUSHALL = "FLUSHALL";
  private static final byte[] FLUSHALL_BYTES = FLUSHALL.getBytes(Charsets.US_ASCII);
  private static final int FLUSHALL_VERSION = parseVersion("0.07");

  // Remove all keys from all databases
  public StatusReply flushall() throws RedisException {
    if (version < FLUSHALL_VERSION) throw new RedisException("Server does not support FLUSHALL");
    return (StatusReply) execute(FLUSHALL, FLUSHALL_BYTES);
  }
  
  private static final String FLUSHDB = "FLUSHDB";
  private static final byte[] FLUSHDB_BYTES = FLUSHDB.getBytes(Charsets.US_ASCII);
  private static final int FLUSHDB_VERSION = parseVersion("0.07");

  // Remove all keys from the current database
  public StatusReply flushdb() throws RedisException {
    if (version < FLUSHDB_VERSION) throw new RedisException("Server does not support FLUSHDB");
    return (StatusReply) execute(FLUSHDB, FLUSHDB_BYTES);
  }
  
  private static final String GET = "GET";
  private static final byte[] GET_BYTES = GET.getBytes(Charsets.US_ASCII);
  private static final int GET_VERSION = parseVersion("0.07");

  // Get the value of a key
  public BulkReply get(Object key0) throws RedisException {
    if (version < GET_VERSION) throw new RedisException("Server does not support GET");
    return (BulkReply) execute(GET, GET_BYTES, key0);
  }
  
  private static final String GETBIT = "GETBIT";
  private static final byte[] GETBIT_BYTES = GETBIT.getBytes(Charsets.US_ASCII);
  private static final int GETBIT_VERSION = parseVersion("2.1.8");

  // Returns the bit value at offset in the string value stored at key
  public IntegerReply getbit(Object key0, Object offset1) throws RedisException {
    if (version < GETBIT_VERSION) throw new RedisException("Server does not support GETBIT");
    return (IntegerReply) execute(GETBIT, GETBIT_BYTES, key0, offset1);
  }
  
  private static final String GETRANGE = "GETRANGE";
  private static final byte[] GETRANGE_BYTES = GETRANGE.getBytes(Charsets.US_ASCII);
  private static final int GETRANGE_VERSION = parseVersion("1.3.4");

  // Get a substring of the string stored at a key
  public BulkReply getrange(Object key0, Object start1, Object end2) throws RedisException {
    if (version < GETRANGE_VERSION) throw new RedisException("Server does not support GETRANGE");
    return (BulkReply) execute(GETRANGE, GETRANGE_BYTES, key0, start1, end2);
  }
  
  private static final String GETSET = "GETSET";
  private static final byte[] GETSET_BYTES = GETSET.getBytes(Charsets.US_ASCII);
  private static final int GETSET_VERSION = parseVersion("0.091");

  // Set the string value of a key and return its old value
  public BulkReply getset(Object key0, Object value1) throws RedisException {
    if (version < GETSET_VERSION) throw new RedisException("Server does not support GETSET");
    return (BulkReply) execute(GETSET, GETSET_BYTES, key0, value1);
  }
  
  private static final String HDEL = "HDEL";
  private static final byte[] HDEL_BYTES = HDEL.getBytes(Charsets.US_ASCII);
  private static final int HDEL_VERSION = parseVersion("1.3.10");

  // Delete one or more hash fields
  public IntegerReply hdel(Object key0, Object... field1) throws RedisException {
    if (version < HDEL_VERSION) throw new RedisException("Server does not support HDEL");
    return (IntegerReply) execute(HDEL, HDEL_BYTES, key0, field1);
  }
  
  private static final String HEXISTS = "HEXISTS";
  private static final byte[] HEXISTS_BYTES = HEXISTS.getBytes(Charsets.US_ASCII);
  private static final int HEXISTS_VERSION = parseVersion("1.3.10");

  // Determine if a hash field exists
  public IntegerReply hexists(Object key0, Object field1) throws RedisException {
    if (version < HEXISTS_VERSION) throw new RedisException("Server does not support HEXISTS");
    return (IntegerReply) execute(HEXISTS, HEXISTS_BYTES, key0, field1);
  }
  
  private static final String HGET = "HGET";
  private static final byte[] HGET_BYTES = HGET.getBytes(Charsets.US_ASCII);
  private static final int HGET_VERSION = parseVersion("1.3.10");

  // Get the value of a hash field
  public BulkReply hget(Object key0, Object field1) throws RedisException {
    if (version < HGET_VERSION) throw new RedisException("Server does not support HGET");
    return (BulkReply) execute(HGET, HGET_BYTES, key0, field1);
  }
  
  private static final String HGETALL = "HGETALL";
  private static final byte[] HGETALL_BYTES = HGETALL.getBytes(Charsets.US_ASCII);
  private static final int HGETALL_VERSION = parseVersion("1.3.10");

  // Get all the fields and values in a hash
  public MultiBulkReply hgetall(Object key0) throws RedisException {
    if (version < HGETALL_VERSION) throw new RedisException("Server does not support HGETALL");
    return (MultiBulkReply) execute(HGETALL, HGETALL_BYTES, key0);
  }
  
  private static final String HINCRBY = "HINCRBY";
  private static final byte[] HINCRBY_BYTES = HINCRBY.getBytes(Charsets.US_ASCII);
  private static final int HINCRBY_VERSION = parseVersion("1.3.10");

  // Increment the integer value of a hash field by the given number
  public IntegerReply hincrby(Object key0, Object field1, Object increment2) throws RedisException {
    if (version < HINCRBY_VERSION) throw new RedisException("Server does not support HINCRBY");
    return (IntegerReply) execute(HINCRBY, HINCRBY_BYTES, key0, field1, increment2);
  }
  
  private static final String HKEYS = "HKEYS";
  private static final byte[] HKEYS_BYTES = HKEYS.getBytes(Charsets.US_ASCII);
  private static final int HKEYS_VERSION = parseVersion("1.3.10");

  // Get all the fields in a hash
  public MultiBulkReply hkeys(Object key0) throws RedisException {
    if (version < HKEYS_VERSION) throw new RedisException("Server does not support HKEYS");
    return (MultiBulkReply) execute(HKEYS, HKEYS_BYTES, key0);
  }
  
  private static final String HLEN = "HLEN";
  private static final byte[] HLEN_BYTES = HLEN.getBytes(Charsets.US_ASCII);
  private static final int HLEN_VERSION = parseVersion("1.3.10");

  // Get the number of fields in a hash
  public IntegerReply hlen(Object key0) throws RedisException {
    if (version < HLEN_VERSION) throw new RedisException("Server does not support HLEN");
    return (IntegerReply) execute(HLEN, HLEN_BYTES, key0);
  }
  
  private static final String HMGET = "HMGET";
  private static final byte[] HMGET_BYTES = HMGET.getBytes(Charsets.US_ASCII);
  private static final int HMGET_VERSION = parseVersion("1.3.10");

  // Get the values of all the given hash fields
  public MultiBulkReply hmget(Object key0, Object... field1) throws RedisException {
    if (version < HMGET_VERSION) throw new RedisException("Server does not support HMGET");
    return (MultiBulkReply) execute(HMGET, HMGET_BYTES, key0, field1);
  }
  
  private static final String HMSET = "HMSET";
  private static final byte[] HMSET_BYTES = HMSET.getBytes(Charsets.US_ASCII);
  private static final int HMSET_VERSION = parseVersion("1.3.8");

  // Set multiple hash fields to multiple values
  public StatusReply hmset(Object key0, Object... field_or_value1) throws RedisException {
    if (version < HMSET_VERSION) throw new RedisException("Server does not support HMSET");
    return (StatusReply) execute(HMSET, HMSET_BYTES, key0, field_or_value1);
  }
  
  private static final String HSET = "HSET";
  private static final byte[] HSET_BYTES = HSET.getBytes(Charsets.US_ASCII);
  private static final int HSET_VERSION = parseVersion("1.3.10");

  // Set the string value of a hash field
  public IntegerReply hset(Object key0, Object field1, Object value2) throws RedisException {
    if (version < HSET_VERSION) throw new RedisException("Server does not support HSET");
    return (IntegerReply) execute(HSET, HSET_BYTES, key0, field1, value2);
  }
  
  private static final String HSETNX = "HSETNX";
  private static final byte[] HSETNX_BYTES = HSETNX.getBytes(Charsets.US_ASCII);
  private static final int HSETNX_VERSION = parseVersion("1.3.8");

  // Set the value of a hash field, only if the field does not exist
  public IntegerReply hsetnx(Object key0, Object field1, Object value2) throws RedisException {
    if (version < HSETNX_VERSION) throw new RedisException("Server does not support HSETNX");
    return (IntegerReply) execute(HSETNX, HSETNX_BYTES, key0, field1, value2);
  }
  
  private static final String HVALS = "HVALS";
  private static final byte[] HVALS_BYTES = HVALS.getBytes(Charsets.US_ASCII);
  private static final int HVALS_VERSION = parseVersion("1.3.10");

  // Get all the values in a hash
  public MultiBulkReply hvals(Object key0) throws RedisException {
    if (version < HVALS_VERSION) throw new RedisException("Server does not support HVALS");
    return (MultiBulkReply) execute(HVALS, HVALS_BYTES, key0);
  }
  
  private static final String INCR = "INCR";
  private static final byte[] INCR_BYTES = INCR.getBytes(Charsets.US_ASCII);
  private static final int INCR_VERSION = parseVersion("0.07");

  // Increment the integer value of a key by one
  public IntegerReply incr(Object key0) throws RedisException {
    if (version < INCR_VERSION) throw new RedisException("Server does not support INCR");
    return (IntegerReply) execute(INCR, INCR_BYTES, key0);
  }
  
  private static final String INCRBY = "INCRBY";
  private static final byte[] INCRBY_BYTES = INCRBY.getBytes(Charsets.US_ASCII);
  private static final int INCRBY_VERSION = parseVersion("0.07");

  // Increment the integer value of a key by the given number
  public IntegerReply incrby(Object key0, Object increment1) throws RedisException {
    if (version < INCRBY_VERSION) throw new RedisException("Server does not support INCRBY");
    return (IntegerReply) execute(INCRBY, INCRBY_BYTES, key0, increment1);
  }
  
  private static final String INFO = "INFO";
  private static final byte[] INFO_BYTES = INFO.getBytes(Charsets.US_ASCII);
  private static final int INFO_VERSION = parseVersion("0.07");

  // Get information and statistics about the server
  public BulkReply info() throws RedisException {
    if (version < INFO_VERSION) throw new RedisException("Server does not support INFO");
    return (BulkReply) execute(INFO, INFO_BYTES);
  }
  
  private static final String KEYS = "KEYS";
  private static final byte[] KEYS_BYTES = KEYS.getBytes(Charsets.US_ASCII);
  private static final int KEYS_VERSION = parseVersion("0.07");

  // Find all keys matching the given pattern
  public MultiBulkReply keys(Object pattern0) throws RedisException {
    if (version < KEYS_VERSION) throw new RedisException("Server does not support KEYS");
    return (MultiBulkReply) execute(KEYS, KEYS_BYTES, pattern0);
  }
  
  private static final String LASTSAVE = "LASTSAVE";
  private static final byte[] LASTSAVE_BYTES = LASTSAVE.getBytes(Charsets.US_ASCII);
  private static final int LASTSAVE_VERSION = parseVersion("0.07");

  // Get the UNIX time stamp of the last successful save to disk
  public IntegerReply lastsave() throws RedisException {
    if (version < LASTSAVE_VERSION) throw new RedisException("Server does not support LASTSAVE");
    return (IntegerReply) execute(LASTSAVE, LASTSAVE_BYTES);
  }
  
  private static final String LINDEX = "LINDEX";
  private static final byte[] LINDEX_BYTES = LINDEX.getBytes(Charsets.US_ASCII);
  private static final int LINDEX_VERSION = parseVersion("0.07");

  // Get an element from a list by its index
  public BulkReply lindex(Object key0, Object index1) throws RedisException {
    if (version < LINDEX_VERSION) throw new RedisException("Server does not support LINDEX");
    return (BulkReply) execute(LINDEX, LINDEX_BYTES, key0, index1);
  }
  
  private static final String LINSERT = "LINSERT";
  private static final byte[] LINSERT_BYTES = LINSERT.getBytes(Charsets.US_ASCII);
  private static final int LINSERT_VERSION = parseVersion("2.1.1");

  // Insert an element before or after another element in a list
  public IntegerReply linsert(Object key0, Object where1, Object pivot2, Object value3) throws RedisException {
    if (version < LINSERT_VERSION) throw new RedisException("Server does not support LINSERT");
    return (IntegerReply) execute(LINSERT, LINSERT_BYTES, key0, where1, pivot2, value3);
  }
  
  private static final String LLEN = "LLEN";
  private static final byte[] LLEN_BYTES = LLEN.getBytes(Charsets.US_ASCII);
  private static final int LLEN_VERSION = parseVersion("0.07");

  // Get the length of a list
  public IntegerReply llen(Object key0) throws RedisException {
    if (version < LLEN_VERSION) throw new RedisException("Server does not support LLEN");
    return (IntegerReply) execute(LLEN, LLEN_BYTES, key0);
  }
  
  private static final String LPOP = "LPOP";
  private static final byte[] LPOP_BYTES = LPOP.getBytes(Charsets.US_ASCII);
  private static final int LPOP_VERSION = parseVersion("0.07");

  // Remove and get the first element in a list
  public BulkReply lpop(Object key0) throws RedisException {
    if (version < LPOP_VERSION) throw new RedisException("Server does not support LPOP");
    return (BulkReply) execute(LPOP, LPOP_BYTES, key0);
  }
  
  private static final String LPUSH = "LPUSH";
  private static final byte[] LPUSH_BYTES = LPUSH.getBytes(Charsets.US_ASCII);
  private static final int LPUSH_VERSION = parseVersion("0.07");

  // Prepend one or multiple values to a list
  public IntegerReply lpush(Object key0, Object... value1) throws RedisException {
    if (version < LPUSH_VERSION) throw new RedisException("Server does not support LPUSH");
    return (IntegerReply) execute(LPUSH, LPUSH_BYTES, key0, value1);
  }
  
  private static final String LPUSHX = "LPUSHX";
  private static final byte[] LPUSHX_BYTES = LPUSHX.getBytes(Charsets.US_ASCII);
  private static final int LPUSHX_VERSION = parseVersion("2.1.1");

  // Prepend a value to a list, only if the list exists
  public IntegerReply lpushx(Object key0, Object value1) throws RedisException {
    if (version < LPUSHX_VERSION) throw new RedisException("Server does not support LPUSHX");
    return (IntegerReply) execute(LPUSHX, LPUSHX_BYTES, key0, value1);
  }
  
  private static final String LRANGE = "LRANGE";
  private static final byte[] LRANGE_BYTES = LRANGE.getBytes(Charsets.US_ASCII);
  private static final int LRANGE_VERSION = parseVersion("0.07");

  // Get a range of elements from a list
  public MultiBulkReply lrange(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < LRANGE_VERSION) throw new RedisException("Server does not support LRANGE");
    return (MultiBulkReply) execute(LRANGE, LRANGE_BYTES, key0, start1, stop2);
  }
  
  private static final String LREM = "LREM";
  private static final byte[] LREM_BYTES = LREM.getBytes(Charsets.US_ASCII);
  private static final int LREM_VERSION = parseVersion("0.07");

  // Remove elements from a list
  public IntegerReply lrem(Object key0, Object count1, Object value2) throws RedisException {
    if (version < LREM_VERSION) throw new RedisException("Server does not support LREM");
    return (IntegerReply) execute(LREM, LREM_BYTES, key0, count1, value2);
  }
  
  private static final String LSET = "LSET";
  private static final byte[] LSET_BYTES = LSET.getBytes(Charsets.US_ASCII);
  private static final int LSET_VERSION = parseVersion("0.07");

  // Set the value of an element in a list by its index
  public StatusReply lset(Object key0, Object index1, Object value2) throws RedisException {
    if (version < LSET_VERSION) throw new RedisException("Server does not support LSET");
    return (StatusReply) execute(LSET, LSET_BYTES, key0, index1, value2);
  }
  
  private static final String LTRIM = "LTRIM";
  private static final byte[] LTRIM_BYTES = LTRIM.getBytes(Charsets.US_ASCII);
  private static final int LTRIM_VERSION = parseVersion("0.07");

  // Trim a list to the specified range
  public StatusReply ltrim(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < LTRIM_VERSION) throw new RedisException("Server does not support LTRIM");
    return (StatusReply) execute(LTRIM, LTRIM_BYTES, key0, start1, stop2);
  }
  
  private static final String MGET = "MGET";
  private static final byte[] MGET_BYTES = MGET.getBytes(Charsets.US_ASCII);
  private static final int MGET_VERSION = parseVersion("0.07");

  // Get the values of all the given keys
  public MultiBulkReply mget(Object... key0) throws RedisException {
    if (version < MGET_VERSION) throw new RedisException("Server does not support MGET");
    return (MultiBulkReply) execute(MGET, MGET_BYTES, key0);
  }
  
  private static final String MONITOR = "MONITOR";
  private static final byte[] MONITOR_BYTES = MONITOR.getBytes(Charsets.US_ASCII);
  private static final int MONITOR_VERSION = parseVersion("0.07");

  // Listen for all requests received by the server in real time
  public Reply monitor() throws RedisException {
    if (version < MONITOR_VERSION) throw new RedisException("Server does not support MONITOR");
    return (Reply) execute(MONITOR, MONITOR_BYTES);
  }
  
  private static final String MOVE = "MOVE";
  private static final byte[] MOVE_BYTES = MOVE.getBytes(Charsets.US_ASCII);
  private static final int MOVE_VERSION = parseVersion("0.07");

  // Move a key to another database
  public IntegerReply move(Object key0, Object db1) throws RedisException {
    if (version < MOVE_VERSION) throw new RedisException("Server does not support MOVE");
    return (IntegerReply) execute(MOVE, MOVE_BYTES, key0, db1);
  }
  
  private static final String MSET = "MSET";
  private static final byte[] MSET_BYTES = MSET.getBytes(Charsets.US_ASCII);
  private static final int MSET_VERSION = parseVersion("1.001");

  // Set multiple keys to multiple values
  public StatusReply mset(Object... key_or_value0) throws RedisException {
    if (version < MSET_VERSION) throw new RedisException("Server does not support MSET");
    return (StatusReply) execute(MSET, MSET_BYTES, key_or_value0);
  }
  
  private static final String MSETNX = "MSETNX";
  private static final byte[] MSETNX_BYTES = MSETNX.getBytes(Charsets.US_ASCII);
  private static final int MSETNX_VERSION = parseVersion("1.001");

  // Set multiple keys to multiple values, only if none of the keys exist
  public IntegerReply msetnx(Object... key_or_value0) throws RedisException {
    if (version < MSETNX_VERSION) throw new RedisException("Server does not support MSETNX");
    return (IntegerReply) execute(MSETNX, MSETNX_BYTES, key_or_value0);
  }
  
  private static final String MULTI = "MULTI";
  private static final byte[] MULTI_BYTES = MULTI.getBytes(Charsets.US_ASCII);
  private static final int MULTI_VERSION = parseVersion("1.1.95");

  // Mark the start of a transaction block
  public StatusReply multi() throws RedisException {
    if (version < MULTI_VERSION) throw new RedisException("Server does not support MULTI");
    return (StatusReply) execute(MULTI, MULTI_BYTES);
  }
  
  private static final String OBJECT = "OBJECT";
  private static final byte[] OBJECT_BYTES = OBJECT.getBytes(Charsets.US_ASCII);
  private static final int OBJECT_VERSION = parseVersion("2.2.3");

  // Inspect the internals of Redis objects
  public Reply object(Object subcommand0, Object... arguments1) throws RedisException {
    if (version < OBJECT_VERSION) throw new RedisException("Server does not support OBJECT");
    return (Reply) execute(OBJECT, OBJECT_BYTES, subcommand0, arguments1);
  }
  
  private static final String PERSIST = "PERSIST";
  private static final byte[] PERSIST_BYTES = PERSIST.getBytes(Charsets.US_ASCII);
  private static final int PERSIST_VERSION = parseVersion("2.1.2");

  // Remove the expiration from a key
  public IntegerReply persist(Object key0) throws RedisException {
    if (version < PERSIST_VERSION) throw new RedisException("Server does not support PERSIST");
    return (IntegerReply) execute(PERSIST, PERSIST_BYTES, key0);
  }
  
  private static final String PING = "PING";
  private static final byte[] PING_BYTES = PING.getBytes(Charsets.US_ASCII);
  private static final int PING_VERSION = parseVersion("0.07");

  // Ping the server
  public StatusReply ping() throws RedisException {
    if (version < PING_VERSION) throw new RedisException("Server does not support PING");
    return (StatusReply) execute(PING, PING_BYTES);
  }
  
  private static final String PSUBSCRIBE = "PSUBSCRIBE";
  private static final byte[] PSUBSCRIBE_BYTES = PSUBSCRIBE.getBytes(Charsets.US_ASCII);
  private static final int PSUBSCRIBE_VERSION = parseVersion("1.3.8");

  // Listen for messages published to channels matching the given patterns
  public Reply psubscribe(Object... pattern0) throws RedisException {
    if (version < PSUBSCRIBE_VERSION) throw new RedisException("Server does not support PSUBSCRIBE");
    return (Reply) execute(PSUBSCRIBE, PSUBSCRIBE_BYTES, pattern0);
  }
  
  private static final String PUBLISH = "PUBLISH";
  private static final byte[] PUBLISH_BYTES = PUBLISH.getBytes(Charsets.US_ASCII);
  private static final int PUBLISH_VERSION = parseVersion("1.3.8");

  // Post a message to a channel
  public IntegerReply publish(Object channel0, Object message1) throws RedisException {
    if (version < PUBLISH_VERSION) throw new RedisException("Server does not support PUBLISH");
    return (IntegerReply) execute(PUBLISH, PUBLISH_BYTES, channel0, message1);
  }
  
  private static final String PUNSUBSCRIBE = "PUNSUBSCRIBE";
  private static final byte[] PUNSUBSCRIBE_BYTES = PUNSUBSCRIBE.getBytes(Charsets.US_ASCII);
  private static final int PUNSUBSCRIBE_VERSION = parseVersion("1.3.8");

  // Stop listening for messages posted to channels matching the given patterns
  public Reply punsubscribe(Object... pattern0) throws RedisException {
    if (version < PUNSUBSCRIBE_VERSION) throw new RedisException("Server does not support PUNSUBSCRIBE");
    return (Reply) execute(PUNSUBSCRIBE, PUNSUBSCRIBE_BYTES, pattern0);
  }
  
  private static final String QUIT = "QUIT";
  private static final byte[] QUIT_BYTES = QUIT.getBytes(Charsets.US_ASCII);
  private static final int QUIT_VERSION = parseVersion("0.07");

  // Close the connection
  public StatusReply quit() throws RedisException {
    if (version < QUIT_VERSION) throw new RedisException("Server does not support QUIT");
    return (StatusReply) execute(QUIT, QUIT_BYTES);
  }
  
  private static final String RANDOMKEY = "RANDOMKEY";
  private static final byte[] RANDOMKEY_BYTES = RANDOMKEY.getBytes(Charsets.US_ASCII);
  private static final int RANDOMKEY_VERSION = parseVersion("0.07");

  // Return a random key from the keyspace
  public BulkReply randomkey() throws RedisException {
    if (version < RANDOMKEY_VERSION) throw new RedisException("Server does not support RANDOMKEY");
    return (BulkReply) execute(RANDOMKEY, RANDOMKEY_BYTES);
  }
  
  private static final String RENAME = "RENAME";
  private static final byte[] RENAME_BYTES = RENAME.getBytes(Charsets.US_ASCII);
  private static final int RENAME_VERSION = parseVersion("0.07");

  // Rename a key
  public StatusReply rename(Object key0, Object newkey1) throws RedisException {
    if (version < RENAME_VERSION) throw new RedisException("Server does not support RENAME");
    return (StatusReply) execute(RENAME, RENAME_BYTES, key0, newkey1);
  }
  
  private static final String RENAMENX = "RENAMENX";
  private static final byte[] RENAMENX_BYTES = RENAMENX.getBytes(Charsets.US_ASCII);
  private static final int RENAMENX_VERSION = parseVersion("0.07");

  // Rename a key, only if the new key does not exist
  public IntegerReply renamenx(Object key0, Object newkey1) throws RedisException {
    if (version < RENAMENX_VERSION) throw new RedisException("Server does not support RENAMENX");
    return (IntegerReply) execute(RENAMENX, RENAMENX_BYTES, key0, newkey1);
  }
  
  private static final String RPOP = "RPOP";
  private static final byte[] RPOP_BYTES = RPOP.getBytes(Charsets.US_ASCII);
  private static final int RPOP_VERSION = parseVersion("0.07");

  // Remove and get the last element in a list
  public BulkReply rpop(Object key0) throws RedisException {
    if (version < RPOP_VERSION) throw new RedisException("Server does not support RPOP");
    return (BulkReply) execute(RPOP, RPOP_BYTES, key0);
  }
  
  private static final String RPOPLPUSH = "RPOPLPUSH";
  private static final byte[] RPOPLPUSH_BYTES = RPOPLPUSH.getBytes(Charsets.US_ASCII);
  private static final int RPOPLPUSH_VERSION = parseVersion("1.1");

  // Remove the last element in a list, append it to another list and return it
  public BulkReply rpoplpush(Object source0, Object destination1) throws RedisException {
    if (version < RPOPLPUSH_VERSION) throw new RedisException("Server does not support RPOPLPUSH");
    return (BulkReply) execute(RPOPLPUSH, RPOPLPUSH_BYTES, source0, destination1);
  }
  
  private static final String RPUSH = "RPUSH";
  private static final byte[] RPUSH_BYTES = RPUSH.getBytes(Charsets.US_ASCII);
  private static final int RPUSH_VERSION = parseVersion("0.07");

  // Append one or multiple values to a list
  public IntegerReply rpush(Object key0, Object... value1) throws RedisException {
    if (version < RPUSH_VERSION) throw new RedisException("Server does not support RPUSH");
    return (IntegerReply) execute(RPUSH, RPUSH_BYTES, key0, value1);
  }
  
  private static final String RPUSHX = "RPUSHX";
  private static final byte[] RPUSHX_BYTES = RPUSHX.getBytes(Charsets.US_ASCII);
  private static final int RPUSHX_VERSION = parseVersion("2.1.1");

  // Append a value to a list, only if the list exists
  public IntegerReply rpushx(Object key0, Object value1) throws RedisException {
    if (version < RPUSHX_VERSION) throw new RedisException("Server does not support RPUSHX");
    return (IntegerReply) execute(RPUSHX, RPUSHX_BYTES, key0, value1);
  }
  
  private static final String SADD = "SADD";
  private static final byte[] SADD_BYTES = SADD.getBytes(Charsets.US_ASCII);
  private static final int SADD_VERSION = parseVersion("0.07");

  // Add one or more members to a set
  public IntegerReply sadd(Object key0, Object... member1) throws RedisException {
    if (version < SADD_VERSION) throw new RedisException("Server does not support SADD");
    return (IntegerReply) execute(SADD, SADD_BYTES, key0, member1);
  }
  
  private static final String SAVE = "SAVE";
  private static final byte[] SAVE_BYTES = SAVE.getBytes(Charsets.US_ASCII);
  private static final int SAVE_VERSION = parseVersion("0.07");

  // Synchronously save the dataset to disk
  public Reply save() throws RedisException {
    if (version < SAVE_VERSION) throw new RedisException("Server does not support SAVE");
    return (Reply) execute(SAVE, SAVE_BYTES);
  }
  
  private static final String SCARD = "SCARD";
  private static final byte[] SCARD_BYTES = SCARD.getBytes(Charsets.US_ASCII);
  private static final int SCARD_VERSION = parseVersion("0.07");

  // Get the number of members in a set
  public IntegerReply scard(Object key0) throws RedisException {
    if (version < SCARD_VERSION) throw new RedisException("Server does not support SCARD");
    return (IntegerReply) execute(SCARD, SCARD_BYTES, key0);
  }
  
  private static final String SDIFF = "SDIFF";
  private static final byte[] SDIFF_BYTES = SDIFF.getBytes(Charsets.US_ASCII);
  private static final int SDIFF_VERSION = parseVersion("0.100");

  // Subtract multiple sets
  public MultiBulkReply sdiff(Object... key0) throws RedisException {
    if (version < SDIFF_VERSION) throw new RedisException("Server does not support SDIFF");
    return (MultiBulkReply) execute(SDIFF, SDIFF_BYTES, key0);
  }
  
  private static final String SDIFFSTORE = "SDIFFSTORE";
  private static final byte[] SDIFFSTORE_BYTES = SDIFFSTORE.getBytes(Charsets.US_ASCII);
  private static final int SDIFFSTORE_VERSION = parseVersion("0.100");

  // Subtract multiple sets and store the resulting set in a key
  public IntegerReply sdiffstore(Object destination0, Object... key1) throws RedisException {
    if (version < SDIFFSTORE_VERSION) throw new RedisException("Server does not support SDIFFSTORE");
    return (IntegerReply) execute(SDIFFSTORE, SDIFFSTORE_BYTES, destination0, key1);
  }
  
  private static final String SELECT = "SELECT";
  private static final byte[] SELECT_BYTES = SELECT.getBytes(Charsets.US_ASCII);
  private static final int SELECT_VERSION = parseVersion("0.07");

  // Change the selected database for the current connection
  public StatusReply select(Object index0) throws RedisException {
    if (version < SELECT_VERSION) throw new RedisException("Server does not support SELECT");
    return (StatusReply) execute(SELECT, SELECT_BYTES, index0);
  }
  
  private static final String SET = "SET";
  private static final byte[] SET_BYTES = SET.getBytes(Charsets.US_ASCII);
  private static final int SET_VERSION = parseVersion("0.07");

  // Set the string value of a key
  public StatusReply set(Object key0, Object value1) throws RedisException {
    if (version < SET_VERSION) throw new RedisException("Server does not support SET");
    return (StatusReply) execute(SET, SET_BYTES, key0, value1);
  }
  
  private static final String SETBIT = "SETBIT";
  private static final byte[] SETBIT_BYTES = SETBIT.getBytes(Charsets.US_ASCII);
  private static final int SETBIT_VERSION = parseVersion("2.1.8");

  // Sets or clears the bit at offset in the string value stored at key
  public IntegerReply setbit(Object key0, Object offset1, Object value2) throws RedisException {
    if (version < SETBIT_VERSION) throw new RedisException("Server does not support SETBIT");
    return (IntegerReply) execute(SETBIT, SETBIT_BYTES, key0, offset1, value2);
  }
  
  private static final String SETEX = "SETEX";
  private static final byte[] SETEX_BYTES = SETEX.getBytes(Charsets.US_ASCII);
  private static final int SETEX_VERSION = parseVersion("1.3.10");

  // Set the value and expiration of a key
  public StatusReply setex(Object key0, Object seconds1, Object value2) throws RedisException {
    if (version < SETEX_VERSION) throw new RedisException("Server does not support SETEX");
    return (StatusReply) execute(SETEX, SETEX_BYTES, key0, seconds1, value2);
  }
  
  private static final String SETNX = "SETNX";
  private static final byte[] SETNX_BYTES = SETNX.getBytes(Charsets.US_ASCII);
  private static final int SETNX_VERSION = parseVersion("0.07");

  // Set the value of a key, only if the key does not exist
  public IntegerReply setnx(Object key0, Object value1) throws RedisException {
    if (version < SETNX_VERSION) throw new RedisException("Server does not support SETNX");
    return (IntegerReply) execute(SETNX, SETNX_BYTES, key0, value1);
  }
  
  private static final String SETRANGE = "SETRANGE";
  private static final byte[] SETRANGE_BYTES = SETRANGE.getBytes(Charsets.US_ASCII);
  private static final int SETRANGE_VERSION = parseVersion("2.1.8");

  // Overwrite part of a string at key starting at the specified offset
  public IntegerReply setrange(Object key0, Object offset1, Object value2) throws RedisException {
    if (version < SETRANGE_VERSION) throw new RedisException("Server does not support SETRANGE");
    return (IntegerReply) execute(SETRANGE, SETRANGE_BYTES, key0, offset1, value2);
  }
  
  private static final String SHUTDOWN = "SHUTDOWN";
  private static final byte[] SHUTDOWN_BYTES = SHUTDOWN.getBytes(Charsets.US_ASCII);
  private static final int SHUTDOWN_VERSION = parseVersion("0.07");

  // Synchronously save the dataset to disk and then shut down the server
  public StatusReply shutdown() throws RedisException {
    if (version < SHUTDOWN_VERSION) throw new RedisException("Server does not support SHUTDOWN");
    return (StatusReply) execute(SHUTDOWN, SHUTDOWN_BYTES);
  }
  
  private static final String SINTER = "SINTER";
  private static final byte[] SINTER_BYTES = SINTER.getBytes(Charsets.US_ASCII);
  private static final int SINTER_VERSION = parseVersion("0.07");

  // Intersect multiple sets
  public MultiBulkReply sinter(Object... key0) throws RedisException {
    if (version < SINTER_VERSION) throw new RedisException("Server does not support SINTER");
    return (MultiBulkReply) execute(SINTER, SINTER_BYTES, key0);
  }
  
  private static final String SINTERSTORE = "SINTERSTORE";
  private static final byte[] SINTERSTORE_BYTES = SINTERSTORE.getBytes(Charsets.US_ASCII);
  private static final int SINTERSTORE_VERSION = parseVersion("0.07");

  // Intersect multiple sets and store the resulting set in a key
  public IntegerReply sinterstore(Object destination0, Object... key1) throws RedisException {
    if (version < SINTERSTORE_VERSION) throw new RedisException("Server does not support SINTERSTORE");
    return (IntegerReply) execute(SINTERSTORE, SINTERSTORE_BYTES, destination0, key1);
  }
  
  private static final String SISMEMBER = "SISMEMBER";
  private static final byte[] SISMEMBER_BYTES = SISMEMBER.getBytes(Charsets.US_ASCII);
  private static final int SISMEMBER_VERSION = parseVersion("0.07");

  // Determine if a given value is a member of a set
  public IntegerReply sismember(Object key0, Object member1) throws RedisException {
    if (version < SISMEMBER_VERSION) throw new RedisException("Server does not support SISMEMBER");
    return (IntegerReply) execute(SISMEMBER, SISMEMBER_BYTES, key0, member1);
  }
  
  private static final String SLAVEOF = "SLAVEOF";
  private static final byte[] SLAVEOF_BYTES = SLAVEOF.getBytes(Charsets.US_ASCII);
  private static final int SLAVEOF_VERSION = parseVersion("0.100");

  // Make the server a slave of another instance, or promote it as master
  public StatusReply slaveof(Object host0, Object port1) throws RedisException {
    if (version < SLAVEOF_VERSION) throw new RedisException("Server does not support SLAVEOF");
    return (StatusReply) execute(SLAVEOF, SLAVEOF_BYTES, host0, port1);
  }
  
  private static final String SLOWLOG = "SLOWLOG";
  private static final byte[] SLOWLOG_BYTES = SLOWLOG.getBytes(Charsets.US_ASCII);
  private static final int SLOWLOG_VERSION = parseVersion("2.2.12");

  // Manages the Redis slow queries log
  public Reply slowlog(Object subcommand0, Object argument1) throws RedisException {
    if (version < SLOWLOG_VERSION) throw new RedisException("Server does not support SLOWLOG");
    return (Reply) execute(SLOWLOG, SLOWLOG_BYTES, subcommand0, argument1);
  }
  
  private static final String SMEMBERS = "SMEMBERS";
  private static final byte[] SMEMBERS_BYTES = SMEMBERS.getBytes(Charsets.US_ASCII);
  private static final int SMEMBERS_VERSION = parseVersion("0.07");

  // Get all the members in a set
  public MultiBulkReply smembers(Object key0) throws RedisException {
    if (version < SMEMBERS_VERSION) throw new RedisException("Server does not support SMEMBERS");
    return (MultiBulkReply) execute(SMEMBERS, SMEMBERS_BYTES, key0);
  }
  
  private static final String SMOVE = "SMOVE";
  private static final byte[] SMOVE_BYTES = SMOVE.getBytes(Charsets.US_ASCII);
  private static final int SMOVE_VERSION = parseVersion("0.091");

  // Move a member from one set to another
  public IntegerReply smove(Object source0, Object destination1, Object member2) throws RedisException {
    if (version < SMOVE_VERSION) throw new RedisException("Server does not support SMOVE");
    return (IntegerReply) execute(SMOVE, SMOVE_BYTES, source0, destination1, member2);
  }
  
  private static final String SORT = "SORT";
  private static final byte[] SORT_BYTES = SORT.getBytes(Charsets.US_ASCII);
  private static final int SORT_VERSION = parseVersion("0.07");

  // Sort the elements in a list, set or sorted set
  public MultiBulkReply sort(Object key0, Object pattern1, Object offset_or_count2, Object... pattern3) throws RedisException {
    if (version < SORT_VERSION) throw new RedisException("Server does not support SORT");
    return (MultiBulkReply) execute(SORT, SORT_BYTES, key0, pattern1, offset_or_count2, pattern3);
  }
  
  private static final String SPOP = "SPOP";
  private static final byte[] SPOP_BYTES = SPOP.getBytes(Charsets.US_ASCII);
  private static final int SPOP_VERSION = parseVersion("0.101");

  // Remove and return a random member from a set
  public BulkReply spop(Object key0) throws RedisException {
    if (version < SPOP_VERSION) throw new RedisException("Server does not support SPOP");
    return (BulkReply) execute(SPOP, SPOP_BYTES, key0);
  }
  
  private static final String SRANDMEMBER = "SRANDMEMBER";
  private static final byte[] SRANDMEMBER_BYTES = SRANDMEMBER.getBytes(Charsets.US_ASCII);
  private static final int SRANDMEMBER_VERSION = parseVersion("1.001");

  // Get a random member from a set
  public BulkReply srandmember(Object key0) throws RedisException {
    if (version < SRANDMEMBER_VERSION) throw new RedisException("Server does not support SRANDMEMBER");
    return (BulkReply) execute(SRANDMEMBER, SRANDMEMBER_BYTES, key0);
  }
  
  private static final String SREM = "SREM";
  private static final byte[] SREM_BYTES = SREM.getBytes(Charsets.US_ASCII);
  private static final int SREM_VERSION = parseVersion("0.07");

  // Remove one or more members from a set
  public IntegerReply srem(Object key0, Object... member1) throws RedisException {
    if (version < SREM_VERSION) throw new RedisException("Server does not support SREM");
    return (IntegerReply) execute(SREM, SREM_BYTES, key0, member1);
  }
  
  private static final String STRLEN = "STRLEN";
  private static final byte[] STRLEN_BYTES = STRLEN.getBytes(Charsets.US_ASCII);
  private static final int STRLEN_VERSION = parseVersion("2.1.2");

  // Get the length of the value stored in a key
  public IntegerReply strlen(Object key0) throws RedisException {
    if (version < STRLEN_VERSION) throw new RedisException("Server does not support STRLEN");
    return (IntegerReply) execute(STRLEN, STRLEN_BYTES, key0);
  }
  
  private static final String SUBSCRIBE = "SUBSCRIBE";
  private static final byte[] SUBSCRIBE_BYTES = SUBSCRIBE.getBytes(Charsets.US_ASCII);
  private static final int SUBSCRIBE_VERSION = parseVersion("1.3.8");

  // Listen for messages published to the given channels
  public Reply subscribe(Object... channel0) throws RedisException {
    if (version < SUBSCRIBE_VERSION) throw new RedisException("Server does not support SUBSCRIBE");
    return (Reply) execute(SUBSCRIBE, SUBSCRIBE_BYTES, channel0);
  }
  
  private static final String SUNION = "SUNION";
  private static final byte[] SUNION_BYTES = SUNION.getBytes(Charsets.US_ASCII);
  private static final int SUNION_VERSION = parseVersion("0.091");

  // Add multiple sets
  public MultiBulkReply sunion(Object... key0) throws RedisException {
    if (version < SUNION_VERSION) throw new RedisException("Server does not support SUNION");
    return (MultiBulkReply) execute(SUNION, SUNION_BYTES, key0);
  }
  
  private static final String SUNIONSTORE = "SUNIONSTORE";
  private static final byte[] SUNIONSTORE_BYTES = SUNIONSTORE.getBytes(Charsets.US_ASCII);
  private static final int SUNIONSTORE_VERSION = parseVersion("0.091");

  // Add multiple sets and store the resulting set in a key
  public IntegerReply sunionstore(Object destination0, Object... key1) throws RedisException {
    if (version < SUNIONSTORE_VERSION) throw new RedisException("Server does not support SUNIONSTORE");
    return (IntegerReply) execute(SUNIONSTORE, SUNIONSTORE_BYTES, destination0, key1);
  }
  
  private static final String SYNC = "SYNC";
  private static final byte[] SYNC_BYTES = SYNC.getBytes(Charsets.US_ASCII);
  private static final int SYNC_VERSION = parseVersion("0.07");

  // Internal command used for replication
  public Reply sync() throws RedisException {
    if (version < SYNC_VERSION) throw new RedisException("Server does not support SYNC");
    return (Reply) execute(SYNC, SYNC_BYTES);
  }
  
  private static final String TTL = "TTL";
  private static final byte[] TTL_BYTES = TTL.getBytes(Charsets.US_ASCII);
  private static final int TTL_VERSION = parseVersion("0.100");

  // Get the time to live for a key
  public IntegerReply ttl(Object key0) throws RedisException {
    if (version < TTL_VERSION) throw new RedisException("Server does not support TTL");
    return (IntegerReply) execute(TTL, TTL_BYTES, key0);
  }
  
  private static final String TYPE = "TYPE";
  private static final byte[] TYPE_BYTES = TYPE.getBytes(Charsets.US_ASCII);
  private static final int TYPE_VERSION = parseVersion("0.07");

  // Determine the type stored at key
  public StatusReply type(Object key0) throws RedisException {
    if (version < TYPE_VERSION) throw new RedisException("Server does not support TYPE");
    return (StatusReply) execute(TYPE, TYPE_BYTES, key0);
  }
  
  private static final String UNSUBSCRIBE = "UNSUBSCRIBE";
  private static final byte[] UNSUBSCRIBE_BYTES = UNSUBSCRIBE.getBytes(Charsets.US_ASCII);
  private static final int UNSUBSCRIBE_VERSION = parseVersion("1.3.8");

  // Stop listening for messages posted to the given channels
  public Reply unsubscribe(Object... channel0) throws RedisException {
    if (version < UNSUBSCRIBE_VERSION) throw new RedisException("Server does not support UNSUBSCRIBE");
    return (Reply) execute(UNSUBSCRIBE, UNSUBSCRIBE_BYTES, channel0);
  }
  
  private static final String UNWATCH = "UNWATCH";
  private static final byte[] UNWATCH_BYTES = UNWATCH.getBytes(Charsets.US_ASCII);
  private static final int UNWATCH_VERSION = parseVersion("2.1.0");

  // Forget about all watched keys
  public StatusReply unwatch() throws RedisException {
    if (version < UNWATCH_VERSION) throw new RedisException("Server does not support UNWATCH");
    return (StatusReply) execute(UNWATCH, UNWATCH_BYTES);
  }
  
  private static final String WATCH = "WATCH";
  private static final byte[] WATCH_BYTES = WATCH.getBytes(Charsets.US_ASCII);
  private static final int WATCH_VERSION = parseVersion("2.1.0");

  // Watch the given keys to determine execution of the MULTI/EXEC block
  public StatusReply watch(Object... key0) throws RedisException {
    if (version < WATCH_VERSION) throw new RedisException("Server does not support WATCH");
    return (StatusReply) execute(WATCH, WATCH_BYTES, key0);
  }
  
  private static final String ZADD = "ZADD";
  private static final byte[] ZADD_BYTES = ZADD.getBytes(Charsets.US_ASCII);
  private static final int ZADD_VERSION = parseVersion("1.1");

  // Add one or more members to a sorted set, or update its score if it already exists
  public IntegerReply zadd(Object key0, Object score1, Object member2, Object score3, Object member4) throws RedisException {
    if (version < ZADD_VERSION) throw new RedisException("Server does not support ZADD");
    return (IntegerReply) execute(ZADD, ZADD_BYTES, key0, score1, member2, score3, member4);
  }
  
  private static final String ZCARD = "ZCARD";
  private static final byte[] ZCARD_BYTES = ZCARD.getBytes(Charsets.US_ASCII);
  private static final int ZCARD_VERSION = parseVersion("1.1");

  // Get the number of members in a sorted set
  public IntegerReply zcard(Object key0) throws RedisException {
    if (version < ZCARD_VERSION) throw new RedisException("Server does not support ZCARD");
    return (IntegerReply) execute(ZCARD, ZCARD_BYTES, key0);
  }
  
  private static final String ZCOUNT = "ZCOUNT";
  private static final byte[] ZCOUNT_BYTES = ZCOUNT.getBytes(Charsets.US_ASCII);
  private static final int ZCOUNT_VERSION = parseVersion("1.3.3");

  // Count the members in a sorted set with scores within the given values
  public IntegerReply zcount(Object key0, Object min1, Object max2) throws RedisException {
    if (version < ZCOUNT_VERSION) throw new RedisException("Server does not support ZCOUNT");
    return (IntegerReply) execute(ZCOUNT, ZCOUNT_BYTES, key0, min1, max2);
  }
  
  private static final String ZINCRBY = "ZINCRBY";
  private static final byte[] ZINCRBY_BYTES = ZINCRBY.getBytes(Charsets.US_ASCII);
  private static final int ZINCRBY_VERSION = parseVersion("1.1");

  // Increment the score of a member in a sorted set
  public BulkReply zincrby(Object key0, Object increment1, Object member2) throws RedisException {
    if (version < ZINCRBY_VERSION) throw new RedisException("Server does not support ZINCRBY");
    return (BulkReply) execute(ZINCRBY, ZINCRBY_BYTES, key0, increment1, member2);
  }
  
  private static final String ZINTERSTORE = "ZINTERSTORE";
  private static final byte[] ZINTERSTORE_BYTES = ZINTERSTORE.getBytes(Charsets.US_ASCII);
  private static final int ZINTERSTORE_VERSION = parseVersion("1.3.10");

  // Intersect multiple sorted sets and store the resulting sorted set in a new key
  public IntegerReply zinterstore(Object destination0, Object numkeys1, Object... key2) throws RedisException {
    if (version < ZINTERSTORE_VERSION) throw new RedisException("Server does not support ZINTERSTORE");
    return (IntegerReply) execute(ZINTERSTORE, ZINTERSTORE_BYTES, destination0, numkeys1, key2);
  }
  
  private static final String ZRANGE = "ZRANGE";
  private static final byte[] ZRANGE_BYTES = ZRANGE.getBytes(Charsets.US_ASCII);
  private static final int ZRANGE_VERSION = parseVersion("1.1");

  // Return a range of members in a sorted set, by index
  public MultiBulkReply zrange(Object key0, Object start1, Object stop2, Object withscores3) throws RedisException {
    if (version < ZRANGE_VERSION) throw new RedisException("Server does not support ZRANGE");
    return (MultiBulkReply) execute(ZRANGE, ZRANGE_BYTES, key0, start1, stop2, withscores3);
  }
  
  private static final String ZRANGEBYSCORE = "ZRANGEBYSCORE";
  private static final byte[] ZRANGEBYSCORE_BYTES = ZRANGEBYSCORE.getBytes(Charsets.US_ASCII);
  private static final int ZRANGEBYSCORE_VERSION = parseVersion("1.050");

  // Return a range of members in a sorted set, by score
  public MultiBulkReply zrangebyscore(Object key0, Object min1, Object max2, Object withscores3, Object offset_or_count4) throws RedisException {
    if (version < ZRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZRANGEBYSCORE");
    return (MultiBulkReply) execute(ZRANGEBYSCORE, ZRANGEBYSCORE_BYTES, key0, min1, max2, withscores3, offset_or_count4);
  }
  
  private static final String ZRANK = "ZRANK";
  private static final byte[] ZRANK_BYTES = ZRANK.getBytes(Charsets.US_ASCII);
  private static final int ZRANK_VERSION = parseVersion("1.3.4");

  // Determine the index of a member in a sorted set
  public IntegerReply zrank(Object key0, Object member1) throws RedisException {
    if (version < ZRANK_VERSION) throw new RedisException("Server does not support ZRANK");
    return (IntegerReply) execute(ZRANK, ZRANK_BYTES, key0, member1);
  }
  
  private static final String ZREM = "ZREM";
  private static final byte[] ZREM_BYTES = ZREM.getBytes(Charsets.US_ASCII);
  private static final int ZREM_VERSION = parseVersion("1.1");

  // Remove one or more members from a sorted set
  public IntegerReply zrem(Object key0, Object... member1) throws RedisException {
    if (version < ZREM_VERSION) throw new RedisException("Server does not support ZREM");
    return (IntegerReply) execute(ZREM, ZREM_BYTES, key0, member1);
  }
  
  private static final String ZREMRANGEBYRANK = "ZREMRANGEBYRANK";
  private static final byte[] ZREMRANGEBYRANK_BYTES = ZREMRANGEBYRANK.getBytes(Charsets.US_ASCII);
  private static final int ZREMRANGEBYRANK_VERSION = parseVersion("1.3.4");

  // Remove all members in a sorted set within the given indexes
  public IntegerReply zremrangebyrank(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < ZREMRANGEBYRANK_VERSION) throw new RedisException("Server does not support ZREMRANGEBYRANK");
    return (IntegerReply) execute(ZREMRANGEBYRANK, ZREMRANGEBYRANK_BYTES, key0, start1, stop2);
  }
  
  private static final String ZREMRANGEBYSCORE = "ZREMRANGEBYSCORE";
  private static final byte[] ZREMRANGEBYSCORE_BYTES = ZREMRANGEBYSCORE.getBytes(Charsets.US_ASCII);
  private static final int ZREMRANGEBYSCORE_VERSION = parseVersion("1.1");

  // Remove all members in a sorted set within the given scores
  public IntegerReply zremrangebyscore(Object key0, Object min1, Object max2) throws RedisException {
    if (version < ZREMRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREMRANGEBYSCORE");
    return (IntegerReply) execute(ZREMRANGEBYSCORE, ZREMRANGEBYSCORE_BYTES, key0, min1, max2);
  }
  
  private static final String ZREVRANGE = "ZREVRANGE";
  private static final byte[] ZREVRANGE_BYTES = ZREVRANGE.getBytes(Charsets.US_ASCII);
  private static final int ZREVRANGE_VERSION = parseVersion("1.1");

  // Return a range of members in a sorted set, by index, with scores ordered from high to low
  public MultiBulkReply zrevrange(Object key0, Object start1, Object stop2, Object withscores3) throws RedisException {
    if (version < ZREVRANGE_VERSION) throw new RedisException("Server does not support ZREVRANGE");
    return (MultiBulkReply) execute(ZREVRANGE, ZREVRANGE_BYTES, key0, start1, stop2, withscores3);
  }
  
  private static final String ZREVRANGEBYSCORE = "ZREVRANGEBYSCORE";
  private static final byte[] ZREVRANGEBYSCORE_BYTES = ZREVRANGEBYSCORE.getBytes(Charsets.US_ASCII);
  private static final int ZREVRANGEBYSCORE_VERSION = parseVersion("2.1.6");

  // Return a range of members in a sorted set, by score, with scores ordered from high to low
  public MultiBulkReply zrevrangebyscore(Object key0, Object max1, Object min2, Object withscores3, Object offset_or_count4) throws RedisException {
    if (version < ZREVRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREVRANGEBYSCORE");
    return (MultiBulkReply) execute(ZREVRANGEBYSCORE, ZREVRANGEBYSCORE_BYTES, key0, max1, min2, withscores3, offset_or_count4);
  }
  
  private static final String ZREVRANK = "ZREVRANK";
  private static final byte[] ZREVRANK_BYTES = ZREVRANK.getBytes(Charsets.US_ASCII);
  private static final int ZREVRANK_VERSION = parseVersion("1.3.4");

  // Determine the index of a member in a sorted set, with scores ordered from high to low
  public IntegerReply zrevrank(Object key0, Object member1) throws RedisException {
    if (version < ZREVRANK_VERSION) throw new RedisException("Server does not support ZREVRANK");
    return (IntegerReply) execute(ZREVRANK, ZREVRANK_BYTES, key0, member1);
  }
  
  private static final String ZSCORE = "ZSCORE";
  private static final byte[] ZSCORE_BYTES = ZSCORE.getBytes(Charsets.US_ASCII);
  private static final int ZSCORE_VERSION = parseVersion("1.1");

  // Get the score associated with the given member in a sorted set
  public BulkReply zscore(Object key0, Object member1) throws RedisException {
    if (version < ZSCORE_VERSION) throw new RedisException("Server does not support ZSCORE");
    return (BulkReply) execute(ZSCORE, ZSCORE_BYTES, key0, member1);
  }
  
  private static final String ZUNIONSTORE = "ZUNIONSTORE";
  private static final byte[] ZUNIONSTORE_BYTES = ZUNIONSTORE.getBytes(Charsets.US_ASCII);
  private static final int ZUNIONSTORE_VERSION = parseVersion("1.3.10");

  // Add multiple sorted sets and store the resulting sorted set in a new key
  public IntegerReply zunionstore(Object destination0, Object numkeys1, Object... key2) throws RedisException {
    if (version < ZUNIONSTORE_VERSION) throw new RedisException("Server does not support ZUNIONSTORE");
    return (IntegerReply) execute(ZUNIONSTORE, ZUNIONSTORE_BYTES, destination0, numkeys1, key2);
  }
  
  private static final String EVAL = "EVAL";
  private static final byte[] EVAL_BYTES = EVAL.getBytes(Charsets.US_ASCII);
  private static final int EVAL_VERSION = parseVersion("2.6.0");

  // Execute a Lua script server side
  public Reply eval(Object script0, Object numkeys1, Object... key2) throws RedisException {
    if (version < EVAL_VERSION) throw new RedisException("Server does not support EVAL");
    return (Reply) execute(EVAL, EVAL_BYTES, script0, numkeys1, key2);
  }

  public class Pipeline {

  // Append a value to a key
  public ListenableFuture<IntegerReply> append(Object key0, Object value1) throws RedisException {
    if (version < APPEND_VERSION) throw new RedisException("Server does not support APPEND");
    return (ListenableFuture<IntegerReply>) pipeline(APPEND, APPEND_BYTES, key0, value1);
  }

  // Authenticate to the server
  public ListenableFuture<StatusReply> auth(Object password0) throws RedisException {
    if (version < AUTH_VERSION) throw new RedisException("Server does not support AUTH");
    return (ListenableFuture<StatusReply>) pipeline(AUTH, AUTH_BYTES, password0);
  }

  // Asynchronously rewrite the append-only file
  public ListenableFuture<StatusReply> bgrewriteaof() throws RedisException {
    if (version < BGREWRITEAOF_VERSION) throw new RedisException("Server does not support BGREWRITEAOF");
    return (ListenableFuture<StatusReply>) pipeline(BGREWRITEAOF, BGREWRITEAOF_BYTES);
  }

  // Asynchronously save the dataset to disk
  public ListenableFuture<StatusReply> bgsave() throws RedisException {
    if (version < BGSAVE_VERSION) throw new RedisException("Server does not support BGSAVE");
    return (ListenableFuture<StatusReply>) pipeline(BGSAVE, BGSAVE_BYTES);
  }

  // Remove and get the first element in a list, or block until one is available
  public ListenableFuture<MultiBulkReply> blpop(Object... key0) throws RedisException {
    if (version < BLPOP_VERSION) throw new RedisException("Server does not support BLPOP");
    return (ListenableFuture<MultiBulkReply>) pipeline(BLPOP, BLPOP_BYTES, key0);
  }

  // Remove and get the last element in a list, or block until one is available
  public ListenableFuture<MultiBulkReply> brpop(Object... key0) throws RedisException {
    if (version < BRPOP_VERSION) throw new RedisException("Server does not support BRPOP");
    return (ListenableFuture<MultiBulkReply>) pipeline(BRPOP, BRPOP_BYTES, key0);
  }

  // Pop a value from a list, push it to another list and return it; or block until one is available
  public ListenableFuture<BulkReply> brpoplpush(Object source0, Object destination1, Object timeout2) throws RedisException {
    if (version < BRPOPLPUSH_VERSION) throw new RedisException("Server does not support BRPOPLPUSH");
    return (ListenableFuture<BulkReply>) pipeline(BRPOPLPUSH, BRPOPLPUSH_BYTES, source0, destination1, timeout2);
  }

  // Get the value of a configuration parameter
  public ListenableFuture<Reply> config_get(Object parameter0) throws RedisException {
    if (version < CONFIG_GET_VERSION) throw new RedisException("Server does not support CONFIG_GET");
    return (ListenableFuture<Reply>) pipeline(CONFIG_GET, CONFIG_GET_BYTES, parameter0);
  }

  // Set a configuration parameter to the given value
  public ListenableFuture<Reply> config_set(Object parameter0, Object value1) throws RedisException {
    if (version < CONFIG_SET_VERSION) throw new RedisException("Server does not support CONFIG_SET");
    return (ListenableFuture<Reply>) pipeline(CONFIG_SET, CONFIG_SET_BYTES, parameter0, value1);
  }

  // Reset the stats returned by INFO
  public ListenableFuture<Reply> config_resetstat() throws RedisException {
    if (version < CONFIG_RESETSTAT_VERSION) throw new RedisException("Server does not support CONFIG_RESETSTAT");
    return (ListenableFuture<Reply>) pipeline(CONFIG_RESETSTAT, CONFIG_RESETSTAT_BYTES);
  }

  // Return the number of keys in the selected database
  public ListenableFuture<IntegerReply> dbsize() throws RedisException {
    if (version < DBSIZE_VERSION) throw new RedisException("Server does not support DBSIZE");
    return (ListenableFuture<IntegerReply>) pipeline(DBSIZE, DBSIZE_BYTES);
  }

  // Get debugging information about a key
  public ListenableFuture<Reply> debug_object(Object key0) throws RedisException {
    if (version < DEBUG_OBJECT_VERSION) throw new RedisException("Server does not support DEBUG_OBJECT");
    return (ListenableFuture<Reply>) pipeline(DEBUG_OBJECT, DEBUG_OBJECT_BYTES, key0);
  }

  // Make the server crash
  public ListenableFuture<Reply> debug_segfault() throws RedisException {
    if (version < DEBUG_SEGFAULT_VERSION) throw new RedisException("Server does not support DEBUG_SEGFAULT");
    return (ListenableFuture<Reply>) pipeline(DEBUG_SEGFAULT, DEBUG_SEGFAULT_BYTES);
  }

  // Decrement the integer value of a key by one
  public ListenableFuture<IntegerReply> decr(Object key0) throws RedisException {
    if (version < DECR_VERSION) throw new RedisException("Server does not support DECR");
    return (ListenableFuture<IntegerReply>) pipeline(DECR, DECR_BYTES, key0);
  }

  // Decrement the integer value of a key by the given number
  public ListenableFuture<IntegerReply> decrby(Object key0, Object decrement1) throws RedisException {
    if (version < DECRBY_VERSION) throw new RedisException("Server does not support DECRBY");
    return (ListenableFuture<IntegerReply>) pipeline(DECRBY, DECRBY_BYTES, key0, decrement1);
  }

  // Delete a key
  public ListenableFuture<IntegerReply> del(Object... key0) throws RedisException {
    if (version < DEL_VERSION) throw new RedisException("Server does not support DEL");
    return (ListenableFuture<IntegerReply>) pipeline(DEL, DEL_BYTES, key0);
  }

  // Discard all commands issued after MULTI
  public ListenableFuture<StatusReply> discard() throws RedisException {
    if (version < DISCARD_VERSION) throw new RedisException("Server does not support DISCARD");
    return (ListenableFuture<StatusReply>) pipeline(DISCARD, DISCARD_BYTES);
  }

  // Echo the given string
  public ListenableFuture<BulkReply> echo(Object message0) throws RedisException {
    if (version < ECHO_VERSION) throw new RedisException("Server does not support ECHO");
    return (ListenableFuture<BulkReply>) pipeline(ECHO, ECHO_BYTES, message0);
  }

  // Execute all commands issued after MULTI
  public ListenableFuture<MultiBulkReply> exec() throws RedisException {
    if (version < EXEC_VERSION) throw new RedisException("Server does not support EXEC");
    return (ListenableFuture<MultiBulkReply>) pipeline(EXEC, EXEC_BYTES);
  }

  // Determine if a key exists
  public ListenableFuture<IntegerReply> exists(Object key0) throws RedisException {
    if (version < EXISTS_VERSION) throw new RedisException("Server does not support EXISTS");
    return (ListenableFuture<IntegerReply>) pipeline(EXISTS, EXISTS_BYTES, key0);
  }

  // Set a key's time to live in seconds
  public ListenableFuture<IntegerReply> expire(Object key0, Object seconds1) throws RedisException {
    if (version < EXPIRE_VERSION) throw new RedisException("Server does not support EXPIRE");
    return (ListenableFuture<IntegerReply>) pipeline(EXPIRE, EXPIRE_BYTES, key0, seconds1);
  }

  // Set the expiration for a key as a UNIX timestamp
  public ListenableFuture<IntegerReply> expireat(Object key0, Object timestamp1) throws RedisException {
    if (version < EXPIREAT_VERSION) throw new RedisException("Server does not support EXPIREAT");
    return (ListenableFuture<IntegerReply>) pipeline(EXPIREAT, EXPIREAT_BYTES, key0, timestamp1);
  }

  // Remove all keys from all databases
  public ListenableFuture<StatusReply> flushall() throws RedisException {
    if (version < FLUSHALL_VERSION) throw new RedisException("Server does not support FLUSHALL");
    return (ListenableFuture<StatusReply>) pipeline(FLUSHALL, FLUSHALL_BYTES);
  }

  // Remove all keys from the current database
  public ListenableFuture<StatusReply> flushdb() throws RedisException {
    if (version < FLUSHDB_VERSION) throw new RedisException("Server does not support FLUSHDB");
    return (ListenableFuture<StatusReply>) pipeline(FLUSHDB, FLUSHDB_BYTES);
  }

  // Get the value of a key
  public ListenableFuture<BulkReply> get(Object key0) throws RedisException {
    if (version < GET_VERSION) throw new RedisException("Server does not support GET");
    return (ListenableFuture<BulkReply>) pipeline(GET, GET_BYTES, key0);
  }

  // Returns the bit value at offset in the string value stored at key
  public ListenableFuture<IntegerReply> getbit(Object key0, Object offset1) throws RedisException {
    if (version < GETBIT_VERSION) throw new RedisException("Server does not support GETBIT");
    return (ListenableFuture<IntegerReply>) pipeline(GETBIT, GETBIT_BYTES, key0, offset1);
  }

  // Get a substring of the string stored at a key
  public ListenableFuture<BulkReply> getrange(Object key0, Object start1, Object end2) throws RedisException {
    if (version < GETRANGE_VERSION) throw new RedisException("Server does not support GETRANGE");
    return (ListenableFuture<BulkReply>) pipeline(GETRANGE, GETRANGE_BYTES, key0, start1, end2);
  }

  // Set the string value of a key and return its old value
  public ListenableFuture<BulkReply> getset(Object key0, Object value1) throws RedisException {
    if (version < GETSET_VERSION) throw new RedisException("Server does not support GETSET");
    return (ListenableFuture<BulkReply>) pipeline(GETSET, GETSET_BYTES, key0, value1);
  }

  // Delete one or more hash fields
  public ListenableFuture<IntegerReply> hdel(Object key0, Object... field1) throws RedisException {
    if (version < HDEL_VERSION) throw new RedisException("Server does not support HDEL");
    return (ListenableFuture<IntegerReply>) pipeline(HDEL, HDEL_BYTES, key0, field1);
  }

  // Determine if a hash field exists
  public ListenableFuture<IntegerReply> hexists(Object key0, Object field1) throws RedisException {
    if (version < HEXISTS_VERSION) throw new RedisException("Server does not support HEXISTS");
    return (ListenableFuture<IntegerReply>) pipeline(HEXISTS, HEXISTS_BYTES, key0, field1);
  }

  // Get the value of a hash field
  public ListenableFuture<BulkReply> hget(Object key0, Object field1) throws RedisException {
    if (version < HGET_VERSION) throw new RedisException("Server does not support HGET");
    return (ListenableFuture<BulkReply>) pipeline(HGET, HGET_BYTES, key0, field1);
  }

  // Get all the fields and values in a hash
  public ListenableFuture<MultiBulkReply> hgetall(Object key0) throws RedisException {
    if (version < HGETALL_VERSION) throw new RedisException("Server does not support HGETALL");
    return (ListenableFuture<MultiBulkReply>) pipeline(HGETALL, HGETALL_BYTES, key0);
  }

  // Increment the integer value of a hash field by the given number
  public ListenableFuture<IntegerReply> hincrby(Object key0, Object field1, Object increment2) throws RedisException {
    if (version < HINCRBY_VERSION) throw new RedisException("Server does not support HINCRBY");
    return (ListenableFuture<IntegerReply>) pipeline(HINCRBY, HINCRBY_BYTES, key0, field1, increment2);
  }

  // Get all the fields in a hash
  public ListenableFuture<MultiBulkReply> hkeys(Object key0) throws RedisException {
    if (version < HKEYS_VERSION) throw new RedisException("Server does not support HKEYS");
    return (ListenableFuture<MultiBulkReply>) pipeline(HKEYS, HKEYS_BYTES, key0);
  }

  // Get the number of fields in a hash
  public ListenableFuture<IntegerReply> hlen(Object key0) throws RedisException {
    if (version < HLEN_VERSION) throw new RedisException("Server does not support HLEN");
    return (ListenableFuture<IntegerReply>) pipeline(HLEN, HLEN_BYTES, key0);
  }

  // Get the values of all the given hash fields
  public ListenableFuture<MultiBulkReply> hmget(Object key0, Object... field1) throws RedisException {
    if (version < HMGET_VERSION) throw new RedisException("Server does not support HMGET");
    return (ListenableFuture<MultiBulkReply>) pipeline(HMGET, HMGET_BYTES, key0, field1);
  }

  // Set multiple hash fields to multiple values
  public ListenableFuture<StatusReply> hmset(Object key0, Object... field_or_value1) throws RedisException {
    if (version < HMSET_VERSION) throw new RedisException("Server does not support HMSET");
    return (ListenableFuture<StatusReply>) pipeline(HMSET, HMSET_BYTES, key0, field_or_value1);
  }

  // Set the string value of a hash field
  public ListenableFuture<IntegerReply> hset(Object key0, Object field1, Object value2) throws RedisException {
    if (version < HSET_VERSION) throw new RedisException("Server does not support HSET");
    return (ListenableFuture<IntegerReply>) pipeline(HSET, HSET_BYTES, key0, field1, value2);
  }

  // Set the value of a hash field, only if the field does not exist
  public ListenableFuture<IntegerReply> hsetnx(Object key0, Object field1, Object value2) throws RedisException {
    if (version < HSETNX_VERSION) throw new RedisException("Server does not support HSETNX");
    return (ListenableFuture<IntegerReply>) pipeline(HSETNX, HSETNX_BYTES, key0, field1, value2);
  }

  // Get all the values in a hash
  public ListenableFuture<MultiBulkReply> hvals(Object key0) throws RedisException {
    if (version < HVALS_VERSION) throw new RedisException("Server does not support HVALS");
    return (ListenableFuture<MultiBulkReply>) pipeline(HVALS, HVALS_BYTES, key0);
  }

  // Increment the integer value of a key by one
  public ListenableFuture<IntegerReply> incr(Object key0) throws RedisException {
    if (version < INCR_VERSION) throw new RedisException("Server does not support INCR");
    return (ListenableFuture<IntegerReply>) pipeline(INCR, INCR_BYTES, key0);
  }

  // Increment the integer value of a key by the given number
  public ListenableFuture<IntegerReply> incrby(Object key0, Object increment1) throws RedisException {
    if (version < INCRBY_VERSION) throw new RedisException("Server does not support INCRBY");
    return (ListenableFuture<IntegerReply>) pipeline(INCRBY, INCRBY_BYTES, key0, increment1);
  }

  // Get information and statistics about the server
  public ListenableFuture<BulkReply> info() throws RedisException {
    if (version < INFO_VERSION) throw new RedisException("Server does not support INFO");
    return (ListenableFuture<BulkReply>) pipeline(INFO, INFO_BYTES);
  }

  // Find all keys matching the given pattern
  public ListenableFuture<MultiBulkReply> keys(Object pattern0) throws RedisException {
    if (version < KEYS_VERSION) throw new RedisException("Server does not support KEYS");
    return (ListenableFuture<MultiBulkReply>) pipeline(KEYS, KEYS_BYTES, pattern0);
  }

  // Get the UNIX time stamp of the last successful save to disk
  public ListenableFuture<IntegerReply> lastsave() throws RedisException {
    if (version < LASTSAVE_VERSION) throw new RedisException("Server does not support LASTSAVE");
    return (ListenableFuture<IntegerReply>) pipeline(LASTSAVE, LASTSAVE_BYTES);
  }

  // Get an element from a list by its index
  public ListenableFuture<BulkReply> lindex(Object key0, Object index1) throws RedisException {
    if (version < LINDEX_VERSION) throw new RedisException("Server does not support LINDEX");
    return (ListenableFuture<BulkReply>) pipeline(LINDEX, LINDEX_BYTES, key0, index1);
  }

  // Insert an element before or after another element in a list
  public ListenableFuture<IntegerReply> linsert(Object key0, Object where1, Object pivot2, Object value3) throws RedisException {
    if (version < LINSERT_VERSION) throw new RedisException("Server does not support LINSERT");
    return (ListenableFuture<IntegerReply>) pipeline(LINSERT, LINSERT_BYTES, key0, where1, pivot2, value3);
  }

  // Get the length of a list
  public ListenableFuture<IntegerReply> llen(Object key0) throws RedisException {
    if (version < LLEN_VERSION) throw new RedisException("Server does not support LLEN");
    return (ListenableFuture<IntegerReply>) pipeline(LLEN, LLEN_BYTES, key0);
  }

  // Remove and get the first element in a list
  public ListenableFuture<BulkReply> lpop(Object key0) throws RedisException {
    if (version < LPOP_VERSION) throw new RedisException("Server does not support LPOP");
    return (ListenableFuture<BulkReply>) pipeline(LPOP, LPOP_BYTES, key0);
  }

  // Prepend one or multiple values to a list
  public ListenableFuture<IntegerReply> lpush(Object key0, Object... value1) throws RedisException {
    if (version < LPUSH_VERSION) throw new RedisException("Server does not support LPUSH");
    return (ListenableFuture<IntegerReply>) pipeline(LPUSH, LPUSH_BYTES, key0, value1);
  }

  // Prepend a value to a list, only if the list exists
  public ListenableFuture<IntegerReply> lpushx(Object key0, Object value1) throws RedisException {
    if (version < LPUSHX_VERSION) throw new RedisException("Server does not support LPUSHX");
    return (ListenableFuture<IntegerReply>) pipeline(LPUSHX, LPUSHX_BYTES, key0, value1);
  }

  // Get a range of elements from a list
  public ListenableFuture<MultiBulkReply> lrange(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < LRANGE_VERSION) throw new RedisException("Server does not support LRANGE");
    return (ListenableFuture<MultiBulkReply>) pipeline(LRANGE, LRANGE_BYTES, key0, start1, stop2);
  }

  // Remove elements from a list
  public ListenableFuture<IntegerReply> lrem(Object key0, Object count1, Object value2) throws RedisException {
    if (version < LREM_VERSION) throw new RedisException("Server does not support LREM");
    return (ListenableFuture<IntegerReply>) pipeline(LREM, LREM_BYTES, key0, count1, value2);
  }

  // Set the value of an element in a list by its index
  public ListenableFuture<StatusReply> lset(Object key0, Object index1, Object value2) throws RedisException {
    if (version < LSET_VERSION) throw new RedisException("Server does not support LSET");
    return (ListenableFuture<StatusReply>) pipeline(LSET, LSET_BYTES, key0, index1, value2);
  }

  // Trim a list to the specified range
  public ListenableFuture<StatusReply> ltrim(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < LTRIM_VERSION) throw new RedisException("Server does not support LTRIM");
    return (ListenableFuture<StatusReply>) pipeline(LTRIM, LTRIM_BYTES, key0, start1, stop2);
  }

  // Get the values of all the given keys
  public ListenableFuture<MultiBulkReply> mget(Object... key0) throws RedisException {
    if (version < MGET_VERSION) throw new RedisException("Server does not support MGET");
    return (ListenableFuture<MultiBulkReply>) pipeline(MGET, MGET_BYTES, key0);
  }

  // Listen for all requests received by the server in real time
  public ListenableFuture<Reply> monitor() throws RedisException {
    if (version < MONITOR_VERSION) throw new RedisException("Server does not support MONITOR");
    return (ListenableFuture<Reply>) pipeline(MONITOR, MONITOR_BYTES);
  }

  // Move a key to another database
  public ListenableFuture<IntegerReply> move(Object key0, Object db1) throws RedisException {
    if (version < MOVE_VERSION) throw new RedisException("Server does not support MOVE");
    return (ListenableFuture<IntegerReply>) pipeline(MOVE, MOVE_BYTES, key0, db1);
  }

  // Set multiple keys to multiple values
  public ListenableFuture<StatusReply> mset(Object... key_or_value0) throws RedisException {
    if (version < MSET_VERSION) throw new RedisException("Server does not support MSET");
    return (ListenableFuture<StatusReply>) pipeline(MSET, MSET_BYTES, key_or_value0);
  }

  // Set multiple keys to multiple values, only if none of the keys exist
  public ListenableFuture<IntegerReply> msetnx(Object... key_or_value0) throws RedisException {
    if (version < MSETNX_VERSION) throw new RedisException("Server does not support MSETNX");
    return (ListenableFuture<IntegerReply>) pipeline(MSETNX, MSETNX_BYTES, key_or_value0);
  }

  // Mark the start of a transaction block
  public ListenableFuture<StatusReply> multi() throws RedisException {
    if (version < MULTI_VERSION) throw new RedisException("Server does not support MULTI");
    return (ListenableFuture<StatusReply>) pipeline(MULTI, MULTI_BYTES);
  }

  // Inspect the internals of Redis objects
  public ListenableFuture<Reply> object(Object subcommand0, Object... arguments1) throws RedisException {
    if (version < OBJECT_VERSION) throw new RedisException("Server does not support OBJECT");
    return (ListenableFuture<Reply>) pipeline(OBJECT, OBJECT_BYTES, subcommand0, arguments1);
  }

  // Remove the expiration from a key
  public ListenableFuture<IntegerReply> persist(Object key0) throws RedisException {
    if (version < PERSIST_VERSION) throw new RedisException("Server does not support PERSIST");
    return (ListenableFuture<IntegerReply>) pipeline(PERSIST, PERSIST_BYTES, key0);
  }

  // Ping the server
  public ListenableFuture<StatusReply> ping() throws RedisException {
    if (version < PING_VERSION) throw new RedisException("Server does not support PING");
    return (ListenableFuture<StatusReply>) pipeline(PING, PING_BYTES);
  }

  // Listen for messages published to channels matching the given patterns
  public ListenableFuture<Reply> psubscribe(Object... pattern0) throws RedisException {
    if (version < PSUBSCRIBE_VERSION) throw new RedisException("Server does not support PSUBSCRIBE");
    return (ListenableFuture<Reply>) pipeline(PSUBSCRIBE, PSUBSCRIBE_BYTES, pattern0);
  }

  // Post a message to a channel
  public ListenableFuture<IntegerReply> publish(Object channel0, Object message1) throws RedisException {
    if (version < PUBLISH_VERSION) throw new RedisException("Server does not support PUBLISH");
    return (ListenableFuture<IntegerReply>) pipeline(PUBLISH, PUBLISH_BYTES, channel0, message1);
  }

  // Stop listening for messages posted to channels matching the given patterns
  public ListenableFuture<Reply> punsubscribe(Object... pattern0) throws RedisException {
    if (version < PUNSUBSCRIBE_VERSION) throw new RedisException("Server does not support PUNSUBSCRIBE");
    return (ListenableFuture<Reply>) pipeline(PUNSUBSCRIBE, PUNSUBSCRIBE_BYTES, pattern0);
  }

  // Close the connection
  public ListenableFuture<StatusReply> quit() throws RedisException {
    if (version < QUIT_VERSION) throw new RedisException("Server does not support QUIT");
    return (ListenableFuture<StatusReply>) pipeline(QUIT, QUIT_BYTES);
  }

  // Return a random key from the keyspace
  public ListenableFuture<BulkReply> randomkey() throws RedisException {
    if (version < RANDOMKEY_VERSION) throw new RedisException("Server does not support RANDOMKEY");
    return (ListenableFuture<BulkReply>) pipeline(RANDOMKEY, RANDOMKEY_BYTES);
  }

  // Rename a key
  public ListenableFuture<StatusReply> rename(Object key0, Object newkey1) throws RedisException {
    if (version < RENAME_VERSION) throw new RedisException("Server does not support RENAME");
    return (ListenableFuture<StatusReply>) pipeline(RENAME, RENAME_BYTES, key0, newkey1);
  }

  // Rename a key, only if the new key does not exist
  public ListenableFuture<IntegerReply> renamenx(Object key0, Object newkey1) throws RedisException {
    if (version < RENAMENX_VERSION) throw new RedisException("Server does not support RENAMENX");
    return (ListenableFuture<IntegerReply>) pipeline(RENAMENX, RENAMENX_BYTES, key0, newkey1);
  }

  // Remove and get the last element in a list
  public ListenableFuture<BulkReply> rpop(Object key0) throws RedisException {
    if (version < RPOP_VERSION) throw new RedisException("Server does not support RPOP");
    return (ListenableFuture<BulkReply>) pipeline(RPOP, RPOP_BYTES, key0);
  }

  // Remove the last element in a list, append it to another list and return it
  public ListenableFuture<BulkReply> rpoplpush(Object source0, Object destination1) throws RedisException {
    if (version < RPOPLPUSH_VERSION) throw new RedisException("Server does not support RPOPLPUSH");
    return (ListenableFuture<BulkReply>) pipeline(RPOPLPUSH, RPOPLPUSH_BYTES, source0, destination1);
  }

  // Append one or multiple values to a list
  public ListenableFuture<IntegerReply> rpush(Object key0, Object... value1) throws RedisException {
    if (version < RPUSH_VERSION) throw new RedisException("Server does not support RPUSH");
    return (ListenableFuture<IntegerReply>) pipeline(RPUSH, RPUSH_BYTES, key0, value1);
  }

  // Append a value to a list, only if the list exists
  public ListenableFuture<IntegerReply> rpushx(Object key0, Object value1) throws RedisException {
    if (version < RPUSHX_VERSION) throw new RedisException("Server does not support RPUSHX");
    return (ListenableFuture<IntegerReply>) pipeline(RPUSHX, RPUSHX_BYTES, key0, value1);
  }

  // Add one or more members to a set
  public ListenableFuture<IntegerReply> sadd(Object key0, Object... member1) throws RedisException {
    if (version < SADD_VERSION) throw new RedisException("Server does not support SADD");
    return (ListenableFuture<IntegerReply>) pipeline(SADD, SADD_BYTES, key0, member1);
  }

  // Synchronously save the dataset to disk
  public ListenableFuture<Reply> save() throws RedisException {
    if (version < SAVE_VERSION) throw new RedisException("Server does not support SAVE");
    return (ListenableFuture<Reply>) pipeline(SAVE, SAVE_BYTES);
  }

  // Get the number of members in a set
  public ListenableFuture<IntegerReply> scard(Object key0) throws RedisException {
    if (version < SCARD_VERSION) throw new RedisException("Server does not support SCARD");
    return (ListenableFuture<IntegerReply>) pipeline(SCARD, SCARD_BYTES, key0);
  }

  // Subtract multiple sets
  public ListenableFuture<MultiBulkReply> sdiff(Object... key0) throws RedisException {
    if (version < SDIFF_VERSION) throw new RedisException("Server does not support SDIFF");
    return (ListenableFuture<MultiBulkReply>) pipeline(SDIFF, SDIFF_BYTES, key0);
  }

  // Subtract multiple sets and store the resulting set in a key
  public ListenableFuture<IntegerReply> sdiffstore(Object destination0, Object... key1) throws RedisException {
    if (version < SDIFFSTORE_VERSION) throw new RedisException("Server does not support SDIFFSTORE");
    return (ListenableFuture<IntegerReply>) pipeline(SDIFFSTORE, SDIFFSTORE_BYTES, destination0, key1);
  }

  // Change the selected database for the current connection
  public ListenableFuture<StatusReply> select(Object index0) throws RedisException {
    if (version < SELECT_VERSION) throw new RedisException("Server does not support SELECT");
    return (ListenableFuture<StatusReply>) pipeline(SELECT, SELECT_BYTES, index0);
  }

  // Set the string value of a key
  public ListenableFuture<StatusReply> set(Object key0, Object value1) throws RedisException {
    if (version < SET_VERSION) throw new RedisException("Server does not support SET");
    return (ListenableFuture<StatusReply>) pipeline(SET, SET_BYTES, key0, value1);
  }

  // Sets or clears the bit at offset in the string value stored at key
  public ListenableFuture<IntegerReply> setbit(Object key0, Object offset1, Object value2) throws RedisException {
    if (version < SETBIT_VERSION) throw new RedisException("Server does not support SETBIT");
    return (ListenableFuture<IntegerReply>) pipeline(SETBIT, SETBIT_BYTES, key0, offset1, value2);
  }

  // Set the value and expiration of a key
  public ListenableFuture<StatusReply> setex(Object key0, Object seconds1, Object value2) throws RedisException {
    if (version < SETEX_VERSION) throw new RedisException("Server does not support SETEX");
    return (ListenableFuture<StatusReply>) pipeline(SETEX, SETEX_BYTES, key0, seconds1, value2);
  }

  // Set the value of a key, only if the key does not exist
  public ListenableFuture<IntegerReply> setnx(Object key0, Object value1) throws RedisException {
    if (version < SETNX_VERSION) throw new RedisException("Server does not support SETNX");
    return (ListenableFuture<IntegerReply>) pipeline(SETNX, SETNX_BYTES, key0, value1);
  }

  // Overwrite part of a string at key starting at the specified offset
  public ListenableFuture<IntegerReply> setrange(Object key0, Object offset1, Object value2) throws RedisException {
    if (version < SETRANGE_VERSION) throw new RedisException("Server does not support SETRANGE");
    return (ListenableFuture<IntegerReply>) pipeline(SETRANGE, SETRANGE_BYTES, key0, offset1, value2);
  }

  // Synchronously save the dataset to disk and then shut down the server
  public ListenableFuture<StatusReply> shutdown() throws RedisException {
    if (version < SHUTDOWN_VERSION) throw new RedisException("Server does not support SHUTDOWN");
    return (ListenableFuture<StatusReply>) pipeline(SHUTDOWN, SHUTDOWN_BYTES);
  }

  // Intersect multiple sets
  public ListenableFuture<MultiBulkReply> sinter(Object... key0) throws RedisException {
    if (version < SINTER_VERSION) throw new RedisException("Server does not support SINTER");
    return (ListenableFuture<MultiBulkReply>) pipeline(SINTER, SINTER_BYTES, key0);
  }

  // Intersect multiple sets and store the resulting set in a key
  public ListenableFuture<IntegerReply> sinterstore(Object destination0, Object... key1) throws RedisException {
    if (version < SINTERSTORE_VERSION) throw new RedisException("Server does not support SINTERSTORE");
    return (ListenableFuture<IntegerReply>) pipeline(SINTERSTORE, SINTERSTORE_BYTES, destination0, key1);
  }

  // Determine if a given value is a member of a set
  public ListenableFuture<IntegerReply> sismember(Object key0, Object member1) throws RedisException {
    if (version < SISMEMBER_VERSION) throw new RedisException("Server does not support SISMEMBER");
    return (ListenableFuture<IntegerReply>) pipeline(SISMEMBER, SISMEMBER_BYTES, key0, member1);
  }

  // Make the server a slave of another instance, or promote it as master
  public ListenableFuture<StatusReply> slaveof(Object host0, Object port1) throws RedisException {
    if (version < SLAVEOF_VERSION) throw new RedisException("Server does not support SLAVEOF");
    return (ListenableFuture<StatusReply>) pipeline(SLAVEOF, SLAVEOF_BYTES, host0, port1);
  }

  // Manages the Redis slow queries log
  public ListenableFuture<Reply> slowlog(Object subcommand0, Object argument1) throws RedisException {
    if (version < SLOWLOG_VERSION) throw new RedisException("Server does not support SLOWLOG");
    return (ListenableFuture<Reply>) pipeline(SLOWLOG, SLOWLOG_BYTES, subcommand0, argument1);
  }

  // Get all the members in a set
  public ListenableFuture<MultiBulkReply> smembers(Object key0) throws RedisException {
    if (version < SMEMBERS_VERSION) throw new RedisException("Server does not support SMEMBERS");
    return (ListenableFuture<MultiBulkReply>) pipeline(SMEMBERS, SMEMBERS_BYTES, key0);
  }

  // Move a member from one set to another
  public ListenableFuture<IntegerReply> smove(Object source0, Object destination1, Object member2) throws RedisException {
    if (version < SMOVE_VERSION) throw new RedisException("Server does not support SMOVE");
    return (ListenableFuture<IntegerReply>) pipeline(SMOVE, SMOVE_BYTES, source0, destination1, member2);
  }

  // Sort the elements in a list, set or sorted set
  public ListenableFuture<MultiBulkReply> sort(Object key0, Object pattern1, Object offset_or_count2, Object... pattern3) throws RedisException {
    if (version < SORT_VERSION) throw new RedisException("Server does not support SORT");
    return (ListenableFuture<MultiBulkReply>) pipeline(SORT, SORT_BYTES, key0, pattern1, offset_or_count2, pattern3);
  }

  // Remove and return a random member from a set
  public ListenableFuture<BulkReply> spop(Object key0) throws RedisException {
    if (version < SPOP_VERSION) throw new RedisException("Server does not support SPOP");
    return (ListenableFuture<BulkReply>) pipeline(SPOP, SPOP_BYTES, key0);
  }

  // Get a random member from a set
  public ListenableFuture<BulkReply> srandmember(Object key0) throws RedisException {
    if (version < SRANDMEMBER_VERSION) throw new RedisException("Server does not support SRANDMEMBER");
    return (ListenableFuture<BulkReply>) pipeline(SRANDMEMBER, SRANDMEMBER_BYTES, key0);
  }

  // Remove one or more members from a set
  public ListenableFuture<IntegerReply> srem(Object key0, Object... member1) throws RedisException {
    if (version < SREM_VERSION) throw new RedisException("Server does not support SREM");
    return (ListenableFuture<IntegerReply>) pipeline(SREM, SREM_BYTES, key0, member1);
  }

  // Get the length of the value stored in a key
  public ListenableFuture<IntegerReply> strlen(Object key0) throws RedisException {
    if (version < STRLEN_VERSION) throw new RedisException("Server does not support STRLEN");
    return (ListenableFuture<IntegerReply>) pipeline(STRLEN, STRLEN_BYTES, key0);
  }

  // Listen for messages published to the given channels
  public ListenableFuture<Reply> subscribe(Object... channel0) throws RedisException {
    if (version < SUBSCRIBE_VERSION) throw new RedisException("Server does not support SUBSCRIBE");
    return (ListenableFuture<Reply>) pipeline(SUBSCRIBE, SUBSCRIBE_BYTES, channel0);
  }

  // Add multiple sets
  public ListenableFuture<MultiBulkReply> sunion(Object... key0) throws RedisException {
    if (version < SUNION_VERSION) throw new RedisException("Server does not support SUNION");
    return (ListenableFuture<MultiBulkReply>) pipeline(SUNION, SUNION_BYTES, key0);
  }

  // Add multiple sets and store the resulting set in a key
  public ListenableFuture<IntegerReply> sunionstore(Object destination0, Object... key1) throws RedisException {
    if (version < SUNIONSTORE_VERSION) throw new RedisException("Server does not support SUNIONSTORE");
    return (ListenableFuture<IntegerReply>) pipeline(SUNIONSTORE, SUNIONSTORE_BYTES, destination0, key1);
  }

  // Internal command used for replication
  public ListenableFuture<Reply> sync() throws RedisException {
    if (version < SYNC_VERSION) throw new RedisException("Server does not support SYNC");
    return (ListenableFuture<Reply>) pipeline(SYNC, SYNC_BYTES);
  }

  // Get the time to live for a key
  public ListenableFuture<IntegerReply> ttl(Object key0) throws RedisException {
    if (version < TTL_VERSION) throw new RedisException("Server does not support TTL");
    return (ListenableFuture<IntegerReply>) pipeline(TTL, TTL_BYTES, key0);
  }

  // Determine the type stored at key
  public ListenableFuture<StatusReply> type(Object key0) throws RedisException {
    if (version < TYPE_VERSION) throw new RedisException("Server does not support TYPE");
    return (ListenableFuture<StatusReply>) pipeline(TYPE, TYPE_BYTES, key0);
  }

  // Stop listening for messages posted to the given channels
  public ListenableFuture<Reply> unsubscribe(Object... channel0) throws RedisException {
    if (version < UNSUBSCRIBE_VERSION) throw new RedisException("Server does not support UNSUBSCRIBE");
    return (ListenableFuture<Reply>) pipeline(UNSUBSCRIBE, UNSUBSCRIBE_BYTES, channel0);
  }

  // Forget about all watched keys
  public ListenableFuture<StatusReply> unwatch() throws RedisException {
    if (version < UNWATCH_VERSION) throw new RedisException("Server does not support UNWATCH");
    return (ListenableFuture<StatusReply>) pipeline(UNWATCH, UNWATCH_BYTES);
  }

  // Watch the given keys to determine execution of the MULTI/EXEC block
  public ListenableFuture<StatusReply> watch(Object... key0) throws RedisException {
    if (version < WATCH_VERSION) throw new RedisException("Server does not support WATCH");
    return (ListenableFuture<StatusReply>) pipeline(WATCH, WATCH_BYTES, key0);
  }

  // Add one or more members to a sorted set, or update its score if it already exists
  public ListenableFuture<IntegerReply> zadd(Object key0, Object score1, Object member2, Object score3, Object member4) throws RedisException {
    if (version < ZADD_VERSION) throw new RedisException("Server does not support ZADD");
    return (ListenableFuture<IntegerReply>) pipeline(ZADD, ZADD_BYTES, key0, score1, member2, score3, member4);
  }

  // Get the number of members in a sorted set
  public ListenableFuture<IntegerReply> zcard(Object key0) throws RedisException {
    if (version < ZCARD_VERSION) throw new RedisException("Server does not support ZCARD");
    return (ListenableFuture<IntegerReply>) pipeline(ZCARD, ZCARD_BYTES, key0);
  }

  // Count the members in a sorted set with scores within the given values
  public ListenableFuture<IntegerReply> zcount(Object key0, Object min1, Object max2) throws RedisException {
    if (version < ZCOUNT_VERSION) throw new RedisException("Server does not support ZCOUNT");
    return (ListenableFuture<IntegerReply>) pipeline(ZCOUNT, ZCOUNT_BYTES, key0, min1, max2);
  }

  // Increment the score of a member in a sorted set
  public ListenableFuture<BulkReply> zincrby(Object key0, Object increment1, Object member2) throws RedisException {
    if (version < ZINCRBY_VERSION) throw new RedisException("Server does not support ZINCRBY");
    return (ListenableFuture<BulkReply>) pipeline(ZINCRBY, ZINCRBY_BYTES, key0, increment1, member2);
  }

  // Intersect multiple sorted sets and store the resulting sorted set in a new key
  public ListenableFuture<IntegerReply> zinterstore(Object destination0, Object numkeys1, Object... key2) throws RedisException {
    if (version < ZINTERSTORE_VERSION) throw new RedisException("Server does not support ZINTERSTORE");
    return (ListenableFuture<IntegerReply>) pipeline(ZINTERSTORE, ZINTERSTORE_BYTES, destination0, numkeys1, key2);
  }

  // Return a range of members in a sorted set, by index
  public ListenableFuture<MultiBulkReply> zrange(Object key0, Object start1, Object stop2, Object withscores3) throws RedisException {
    if (version < ZRANGE_VERSION) throw new RedisException("Server does not support ZRANGE");
    return (ListenableFuture<MultiBulkReply>) pipeline(ZRANGE, ZRANGE_BYTES, key0, start1, stop2, withscores3);
  }

  // Return a range of members in a sorted set, by score
  public ListenableFuture<MultiBulkReply> zrangebyscore(Object key0, Object min1, Object max2, Object withscores3, Object offset_or_count4) throws RedisException {
    if (version < ZRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZRANGEBYSCORE");
    return (ListenableFuture<MultiBulkReply>) pipeline(ZRANGEBYSCORE, ZRANGEBYSCORE_BYTES, key0, min1, max2, withscores3, offset_or_count4);
  }

  // Determine the index of a member in a sorted set
  public ListenableFuture<IntegerReply> zrank(Object key0, Object member1) throws RedisException {
    if (version < ZRANK_VERSION) throw new RedisException("Server does not support ZRANK");
    return (ListenableFuture<IntegerReply>) pipeline(ZRANK, ZRANK_BYTES, key0, member1);
  }

  // Remove one or more members from a sorted set
  public ListenableFuture<IntegerReply> zrem(Object key0, Object... member1) throws RedisException {
    if (version < ZREM_VERSION) throw new RedisException("Server does not support ZREM");
    return (ListenableFuture<IntegerReply>) pipeline(ZREM, ZREM_BYTES, key0, member1);
  }

  // Remove all members in a sorted set within the given indexes
  public ListenableFuture<IntegerReply> zremrangebyrank(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < ZREMRANGEBYRANK_VERSION) throw new RedisException("Server does not support ZREMRANGEBYRANK");
    return (ListenableFuture<IntegerReply>) pipeline(ZREMRANGEBYRANK, ZREMRANGEBYRANK_BYTES, key0, start1, stop2);
  }

  // Remove all members in a sorted set within the given scores
  public ListenableFuture<IntegerReply> zremrangebyscore(Object key0, Object min1, Object max2) throws RedisException {
    if (version < ZREMRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREMRANGEBYSCORE");
    return (ListenableFuture<IntegerReply>) pipeline(ZREMRANGEBYSCORE, ZREMRANGEBYSCORE_BYTES, key0, min1, max2);
  }

  // Return a range of members in a sorted set, by index, with scores ordered from high to low
  public ListenableFuture<MultiBulkReply> zrevrange(Object key0, Object start1, Object stop2, Object withscores3) throws RedisException {
    if (version < ZREVRANGE_VERSION) throw new RedisException("Server does not support ZREVRANGE");
    return (ListenableFuture<MultiBulkReply>) pipeline(ZREVRANGE, ZREVRANGE_BYTES, key0, start1, stop2, withscores3);
  }

  // Return a range of members in a sorted set, by score, with scores ordered from high to low
  public ListenableFuture<MultiBulkReply> zrevrangebyscore(Object key0, Object max1, Object min2, Object withscores3, Object offset_or_count4) throws RedisException {
    if (version < ZREVRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREVRANGEBYSCORE");
    return (ListenableFuture<MultiBulkReply>) pipeline(ZREVRANGEBYSCORE, ZREVRANGEBYSCORE_BYTES, key0, max1, min2, withscores3, offset_or_count4);
  }

  // Determine the index of a member in a sorted set, with scores ordered from high to low
  public ListenableFuture<IntegerReply> zrevrank(Object key0, Object member1) throws RedisException {
    if (version < ZREVRANK_VERSION) throw new RedisException("Server does not support ZREVRANK");
    return (ListenableFuture<IntegerReply>) pipeline(ZREVRANK, ZREVRANK_BYTES, key0, member1);
  }

  // Get the score associated with the given member in a sorted set
  public ListenableFuture<BulkReply> zscore(Object key0, Object member1) throws RedisException {
    if (version < ZSCORE_VERSION) throw new RedisException("Server does not support ZSCORE");
    return (ListenableFuture<BulkReply>) pipeline(ZSCORE, ZSCORE_BYTES, key0, member1);
  }

  // Add multiple sorted sets and store the resulting sorted set in a new key
  public ListenableFuture<IntegerReply> zunionstore(Object destination0, Object numkeys1, Object... key2) throws RedisException {
    if (version < ZUNIONSTORE_VERSION) throw new RedisException("Server does not support ZUNIONSTORE");
    return (ListenableFuture<IntegerReply>) pipeline(ZUNIONSTORE, ZUNIONSTORE_BYTES, destination0, numkeys1, key2);
  }

  // Execute a Lua script server side
  public ListenableFuture<Reply> eval(Object script0, Object numkeys1, Object... key2) throws RedisException {
    if (version < EVAL_VERSION) throw new RedisException("Server does not support EVAL");
    return (ListenableFuture<Reply>) pipeline(EVAL, EVAL_BYTES, script0, numkeys1, key2);
  }
  }
}
