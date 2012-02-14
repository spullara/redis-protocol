package redis.client;

import com.google.common.base.Charsets;
import redis.Command;
import redis.reply.*;

import java.io.IOException;
import java.util.concurrent.Future;

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

  // Append a value to a key
  public IntegerReply append(Object key, Object value) throws RedisException {
    return (IntegerReply) execute(APPEND, APPEND_BYTES, key, value);
  }
  
  private static final String AUTH = "AUTH";
  private static final byte[] AUTH_BYTES = AUTH.getBytes(Charsets.US_ASCII);

  // Authenticate to the server
  public StatusReply auth(Object password) throws RedisException {
    return (StatusReply) execute(AUTH, AUTH_BYTES, password);
  }
  
  private static final String BGREWRITEAOF = "BGREWRITEAOF";
  private static final byte[] BGREWRITEAOF_BYTES = BGREWRITEAOF.getBytes(Charsets.US_ASCII);

  // Asynchronously rewrite the append-only file
  public StatusReply bgrewriteaof() throws RedisException {
    return (StatusReply) execute(BGREWRITEAOF, BGREWRITEAOF_BYTES);
  }
  
  private static final String BGSAVE = "BGSAVE";
  private static final byte[] BGSAVE_BYTES = BGSAVE.getBytes(Charsets.US_ASCII);

  // Asynchronously save the dataset to disk
  public StatusReply bgsave() throws RedisException {
    return (StatusReply) execute(BGSAVE, BGSAVE_BYTES);
  }
  
  private static final String BRPOPLPUSH = "BRPOPLPUSH";
  private static final byte[] BRPOPLPUSH_BYTES = BRPOPLPUSH.getBytes(Charsets.US_ASCII);

  // Pop a value from a list, push it to another list and return it; or block until one is available
  public BulkReply brpoplpush(Object source, Object destination, Object timeout) throws RedisException {
    return (BulkReply) execute(BRPOPLPUSH, BRPOPLPUSH_BYTES, source, destination, timeout);
  }
  
  private static final String CONFIG_GET = "CONFIG_GET";
  private static final byte[] CONFIG_GET_BYTES = CONFIG_GET.getBytes(Charsets.US_ASCII);

  // Get the value of a configuration parameter
  public Reply config_get(Object parameter) throws RedisException {
    return (Reply) execute(CONFIG_GET, CONFIG_GET_BYTES, parameter);
  }
  
  private static final String CONFIG_SET = "CONFIG_SET";
  private static final byte[] CONFIG_SET_BYTES = CONFIG_SET.getBytes(Charsets.US_ASCII);

  // Set a configuration parameter to the given value
  public Reply config_set(Object parameter, Object value) throws RedisException {
    return (Reply) execute(CONFIG_SET, CONFIG_SET_BYTES, parameter, value);
  }
  
  private static final String CONFIG_RESETSTAT = "CONFIG_RESETSTAT";
  private static final byte[] CONFIG_RESETSTAT_BYTES = CONFIG_RESETSTAT.getBytes(Charsets.US_ASCII);

  // Reset the stats returned by INFO
  public Reply config_resetstat() throws RedisException {
    return (Reply) execute(CONFIG_RESETSTAT, CONFIG_RESETSTAT_BYTES);
  }
  
  private static final String DBSIZE = "DBSIZE";
  private static final byte[] DBSIZE_BYTES = DBSIZE.getBytes(Charsets.US_ASCII);

  // Return the number of keys in the selected database
  public IntegerReply dbsize() throws RedisException {
    return (IntegerReply) execute(DBSIZE, DBSIZE_BYTES);
  }
  
  private static final String DEBUG_OBJECT = "DEBUG_OBJECT";
  private static final byte[] DEBUG_OBJECT_BYTES = DEBUG_OBJECT.getBytes(Charsets.US_ASCII);

  // Get debugging information about a key
  public Reply debug_object(Object key) throws RedisException {
    return (Reply) execute(DEBUG_OBJECT, DEBUG_OBJECT_BYTES, key);
  }
  
  private static final String DEBUG_SEGFAULT = "DEBUG_SEGFAULT";
  private static final byte[] DEBUG_SEGFAULT_BYTES = DEBUG_SEGFAULT.getBytes(Charsets.US_ASCII);

  // Make the server crash
  public Reply debug_segfault() throws RedisException {
    return (Reply) execute(DEBUG_SEGFAULT, DEBUG_SEGFAULT_BYTES);
  }
  
  private static final String DECR = "DECR";
  private static final byte[] DECR_BYTES = DECR.getBytes(Charsets.US_ASCII);

  // Decrement the integer value of a key by one
  public IntegerReply decr(Object key) throws RedisException {
    return (IntegerReply) execute(DECR, DECR_BYTES, key);
  }
  
  private static final String DECRBY = "DECRBY";
  private static final byte[] DECRBY_BYTES = DECRBY.getBytes(Charsets.US_ASCII);

  // Decrement the integer value of a key by the given number
  public IntegerReply decrby(Object key, Object decrement) throws RedisException {
    return (IntegerReply) execute(DECRBY, DECRBY_BYTES, key, decrement);
  }
  
  private static final String DISCARD = "DISCARD";
  private static final byte[] DISCARD_BYTES = DISCARD.getBytes(Charsets.US_ASCII);

  // Discard all commands issued after MULTI
  public StatusReply discard() throws RedisException {
    return (StatusReply) execute(DISCARD, DISCARD_BYTES);
  }
  
  private static final String ECHO = "ECHO";
  private static final byte[] ECHO_BYTES = ECHO.getBytes(Charsets.US_ASCII);

  // Echo the given string
  public BulkReply echo(Object message) throws RedisException {
    return (BulkReply) execute(ECHO, ECHO_BYTES, message);
  }
  
  private static final String EXEC = "EXEC";
  private static final byte[] EXEC_BYTES = EXEC.getBytes(Charsets.US_ASCII);

  // Execute all commands issued after MULTI
  public MultiBulkReply exec() throws RedisException {
    return (MultiBulkReply) execute(EXEC, EXEC_BYTES);
  }
  
  private static final String EXISTS = "EXISTS";
  private static final byte[] EXISTS_BYTES = EXISTS.getBytes(Charsets.US_ASCII);

  // Determine if a key exists
  public IntegerReply exists(Object key) throws RedisException {
    return (IntegerReply) execute(EXISTS, EXISTS_BYTES, key);
  }
  
  private static final String EXPIRE = "EXPIRE";
  private static final byte[] EXPIRE_BYTES = EXPIRE.getBytes(Charsets.US_ASCII);

  // Set a key's time to live in seconds
  public IntegerReply expire(Object key, Object seconds) throws RedisException {
    return (IntegerReply) execute(EXPIRE, EXPIRE_BYTES, key, seconds);
  }
  
  private static final String EXPIREAT = "EXPIREAT";
  private static final byte[] EXPIREAT_BYTES = EXPIREAT.getBytes(Charsets.US_ASCII);

  // Set the expiration for a key as a UNIX timestamp
  public IntegerReply expireat(Object key, Object timestamp) throws RedisException {
    return (IntegerReply) execute(EXPIREAT, EXPIREAT_BYTES, key, timestamp);
  }
  
  private static final String FLUSHALL = "FLUSHALL";
  private static final byte[] FLUSHALL_BYTES = FLUSHALL.getBytes(Charsets.US_ASCII);

  // Remove all keys from all databases
  public StatusReply flushall() throws RedisException {
    return (StatusReply) execute(FLUSHALL, FLUSHALL_BYTES);
  }
  
  private static final String FLUSHDB = "FLUSHDB";
  private static final byte[] FLUSHDB_BYTES = FLUSHDB.getBytes(Charsets.US_ASCII);

  // Remove all keys from the current database
  public StatusReply flushdb() throws RedisException {
    return (StatusReply) execute(FLUSHDB, FLUSHDB_BYTES);
  }
  
  private static final String GET = "GET";
  private static final byte[] GET_BYTES = GET.getBytes(Charsets.US_ASCII);

  // Get the value of a key
  public BulkReply get(Object key) throws RedisException {
    return (BulkReply) execute(GET, GET_BYTES, key);
  }
  
  private static final String GETBIT = "GETBIT";
  private static final byte[] GETBIT_BYTES = GETBIT.getBytes(Charsets.US_ASCII);

  // Returns the bit value at offset in the string value stored at key
  public IntegerReply getbit(Object key, Object offset) throws RedisException {
    return (IntegerReply) execute(GETBIT, GETBIT_BYTES, key, offset);
  }
  
  private static final String GETRANGE = "GETRANGE";
  private static final byte[] GETRANGE_BYTES = GETRANGE.getBytes(Charsets.US_ASCII);

  // Get a substring of the string stored at a key
  public BulkReply getrange(Object key, Object start, Object end) throws RedisException {
    return (BulkReply) execute(GETRANGE, GETRANGE_BYTES, key, start, end);
  }
  
  private static final String GETSET = "GETSET";
  private static final byte[] GETSET_BYTES = GETSET.getBytes(Charsets.US_ASCII);

  // Set the string value of a key and return its old value
  public BulkReply getset(Object key, Object value) throws RedisException {
    return (BulkReply) execute(GETSET, GETSET_BYTES, key, value);
  }
  
  private static final String HEXISTS = "HEXISTS";
  private static final byte[] HEXISTS_BYTES = HEXISTS.getBytes(Charsets.US_ASCII);

  // Determine if a hash field exists
  public IntegerReply hexists(Object key, Object field) throws RedisException {
    return (IntegerReply) execute(HEXISTS, HEXISTS_BYTES, key, field);
  }
  
  private static final String HGET = "HGET";
  private static final byte[] HGET_BYTES = HGET.getBytes(Charsets.US_ASCII);

  // Get the value of a hash field
  public BulkReply hget(Object key, Object field) throws RedisException {
    return (BulkReply) execute(HGET, HGET_BYTES, key, field);
  }
  
  private static final String HGETALL = "HGETALL";
  private static final byte[] HGETALL_BYTES = HGETALL.getBytes(Charsets.US_ASCII);

  // Get all the fields and values in a hash
  public MultiBulkReply hgetall(Object key) throws RedisException {
    return (MultiBulkReply) execute(HGETALL, HGETALL_BYTES, key);
  }
  
  private static final String HINCRBY = "HINCRBY";
  private static final byte[] HINCRBY_BYTES = HINCRBY.getBytes(Charsets.US_ASCII);

  // Increment the integer value of a hash field by the given number
  public IntegerReply hincrby(Object key, Object field, Object increment) throws RedisException {
    return (IntegerReply) execute(HINCRBY, HINCRBY_BYTES, key, field, increment);
  }
  
  private static final String HKEYS = "HKEYS";
  private static final byte[] HKEYS_BYTES = HKEYS.getBytes(Charsets.US_ASCII);

  // Get all the fields in a hash
  public MultiBulkReply hkeys(Object key) throws RedisException {
    return (MultiBulkReply) execute(HKEYS, HKEYS_BYTES, key);
  }
  
  private static final String HLEN = "HLEN";
  private static final byte[] HLEN_BYTES = HLEN.getBytes(Charsets.US_ASCII);

  // Get the number of fields in a hash
  public IntegerReply hlen(Object key) throws RedisException {
    return (IntegerReply) execute(HLEN, HLEN_BYTES, key);
  }
  
  private static final String HSET = "HSET";
  private static final byte[] HSET_BYTES = HSET.getBytes(Charsets.US_ASCII);

  // Set the string value of a hash field
  public IntegerReply hset(Object key, Object field, Object value) throws RedisException {
    return (IntegerReply) execute(HSET, HSET_BYTES, key, field, value);
  }
  
  private static final String HSETNX = "HSETNX";
  private static final byte[] HSETNX_BYTES = HSETNX.getBytes(Charsets.US_ASCII);

  // Set the value of a hash field, only if the field does not exist
  public IntegerReply hsetnx(Object key, Object field, Object value) throws RedisException {
    return (IntegerReply) execute(HSETNX, HSETNX_BYTES, key, field, value);
  }
  
  private static final String HVALS = "HVALS";
  private static final byte[] HVALS_BYTES = HVALS.getBytes(Charsets.US_ASCII);

  // Get all the values in a hash
  public MultiBulkReply hvals(Object key) throws RedisException {
    return (MultiBulkReply) execute(HVALS, HVALS_BYTES, key);
  }
  
  private static final String INCR = "INCR";
  private static final byte[] INCR_BYTES = INCR.getBytes(Charsets.US_ASCII);

  // Increment the integer value of a key by one
  public IntegerReply incr(Object key) throws RedisException {
    return (IntegerReply) execute(INCR, INCR_BYTES, key);
  }
  
  private static final String INCRBY = "INCRBY";
  private static final byte[] INCRBY_BYTES = INCRBY.getBytes(Charsets.US_ASCII);

  // Increment the integer value of a key by the given number
  public IntegerReply incrby(Object key, Object increment) throws RedisException {
    return (IntegerReply) execute(INCRBY, INCRBY_BYTES, key, increment);
  }
  
  private static final String INFO = "INFO";
  private static final byte[] INFO_BYTES = INFO.getBytes(Charsets.US_ASCII);

  // Get information and statistics about the server
  public BulkReply info() throws RedisException {
    return (BulkReply) execute(INFO, INFO_BYTES);
  }
  
  private static final String KEYS = "KEYS";
  private static final byte[] KEYS_BYTES = KEYS.getBytes(Charsets.US_ASCII);

  // Find all keys matching the given pattern
  public MultiBulkReply keys(Object pattern) throws RedisException {
    return (MultiBulkReply) execute(KEYS, KEYS_BYTES, pattern);
  }
  
  private static final String LASTSAVE = "LASTSAVE";
  private static final byte[] LASTSAVE_BYTES = LASTSAVE.getBytes(Charsets.US_ASCII);

  // Get the UNIX time stamp of the last successful save to disk
  public IntegerReply lastsave() throws RedisException {
    return (IntegerReply) execute(LASTSAVE, LASTSAVE_BYTES);
  }
  
  private static final String LINDEX = "LINDEX";
  private static final byte[] LINDEX_BYTES = LINDEX.getBytes(Charsets.US_ASCII);

  // Get an element from a list by its index
  public BulkReply lindex(Object key, Object index) throws RedisException {
    return (BulkReply) execute(LINDEX, LINDEX_BYTES, key, index);
  }
  
  private static final String LLEN = "LLEN";
  private static final byte[] LLEN_BYTES = LLEN.getBytes(Charsets.US_ASCII);

  // Get the length of a list
  public IntegerReply llen(Object key) throws RedisException {
    return (IntegerReply) execute(LLEN, LLEN_BYTES, key);
  }
  
  private static final String LPOP = "LPOP";
  private static final byte[] LPOP_BYTES = LPOP.getBytes(Charsets.US_ASCII);

  // Remove and get the first element in a list
  public BulkReply lpop(Object key) throws RedisException {
    return (BulkReply) execute(LPOP, LPOP_BYTES, key);
  }
  
  private static final String LPUSHX = "LPUSHX";
  private static final byte[] LPUSHX_BYTES = LPUSHX.getBytes(Charsets.US_ASCII);

  // Prepend a value to a list, only if the list exists
  public IntegerReply lpushx(Object key, Object value) throws RedisException {
    return (IntegerReply) execute(LPUSHX, LPUSHX_BYTES, key, value);
  }
  
  private static final String LRANGE = "LRANGE";
  private static final byte[] LRANGE_BYTES = LRANGE.getBytes(Charsets.US_ASCII);

  // Get a range of elements from a list
  public MultiBulkReply lrange(Object key, Object start, Object stop) throws RedisException {
    return (MultiBulkReply) execute(LRANGE, LRANGE_BYTES, key, start, stop);
  }
  
  private static final String LREM = "LREM";
  private static final byte[] LREM_BYTES = LREM.getBytes(Charsets.US_ASCII);

  // Remove elements from a list
  public IntegerReply lrem(Object key, Object count, Object value) throws RedisException {
    return (IntegerReply) execute(LREM, LREM_BYTES, key, count, value);
  }
  
  private static final String LSET = "LSET";
  private static final byte[] LSET_BYTES = LSET.getBytes(Charsets.US_ASCII);

  // Set the value of an element in a list by its index
  public StatusReply lset(Object key, Object index, Object value) throws RedisException {
    return (StatusReply) execute(LSET, LSET_BYTES, key, index, value);
  }
  
  private static final String LTRIM = "LTRIM";
  private static final byte[] LTRIM_BYTES = LTRIM.getBytes(Charsets.US_ASCII);

  // Trim a list to the specified range
  public StatusReply ltrim(Object key, Object start, Object stop) throws RedisException {
    return (StatusReply) execute(LTRIM, LTRIM_BYTES, key, start, stop);
  }
  
  private static final String MONITOR = "MONITOR";
  private static final byte[] MONITOR_BYTES = MONITOR.getBytes(Charsets.US_ASCII);

  // Listen for all requests received by the server in real time
  public Reply monitor() throws RedisException {
    return (Reply) execute(MONITOR, MONITOR_BYTES);
  }
  
  private static final String MOVE = "MOVE";
  private static final byte[] MOVE_BYTES = MOVE.getBytes(Charsets.US_ASCII);

  // Move a key to another database
  public IntegerReply move(Object key, Object db) throws RedisException {
    return (IntegerReply) execute(MOVE, MOVE_BYTES, key, db);
  }
  
  private static final String MULTI = "MULTI";
  private static final byte[] MULTI_BYTES = MULTI.getBytes(Charsets.US_ASCII);

  // Mark the start of a transaction block
  public StatusReply multi() throws RedisException {
    return (StatusReply) execute(MULTI, MULTI_BYTES);
  }
  
  private static final String PERSIST = "PERSIST";
  private static final byte[] PERSIST_BYTES = PERSIST.getBytes(Charsets.US_ASCII);

  // Remove the expiration from a key
  public IntegerReply persist(Object key) throws RedisException {
    return (IntegerReply) execute(PERSIST, PERSIST_BYTES, key);
  }
  
  private static final String PING = "PING";
  private static final byte[] PING_BYTES = PING.getBytes(Charsets.US_ASCII);

  // Ping the server
  public StatusReply ping() throws RedisException {
    return (StatusReply) execute(PING, PING_BYTES);
  }
  
  private static final String PUBLISH = "PUBLISH";
  private static final byte[] PUBLISH_BYTES = PUBLISH.getBytes(Charsets.US_ASCII);

  // Post a message to a channel
  public IntegerReply publish(Object channel, Object message) throws RedisException {
    return (IntegerReply) execute(PUBLISH, PUBLISH_BYTES, channel, message);
  }
  
  private static final String QUIT = "QUIT";
  private static final byte[] QUIT_BYTES = QUIT.getBytes(Charsets.US_ASCII);

  // Close the connection
  public StatusReply quit() throws RedisException {
    return (StatusReply) execute(QUIT, QUIT_BYTES);
  }
  
  private static final String RANDOMKEY = "RANDOMKEY";
  private static final byte[] RANDOMKEY_BYTES = RANDOMKEY.getBytes(Charsets.US_ASCII);

  // Return a random key from the keyspace
  public BulkReply randomkey() throws RedisException {
    return (BulkReply) execute(RANDOMKEY, RANDOMKEY_BYTES);
  }
  
  private static final String RENAME = "RENAME";
  private static final byte[] RENAME_BYTES = RENAME.getBytes(Charsets.US_ASCII);

  // Rename a key
  public StatusReply rename(Object key, Object newkey) throws RedisException {
    return (StatusReply) execute(RENAME, RENAME_BYTES, key, newkey);
  }
  
  private static final String RENAMENX = "RENAMENX";
  private static final byte[] RENAMENX_BYTES = RENAMENX.getBytes(Charsets.US_ASCII);

  // Rename a key, only if the new key does not exist
  public IntegerReply renamenx(Object key, Object newkey) throws RedisException {
    return (IntegerReply) execute(RENAMENX, RENAMENX_BYTES, key, newkey);
  }
  
  private static final String RPOP = "RPOP";
  private static final byte[] RPOP_BYTES = RPOP.getBytes(Charsets.US_ASCII);

  // Remove and get the last element in a list
  public BulkReply rpop(Object key) throws RedisException {
    return (BulkReply) execute(RPOP, RPOP_BYTES, key);
  }
  
  private static final String RPOPLPUSH = "RPOPLPUSH";
  private static final byte[] RPOPLPUSH_BYTES = RPOPLPUSH.getBytes(Charsets.US_ASCII);

  // Remove the last element in a list, append it to another list and return it
  public BulkReply rpoplpush(Object source, Object destination) throws RedisException {
    return (BulkReply) execute(RPOPLPUSH, RPOPLPUSH_BYTES, source, destination);
  }
  
  private static final String RPUSHX = "RPUSHX";
  private static final byte[] RPUSHX_BYTES = RPUSHX.getBytes(Charsets.US_ASCII);

  // Append a value to a list, only if the list exists
  public IntegerReply rpushx(Object key, Object value) throws RedisException {
    return (IntegerReply) execute(RPUSHX, RPUSHX_BYTES, key, value);
  }
  
  private static final String SAVE = "SAVE";
  private static final byte[] SAVE_BYTES = SAVE.getBytes(Charsets.US_ASCII);

  // Synchronously save the dataset to disk
  public Reply save() throws RedisException {
    return (Reply) execute(SAVE, SAVE_BYTES);
  }
  
  private static final String SCARD = "SCARD";
  private static final byte[] SCARD_BYTES = SCARD.getBytes(Charsets.US_ASCII);

  // Get the number of members in a set
  public IntegerReply scard(Object key) throws RedisException {
    return (IntegerReply) execute(SCARD, SCARD_BYTES, key);
  }
  
  private static final String SELECT = "SELECT";
  private static final byte[] SELECT_BYTES = SELECT.getBytes(Charsets.US_ASCII);

  // Change the selected database for the current connection
  public StatusReply select(Object index) throws RedisException {
    return (StatusReply) execute(SELECT, SELECT_BYTES, index);
  }
  
  private static final String SET = "SET";
  private static final byte[] SET_BYTES = SET.getBytes(Charsets.US_ASCII);

  // Set the string value of a key
  public StatusReply set(Object key, Object value) throws RedisException {
    return (StatusReply) execute(SET, SET_BYTES, key, value);
  }
  
  private static final String SETBIT = "SETBIT";
  private static final byte[] SETBIT_BYTES = SETBIT.getBytes(Charsets.US_ASCII);

  // Sets or clears the bit at offset in the string value stored at key
  public IntegerReply setbit(Object key, Object offset, Object value) throws RedisException {
    return (IntegerReply) execute(SETBIT, SETBIT_BYTES, key, offset, value);
  }
  
  private static final String SETEX = "SETEX";
  private static final byte[] SETEX_BYTES = SETEX.getBytes(Charsets.US_ASCII);

  // Set the value and expiration of a key
  public StatusReply setex(Object key, Object seconds, Object value) throws RedisException {
    return (StatusReply) execute(SETEX, SETEX_BYTES, key, seconds, value);
  }
  
  private static final String SETNX = "SETNX";
  private static final byte[] SETNX_BYTES = SETNX.getBytes(Charsets.US_ASCII);

  // Set the value of a key, only if the key does not exist
  public IntegerReply setnx(Object key, Object value) throws RedisException {
    return (IntegerReply) execute(SETNX, SETNX_BYTES, key, value);
  }
  
  private static final String SETRANGE = "SETRANGE";
  private static final byte[] SETRANGE_BYTES = SETRANGE.getBytes(Charsets.US_ASCII);

  // Overwrite part of a string at key starting at the specified offset
  public IntegerReply setrange(Object key, Object offset, Object value) throws RedisException {
    return (IntegerReply) execute(SETRANGE, SETRANGE_BYTES, key, offset, value);
  }
  
  private static final String SHUTDOWN = "SHUTDOWN";
  private static final byte[] SHUTDOWN_BYTES = SHUTDOWN.getBytes(Charsets.US_ASCII);

  // Synchronously save the dataset to disk and then shut down the server
  public StatusReply shutdown() throws RedisException {
    return (StatusReply) execute(SHUTDOWN, SHUTDOWN_BYTES);
  }
  
  private static final String SISMEMBER = "SISMEMBER";
  private static final byte[] SISMEMBER_BYTES = SISMEMBER.getBytes(Charsets.US_ASCII);

  // Determine if a given value is a member of a set
  public IntegerReply sismember(Object key, Object member) throws RedisException {
    return (IntegerReply) execute(SISMEMBER, SISMEMBER_BYTES, key, member);
  }
  
  private static final String SLAVEOF = "SLAVEOF";
  private static final byte[] SLAVEOF_BYTES = SLAVEOF.getBytes(Charsets.US_ASCII);

  // Make the server a slave of another instance, or promote it as master
  public StatusReply slaveof(Object host, Object port) throws RedisException {
    return (StatusReply) execute(SLAVEOF, SLAVEOF_BYTES, host, port);
  }
  
  private static final String SMEMBERS = "SMEMBERS";
  private static final byte[] SMEMBERS_BYTES = SMEMBERS.getBytes(Charsets.US_ASCII);

  // Get all the members in a set
  public MultiBulkReply smembers(Object key) throws RedisException {
    return (MultiBulkReply) execute(SMEMBERS, SMEMBERS_BYTES, key);
  }
  
  private static final String SMOVE = "SMOVE";
  private static final byte[] SMOVE_BYTES = SMOVE.getBytes(Charsets.US_ASCII);

  // Move a member from one set to another
  public IntegerReply smove(Object source, Object destination, Object member) throws RedisException {
    return (IntegerReply) execute(SMOVE, SMOVE_BYTES, source, destination, member);
  }
  
  private static final String SPOP = "SPOP";
  private static final byte[] SPOP_BYTES = SPOP.getBytes(Charsets.US_ASCII);

  // Remove and return a random member from a set
  public BulkReply spop(Object key) throws RedisException {
    return (BulkReply) execute(SPOP, SPOP_BYTES, key);
  }
  
  private static final String SRANDMEMBER = "SRANDMEMBER";
  private static final byte[] SRANDMEMBER_BYTES = SRANDMEMBER.getBytes(Charsets.US_ASCII);

  // Get a random member from a set
  public BulkReply srandmember(Object key) throws RedisException {
    return (BulkReply) execute(SRANDMEMBER, SRANDMEMBER_BYTES, key);
  }
  
  private static final String STRLEN = "STRLEN";
  private static final byte[] STRLEN_BYTES = STRLEN.getBytes(Charsets.US_ASCII);

  // Get the length of the value stored in a key
  public IntegerReply strlen(Object key) throws RedisException {
    return (IntegerReply) execute(STRLEN, STRLEN_BYTES, key);
  }
  
  private static final String SYNC = "SYNC";
  private static final byte[] SYNC_BYTES = SYNC.getBytes(Charsets.US_ASCII);

  // Internal command used for replication
  public Reply sync() throws RedisException {
    return (Reply) execute(SYNC, SYNC_BYTES);
  }
  
  private static final String TTL = "TTL";
  private static final byte[] TTL_BYTES = TTL.getBytes(Charsets.US_ASCII);

  // Get the time to live for a key
  public IntegerReply ttl(Object key) throws RedisException {
    return (IntegerReply) execute(TTL, TTL_BYTES, key);
  }
  
  private static final String TYPE = "TYPE";
  private static final byte[] TYPE_BYTES = TYPE.getBytes(Charsets.US_ASCII);

  // Determine the type stored at key
  public StatusReply type(Object key) throws RedisException {
    return (StatusReply) execute(TYPE, TYPE_BYTES, key);
  }
  
  private static final String UNWATCH = "UNWATCH";
  private static final byte[] UNWATCH_BYTES = UNWATCH.getBytes(Charsets.US_ASCII);

  // Forget about all watched keys
  public StatusReply unwatch() throws RedisException {
    return (StatusReply) execute(UNWATCH, UNWATCH_BYTES);
  }
  
  private static final String ZCARD = "ZCARD";
  private static final byte[] ZCARD_BYTES = ZCARD.getBytes(Charsets.US_ASCII);

  // Get the number of members in a sorted set
  public IntegerReply zcard(Object key) throws RedisException {
    return (IntegerReply) execute(ZCARD, ZCARD_BYTES, key);
  }
  
  private static final String ZCOUNT = "ZCOUNT";
  private static final byte[] ZCOUNT_BYTES = ZCOUNT.getBytes(Charsets.US_ASCII);

  // Count the members in a sorted set with scores within the given values
  public IntegerReply zcount(Object key, Object min, Object max) throws RedisException {
    return (IntegerReply) execute(ZCOUNT, ZCOUNT_BYTES, key, min, max);
  }
  
  private static final String ZINCRBY = "ZINCRBY";
  private static final byte[] ZINCRBY_BYTES = ZINCRBY.getBytes(Charsets.US_ASCII);

  // Increment the score of a member in a sorted set
  public BulkReply zincrby(Object key, Object increment, Object member) throws RedisException {
    return (BulkReply) execute(ZINCRBY, ZINCRBY_BYTES, key, increment, member);
  }
  
  private static final String ZRANK = "ZRANK";
  private static final byte[] ZRANK_BYTES = ZRANK.getBytes(Charsets.US_ASCII);

  // Determine the index of a member in a sorted set
  public IntegerReply zrank(Object key, Object member) throws RedisException {
    return (IntegerReply) execute(ZRANK, ZRANK_BYTES, key, member);
  }
  
  private static final String ZREMRANGEBYRANK = "ZREMRANGEBYRANK";
  private static final byte[] ZREMRANGEBYRANK_BYTES = ZREMRANGEBYRANK.getBytes(Charsets.US_ASCII);

  // Remove all members in a sorted set within the given indexes
  public IntegerReply zremrangebyrank(Object key, Object start, Object stop) throws RedisException {
    return (IntegerReply) execute(ZREMRANGEBYRANK, ZREMRANGEBYRANK_BYTES, key, start, stop);
  }
  
  private static final String ZREMRANGEBYSCORE = "ZREMRANGEBYSCORE";
  private static final byte[] ZREMRANGEBYSCORE_BYTES = ZREMRANGEBYSCORE.getBytes(Charsets.US_ASCII);

  // Remove all members in a sorted set within the given scores
  public IntegerReply zremrangebyscore(Object key, Object min, Object max) throws RedisException {
    return (IntegerReply) execute(ZREMRANGEBYSCORE, ZREMRANGEBYSCORE_BYTES, key, min, max);
  }
  
  private static final String ZREVRANK = "ZREVRANK";
  private static final byte[] ZREVRANK_BYTES = ZREVRANK.getBytes(Charsets.US_ASCII);

  // Determine the index of a member in a sorted set, with scores ordered from high to low
  public IntegerReply zrevrank(Object key, Object member) throws RedisException {
    return (IntegerReply) execute(ZREVRANK, ZREVRANK_BYTES, key, member);
  }
  
  private static final String ZSCORE = "ZSCORE";
  private static final byte[] ZSCORE_BYTES = ZSCORE.getBytes(Charsets.US_ASCII);

  // Get the score associated with the given member in a sorted set
  public BulkReply zscore(Object key, Object member) throws RedisException {
    return (BulkReply) execute(ZSCORE, ZSCORE_BYTES, key, member);
  }

  public class Pipeline {

  // Append a value to a key
  public Future<IntegerReply> append(Object key, Object value) throws RedisException {
    return (Future<IntegerReply>) pipeline(APPEND, APPEND_BYTES, key, value);
  }

  // Authenticate to the server
  public Future<StatusReply> auth(Object password) throws RedisException {
    return (Future<StatusReply>) pipeline(AUTH, AUTH_BYTES, password);
  }

  // Asynchronously rewrite the append-only file
  public Future<StatusReply> bgrewriteaof() throws RedisException {
    return (Future<StatusReply>) pipeline(BGREWRITEAOF, BGREWRITEAOF_BYTES);
  }

  // Asynchronously save the dataset to disk
  public Future<StatusReply> bgsave() throws RedisException {
    return (Future<StatusReply>) pipeline(BGSAVE, BGSAVE_BYTES);
  }

  // Pop a value from a list, push it to another list and return it; or block until one is available
  public Future<BulkReply> brpoplpush(Object source, Object destination, Object timeout) throws RedisException {
    return (Future<BulkReply>) pipeline(BRPOPLPUSH, BRPOPLPUSH_BYTES, source, destination, timeout);
  }

  // Get the value of a configuration parameter
  public Future<Reply> config_get(Object parameter) throws RedisException {
    return (Future<Reply>) pipeline(CONFIG_GET, CONFIG_GET_BYTES, parameter);
  }

  // Set a configuration parameter to the given value
  public Future<Reply> config_set(Object parameter, Object value) throws RedisException {
    return (Future<Reply>) pipeline(CONFIG_SET, CONFIG_SET_BYTES, parameter, value);
  }

  // Reset the stats returned by INFO
  public Future<Reply> config_resetstat() throws RedisException {
    return (Future<Reply>) pipeline(CONFIG_RESETSTAT, CONFIG_RESETSTAT_BYTES);
  }

  // Return the number of keys in the selected database
  public Future<IntegerReply> dbsize() throws RedisException {
    return (Future<IntegerReply>) pipeline(DBSIZE, DBSIZE_BYTES);
  }

  // Get debugging information about a key
  public Future<Reply> debug_object(Object key) throws RedisException {
    return (Future<Reply>) pipeline(DEBUG_OBJECT, DEBUG_OBJECT_BYTES, key);
  }

  // Make the server crash
  public Future<Reply> debug_segfault() throws RedisException {
    return (Future<Reply>) pipeline(DEBUG_SEGFAULT, DEBUG_SEGFAULT_BYTES);
  }

  // Decrement the integer value of a key by one
  public Future<IntegerReply> decr(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(DECR, DECR_BYTES, key);
  }

  // Decrement the integer value of a key by the given number
  public Future<IntegerReply> decrby(Object key, Object decrement) throws RedisException {
    return (Future<IntegerReply>) pipeline(DECRBY, DECRBY_BYTES, key, decrement);
  }

  // Discard all commands issued after MULTI
  public Future<StatusReply> discard() throws RedisException {
    return (Future<StatusReply>) pipeline(DISCARD, DISCARD_BYTES);
  }

  // Echo the given string
  public Future<BulkReply> echo(Object message) throws RedisException {
    return (Future<BulkReply>) pipeline(ECHO, ECHO_BYTES, message);
  }

  // Execute all commands issued after MULTI
  public Future<MultiBulkReply> exec() throws RedisException {
    return (Future<MultiBulkReply>) pipeline(EXEC, EXEC_BYTES);
  }

  // Determine if a key exists
  public Future<IntegerReply> exists(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(EXISTS, EXISTS_BYTES, key);
  }

  // Set a key's time to live in seconds
  public Future<IntegerReply> expire(Object key, Object seconds) throws RedisException {
    return (Future<IntegerReply>) pipeline(EXPIRE, EXPIRE_BYTES, key, seconds);
  }

  // Set the expiration for a key as a UNIX timestamp
  public Future<IntegerReply> expireat(Object key, Object timestamp) throws RedisException {
    return (Future<IntegerReply>) pipeline(EXPIREAT, EXPIREAT_BYTES, key, timestamp);
  }

  // Remove all keys from all databases
  public Future<StatusReply> flushall() throws RedisException {
    return (Future<StatusReply>) pipeline(FLUSHALL, FLUSHALL_BYTES);
  }

  // Remove all keys from the current database
  public Future<StatusReply> flushdb() throws RedisException {
    return (Future<StatusReply>) pipeline(FLUSHDB, FLUSHDB_BYTES);
  }

  // Get the value of a key
  public Future<BulkReply> get(Object key) throws RedisException {
    return (Future<BulkReply>) pipeline(GET, GET_BYTES, key);
  }

  // Returns the bit value at offset in the string value stored at key
  public Future<IntegerReply> getbit(Object key, Object offset) throws RedisException {
    return (Future<IntegerReply>) pipeline(GETBIT, GETBIT_BYTES, key, offset);
  }

  // Get a substring of the string stored at a key
  public Future<BulkReply> getrange(Object key, Object start, Object end) throws RedisException {
    return (Future<BulkReply>) pipeline(GETRANGE, GETRANGE_BYTES, key, start, end);
  }

  // Set the string value of a key and return its old value
  public Future<BulkReply> getset(Object key, Object value) throws RedisException {
    return (Future<BulkReply>) pipeline(GETSET, GETSET_BYTES, key, value);
  }

  // Determine if a hash field exists
  public Future<IntegerReply> hexists(Object key, Object field) throws RedisException {
    return (Future<IntegerReply>) pipeline(HEXISTS, HEXISTS_BYTES, key, field);
  }

  // Get the value of a hash field
  public Future<BulkReply> hget(Object key, Object field) throws RedisException {
    return (Future<BulkReply>) pipeline(HGET, HGET_BYTES, key, field);
  }

  // Get all the fields and values in a hash
  public Future<MultiBulkReply> hgetall(Object key) throws RedisException {
    return (Future<MultiBulkReply>) pipeline(HGETALL, HGETALL_BYTES, key);
  }

  // Increment the integer value of a hash field by the given number
  public Future<IntegerReply> hincrby(Object key, Object field, Object increment) throws RedisException {
    return (Future<IntegerReply>) pipeline(HINCRBY, HINCRBY_BYTES, key, field, increment);
  }

  // Get all the fields in a hash
  public Future<MultiBulkReply> hkeys(Object key) throws RedisException {
    return (Future<MultiBulkReply>) pipeline(HKEYS, HKEYS_BYTES, key);
  }

  // Get the number of fields in a hash
  public Future<IntegerReply> hlen(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(HLEN, HLEN_BYTES, key);
  }

  // Set the string value of a hash field
  public Future<IntegerReply> hset(Object key, Object field, Object value) throws RedisException {
    return (Future<IntegerReply>) pipeline(HSET, HSET_BYTES, key, field, value);
  }

  // Set the value of a hash field, only if the field does not exist
  public Future<IntegerReply> hsetnx(Object key, Object field, Object value) throws RedisException {
    return (Future<IntegerReply>) pipeline(HSETNX, HSETNX_BYTES, key, field, value);
  }

  // Get all the values in a hash
  public Future<MultiBulkReply> hvals(Object key) throws RedisException {
    return (Future<MultiBulkReply>) pipeline(HVALS, HVALS_BYTES, key);
  }

  // Increment the integer value of a key by one
  public Future<IntegerReply> incr(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(INCR, INCR_BYTES, key);
  }

  // Increment the integer value of a key by the given number
  public Future<IntegerReply> incrby(Object key, Object increment) throws RedisException {
    return (Future<IntegerReply>) pipeline(INCRBY, INCRBY_BYTES, key, increment);
  }

  // Get information and statistics about the server
  public Future<BulkReply> info() throws RedisException {
    return (Future<BulkReply>) pipeline(INFO, INFO_BYTES);
  }

  // Find all keys matching the given pattern
  public Future<MultiBulkReply> keys(Object pattern) throws RedisException {
    return (Future<MultiBulkReply>) pipeline(KEYS, KEYS_BYTES, pattern);
  }

  // Get the UNIX time stamp of the last successful save to disk
  public Future<IntegerReply> lastsave() throws RedisException {
    return (Future<IntegerReply>) pipeline(LASTSAVE, LASTSAVE_BYTES);
  }

  // Get an element from a list by its index
  public Future<BulkReply> lindex(Object key, Object index) throws RedisException {
    return (Future<BulkReply>) pipeline(LINDEX, LINDEX_BYTES, key, index);
  }

  // Get the length of a list
  public Future<IntegerReply> llen(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(LLEN, LLEN_BYTES, key);
  }

  // Remove and get the first element in a list
  public Future<BulkReply> lpop(Object key) throws RedisException {
    return (Future<BulkReply>) pipeline(LPOP, LPOP_BYTES, key);
  }

  // Prepend a value to a list, only if the list exists
  public Future<IntegerReply> lpushx(Object key, Object value) throws RedisException {
    return (Future<IntegerReply>) pipeline(LPUSHX, LPUSHX_BYTES, key, value);
  }

  // Get a range of elements from a list
  public Future<MultiBulkReply> lrange(Object key, Object start, Object stop) throws RedisException {
    return (Future<MultiBulkReply>) pipeline(LRANGE, LRANGE_BYTES, key, start, stop);
  }

  // Remove elements from a list
  public Future<IntegerReply> lrem(Object key, Object count, Object value) throws RedisException {
    return (Future<IntegerReply>) pipeline(LREM, LREM_BYTES, key, count, value);
  }

  // Set the value of an element in a list by its index
  public Future<StatusReply> lset(Object key, Object index, Object value) throws RedisException {
    return (Future<StatusReply>) pipeline(LSET, LSET_BYTES, key, index, value);
  }

  // Trim a list to the specified range
  public Future<StatusReply> ltrim(Object key, Object start, Object stop) throws RedisException {
    return (Future<StatusReply>) pipeline(LTRIM, LTRIM_BYTES, key, start, stop);
  }

  // Listen for all requests received by the server in real time
  public Future<Reply> monitor() throws RedisException {
    return (Future<Reply>) pipeline(MONITOR, MONITOR_BYTES);
  }

  // Move a key to another database
  public Future<IntegerReply> move(Object key, Object db) throws RedisException {
    return (Future<IntegerReply>) pipeline(MOVE, MOVE_BYTES, key, db);
  }

  // Mark the start of a transaction block
  public Future<StatusReply> multi() throws RedisException {
    return (Future<StatusReply>) pipeline(MULTI, MULTI_BYTES);
  }

  // Remove the expiration from a key
  public Future<IntegerReply> persist(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(PERSIST, PERSIST_BYTES, key);
  }

  // Ping the server
  public Future<StatusReply> ping() throws RedisException {
    return (Future<StatusReply>) pipeline(PING, PING_BYTES);
  }

  // Post a message to a channel
  public Future<IntegerReply> publish(Object channel, Object message) throws RedisException {
    return (Future<IntegerReply>) pipeline(PUBLISH, PUBLISH_BYTES, channel, message);
  }

  // Close the connection
  public Future<StatusReply> quit() throws RedisException {
    return (Future<StatusReply>) pipeline(QUIT, QUIT_BYTES);
  }

  // Return a random key from the keyspace
  public Future<BulkReply> randomkey() throws RedisException {
    return (Future<BulkReply>) pipeline(RANDOMKEY, RANDOMKEY_BYTES);
  }

  // Rename a key
  public Future<StatusReply> rename(Object key, Object newkey) throws RedisException {
    return (Future<StatusReply>) pipeline(RENAME, RENAME_BYTES, key, newkey);
  }

  // Rename a key, only if the new key does not exist
  public Future<IntegerReply> renamenx(Object key, Object newkey) throws RedisException {
    return (Future<IntegerReply>) pipeline(RENAMENX, RENAMENX_BYTES, key, newkey);
  }

  // Remove and get the last element in a list
  public Future<BulkReply> rpop(Object key) throws RedisException {
    return (Future<BulkReply>) pipeline(RPOP, RPOP_BYTES, key);
  }

  // Remove the last element in a list, append it to another list and return it
  public Future<BulkReply> rpoplpush(Object source, Object destination) throws RedisException {
    return (Future<BulkReply>) pipeline(RPOPLPUSH, RPOPLPUSH_BYTES, source, destination);
  }

  // Append a value to a list, only if the list exists
  public Future<IntegerReply> rpushx(Object key, Object value) throws RedisException {
    return (Future<IntegerReply>) pipeline(RPUSHX, RPUSHX_BYTES, key, value);
  }

  // Synchronously save the dataset to disk
  public Future<Reply> save() throws RedisException {
    return (Future<Reply>) pipeline(SAVE, SAVE_BYTES);
  }

  // Get the number of members in a set
  public Future<IntegerReply> scard(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(SCARD, SCARD_BYTES, key);
  }

  // Change the selected database for the current connection
  public Future<StatusReply> select(Object index) throws RedisException {
    return (Future<StatusReply>) pipeline(SELECT, SELECT_BYTES, index);
  }

  // Set the string value of a key
  public Future<StatusReply> set(Object key, Object value) throws RedisException {
    return (Future<StatusReply>) pipeline(SET, SET_BYTES, key, value);
  }

  // Sets or clears the bit at offset in the string value stored at key
  public Future<IntegerReply> setbit(Object key, Object offset, Object value) throws RedisException {
    return (Future<IntegerReply>) pipeline(SETBIT, SETBIT_BYTES, key, offset, value);
  }

  // Set the value and expiration of a key
  public Future<StatusReply> setex(Object key, Object seconds, Object value) throws RedisException {
    return (Future<StatusReply>) pipeline(SETEX, SETEX_BYTES, key, seconds, value);
  }

  // Set the value of a key, only if the key does not exist
  public Future<IntegerReply> setnx(Object key, Object value) throws RedisException {
    return (Future<IntegerReply>) pipeline(SETNX, SETNX_BYTES, key, value);
  }

  // Overwrite part of a string at key starting at the specified offset
  public Future<IntegerReply> setrange(Object key, Object offset, Object value) throws RedisException {
    return (Future<IntegerReply>) pipeline(SETRANGE, SETRANGE_BYTES, key, offset, value);
  }

  // Synchronously save the dataset to disk and then shut down the server
  public Future<StatusReply> shutdown() throws RedisException {
    return (Future<StatusReply>) pipeline(SHUTDOWN, SHUTDOWN_BYTES);
  }

  // Determine if a given value is a member of a set
  public Future<IntegerReply> sismember(Object key, Object member) throws RedisException {
    return (Future<IntegerReply>) pipeline(SISMEMBER, SISMEMBER_BYTES, key, member);
  }

  // Make the server a slave of another instance, or promote it as master
  public Future<StatusReply> slaveof(Object host, Object port) throws RedisException {
    return (Future<StatusReply>) pipeline(SLAVEOF, SLAVEOF_BYTES, host, port);
  }

  // Get all the members in a set
  public Future<MultiBulkReply> smembers(Object key) throws RedisException {
    return (Future<MultiBulkReply>) pipeline(SMEMBERS, SMEMBERS_BYTES, key);
  }

  // Move a member from one set to another
  public Future<IntegerReply> smove(Object source, Object destination, Object member) throws RedisException {
    return (Future<IntegerReply>) pipeline(SMOVE, SMOVE_BYTES, source, destination, member);
  }

  // Remove and return a random member from a set
  public Future<BulkReply> spop(Object key) throws RedisException {
    return (Future<BulkReply>) pipeline(SPOP, SPOP_BYTES, key);
  }

  // Get a random member from a set
  public Future<BulkReply> srandmember(Object key) throws RedisException {
    return (Future<BulkReply>) pipeline(SRANDMEMBER, SRANDMEMBER_BYTES, key);
  }

  // Get the length of the value stored in a key
  public Future<IntegerReply> strlen(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(STRLEN, STRLEN_BYTES, key);
  }

  // Internal command used for replication
  public Future<Reply> sync() throws RedisException {
    return (Future<Reply>) pipeline(SYNC, SYNC_BYTES);
  }

  // Get the time to live for a key
  public Future<IntegerReply> ttl(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(TTL, TTL_BYTES, key);
  }

  // Determine the type stored at key
  public Future<StatusReply> type(Object key) throws RedisException {
    return (Future<StatusReply>) pipeline(TYPE, TYPE_BYTES, key);
  }

  // Forget about all watched keys
  public Future<StatusReply> unwatch() throws RedisException {
    return (Future<StatusReply>) pipeline(UNWATCH, UNWATCH_BYTES);
  }

  // Get the number of members in a sorted set
  public Future<IntegerReply> zcard(Object key) throws RedisException {
    return (Future<IntegerReply>) pipeline(ZCARD, ZCARD_BYTES, key);
  }

  // Count the members in a sorted set with scores within the given values
  public Future<IntegerReply> zcount(Object key, Object min, Object max) throws RedisException {
    return (Future<IntegerReply>) pipeline(ZCOUNT, ZCOUNT_BYTES, key, min, max);
  }

  // Increment the score of a member in a sorted set
  public Future<BulkReply> zincrby(Object key, Object increment, Object member) throws RedisException {
    return (Future<BulkReply>) pipeline(ZINCRBY, ZINCRBY_BYTES, key, increment, member);
  }

  // Determine the index of a member in a sorted set
  public Future<IntegerReply> zrank(Object key, Object member) throws RedisException {
    return (Future<IntegerReply>) pipeline(ZRANK, ZRANK_BYTES, key, member);
  }

  // Remove all members in a sorted set within the given indexes
  public Future<IntegerReply> zremrangebyrank(Object key, Object start, Object stop) throws RedisException {
    return (Future<IntegerReply>) pipeline(ZREMRANGEBYRANK, ZREMRANGEBYRANK_BYTES, key, start, stop);
  }

  // Remove all members in a sorted set within the given scores
  public Future<IntegerReply> zremrangebyscore(Object key, Object min, Object max) throws RedisException {
    return (Future<IntegerReply>) pipeline(ZREMRANGEBYSCORE, ZREMRANGEBYSCORE_BYTES, key, min, max);
  }

  // Determine the index of a member in a sorted set, with scores ordered from high to low
  public Future<IntegerReply> zrevrank(Object key, Object member) throws RedisException {
    return (Future<IntegerReply>) pipeline(ZREVRANK, ZREVRANK_BYTES, key, member);
  }

  // Get the score associated with the given member in a sorted set
  public Future<BulkReply> zscore(Object key, Object member) throws RedisException {
    return (Future<BulkReply>) pipeline(ZSCORE, ZSCORE_BYTES, key, member);
  }
  }
}
