package redis.client;

import com.google.common.base.Charsets;
import redis.Command;
import redis.reply.Reply;

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
  public Reply append(Object key, Object value) throws RedisException {
    return execute(APPEND, new Command(APPEND_BYTES, key, value));
  }
  private static final String AUTH = "AUTH";
  private static final byte[] AUTH_BYTES = AUTH.getBytes(Charsets.US_ASCII);

  // Authenticate to the server
  public Reply auth(Object password) throws RedisException {
    return execute(AUTH, new Command(AUTH_BYTES, password));
  }
  private static final String BGREWRITEAOF = "BGREWRITEAOF";
  private static final byte[] BGREWRITEAOF_BYTES = BGREWRITEAOF.getBytes(Charsets.US_ASCII);

  // Asynchronously rewrite the append-only file
  public Reply bgrewriteaof() throws RedisException {
    return execute(BGREWRITEAOF, new Command(BGREWRITEAOF_BYTES));
  }
  private static final String BGSAVE = "BGSAVE";
  private static final byte[] BGSAVE_BYTES = BGSAVE.getBytes(Charsets.US_ASCII);

  // Asynchronously save the dataset to disk
  public Reply bgsave() throws RedisException {
    return execute(BGSAVE, new Command(BGSAVE_BYTES));
  }
  private static final String BRPOPLPUSH = "BRPOPLPUSH";
  private static final byte[] BRPOPLPUSH_BYTES = BRPOPLPUSH.getBytes(Charsets.US_ASCII);

  // Pop a value from a list, push it to another list and return it; or block until one is available
  public Reply brpoplpush(Object source, Object destination, Object timeout) throws RedisException {
    return execute(BRPOPLPUSH, new Command(BRPOPLPUSH_BYTES, source, destination, timeout));
  }
  private static final String CONFIG_GET = "CONFIG_GET";
  private static final byte[] CONFIG_GET_BYTES = CONFIG_GET.getBytes(Charsets.US_ASCII);

  // Get the value of a configuration parameter
  public Reply config_get(Object parameter) throws RedisException {
    return execute(CONFIG_GET, new Command(CONFIG_GET_BYTES, parameter));
  }
  private static final String CONFIG_SET = "CONFIG_SET";
  private static final byte[] CONFIG_SET_BYTES = CONFIG_SET.getBytes(Charsets.US_ASCII);

  // Set a configuration parameter to the given value
  public Reply config_set(Object parameter, Object value) throws RedisException {
    return execute(CONFIG_SET, new Command(CONFIG_SET_BYTES, parameter, value));
  }
  private static final String CONFIG_RESETSTAT = "CONFIG_RESETSTAT";
  private static final byte[] CONFIG_RESETSTAT_BYTES = CONFIG_RESETSTAT.getBytes(Charsets.US_ASCII);

  // Reset the stats returned by INFO
  public Reply config_resetstat() throws RedisException {
    return execute(CONFIG_RESETSTAT, new Command(CONFIG_RESETSTAT_BYTES));
  }
  private static final String DBSIZE = "DBSIZE";
  private static final byte[] DBSIZE_BYTES = DBSIZE.getBytes(Charsets.US_ASCII);

  // Return the number of keys in the selected database
  public Reply dbsize() throws RedisException {
    return execute(DBSIZE, new Command(DBSIZE_BYTES));
  }
  private static final String DEBUG_OBJECT = "DEBUG_OBJECT";
  private static final byte[] DEBUG_OBJECT_BYTES = DEBUG_OBJECT.getBytes(Charsets.US_ASCII);

  // Get debugging information about a key
  public Reply debug_object(Object key) throws RedisException {
    return execute(DEBUG_OBJECT, new Command(DEBUG_OBJECT_BYTES, key));
  }
  private static final String DEBUG_SEGFAULT = "DEBUG_SEGFAULT";
  private static final byte[] DEBUG_SEGFAULT_BYTES = DEBUG_SEGFAULT.getBytes(Charsets.US_ASCII);

  // Make the server crash
  public Reply debug_segfault() throws RedisException {
    return execute(DEBUG_SEGFAULT, new Command(DEBUG_SEGFAULT_BYTES));
  }
  private static final String DECR = "DECR";
  private static final byte[] DECR_BYTES = DECR.getBytes(Charsets.US_ASCII);

  // Decrement the integer value of a key by one
  public Reply decr(Object key) throws RedisException {
    return execute(DECR, new Command(DECR_BYTES, key));
  }
  private static final String DECRBY = "DECRBY";
  private static final byte[] DECRBY_BYTES = DECRBY.getBytes(Charsets.US_ASCII);

  // Decrement the integer value of a key by the given number
  public Reply decrby(Object key, Object decrement) throws RedisException {
    return execute(DECRBY, new Command(DECRBY_BYTES, key, decrement));
  }
  private static final String DISCARD = "DISCARD";
  private static final byte[] DISCARD_BYTES = DISCARD.getBytes(Charsets.US_ASCII);

  // Discard all commands issued after MULTI
  public Reply discard() throws RedisException {
    return execute(DISCARD, new Command(DISCARD_BYTES));
  }
  private static final String ECHO = "ECHO";
  private static final byte[] ECHO_BYTES = ECHO.getBytes(Charsets.US_ASCII);

  // Echo the given string
  public Reply echo(Object message) throws RedisException {
    return execute(ECHO, new Command(ECHO_BYTES, message));
  }
  private static final String EXEC = "EXEC";
  private static final byte[] EXEC_BYTES = EXEC.getBytes(Charsets.US_ASCII);

  // Execute all commands issued after MULTI
  public Reply exec() throws RedisException {
    return execute(EXEC, new Command(EXEC_BYTES));
  }
  private static final String EXISTS = "EXISTS";
  private static final byte[] EXISTS_BYTES = EXISTS.getBytes(Charsets.US_ASCII);

  // Determine if a key exists
  public Reply exists(Object key) throws RedisException {
    return execute(EXISTS, new Command(EXISTS_BYTES, key));
  }
  private static final String EXPIRE = "EXPIRE";
  private static final byte[] EXPIRE_BYTES = EXPIRE.getBytes(Charsets.US_ASCII);

  // Set a key's time to live in seconds
  public Reply expire(Object key, Object seconds) throws RedisException {
    return execute(EXPIRE, new Command(EXPIRE_BYTES, key, seconds));
  }
  private static final String EXPIREAT = "EXPIREAT";
  private static final byte[] EXPIREAT_BYTES = EXPIREAT.getBytes(Charsets.US_ASCII);

  // Set the expiration for a key as a UNIX timestamp
  public Reply expireat(Object key, Object timestamp) throws RedisException {
    return execute(EXPIREAT, new Command(EXPIREAT_BYTES, key, timestamp));
  }
  private static final String FLUSHALL = "FLUSHALL";
  private static final byte[] FLUSHALL_BYTES = FLUSHALL.getBytes(Charsets.US_ASCII);

  // Remove all keys from all databases
  public Reply flushall() throws RedisException {
    return execute(FLUSHALL, new Command(FLUSHALL_BYTES));
  }
  private static final String FLUSHDB = "FLUSHDB";
  private static final byte[] FLUSHDB_BYTES = FLUSHDB.getBytes(Charsets.US_ASCII);

  // Remove all keys from the current database
  public Reply flushdb() throws RedisException {
    return execute(FLUSHDB, new Command(FLUSHDB_BYTES));
  }
  private static final String GET = "GET";
  private static final byte[] GET_BYTES = GET.getBytes(Charsets.US_ASCII);

  // Get the value of a key
  public Reply get(Object key) throws RedisException {
    return execute(GET, new Command(GET_BYTES, key));
  }
  private static final String GETBIT = "GETBIT";
  private static final byte[] GETBIT_BYTES = GETBIT.getBytes(Charsets.US_ASCII);

  // Returns the bit value at offset in the string value stored at key
  public Reply getbit(Object key, Object offset) throws RedisException {
    return execute(GETBIT, new Command(GETBIT_BYTES, key, offset));
  }
  private static final String GETRANGE = "GETRANGE";
  private static final byte[] GETRANGE_BYTES = GETRANGE.getBytes(Charsets.US_ASCII);

  // Get a substring of the string stored at a key
  public Reply getrange(Object key, Object start, Object end) throws RedisException {
    return execute(GETRANGE, new Command(GETRANGE_BYTES, key, start, end));
  }
  private static final String GETSET = "GETSET";
  private static final byte[] GETSET_BYTES = GETSET.getBytes(Charsets.US_ASCII);

  // Set the string value of a key and return its old value
  public Reply getset(Object key, Object value) throws RedisException {
    return execute(GETSET, new Command(GETSET_BYTES, key, value));
  }
  private static final String HEXISTS = "HEXISTS";
  private static final byte[] HEXISTS_BYTES = HEXISTS.getBytes(Charsets.US_ASCII);

  // Determine if a hash field exists
  public Reply hexists(Object key, Object field) throws RedisException {
    return execute(HEXISTS, new Command(HEXISTS_BYTES, key, field));
  }
  private static final String HGET = "HGET";
  private static final byte[] HGET_BYTES = HGET.getBytes(Charsets.US_ASCII);

  // Get the value of a hash field
  public Reply hget(Object key, Object field) throws RedisException {
    return execute(HGET, new Command(HGET_BYTES, key, field));
  }
  private static final String HGETALL = "HGETALL";
  private static final byte[] HGETALL_BYTES = HGETALL.getBytes(Charsets.US_ASCII);

  // Get all the fields and values in a hash
  public Reply hgetall(Object key) throws RedisException {
    return execute(HGETALL, new Command(HGETALL_BYTES, key));
  }
  private static final String HINCRBY = "HINCRBY";
  private static final byte[] HINCRBY_BYTES = HINCRBY.getBytes(Charsets.US_ASCII);

  // Increment the integer value of a hash field by the given number
  public Reply hincrby(Object key, Object field, Object increment) throws RedisException {
    return execute(HINCRBY, new Command(HINCRBY_BYTES, key, field, increment));
  }
  private static final String HKEYS = "HKEYS";
  private static final byte[] HKEYS_BYTES = HKEYS.getBytes(Charsets.US_ASCII);

  // Get all the fields in a hash
  public Reply hkeys(Object key) throws RedisException {
    return execute(HKEYS, new Command(HKEYS_BYTES, key));
  }
  private static final String HLEN = "HLEN";
  private static final byte[] HLEN_BYTES = HLEN.getBytes(Charsets.US_ASCII);

  // Get the number of fields in a hash
  public Reply hlen(Object key) throws RedisException {
    return execute(HLEN, new Command(HLEN_BYTES, key));
  }
  private static final String HSET = "HSET";
  private static final byte[] HSET_BYTES = HSET.getBytes(Charsets.US_ASCII);

  // Set the string value of a hash field
  public Reply hset(Object key, Object field, Object value) throws RedisException {
    return execute(HSET, new Command(HSET_BYTES, key, field, value));
  }
  private static final String HSETNX = "HSETNX";
  private static final byte[] HSETNX_BYTES = HSETNX.getBytes(Charsets.US_ASCII);

  // Set the value of a hash field, only if the field does not exist
  public Reply hsetnx(Object key, Object field, Object value) throws RedisException {
    return execute(HSETNX, new Command(HSETNX_BYTES, key, field, value));
  }
  private static final String HVALS = "HVALS";
  private static final byte[] HVALS_BYTES = HVALS.getBytes(Charsets.US_ASCII);

  // Get all the values in a hash
  public Reply hvals(Object key) throws RedisException {
    return execute(HVALS, new Command(HVALS_BYTES, key));
  }
  private static final String INCR = "INCR";
  private static final byte[] INCR_BYTES = INCR.getBytes(Charsets.US_ASCII);

  // Increment the integer value of a key by one
  public Reply incr(Object key) throws RedisException {
    return execute(INCR, new Command(INCR_BYTES, key));
  }
  private static final String INCRBY = "INCRBY";
  private static final byte[] INCRBY_BYTES = INCRBY.getBytes(Charsets.US_ASCII);

  // Increment the integer value of a key by the given number
  public Reply incrby(Object key, Object increment) throws RedisException {
    return execute(INCRBY, new Command(INCRBY_BYTES, key, increment));
  }
  private static final String INFO = "INFO";
  private static final byte[] INFO_BYTES = INFO.getBytes(Charsets.US_ASCII);

  // Get information and statistics about the server
  public Reply info() throws RedisException {
    return execute(INFO, new Command(INFO_BYTES));
  }
  private static final String KEYS = "KEYS";
  private static final byte[] KEYS_BYTES = KEYS.getBytes(Charsets.US_ASCII);

  // Find all keys matching the given pattern
  public Reply keys(Object pattern) throws RedisException {
    return execute(KEYS, new Command(KEYS_BYTES, pattern));
  }
  private static final String LASTSAVE = "LASTSAVE";
  private static final byte[] LASTSAVE_BYTES = LASTSAVE.getBytes(Charsets.US_ASCII);

  // Get the UNIX time stamp of the last successful save to disk
  public Reply lastsave() throws RedisException {
    return execute(LASTSAVE, new Command(LASTSAVE_BYTES));
  }
  private static final String LINDEX = "LINDEX";
  private static final byte[] LINDEX_BYTES = LINDEX.getBytes(Charsets.US_ASCII);

  // Get an element from a list by its index
  public Reply lindex(Object key, Object index) throws RedisException {
    return execute(LINDEX, new Command(LINDEX_BYTES, key, index));
  }
  private static final String LLEN = "LLEN";
  private static final byte[] LLEN_BYTES = LLEN.getBytes(Charsets.US_ASCII);

  // Get the length of a list
  public Reply llen(Object key) throws RedisException {
    return execute(LLEN, new Command(LLEN_BYTES, key));
  }
  private static final String LPOP = "LPOP";
  private static final byte[] LPOP_BYTES = LPOP.getBytes(Charsets.US_ASCII);

  // Remove and get the first element in a list
  public Reply lpop(Object key) throws RedisException {
    return execute(LPOP, new Command(LPOP_BYTES, key));
  }
  private static final String LPUSHX = "LPUSHX";
  private static final byte[] LPUSHX_BYTES = LPUSHX.getBytes(Charsets.US_ASCII);

  // Prepend a value to a list, only if the list exists
  public Reply lpushx(Object key, Object value) throws RedisException {
    return execute(LPUSHX, new Command(LPUSHX_BYTES, key, value));
  }
  private static final String LRANGE = "LRANGE";
  private static final byte[] LRANGE_BYTES = LRANGE.getBytes(Charsets.US_ASCII);

  // Get a range of elements from a list
  public Reply lrange(Object key, Object start, Object stop) throws RedisException {
    return execute(LRANGE, new Command(LRANGE_BYTES, key, start, stop));
  }
  private static final String LREM = "LREM";
  private static final byte[] LREM_BYTES = LREM.getBytes(Charsets.US_ASCII);

  // Remove elements from a list
  public Reply lrem(Object key, Object count, Object value) throws RedisException {
    return execute(LREM, new Command(LREM_BYTES, key, count, value));
  }
  private static final String LSET = "LSET";
  private static final byte[] LSET_BYTES = LSET.getBytes(Charsets.US_ASCII);

  // Set the value of an element in a list by its index
  public Reply lset(Object key, Object index, Object value) throws RedisException {
    return execute(LSET, new Command(LSET_BYTES, key, index, value));
  }
  private static final String LTRIM = "LTRIM";
  private static final byte[] LTRIM_BYTES = LTRIM.getBytes(Charsets.US_ASCII);

  // Trim a list to the specified range
  public Reply ltrim(Object key, Object start, Object stop) throws RedisException {
    return execute(LTRIM, new Command(LTRIM_BYTES, key, start, stop));
  }
  private static final String MONITOR = "MONITOR";
  private static final byte[] MONITOR_BYTES = MONITOR.getBytes(Charsets.US_ASCII);

  // Listen for all requests received by the server in real time
  public Reply monitor() throws RedisException {
    return execute(MONITOR, new Command(MONITOR_BYTES));
  }
  private static final String MOVE = "MOVE";
  private static final byte[] MOVE_BYTES = MOVE.getBytes(Charsets.US_ASCII);

  // Move a key to another database
  public Reply move(Object key, Object db) throws RedisException {
    return execute(MOVE, new Command(MOVE_BYTES, key, db));
  }
  private static final String MULTI = "MULTI";
  private static final byte[] MULTI_BYTES = MULTI.getBytes(Charsets.US_ASCII);

  // Mark the start of a transaction block
  public Reply multi() throws RedisException {
    return execute(MULTI, new Command(MULTI_BYTES));
  }
  private static final String PERSIST = "PERSIST";
  private static final byte[] PERSIST_BYTES = PERSIST.getBytes(Charsets.US_ASCII);

  // Remove the expiration from a key
  public Reply persist(Object key) throws RedisException {
    return execute(PERSIST, new Command(PERSIST_BYTES, key));
  }
  private static final String PING = "PING";
  private static final byte[] PING_BYTES = PING.getBytes(Charsets.US_ASCII);

  // Ping the server
  public Reply ping() throws RedisException {
    return execute(PING, new Command(PING_BYTES));
  }
  private static final String PUBLISH = "PUBLISH";
  private static final byte[] PUBLISH_BYTES = PUBLISH.getBytes(Charsets.US_ASCII);

  // Post a message to a channel
  public Reply publish(Object channel, Object message) throws RedisException {
    return execute(PUBLISH, new Command(PUBLISH_BYTES, channel, message));
  }
  private static final String QUIT = "QUIT";
  private static final byte[] QUIT_BYTES = QUIT.getBytes(Charsets.US_ASCII);

  // Close the connection
  public Reply quit() throws RedisException {
    return execute(QUIT, new Command(QUIT_BYTES));
  }
  private static final String RANDOMKEY = "RANDOMKEY";
  private static final byte[] RANDOMKEY_BYTES = RANDOMKEY.getBytes(Charsets.US_ASCII);

  // Return a random key from the keyspace
  public Reply randomkey() throws RedisException {
    return execute(RANDOMKEY, new Command(RANDOMKEY_BYTES));
  }
  private static final String RENAME = "RENAME";
  private static final byte[] RENAME_BYTES = RENAME.getBytes(Charsets.US_ASCII);

  // Rename a key
  public Reply rename(Object key, Object newkey) throws RedisException {
    return execute(RENAME, new Command(RENAME_BYTES, key, newkey));
  }
  private static final String RENAMENX = "RENAMENX";
  private static final byte[] RENAMENX_BYTES = RENAMENX.getBytes(Charsets.US_ASCII);

  // Rename a key, only if the new key does not exist
  public Reply renamenx(Object key, Object newkey) throws RedisException {
    return execute(RENAMENX, new Command(RENAMENX_BYTES, key, newkey));
  }
  private static final String RPOP = "RPOP";
  private static final byte[] RPOP_BYTES = RPOP.getBytes(Charsets.US_ASCII);

  // Remove and get the last element in a list
  public Reply rpop(Object key) throws RedisException {
    return execute(RPOP, new Command(RPOP_BYTES, key));
  }
  private static final String RPOPLPUSH = "RPOPLPUSH";
  private static final byte[] RPOPLPUSH_BYTES = RPOPLPUSH.getBytes(Charsets.US_ASCII);

  // Remove the last element in a list, append it to another list and return it
  public Reply rpoplpush(Object source, Object destination) throws RedisException {
    return execute(RPOPLPUSH, new Command(RPOPLPUSH_BYTES, source, destination));
  }
  private static final String RPUSHX = "RPUSHX";
  private static final byte[] RPUSHX_BYTES = RPUSHX.getBytes(Charsets.US_ASCII);

  // Append a value to a list, only if the list exists
  public Reply rpushx(Object key, Object value) throws RedisException {
    return execute(RPUSHX, new Command(RPUSHX_BYTES, key, value));
  }
  private static final String SAVE = "SAVE";
  private static final byte[] SAVE_BYTES = SAVE.getBytes(Charsets.US_ASCII);

  // Synchronously save the dataset to disk
  public Reply save() throws RedisException {
    return execute(SAVE, new Command(SAVE_BYTES));
  }
  private static final String SCARD = "SCARD";
  private static final byte[] SCARD_BYTES = SCARD.getBytes(Charsets.US_ASCII);

  // Get the number of members in a set
  public Reply scard(Object key) throws RedisException {
    return execute(SCARD, new Command(SCARD_BYTES, key));
  }
  private static final String SELECT = "SELECT";
  private static final byte[] SELECT_BYTES = SELECT.getBytes(Charsets.US_ASCII);

  // Change the selected database for the current connection
  public Reply select(Object index) throws RedisException {
    return execute(SELECT, new Command(SELECT_BYTES, index));
  }
  private static final String SET = "SET";
  private static final byte[] SET_BYTES = SET.getBytes(Charsets.US_ASCII);

  // Set the string value of a key
  public Reply set(Object key, Object value) throws RedisException {
    return execute(SET, new Command(SET_BYTES, key, value));
  }
  private static final String SETBIT = "SETBIT";
  private static final byte[] SETBIT_BYTES = SETBIT.getBytes(Charsets.US_ASCII);

  // Sets or clears the bit at offset in the string value stored at key
  public Reply setbit(Object key, Object offset, Object value) throws RedisException {
    return execute(SETBIT, new Command(SETBIT_BYTES, key, offset, value));
  }
  private static final String SETEX = "SETEX";
  private static final byte[] SETEX_BYTES = SETEX.getBytes(Charsets.US_ASCII);

  // Set the value and expiration of a key
  public Reply setex(Object key, Object seconds, Object value) throws RedisException {
    return execute(SETEX, new Command(SETEX_BYTES, key, seconds, value));
  }
  private static final String SETNX = "SETNX";
  private static final byte[] SETNX_BYTES = SETNX.getBytes(Charsets.US_ASCII);

  // Set the value of a key, only if the key does not exist
  public Reply setnx(Object key, Object value) throws RedisException {
    return execute(SETNX, new Command(SETNX_BYTES, key, value));
  }
  private static final String SETRANGE = "SETRANGE";
  private static final byte[] SETRANGE_BYTES = SETRANGE.getBytes(Charsets.US_ASCII);

  // Overwrite part of a string at key starting at the specified offset
  public Reply setrange(Object key, Object offset, Object value) throws RedisException {
    return execute(SETRANGE, new Command(SETRANGE_BYTES, key, offset, value));
  }
  private static final String SHUTDOWN = "SHUTDOWN";
  private static final byte[] SHUTDOWN_BYTES = SHUTDOWN.getBytes(Charsets.US_ASCII);

  // Synchronously save the dataset to disk and then shut down the server
  public Reply shutdown() throws RedisException {
    return execute(SHUTDOWN, new Command(SHUTDOWN_BYTES));
  }
  private static final String SISMEMBER = "SISMEMBER";
  private static final byte[] SISMEMBER_BYTES = SISMEMBER.getBytes(Charsets.US_ASCII);

  // Determine if a given value is a member of a set
  public Reply sismember(Object key, Object member) throws RedisException {
    return execute(SISMEMBER, new Command(SISMEMBER_BYTES, key, member));
  }
  private static final String SLAVEOF = "SLAVEOF";
  private static final byte[] SLAVEOF_BYTES = SLAVEOF.getBytes(Charsets.US_ASCII);

  // Make the server a slave of another instance, or promote it as master
  public Reply slaveof(Object host, Object port) throws RedisException {
    return execute(SLAVEOF, new Command(SLAVEOF_BYTES, host, port));
  }
  private static final String SMEMBERS = "SMEMBERS";
  private static final byte[] SMEMBERS_BYTES = SMEMBERS.getBytes(Charsets.US_ASCII);

  // Get all the members in a set
  public Reply smembers(Object key) throws RedisException {
    return execute(SMEMBERS, new Command(SMEMBERS_BYTES, key));
  }
  private static final String SMOVE = "SMOVE";
  private static final byte[] SMOVE_BYTES = SMOVE.getBytes(Charsets.US_ASCII);

  // Move a member from one set to another
  public Reply smove(Object source, Object destination, Object member) throws RedisException {
    return execute(SMOVE, new Command(SMOVE_BYTES, source, destination, member));
  }
  private static final String SPOP = "SPOP";
  private static final byte[] SPOP_BYTES = SPOP.getBytes(Charsets.US_ASCII);

  // Remove and return a random member from a set
  public Reply spop(Object key) throws RedisException {
    return execute(SPOP, new Command(SPOP_BYTES, key));
  }
  private static final String SRANDMEMBER = "SRANDMEMBER";
  private static final byte[] SRANDMEMBER_BYTES = SRANDMEMBER.getBytes(Charsets.US_ASCII);

  // Get a random member from a set
  public Reply srandmember(Object key) throws RedisException {
    return execute(SRANDMEMBER, new Command(SRANDMEMBER_BYTES, key));
  }
  private static final String STRLEN = "STRLEN";
  private static final byte[] STRLEN_BYTES = STRLEN.getBytes(Charsets.US_ASCII);

  // Get the length of the value stored in a key
  public Reply strlen(Object key) throws RedisException {
    return execute(STRLEN, new Command(STRLEN_BYTES, key));
  }
  private static final String SYNC = "SYNC";
  private static final byte[] SYNC_BYTES = SYNC.getBytes(Charsets.US_ASCII);

  // Internal command used for replication
  public Reply sync() throws RedisException {
    return execute(SYNC, new Command(SYNC_BYTES));
  }
  private static final String TTL = "TTL";
  private static final byte[] TTL_BYTES = TTL.getBytes(Charsets.US_ASCII);

  // Get the time to live for a key
  public Reply ttl(Object key) throws RedisException {
    return execute(TTL, new Command(TTL_BYTES, key));
  }
  private static final String TYPE = "TYPE";
  private static final byte[] TYPE_BYTES = TYPE.getBytes(Charsets.US_ASCII);

  // Determine the type stored at key
  public Reply type(Object key) throws RedisException {
    return execute(TYPE, new Command(TYPE_BYTES, key));
  }
  private static final String UNWATCH = "UNWATCH";
  private static final byte[] UNWATCH_BYTES = UNWATCH.getBytes(Charsets.US_ASCII);

  // Forget about all watched keys
  public Reply unwatch() throws RedisException {
    return execute(UNWATCH, new Command(UNWATCH_BYTES));
  }
  private static final String ZCARD = "ZCARD";
  private static final byte[] ZCARD_BYTES = ZCARD.getBytes(Charsets.US_ASCII);

  // Get the number of members in a sorted set
  public Reply zcard(Object key) throws RedisException {
    return execute(ZCARD, new Command(ZCARD_BYTES, key));
  }
  private static final String ZCOUNT = "ZCOUNT";
  private static final byte[] ZCOUNT_BYTES = ZCOUNT.getBytes(Charsets.US_ASCII);

  // Count the members in a sorted set with scores within the given values
  public Reply zcount(Object key, Object min, Object max) throws RedisException {
    return execute(ZCOUNT, new Command(ZCOUNT_BYTES, key, min, max));
  }
  private static final String ZINCRBY = "ZINCRBY";
  private static final byte[] ZINCRBY_BYTES = ZINCRBY.getBytes(Charsets.US_ASCII);

  // Increment the score of a member in a sorted set
  public Reply zincrby(Object key, Object increment, Object member) throws RedisException {
    return execute(ZINCRBY, new Command(ZINCRBY_BYTES, key, increment, member));
  }
  private static final String ZRANK = "ZRANK";
  private static final byte[] ZRANK_BYTES = ZRANK.getBytes(Charsets.US_ASCII);

  // Determine the index of a member in a sorted set
  public Reply zrank(Object key, Object member) throws RedisException {
    return execute(ZRANK, new Command(ZRANK_BYTES, key, member));
  }
  private static final String ZREMRANGEBYRANK = "ZREMRANGEBYRANK";
  private static final byte[] ZREMRANGEBYRANK_BYTES = ZREMRANGEBYRANK.getBytes(Charsets.US_ASCII);

  // Remove all members in a sorted set within the given indexes
  public Reply zremrangebyrank(Object key, Object start, Object stop) throws RedisException {
    return execute(ZREMRANGEBYRANK, new Command(ZREMRANGEBYRANK_BYTES, key, start, stop));
  }
  private static final String ZREMRANGEBYSCORE = "ZREMRANGEBYSCORE";
  private static final byte[] ZREMRANGEBYSCORE_BYTES = ZREMRANGEBYSCORE.getBytes(Charsets.US_ASCII);

  // Remove all members in a sorted set within the given scores
  public Reply zremrangebyscore(Object key, Object min, Object max) throws RedisException {
    return execute(ZREMRANGEBYSCORE, new Command(ZREMRANGEBYSCORE_BYTES, key, min, max));
  }
  private static final String ZREVRANK = "ZREVRANK";
  private static final byte[] ZREVRANK_BYTES = ZREVRANK.getBytes(Charsets.US_ASCII);

  // Determine the index of a member in a sorted set, with scores ordered from high to low
  public Reply zrevrank(Object key, Object member) throws RedisException {
    return execute(ZREVRANK, new Command(ZREVRANK_BYTES, key, member));
  }
  private static final String ZSCORE = "ZSCORE";
  private static final byte[] ZSCORE_BYTES = ZSCORE.getBytes(Charsets.US_ASCII);

  // Get the score associated with the given member in a sorted set
  public Reply zscore(Object key, Object member) throws RedisException {
    return execute(ZSCORE, new Command(ZSCORE_BYTES, key, member));
  }

  public class Pipeline {

  // Append a value to a key
  public Future<Reply> append(Object key, Object value) throws RedisException {
    return pipeline(APPEND, new Command(APPEND_BYTES, key, value));
  }

  // Authenticate to the server
  public Future<Reply> auth(Object password) throws RedisException {
    return pipeline(AUTH, new Command(AUTH_BYTES, password));
  }

  // Asynchronously rewrite the append-only file
  public Future<Reply> bgrewriteaof() throws RedisException {
    return pipeline(BGREWRITEAOF, new Command(BGREWRITEAOF_BYTES));
  }

  // Asynchronously save the dataset to disk
  public Future<Reply> bgsave() throws RedisException {
    return pipeline(BGSAVE, new Command(BGSAVE_BYTES));
  }

  // Pop a value from a list, push it to another list and return it; or block until one is available
  public Future<Reply> brpoplpush(Object source, Object destination, Object timeout) throws RedisException {
    return pipeline(BRPOPLPUSH, new Command(BRPOPLPUSH_BYTES, source, destination, timeout));
  }

  // Get the value of a configuration parameter
  public Future<Reply> config_get(Object parameter) throws RedisException {
    return pipeline(CONFIG_GET, new Command(CONFIG_GET_BYTES, parameter));
  }

  // Set a configuration parameter to the given value
  public Future<Reply> config_set(Object parameter, Object value) throws RedisException {
    return pipeline(CONFIG_SET, new Command(CONFIG_SET_BYTES, parameter, value));
  }

  // Reset the stats returned by INFO
  public Future<Reply> config_resetstat() throws RedisException {
    return pipeline(CONFIG_RESETSTAT, new Command(CONFIG_RESETSTAT_BYTES));
  }

  // Return the number of keys in the selected database
  public Future<Reply> dbsize() throws RedisException {
    return pipeline(DBSIZE, new Command(DBSIZE_BYTES));
  }

  // Get debugging information about a key
  public Future<Reply> debug_object(Object key) throws RedisException {
    return pipeline(DEBUG_OBJECT, new Command(DEBUG_OBJECT_BYTES, key));
  }

  // Make the server crash
  public Future<Reply> debug_segfault() throws RedisException {
    return pipeline(DEBUG_SEGFAULT, new Command(DEBUG_SEGFAULT_BYTES));
  }

  // Decrement the integer value of a key by one
  public Future<Reply> decr(Object key) throws RedisException {
    return pipeline(DECR, new Command(DECR_BYTES, key));
  }

  // Decrement the integer value of a key by the given number
  public Future<Reply> decrby(Object key, Object decrement) throws RedisException {
    return pipeline(DECRBY, new Command(DECRBY_BYTES, key, decrement));
  }

  // Discard all commands issued after MULTI
  public Future<Reply> discard() throws RedisException {
    return pipeline(DISCARD, new Command(DISCARD_BYTES));
  }

  // Echo the given string
  public Future<Reply> echo(Object message) throws RedisException {
    return pipeline(ECHO, new Command(ECHO_BYTES, message));
  }

  // Execute all commands issued after MULTI
  public Future<Reply> exec() throws RedisException {
    return pipeline(EXEC, new Command(EXEC_BYTES));
  }

  // Determine if a key exists
  public Future<Reply> exists(Object key) throws RedisException {
    return pipeline(EXISTS, new Command(EXISTS_BYTES, key));
  }

  // Set a key's time to live in seconds
  public Future<Reply> expire(Object key, Object seconds) throws RedisException {
    return pipeline(EXPIRE, new Command(EXPIRE_BYTES, key, seconds));
  }

  // Set the expiration for a key as a UNIX timestamp
  public Future<Reply> expireat(Object key, Object timestamp) throws RedisException {
    return pipeline(EXPIREAT, new Command(EXPIREAT_BYTES, key, timestamp));
  }

  // Remove all keys from all databases
  public Future<Reply> flushall() throws RedisException {
    return pipeline(FLUSHALL, new Command(FLUSHALL_BYTES));
  }

  // Remove all keys from the current database
  public Future<Reply> flushdb() throws RedisException {
    return pipeline(FLUSHDB, new Command(FLUSHDB_BYTES));
  }

  // Get the value of a key
  public Future<Reply> get(Object key) throws RedisException {
    return pipeline(GET, new Command(GET_BYTES, key));
  }

  // Returns the bit value at offset in the string value stored at key
  public Future<Reply> getbit(Object key, Object offset) throws RedisException {
    return pipeline(GETBIT, new Command(GETBIT_BYTES, key, offset));
  }

  // Get a substring of the string stored at a key
  public Future<Reply> getrange(Object key, Object start, Object end) throws RedisException {
    return pipeline(GETRANGE, new Command(GETRANGE_BYTES, key, start, end));
  }

  // Set the string value of a key and return its old value
  public Future<Reply> getset(Object key, Object value) throws RedisException {
    return pipeline(GETSET, new Command(GETSET_BYTES, key, value));
  }

  // Determine if a hash field exists
  public Future<Reply> hexists(Object key, Object field) throws RedisException {
    return pipeline(HEXISTS, new Command(HEXISTS_BYTES, key, field));
  }

  // Get the value of a hash field
  public Future<Reply> hget(Object key, Object field) throws RedisException {
    return pipeline(HGET, new Command(HGET_BYTES, key, field));
  }

  // Get all the fields and values in a hash
  public Future<Reply> hgetall(Object key) throws RedisException {
    return pipeline(HGETALL, new Command(HGETALL_BYTES, key));
  }

  // Increment the integer value of a hash field by the given number
  public Future<Reply> hincrby(Object key, Object field, Object increment) throws RedisException {
    return pipeline(HINCRBY, new Command(HINCRBY_BYTES, key, field, increment));
  }

  // Get all the fields in a hash
  public Future<Reply> hkeys(Object key) throws RedisException {
    return pipeline(HKEYS, new Command(HKEYS_BYTES, key));
  }

  // Get the number of fields in a hash
  public Future<Reply> hlen(Object key) throws RedisException {
    return pipeline(HLEN, new Command(HLEN_BYTES, key));
  }

  // Set the string value of a hash field
  public Future<Reply> hset(Object key, Object field, Object value) throws RedisException {
    return pipeline(HSET, new Command(HSET_BYTES, key, field, value));
  }

  // Set the value of a hash field, only if the field does not exist
  public Future<Reply> hsetnx(Object key, Object field, Object value) throws RedisException {
    return pipeline(HSETNX, new Command(HSETNX_BYTES, key, field, value));
  }

  // Get all the values in a hash
  public Future<Reply> hvals(Object key) throws RedisException {
    return pipeline(HVALS, new Command(HVALS_BYTES, key));
  }

  // Increment the integer value of a key by one
  public Future<Reply> incr(Object key) throws RedisException {
    return pipeline(INCR, new Command(INCR_BYTES, key));
  }

  // Increment the integer value of a key by the given number
  public Future<Reply> incrby(Object key, Object increment) throws RedisException {
    return pipeline(INCRBY, new Command(INCRBY_BYTES, key, increment));
  }

  // Get information and statistics about the server
  public Future<Reply> info() throws RedisException {
    return pipeline(INFO, new Command(INFO_BYTES));
  }

  // Find all keys matching the given pattern
  public Future<Reply> keys(Object pattern) throws RedisException {
    return pipeline(KEYS, new Command(KEYS_BYTES, pattern));
  }

  // Get the UNIX time stamp of the last successful save to disk
  public Future<Reply> lastsave() throws RedisException {
    return pipeline(LASTSAVE, new Command(LASTSAVE_BYTES));
  }

  // Get an element from a list by its index
  public Future<Reply> lindex(Object key, Object index) throws RedisException {
    return pipeline(LINDEX, new Command(LINDEX_BYTES, key, index));
  }

  // Get the length of a list
  public Future<Reply> llen(Object key) throws RedisException {
    return pipeline(LLEN, new Command(LLEN_BYTES, key));
  }

  // Remove and get the first element in a list
  public Future<Reply> lpop(Object key) throws RedisException {
    return pipeline(LPOP, new Command(LPOP_BYTES, key));
  }

  // Prepend a value to a list, only if the list exists
  public Future<Reply> lpushx(Object key, Object value) throws RedisException {
    return pipeline(LPUSHX, new Command(LPUSHX_BYTES, key, value));
  }

  // Get a range of elements from a list
  public Future<Reply> lrange(Object key, Object start, Object stop) throws RedisException {
    return pipeline(LRANGE, new Command(LRANGE_BYTES, key, start, stop));
  }

  // Remove elements from a list
  public Future<Reply> lrem(Object key, Object count, Object value) throws RedisException {
    return pipeline(LREM, new Command(LREM_BYTES, key, count, value));
  }

  // Set the value of an element in a list by its index
  public Future<Reply> lset(Object key, Object index, Object value) throws RedisException {
    return pipeline(LSET, new Command(LSET_BYTES, key, index, value));
  }

  // Trim a list to the specified range
  public Future<Reply> ltrim(Object key, Object start, Object stop) throws RedisException {
    return pipeline(LTRIM, new Command(LTRIM_BYTES, key, start, stop));
  }

  // Listen for all requests received by the server in real time
  public Future<Reply> monitor() throws RedisException {
    return pipeline(MONITOR, new Command(MONITOR_BYTES));
  }

  // Move a key to another database
  public Future<Reply> move(Object key, Object db) throws RedisException {
    return pipeline(MOVE, new Command(MOVE_BYTES, key, db));
  }

  // Mark the start of a transaction block
  public Future<Reply> multi() throws RedisException {
    return pipeline(MULTI, new Command(MULTI_BYTES));
  }

  // Remove the expiration from a key
  public Future<Reply> persist(Object key) throws RedisException {
    return pipeline(PERSIST, new Command(PERSIST_BYTES, key));
  }

  // Ping the server
  public Future<Reply> ping() throws RedisException {
    return pipeline(PING, new Command(PING_BYTES));
  }

  // Post a message to a channel
  public Future<Reply> publish(Object channel, Object message) throws RedisException {
    return pipeline(PUBLISH, new Command(PUBLISH_BYTES, channel, message));
  }

  // Close the connection
  public Future<Reply> quit() throws RedisException {
    return pipeline(QUIT, new Command(QUIT_BYTES));
  }

  // Return a random key from the keyspace
  public Future<Reply> randomkey() throws RedisException {
    return pipeline(RANDOMKEY, new Command(RANDOMKEY_BYTES));
  }

  // Rename a key
  public Future<Reply> rename(Object key, Object newkey) throws RedisException {
    return pipeline(RENAME, new Command(RENAME_BYTES, key, newkey));
  }

  // Rename a key, only if the new key does not exist
  public Future<Reply> renamenx(Object key, Object newkey) throws RedisException {
    return pipeline(RENAMENX, new Command(RENAMENX_BYTES, key, newkey));
  }

  // Remove and get the last element in a list
  public Future<Reply> rpop(Object key) throws RedisException {
    return pipeline(RPOP, new Command(RPOP_BYTES, key));
  }

  // Remove the last element in a list, append it to another list and return it
  public Future<Reply> rpoplpush(Object source, Object destination) throws RedisException {
    return pipeline(RPOPLPUSH, new Command(RPOPLPUSH_BYTES, source, destination));
  }

  // Append a value to a list, only if the list exists
  public Future<Reply> rpushx(Object key, Object value) throws RedisException {
    return pipeline(RPUSHX, new Command(RPUSHX_BYTES, key, value));
  }

  // Synchronously save the dataset to disk
  public Future<Reply> save() throws RedisException {
    return pipeline(SAVE, new Command(SAVE_BYTES));
  }

  // Get the number of members in a set
  public Future<Reply> scard(Object key) throws RedisException {
    return pipeline(SCARD, new Command(SCARD_BYTES, key));
  }

  // Change the selected database for the current connection
  public Future<Reply> select(Object index) throws RedisException {
    return pipeline(SELECT, new Command(SELECT_BYTES, index));
  }

  // Set the string value of a key
  public Future<Reply> set(Object key, Object value) throws RedisException {
    return pipeline(SET, new Command(SET_BYTES, key, value));
  }

  // Sets or clears the bit at offset in the string value stored at key
  public Future<Reply> setbit(Object key, Object offset, Object value) throws RedisException {
    return pipeline(SETBIT, new Command(SETBIT_BYTES, key, offset, value));
  }

  // Set the value and expiration of a key
  public Future<Reply> setex(Object key, Object seconds, Object value) throws RedisException {
    return pipeline(SETEX, new Command(SETEX_BYTES, key, seconds, value));
  }

  // Set the value of a key, only if the key does not exist
  public Future<Reply> setnx(Object key, Object value) throws RedisException {
    return pipeline(SETNX, new Command(SETNX_BYTES, key, value));
  }

  // Overwrite part of a string at key starting at the specified offset
  public Future<Reply> setrange(Object key, Object offset, Object value) throws RedisException {
    return pipeline(SETRANGE, new Command(SETRANGE_BYTES, key, offset, value));
  }

  // Synchronously save the dataset to disk and then shut down the server
  public Future<Reply> shutdown() throws RedisException {
    return pipeline(SHUTDOWN, new Command(SHUTDOWN_BYTES));
  }

  // Determine if a given value is a member of a set
  public Future<Reply> sismember(Object key, Object member) throws RedisException {
    return pipeline(SISMEMBER, new Command(SISMEMBER_BYTES, key, member));
  }

  // Make the server a slave of another instance, or promote it as master
  public Future<Reply> slaveof(Object host, Object port) throws RedisException {
    return pipeline(SLAVEOF, new Command(SLAVEOF_BYTES, host, port));
  }

  // Get all the members in a set
  public Future<Reply> smembers(Object key) throws RedisException {
    return pipeline(SMEMBERS, new Command(SMEMBERS_BYTES, key));
  }

  // Move a member from one set to another
  public Future<Reply> smove(Object source, Object destination, Object member) throws RedisException {
    return pipeline(SMOVE, new Command(SMOVE_BYTES, source, destination, member));
  }

  // Remove and return a random member from a set
  public Future<Reply> spop(Object key) throws RedisException {
    return pipeline(SPOP, new Command(SPOP_BYTES, key));
  }

  // Get a random member from a set
  public Future<Reply> srandmember(Object key) throws RedisException {
    return pipeline(SRANDMEMBER, new Command(SRANDMEMBER_BYTES, key));
  }

  // Get the length of the value stored in a key
  public Future<Reply> strlen(Object key) throws RedisException {
    return pipeline(STRLEN, new Command(STRLEN_BYTES, key));
  }

  // Internal command used for replication
  public Future<Reply> sync() throws RedisException {
    return pipeline(SYNC, new Command(SYNC_BYTES));
  }

  // Get the time to live for a key
  public Future<Reply> ttl(Object key) throws RedisException {
    return pipeline(TTL, new Command(TTL_BYTES, key));
  }

  // Determine the type stored at key
  public Future<Reply> type(Object key) throws RedisException {
    return pipeline(TYPE, new Command(TYPE_BYTES, key));
  }

  // Forget about all watched keys
  public Future<Reply> unwatch() throws RedisException {
    return pipeline(UNWATCH, new Command(UNWATCH_BYTES));
  }

  // Get the number of members in a sorted set
  public Future<Reply> zcard(Object key) throws RedisException {
    return pipeline(ZCARD, new Command(ZCARD_BYTES, key));
  }

  // Count the members in a sorted set with scores within the given values
  public Future<Reply> zcount(Object key, Object min, Object max) throws RedisException {
    return pipeline(ZCOUNT, new Command(ZCOUNT_BYTES, key, min, max));
  }

  // Increment the score of a member in a sorted set
  public Future<Reply> zincrby(Object key, Object increment, Object member) throws RedisException {
    return pipeline(ZINCRBY, new Command(ZINCRBY_BYTES, key, increment, member));
  }

  // Determine the index of a member in a sorted set
  public Future<Reply> zrank(Object key, Object member) throws RedisException {
    return pipeline(ZRANK, new Command(ZRANK_BYTES, key, member));
  }

  // Remove all members in a sorted set within the given indexes
  public Future<Reply> zremrangebyrank(Object key, Object start, Object stop) throws RedisException {
    return pipeline(ZREMRANGEBYRANK, new Command(ZREMRANGEBYRANK_BYTES, key, start, stop));
  }

  // Remove all members in a sorted set within the given scores
  public Future<Reply> zremrangebyscore(Object key, Object min, Object max) throws RedisException {
    return pipeline(ZREMRANGEBYSCORE, new Command(ZREMRANGEBYSCORE_BYTES, key, min, max));
  }

  // Determine the index of a member in a sorted set, with scores ordered from high to low
  public Future<Reply> zrevrank(Object key, Object member) throws RedisException {
    return pipeline(ZREVRANK, new Command(ZREVRANK_BYTES, key, member));
  }

  // Get the score associated with the given member in a sorted set
  public Future<Reply> zscore(Object key, Object member) throws RedisException {
    return pipeline(ZSCORE, new Command(ZSCORE_BYTES, key, member));
  }
  }
}
