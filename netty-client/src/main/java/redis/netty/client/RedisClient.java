package redis.netty.client;

import com.google.common.base.Charsets;
import redis.Command;
import redis.netty.BulkReply;
import redis.netty.IntegerReply;
import redis.netty.MultiBulkReply;
import redis.netty.Reply;
import redis.netty.StatusReply;
import spullara.util.concurrent.Promise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("UnusedDeclaration")
public class RedisClient extends RedisClientBase {

  private static ExecutorService defaultExecutor;

  public static synchronized Promise<RedisClient> connect(String host, int port) {
    if (defaultExecutor == null) {
      defaultExecutor = Executors.newCachedThreadPool();
    }
    RedisClient redisClient = new RedisClient();
    return RedisClientBase.connect(host, port, redisClient, defaultExecutor);
  }

  private static final String APPEND = "APPEND";
  private static final byte[] APPEND_BYTES = APPEND.getBytes(Charsets.US_ASCII);
  private static final int APPEND_VERSION = parseVersion("2.0.0");

  /**
   * Append a value to a key
   * String
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public Promise<IntegerReply> append(Object key0, Object value1) {
    if (version < APPEND_VERSION) return new Promise<>(new RedisException("Server does not support APPEND"));
    return execute(IntegerReply.class, new Command(APPEND_BYTES, key0, value1));
  }

  private static final String BITCOUNT = "BITCOUNT";
  private static final byte[] BITCOUNT_BYTES = BITCOUNT.getBytes(Charsets.US_ASCII);
  private static final int BITCOUNT_VERSION = parseVersion("2.6.0");

  /**
   * Count set bits in a string
   * String
   *
   * @param key0
   * @param start1
   * @param end2
   * @return IntegerReply
   */
  public Promise<IntegerReply> bitcount(Object key0, Object start1, Object end2) {
    if (version < BITCOUNT_VERSION) return new Promise<>(new RedisException("Server does not support BITCOUNT"));
    return execute(IntegerReply.class, new Command(BITCOUNT_BYTES, key0, start1, end2));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> bitcount_(Object... arguments) {
    if (version < BITCOUNT_VERSION) return new Promise<>(new RedisException("Server does not support BITCOUNT"));
    return execute(IntegerReply.class, new Command(BITCOUNT_BYTES, arguments));
  }

  private static final String BITOP = "BITOP";
  private static final byte[] BITOP_BYTES = BITOP.getBytes(Charsets.US_ASCII);
  private static final int BITOP_VERSION = parseVersion("2.6.0");

  /**
   * Perform bitwise operations between strings
   * String
   *
   * @param operation0
   * @param destkey1
   * @param key2
   * @return IntegerReply
   */
  public Promise<IntegerReply> bitop(Object operation0, Object destkey1, Object[] key2) {
    if (version < BITOP_VERSION) return new Promise<>(new RedisException("Server does not support BITOP"));
    List<Object> list = new ArrayList<>();
    list.add(operation0);
    list.add(destkey1);
    Collections.addAll(list, key2);
    return execute(IntegerReply.class, new Command(BITOP_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> bitop_(Object... arguments) {
    if (version < BITOP_VERSION) return new Promise<>(new RedisException("Server does not support BITOP"));
    return execute(IntegerReply.class, new Command(BITOP_BYTES, arguments));
  }

  private static final String DECR = "DECR";
  private static final byte[] DECR_BYTES = DECR.getBytes(Charsets.US_ASCII);
  private static final int DECR_VERSION = parseVersion("1.0.0");

  /**
   * Decrement the integer value of a key by one
   * String
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> decr(Object key0) {
    if (version < DECR_VERSION) return new Promise<>(new RedisException("Server does not support DECR"));
    return execute(IntegerReply.class, new Command(DECR_BYTES, key0));
  }

  private static final String DECRBY = "DECRBY";
  private static final byte[] DECRBY_BYTES = DECRBY.getBytes(Charsets.US_ASCII);
  private static final int DECRBY_VERSION = parseVersion("1.0.0");

  /**
   * Decrement the integer value of a key by the given number
   * String
   *
   * @param key0
   * @param decrement1
   * @return IntegerReply
   */
  public Promise<IntegerReply> decrby(Object key0, Object decrement1) {
    if (version < DECRBY_VERSION) return new Promise<>(new RedisException("Server does not support DECRBY"));
    return execute(IntegerReply.class, new Command(DECRBY_BYTES, key0, decrement1));
  }

  private static final String GET = "GET";
  private static final byte[] GET_BYTES = GET.getBytes(Charsets.US_ASCII);
  private static final int GET_VERSION = parseVersion("1.0.0");

  /**
   * Get the value of a key
   * String
   *
   * @param key0
   * @return BulkReply
   */
  public Promise<BulkReply> get(Object key0) {
    if (version < GET_VERSION) return new Promise<>(new RedisException("Server does not support GET"));
    return execute(BulkReply.class, new Command(GET_BYTES, key0));
  }

  private static final String GETBIT = "GETBIT";
  private static final byte[] GETBIT_BYTES = GETBIT.getBytes(Charsets.US_ASCII);
  private static final int GETBIT_VERSION = parseVersion("2.2.0");

  /**
   * Returns the bit value at offset in the string value stored at key
   * String
   *
   * @param key0
   * @param offset1
   * @return IntegerReply
   */
  public Promise<IntegerReply> getbit(Object key0, Object offset1) {
    if (version < GETBIT_VERSION) return new Promise<>(new RedisException("Server does not support GETBIT"));
    return execute(IntegerReply.class, new Command(GETBIT_BYTES, key0, offset1));
  }

  private static final String GETRANGE = "GETRANGE";
  private static final byte[] GETRANGE_BYTES = GETRANGE.getBytes(Charsets.US_ASCII);
  private static final int GETRANGE_VERSION = parseVersion("2.4.0");

  /**
   * Get a substring of the string stored at a key
   * String
   *
   * @param key0
   * @param start1
   * @param end2
   * @return BulkReply
   */
  public Promise<BulkReply> getrange(Object key0, Object start1, Object end2) {
    if (version < GETRANGE_VERSION) return new Promise<>(new RedisException("Server does not support GETRANGE"));
    return execute(BulkReply.class, new Command(GETRANGE_BYTES, key0, start1, end2));
  }

  private static final String GETSET = "GETSET";
  private static final byte[] GETSET_BYTES = GETSET.getBytes(Charsets.US_ASCII);
  private static final int GETSET_VERSION = parseVersion("1.0.0");

  /**
   * Set the string value of a key and return its old value
   * String
   *
   * @param key0
   * @param value1
   * @return BulkReply
   */
  public Promise<BulkReply> getset(Object key0, Object value1) {
    if (version < GETSET_VERSION) return new Promise<>(new RedisException("Server does not support GETSET"));
    return execute(BulkReply.class, new Command(GETSET_BYTES, key0, value1));
  }

  private static final String INCR = "INCR";
  private static final byte[] INCR_BYTES = INCR.getBytes(Charsets.US_ASCII);
  private static final int INCR_VERSION = parseVersion("1.0.0");

  /**
   * Increment the integer value of a key by one
   * String
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> incr(Object key0) {
    if (version < INCR_VERSION) return new Promise<>(new RedisException("Server does not support INCR"));
    return execute(IntegerReply.class, new Command(INCR_BYTES, key0));
  }

  private static final String INCRBY = "INCRBY";
  private static final byte[] INCRBY_BYTES = INCRBY.getBytes(Charsets.US_ASCII);
  private static final int INCRBY_VERSION = parseVersion("1.0.0");

  /**
   * Increment the integer value of a key by the given amount
   * String
   *
   * @param key0
   * @param increment1
   * @return IntegerReply
   */
  public Promise<IntegerReply> incrby(Object key0, Object increment1) {
    if (version < INCRBY_VERSION) return new Promise<>(new RedisException("Server does not support INCRBY"));
    return execute(IntegerReply.class, new Command(INCRBY_BYTES, key0, increment1));
  }

  private static final String INCRBYFLOAT = "INCRBYFLOAT";
  private static final byte[] INCRBYFLOAT_BYTES = INCRBYFLOAT.getBytes(Charsets.US_ASCII);
  private static final int INCRBYFLOAT_VERSION = parseVersion("2.6.0");

  /**
   * Increment the float value of a key by the given amount
   * String
   *
   * @param key0
   * @param increment1
   * @return BulkReply
   */
  public Promise<BulkReply> incrbyfloat(Object key0, Object increment1) {
    if (version < INCRBYFLOAT_VERSION) return new Promise<>(new RedisException("Server does not support INCRBYFLOAT"));
    return execute(BulkReply.class, new Command(INCRBYFLOAT_BYTES, key0, increment1));
  }

  private static final String MGET = "MGET";
  private static final byte[] MGET_BYTES = MGET.getBytes(Charsets.US_ASCII);
  private static final int MGET_VERSION = parseVersion("1.0.0");

  /**
   * Get the values of all the given keys
   * String
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> mget(Object[] key0) {
    if (version < MGET_VERSION) return new Promise<>(new RedisException("Server does not support MGET"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(MultiBulkReply.class, new Command(MGET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> mget_(Object... arguments) {
    if (version < MGET_VERSION) return new Promise<>(new RedisException("Server does not support MGET"));
    return execute(MultiBulkReply.class, new Command(MGET_BYTES, arguments));
  }

  private static final String MSET = "MSET";
  private static final byte[] MSET_BYTES = MSET.getBytes(Charsets.US_ASCII);
  private static final int MSET_VERSION = parseVersion("1.0.1");

  /**
   * Set multiple keys to multiple values
   * String
   *
   * @param key_or_value0
   * @return StatusReply
   */
  public Promise<StatusReply> mset(Object[] key_or_value0) {
    if (version < MSET_VERSION) return new Promise<>(new RedisException("Server does not support MSET"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key_or_value0);
    return execute(StatusReply.class, new Command(MSET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<StatusReply> mset_(Object... arguments) {
    if (version < MSET_VERSION) return new Promise<>(new RedisException("Server does not support MSET"));
    return execute(StatusReply.class, new Command(MSET_BYTES, arguments));
  }

  private static final String MSETNX = "MSETNX";
  private static final byte[] MSETNX_BYTES = MSETNX.getBytes(Charsets.US_ASCII);
  private static final int MSETNX_VERSION = parseVersion("1.0.1");

  /**
   * Set multiple keys to multiple values, only if none of the keys exist
   * String
   *
   * @param key_or_value0
   * @return IntegerReply
   */
  public Promise<IntegerReply> msetnx(Object[] key_or_value0) {
    if (version < MSETNX_VERSION) return new Promise<>(new RedisException("Server does not support MSETNX"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key_or_value0);
    return execute(IntegerReply.class, new Command(MSETNX_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> msetnx_(Object... arguments) {
    if (version < MSETNX_VERSION) return new Promise<>(new RedisException("Server does not support MSETNX"));
    return execute(IntegerReply.class, new Command(MSETNX_BYTES, arguments));
  }

  private static final String PSETEX = "PSETEX";
  private static final byte[] PSETEX_BYTES = PSETEX.getBytes(Charsets.US_ASCII);
  private static final int PSETEX_VERSION = parseVersion("2.6.0");

  /**
   * Set the value and expiration in milliseconds of a key
   * String
   *
   * @param key0
   * @param milliseconds1
   * @param value2
   * @return Reply
   */
  public Promise<Reply> psetex(Object key0, Object milliseconds1, Object value2) {
    if (version < PSETEX_VERSION) return new Promise<>(new RedisException("Server does not support PSETEX"));
    return execute(Reply.class, new Command(PSETEX_BYTES, key0, milliseconds1, value2));
  }

  private static final String SET = "SET";
  private static final byte[] SET_BYTES = SET.getBytes(Charsets.US_ASCII);
  private static final int SET_VERSION = parseVersion("1.0.0");

  /**
   * Set the string value of a key
   * String
   *
   * @param key0
   * @param value1
   * @return StatusReply
   */
  public Promise<StatusReply> set(Object key0, Object value1) {
    if (version < SET_VERSION) return new Promise<>(new RedisException("Server does not support SET"));
    return execute(StatusReply.class, new Command(SET_BYTES, key0, value1));
  }

  private static final String SETBIT = "SETBIT";
  private static final byte[] SETBIT_BYTES = SETBIT.getBytes(Charsets.US_ASCII);
  private static final int SETBIT_VERSION = parseVersion("2.2.0");

  /**
   * Sets or clears the bit at offset in the string value stored at key
   * String
   *
   * @param key0
   * @param offset1
   * @param value2
   * @return IntegerReply
   */
  public Promise<IntegerReply> setbit(Object key0, Object offset1, Object value2) {
    if (version < SETBIT_VERSION) return new Promise<>(new RedisException("Server does not support SETBIT"));
    return execute(IntegerReply.class, new Command(SETBIT_BYTES, key0, offset1, value2));
  }

  private static final String SETEX = "SETEX";
  private static final byte[] SETEX_BYTES = SETEX.getBytes(Charsets.US_ASCII);
  private static final int SETEX_VERSION = parseVersion("2.0.0");

  /**
   * Set the value and expiration of a key
   * String
   *
   * @param key0
   * @param seconds1
   * @param value2
   * @return StatusReply
   */
  public Promise<StatusReply> setex(Object key0, Object seconds1, Object value2) {
    if (version < SETEX_VERSION) return new Promise<>(new RedisException("Server does not support SETEX"));
    return execute(StatusReply.class, new Command(SETEX_BYTES, key0, seconds1, value2));
  }

  private static final String SETNX = "SETNX";
  private static final byte[] SETNX_BYTES = SETNX.getBytes(Charsets.US_ASCII);
  private static final int SETNX_VERSION = parseVersion("1.0.0");

  /**
   * Set the value of a key, only if the key does not exist
   * String
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public Promise<IntegerReply> setnx(Object key0, Object value1) {
    if (version < SETNX_VERSION) return new Promise<>(new RedisException("Server does not support SETNX"));
    return execute(IntegerReply.class, new Command(SETNX_BYTES, key0, value1));
  }

  private static final String SETRANGE = "SETRANGE";
  private static final byte[] SETRANGE_BYTES = SETRANGE.getBytes(Charsets.US_ASCII);
  private static final int SETRANGE_VERSION = parseVersion("2.2.0");

  /**
   * Overwrite part of a string at key starting at the specified offset
   * String
   *
   * @param key0
   * @param offset1
   * @param value2
   * @return IntegerReply
   */
  public Promise<IntegerReply> setrange(Object key0, Object offset1, Object value2) {
    if (version < SETRANGE_VERSION) return new Promise<>(new RedisException("Server does not support SETRANGE"));
    return execute(IntegerReply.class, new Command(SETRANGE_BYTES, key0, offset1, value2));
  }

  private static final String STRLEN = "STRLEN";
  private static final byte[] STRLEN_BYTES = STRLEN.getBytes(Charsets.US_ASCII);
  private static final int STRLEN_VERSION = parseVersion("2.2.0");

  /**
   * Get the length of the value stored in a key
   * String
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> strlen(Object key0) {
    if (version < STRLEN_VERSION) return new Promise<>(new RedisException("Server does not support STRLEN"));
    return execute(IntegerReply.class, new Command(STRLEN_BYTES, key0));
  }

  private static final String ECHO = "ECHO";
  private static final byte[] ECHO_BYTES = ECHO.getBytes(Charsets.US_ASCII);
  private static final int ECHO_VERSION = parseVersion("1.0.0");

  /**
   * Echo the given string
   * Connection
   *
   * @param message0
   * @return BulkReply
   */
  public Promise<BulkReply> echo(Object message0) {
    if (version < ECHO_VERSION) return new Promise<>(new RedisException("Server does not support ECHO"));
    return execute(BulkReply.class, new Command(ECHO_BYTES, message0));
  }

  private static final String PING = "PING";
  private static final byte[] PING_BYTES = PING.getBytes(Charsets.US_ASCII);
  private static final int PING_VERSION = parseVersion("1.0.0");

  /**
   * Ping the server
   * Connection
   *
   * @return StatusReply
   */
  public Promise<StatusReply> ping() {
    if (version < PING_VERSION) return new Promise<>(new RedisException("Server does not support PING"));
    return execute(StatusReply.class, new Command(PING_BYTES));
  }

  private static final String QUIT = "QUIT";
  private static final byte[] QUIT_BYTES = QUIT.getBytes(Charsets.US_ASCII);
  private static final int QUIT_VERSION = parseVersion("1.0.0");

  /**
   * Close the connection
   * Connection
   *
   * @return StatusReply
   */
  public Promise<StatusReply> quit() {
    if (version < QUIT_VERSION) return new Promise<>(new RedisException("Server does not support QUIT"));
    return execute(StatusReply.class, new Command(QUIT_BYTES));
  }

  private static final String SELECT = "SELECT";
  private static final byte[] SELECT_BYTES = SELECT.getBytes(Charsets.US_ASCII);
  private static final int SELECT_VERSION = parseVersion("1.0.0");

  /**
   * Change the selected database for the current connection
   * Connection
   *
   * @param index0
   * @return StatusReply
   */
  public Promise<StatusReply> select(Object index0) {
    if (version < SELECT_VERSION) return new Promise<>(new RedisException("Server does not support SELECT"));
    return execute(StatusReply.class, new Command(SELECT_BYTES, index0));
  }

  private static final String BGREWRITEAOF = "BGREWRITEAOF";
  private static final byte[] BGREWRITEAOF_BYTES = BGREWRITEAOF.getBytes(Charsets.US_ASCII);
  private static final int BGREWRITEAOF_VERSION = parseVersion("1.0.0");

  /**
   * Asynchronously rewrite the append-only file
   * Server
   *
   * @return StatusReply
   */
  public Promise<StatusReply> bgrewriteaof() {
    if (version < BGREWRITEAOF_VERSION) return new Promise<>(new RedisException("Server does not support BGREWRITEAOF"));
    return execute(StatusReply.class, new Command(BGREWRITEAOF_BYTES));
  }

  private static final String BGSAVE = "BGSAVE";
  private static final byte[] BGSAVE_BYTES = BGSAVE.getBytes(Charsets.US_ASCII);
  private static final int BGSAVE_VERSION = parseVersion("1.0.0");

  /**
   * Asynchronously save the dataset to disk
   * Server
   *
   * @return StatusReply
   */
  public Promise<StatusReply> bgsave() {
    if (version < BGSAVE_VERSION) return new Promise<>(new RedisException("Server does not support BGSAVE"));
    return execute(StatusReply.class, new Command(BGSAVE_BYTES));
  }

  private static final String CLIENT_KILL = "CLIENT";
  private static final String CLIENT_KILL2 = "KILL";
  private static final byte[] CLIENT_KILL2_BYTES = CLIENT_KILL2.getBytes(Charsets.US_ASCII);
  private static final byte[] CLIENT_KILL_BYTES = CLIENT_KILL.getBytes(Charsets.US_ASCII);
  private static final int CLIENT_KILL_VERSION = parseVersion("2.4.0");

  /**
   * Kill the connection of a client
   * Server
   *
   * @param ip_port0
   * @return Reply
   */
  public Promise<Reply> client_kill(Object ip_port0) {
    if (version < CLIENT_KILL_VERSION) return new Promise<>(new RedisException("Server does not support CLIENT_KILL"));
    return execute(Reply.class, new Command(CLIENT_KILL_BYTES, CLIENT_KILL2_BYTES, ip_port0));
  }

  private static final String CLIENT_LIST = "CLIENT";
  private static final String CLIENT_LIST2 = "LIST";
  private static final byte[] CLIENT_LIST2_BYTES = CLIENT_LIST2.getBytes(Charsets.US_ASCII);
  private static final byte[] CLIENT_LIST_BYTES = CLIENT_LIST.getBytes(Charsets.US_ASCII);
  private static final int CLIENT_LIST_VERSION = parseVersion("2.4.0");

  /**
   * Get the list of client connections
   * Server
   *
   * @return Reply
   */
  public Promise<Reply> client_list() {
    if (version < CLIENT_LIST_VERSION) return new Promise<>(new RedisException("Server does not support CLIENT_LIST"));
    return execute(Reply.class, new Command(CLIENT_LIST_BYTES, CLIENT_LIST2_BYTES));
  }

  private static final String CLIENT_GETNAME = "CLIENT";
  private static final String CLIENT_GETNAME2 = "GETNAME";
  private static final byte[] CLIENT_GETNAME2_BYTES = CLIENT_GETNAME2.getBytes(Charsets.US_ASCII);
  private static final byte[] CLIENT_GETNAME_BYTES = CLIENT_GETNAME.getBytes(Charsets.US_ASCII);
  private static final int CLIENT_GETNAME_VERSION = parseVersion("2.6.9");

  /**
   * Get the current connection name
   * Server
   *
   * @return Reply
   */
  public Promise<Reply> client_getname() {
    if (version < CLIENT_GETNAME_VERSION) return new Promise<>(new RedisException("Server does not support CLIENT_GETNAME"));
    return execute(Reply.class, new Command(CLIENT_GETNAME_BYTES, CLIENT_GETNAME2_BYTES));
  }

  private static final String CLIENT_SETNAME = "CLIENT";
  private static final String CLIENT_SETNAME2 = "SETNAME";
  private static final byte[] CLIENT_SETNAME2_BYTES = CLIENT_SETNAME2.getBytes(Charsets.US_ASCII);
  private static final byte[] CLIENT_SETNAME_BYTES = CLIENT_SETNAME.getBytes(Charsets.US_ASCII);
  private static final int CLIENT_SETNAME_VERSION = parseVersion("2.6.9");

  /**
   * Set the current connection name
   * Server
   *
   * @param connection_name0
   * @return Reply
   */
  public Promise<Reply> client_setname(Object connection_name0) {
    if (version < CLIENT_SETNAME_VERSION) return new Promise<>(new RedisException("Server does not support CLIENT_SETNAME"));
    return execute(Reply.class, new Command(CLIENT_SETNAME_BYTES, CLIENT_SETNAME2_BYTES, connection_name0));
  }

  private static final String CONFIG_GET = "CONFIG";
  private static final String CONFIG_GET2 = "GET";
  private static final byte[] CONFIG_GET2_BYTES = CONFIG_GET2.getBytes(Charsets.US_ASCII);
  private static final byte[] CONFIG_GET_BYTES = CONFIG_GET.getBytes(Charsets.US_ASCII);
  private static final int CONFIG_GET_VERSION = parseVersion("2.0.0");

  /**
   * Get the value of a configuration parameter
   * Server
   *
   * @param parameter0
   * @return Reply
   */
  public Promise<Reply> config_get(Object parameter0) {
    if (version < CONFIG_GET_VERSION) return new Promise<>(new RedisException("Server does not support CONFIG_GET"));
    return execute(Reply.class, new Command(CONFIG_GET_BYTES, CONFIG_GET2_BYTES, parameter0));
  }

  private static final String CONFIG_SET = "CONFIG";
  private static final String CONFIG_SET2 = "SET";
  private static final byte[] CONFIG_SET2_BYTES = CONFIG_SET2.getBytes(Charsets.US_ASCII);
  private static final byte[] CONFIG_SET_BYTES = CONFIG_SET.getBytes(Charsets.US_ASCII);
  private static final int CONFIG_SET_VERSION = parseVersion("2.0.0");

  /**
   * Set a configuration parameter to the given value
   * Server
   *
   * @param parameter0
   * @param value1
   * @return Reply
   */
  public Promise<Reply> config_set(Object parameter0, Object value1) {
    if (version < CONFIG_SET_VERSION) return new Promise<>(new RedisException("Server does not support CONFIG_SET"));
    return execute(Reply.class, new Command(CONFIG_SET_BYTES, CONFIG_SET2_BYTES, parameter0, value1));
  }

  private static final String CONFIG_RESETSTAT = "CONFIG";
  private static final String CONFIG_RESETSTAT2 = "RESETSTAT";
  private static final byte[] CONFIG_RESETSTAT2_BYTES = CONFIG_RESETSTAT2.getBytes(Charsets.US_ASCII);
  private static final byte[] CONFIG_RESETSTAT_BYTES = CONFIG_RESETSTAT.getBytes(Charsets.US_ASCII);
  private static final int CONFIG_RESETSTAT_VERSION = parseVersion("2.0.0");

  /**
   * Reset the stats returned by INFO
   * Server
   *
   * @return Reply
   */
  public Promise<Reply> config_resetstat() {
    if (version < CONFIG_RESETSTAT_VERSION) return new Promise<>(new RedisException("Server does not support CONFIG_RESETSTAT"));
    return execute(Reply.class, new Command(CONFIG_RESETSTAT_BYTES, CONFIG_RESETSTAT2_BYTES));
  }

  private static final String DBSIZE = "DBSIZE";
  private static final byte[] DBSIZE_BYTES = DBSIZE.getBytes(Charsets.US_ASCII);
  private static final int DBSIZE_VERSION = parseVersion("1.0.0");

  /**
   * Return the number of keys in the selected database
   * Server
   *
   * @return IntegerReply
   */
  public Promise<IntegerReply> dbsize() {
    if (version < DBSIZE_VERSION) return new Promise<>(new RedisException("Server does not support DBSIZE"));
    return execute(IntegerReply.class, new Command(DBSIZE_BYTES));
  }

  private static final String DEBUG_OBJECT = "DEBUG";
  private static final String DEBUG_OBJECT2 = "OBJECT";
  private static final byte[] DEBUG_OBJECT2_BYTES = DEBUG_OBJECT2.getBytes(Charsets.US_ASCII);
  private static final byte[] DEBUG_OBJECT_BYTES = DEBUG_OBJECT.getBytes(Charsets.US_ASCII);
  private static final int DEBUG_OBJECT_VERSION = parseVersion("1.0.0");

  /**
   * Get debugging information about a key
   * Server
   *
   * @param key0
   * @return Reply
   */
  public Promise<Reply> debug_object(Object key0) {
    if (version < DEBUG_OBJECT_VERSION) return new Promise<>(new RedisException("Server does not support DEBUG_OBJECT"));
    return execute(Reply.class, new Command(DEBUG_OBJECT_BYTES, DEBUG_OBJECT2_BYTES, key0));
  }

  private static final String DEBUG_SEGFAULT = "DEBUG";
  private static final String DEBUG_SEGFAULT2 = "SEGFAULT";
  private static final byte[] DEBUG_SEGFAULT2_BYTES = DEBUG_SEGFAULT2.getBytes(Charsets.US_ASCII);
  private static final byte[] DEBUG_SEGFAULT_BYTES = DEBUG_SEGFAULT.getBytes(Charsets.US_ASCII);
  private static final int DEBUG_SEGFAULT_VERSION = parseVersion("1.0.0");

  /**
   * Make the server crash
   * Server
   *
   * @return Reply
   */
  public Promise<Reply> debug_segfault() {
    if (version < DEBUG_SEGFAULT_VERSION) return new Promise<>(new RedisException("Server does not support DEBUG_SEGFAULT"));
    return execute(Reply.class, new Command(DEBUG_SEGFAULT_BYTES, DEBUG_SEGFAULT2_BYTES));
  }

  private static final String FLUSHALL = "FLUSHALL";
  private static final byte[] FLUSHALL_BYTES = FLUSHALL.getBytes(Charsets.US_ASCII);
  private static final int FLUSHALL_VERSION = parseVersion("1.0.0");

  /**
   * Remove all keys from all databases
   * Server
   *
   * @return StatusReply
   */
  public Promise<StatusReply> flushall() {
    if (version < FLUSHALL_VERSION) return new Promise<>(new RedisException("Server does not support FLUSHALL"));
    return execute(StatusReply.class, new Command(FLUSHALL_BYTES));
  }

  private static final String FLUSHDB = "FLUSHDB";
  private static final byte[] FLUSHDB_BYTES = FLUSHDB.getBytes(Charsets.US_ASCII);
  private static final int FLUSHDB_VERSION = parseVersion("1.0.0");

  /**
   * Remove all keys from the current database
   * Server
   *
   * @return StatusReply
   */
  public Promise<StatusReply> flushdb() {
    if (version < FLUSHDB_VERSION) return new Promise<>(new RedisException("Server does not support FLUSHDB"));
    return execute(StatusReply.class, new Command(FLUSHDB_BYTES));
  }

  private static final String INFO = "INFO";
  private static final byte[] INFO_BYTES = INFO.getBytes(Charsets.US_ASCII);
  private static final int INFO_VERSION = parseVersion("1.0.0");

  /**
   * Get information and statistics about the server
   * Server
   *
   * @param section0
   * @return BulkReply
   */
  public Promise<BulkReply> info(Object section0) {
    if (version < INFO_VERSION) return new Promise<>(new RedisException("Server does not support INFO"));
    return execute(BulkReply.class, new Command(INFO_BYTES, section0));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<BulkReply> info_(Object... arguments) {
    if (version < INFO_VERSION) return new Promise<>(new RedisException("Server does not support INFO"));
    return execute(BulkReply.class, new Command(INFO_BYTES, arguments));
  }

  private static final String LASTSAVE = "LASTSAVE";
  private static final byte[] LASTSAVE_BYTES = LASTSAVE.getBytes(Charsets.US_ASCII);
  private static final int LASTSAVE_VERSION = parseVersion("1.0.0");

  /**
   * Get the UNIX time stamp of the last successful save to disk
   * Server
   *
   * @return IntegerReply
   */
  public Promise<IntegerReply> lastsave() {
    if (version < LASTSAVE_VERSION) return new Promise<>(new RedisException("Server does not support LASTSAVE"));
    return execute(IntegerReply.class, new Command(LASTSAVE_BYTES));
  }

  private static final String MONITOR = "MONITOR";
  private static final byte[] MONITOR_BYTES = MONITOR.getBytes(Charsets.US_ASCII);
  private static final int MONITOR_VERSION = parseVersion("1.0.0");

  /**
   * Listen for all requests received by the server in real time
   * Server
   *
   * @return Reply
   */
  public Promise<Reply> monitor() {
    if (version < MONITOR_VERSION) return new Promise<>(new RedisException("Server does not support MONITOR"));
    return execute(Reply.class, new Command(MONITOR_BYTES));
  }

  private static final String SAVE = "SAVE";
  private static final byte[] SAVE_BYTES = SAVE.getBytes(Charsets.US_ASCII);
  private static final int SAVE_VERSION = parseVersion("1.0.0");

  /**
   * Synchronously save the dataset to disk
   * Server
   *
   * @return Reply
   */
  public Promise<Reply> save() {
    if (version < SAVE_VERSION) return new Promise<>(new RedisException("Server does not support SAVE"));
    return execute(Reply.class, new Command(SAVE_BYTES));
  }

  private static final String SHUTDOWN = "SHUTDOWN";
  private static final byte[] SHUTDOWN_BYTES = SHUTDOWN.getBytes(Charsets.US_ASCII);
  private static final int SHUTDOWN_VERSION = parseVersion("1.0.0");

  /**
   * Synchronously save the dataset to disk and then shut down the server
   * Server
   *
   * @param NOSAVE0
   * @param SAVE1
   * @return StatusReply
   */
  public Promise<StatusReply> shutdown(Object NOSAVE0, Object SAVE1) {
    if (version < SHUTDOWN_VERSION) return new Promise<>(new RedisException("Server does not support SHUTDOWN"));
    return execute(StatusReply.class, new Command(SHUTDOWN_BYTES, NOSAVE0, SAVE1));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<StatusReply> shutdown_(Object... arguments) {
    if (version < SHUTDOWN_VERSION) return new Promise<>(new RedisException("Server does not support SHUTDOWN"));
    return execute(StatusReply.class, new Command(SHUTDOWN_BYTES, arguments));
  }

  private static final String SLAVEOF = "SLAVEOF";
  private static final byte[] SLAVEOF_BYTES = SLAVEOF.getBytes(Charsets.US_ASCII);
  private static final int SLAVEOF_VERSION = parseVersion("1.0.0");

  /**
   * Make the server a slave of another instance, or promote it as master
   * Server
   *
   * @param host0
   * @param port1
   * @return StatusReply
   */
  public Promise<StatusReply> slaveof(Object host0, Object port1) {
    if (version < SLAVEOF_VERSION) return new Promise<>(new RedisException("Server does not support SLAVEOF"));
    return execute(StatusReply.class, new Command(SLAVEOF_BYTES, host0, port1));
  }

  private static final String SLOWLOG = "SLOWLOG";
  private static final byte[] SLOWLOG_BYTES = SLOWLOG.getBytes(Charsets.US_ASCII);
  private static final int SLOWLOG_VERSION = parseVersion("2.2.12");

  /**
   * Manages the Redis slow queries log
   * Server
   *
   * @param subcommand0
   * @param argument1
   * @return Reply
   */
  public Promise<Reply> slowlog(Object subcommand0, Object argument1) {
    if (version < SLOWLOG_VERSION) return new Promise<>(new RedisException("Server does not support SLOWLOG"));
    return execute(Reply.class, new Command(SLOWLOG_BYTES, subcommand0, argument1));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<Reply> slowlog_(Object... arguments) {
    if (version < SLOWLOG_VERSION) return new Promise<>(new RedisException("Server does not support SLOWLOG"));
    return execute(Reply.class, new Command(SLOWLOG_BYTES, arguments));
  }

  private static final String SYNC = "SYNC";
  private static final byte[] SYNC_BYTES = SYNC.getBytes(Charsets.US_ASCII);
  private static final int SYNC_VERSION = parseVersion("1.0.0");

  /**
   * Internal command used for replication
   * Server
   *
   * @return Reply
   */
  public Promise<Reply> sync() {
    if (version < SYNC_VERSION) return new Promise<>(new RedisException("Server does not support SYNC"));
    return execute(Reply.class, new Command(SYNC_BYTES));
  }

  private static final String TIME = "TIME";
  private static final byte[] TIME_BYTES = TIME.getBytes(Charsets.US_ASCII);
  private static final int TIME_VERSION = parseVersion("2.6.0");

  /**
   * Return the current server time
   * Server
   *
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> time() {
    if (version < TIME_VERSION) return new Promise<>(new RedisException("Server does not support TIME"));
    return execute(MultiBulkReply.class, new Command(TIME_BYTES));
  }

  private static final String BLPOP = "BLPOP";
  private static final byte[] BLPOP_BYTES = BLPOP.getBytes(Charsets.US_ASCII);
  private static final int BLPOP_VERSION = parseVersion("2.0.0");

  /**
   * Remove and get the first element in a list, or block until one is available
   * List
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> blpop(Object[] key0) {
    if (version < BLPOP_VERSION) return new Promise<>(new RedisException("Server does not support BLPOP"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(MultiBulkReply.class, new Command(BLPOP_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> blpop_(Object... arguments) {
    if (version < BLPOP_VERSION) return new Promise<>(new RedisException("Server does not support BLPOP"));
    return execute(MultiBulkReply.class, new Command(BLPOP_BYTES, arguments));
  }

  private static final String BRPOP = "BRPOP";
  private static final byte[] BRPOP_BYTES = BRPOP.getBytes(Charsets.US_ASCII);
  private static final int BRPOP_VERSION = parseVersion("2.0.0");

  /**
   * Remove and get the last element in a list, or block until one is available
   * List
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> brpop(Object[] key0) {
    if (version < BRPOP_VERSION) return new Promise<>(new RedisException("Server does not support BRPOP"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(MultiBulkReply.class, new Command(BRPOP_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> brpop_(Object... arguments) {
    if (version < BRPOP_VERSION) return new Promise<>(new RedisException("Server does not support BRPOP"));
    return execute(MultiBulkReply.class, new Command(BRPOP_BYTES, arguments));
  }

  private static final String BRPOPLPUSH = "BRPOPLPUSH";
  private static final byte[] BRPOPLPUSH_BYTES = BRPOPLPUSH.getBytes(Charsets.US_ASCII);
  private static final int BRPOPLPUSH_VERSION = parseVersion("2.2.0");

  /**
   * Pop a value from a list, push it to another list and return it; or block until one is available
   * List
   *
   * @param source0
   * @param destination1
   * @param timeout2
   * @return BulkReply
   */
  public Promise<BulkReply> brpoplpush(Object source0, Object destination1, Object timeout2) {
    if (version < BRPOPLPUSH_VERSION) return new Promise<>(new RedisException("Server does not support BRPOPLPUSH"));
    return execute(BulkReply.class, new Command(BRPOPLPUSH_BYTES, source0, destination1, timeout2));
  }

  private static final String LINDEX = "LINDEX";
  private static final byte[] LINDEX_BYTES = LINDEX.getBytes(Charsets.US_ASCII);
  private static final int LINDEX_VERSION = parseVersion("1.0.0");

  /**
   * Get an element from a list by its index
   * List
   *
   * @param key0
   * @param index1
   * @return BulkReply
   */
  public Promise<BulkReply> lindex(Object key0, Object index1) {
    if (version < LINDEX_VERSION) return new Promise<>(new RedisException("Server does not support LINDEX"));
    return execute(BulkReply.class, new Command(LINDEX_BYTES, key0, index1));
  }

  private static final String LINSERT = "LINSERT";
  private static final byte[] LINSERT_BYTES = LINSERT.getBytes(Charsets.US_ASCII);
  private static final int LINSERT_VERSION = parseVersion("2.2.0");

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
  public Promise<IntegerReply> linsert(Object key0, Object where1, Object pivot2, Object value3) {
    if (version < LINSERT_VERSION) return new Promise<>(new RedisException("Server does not support LINSERT"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    list.add(where1);
    list.add(pivot2);
    list.add(value3);
    return execute(IntegerReply.class, new Command(LINSERT_BYTES, list.toArray(new Object[list.size()])));
  }

  private static final String LLEN = "LLEN";
  private static final byte[] LLEN_BYTES = LLEN.getBytes(Charsets.US_ASCII);
  private static final int LLEN_VERSION = parseVersion("1.0.0");

  /**
   * Get the length of a list
   * List
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> llen(Object key0) {
    if (version < LLEN_VERSION) return new Promise<>(new RedisException("Server does not support LLEN"));
    return execute(IntegerReply.class, new Command(LLEN_BYTES, key0));
  }

  private static final String LPOP = "LPOP";
  private static final byte[] LPOP_BYTES = LPOP.getBytes(Charsets.US_ASCII);
  private static final int LPOP_VERSION = parseVersion("1.0.0");

  /**
   * Remove and get the first element in a list
   * List
   *
   * @param key0
   * @return BulkReply
   */
  public Promise<BulkReply> lpop(Object key0) {
    if (version < LPOP_VERSION) return new Promise<>(new RedisException("Server does not support LPOP"));
    return execute(BulkReply.class, new Command(LPOP_BYTES, key0));
  }

  private static final String LPUSH = "LPUSH";
  private static final byte[] LPUSH_BYTES = LPUSH.getBytes(Charsets.US_ASCII);
  private static final int LPUSH_VERSION = parseVersion("1.0.0");

  /**
   * Prepend one or multiple values to a list
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public Promise<IntegerReply> lpush(Object key0, Object[] value1) {
    if (version < LPUSH_VERSION) return new Promise<>(new RedisException("Server does not support LPUSH"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, value1);
    return execute(IntegerReply.class, new Command(LPUSH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> lpush_(Object... arguments) {
    if (version < LPUSH_VERSION) return new Promise<>(new RedisException("Server does not support LPUSH"));
    return execute(IntegerReply.class, new Command(LPUSH_BYTES, arguments));
  }

  private static final String LPUSHX = "LPUSHX";
  private static final byte[] LPUSHX_BYTES = LPUSHX.getBytes(Charsets.US_ASCII);
  private static final int LPUSHX_VERSION = parseVersion("2.2.0");

  /**
   * Prepend a value to a list, only if the list exists
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public Promise<IntegerReply> lpushx(Object key0, Object value1) {
    if (version < LPUSHX_VERSION) return new Promise<>(new RedisException("Server does not support LPUSHX"));
    return execute(IntegerReply.class, new Command(LPUSHX_BYTES, key0, value1));
  }

  private static final String LRANGE = "LRANGE";
  private static final byte[] LRANGE_BYTES = LRANGE.getBytes(Charsets.US_ASCII);
  private static final int LRANGE_VERSION = parseVersion("1.0.0");

  /**
   * Get a range of elements from a list
   * List
   *
   * @param key0
   * @param start1
   * @param stop2
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> lrange(Object key0, Object start1, Object stop2) {
    if (version < LRANGE_VERSION) return new Promise<>(new RedisException("Server does not support LRANGE"));
    return execute(MultiBulkReply.class, new Command(LRANGE_BYTES, key0, start1, stop2));
  }

  private static final String LREM = "LREM";
  private static final byte[] LREM_BYTES = LREM.getBytes(Charsets.US_ASCII);
  private static final int LREM_VERSION = parseVersion("1.0.0");

  /**
   * Remove elements from a list
   * List
   *
   * @param key0
   * @param count1
   * @param value2
   * @return IntegerReply
   */
  public Promise<IntegerReply> lrem(Object key0, Object count1, Object value2) {
    if (version < LREM_VERSION) return new Promise<>(new RedisException("Server does not support LREM"));
    return execute(IntegerReply.class, new Command(LREM_BYTES, key0, count1, value2));
  }

  private static final String LSET = "LSET";
  private static final byte[] LSET_BYTES = LSET.getBytes(Charsets.US_ASCII);
  private static final int LSET_VERSION = parseVersion("1.0.0");

  /**
   * Set the value of an element in a list by its index
   * List
   *
   * @param key0
   * @param index1
   * @param value2
   * @return StatusReply
   */
  public Promise<StatusReply> lset(Object key0, Object index1, Object value2) {
    if (version < LSET_VERSION) return new Promise<>(new RedisException("Server does not support LSET"));
    return execute(StatusReply.class, new Command(LSET_BYTES, key0, index1, value2));
  }

  private static final String LTRIM = "LTRIM";
  private static final byte[] LTRIM_BYTES = LTRIM.getBytes(Charsets.US_ASCII);
  private static final int LTRIM_VERSION = parseVersion("1.0.0");

  /**
   * Trim a list to the specified range
   * List
   *
   * @param key0
   * @param start1
   * @param stop2
   * @return StatusReply
   */
  public Promise<StatusReply> ltrim(Object key0, Object start1, Object stop2) {
    if (version < LTRIM_VERSION) return new Promise<>(new RedisException("Server does not support LTRIM"));
    return execute(StatusReply.class, new Command(LTRIM_BYTES, key0, start1, stop2));
  }

  private static final String RPOP = "RPOP";
  private static final byte[] RPOP_BYTES = RPOP.getBytes(Charsets.US_ASCII);
  private static final int RPOP_VERSION = parseVersion("1.0.0");

  /**
   * Remove and get the last element in a list
   * List
   *
   * @param key0
   * @return BulkReply
   */
  public Promise<BulkReply> rpop(Object key0) {
    if (version < RPOP_VERSION) return new Promise<>(new RedisException("Server does not support RPOP"));
    return execute(BulkReply.class, new Command(RPOP_BYTES, key0));
  }

  private static final String RPOPLPUSH = "RPOPLPUSH";
  private static final byte[] RPOPLPUSH_BYTES = RPOPLPUSH.getBytes(Charsets.US_ASCII);
  private static final int RPOPLPUSH_VERSION = parseVersion("1.2.0");

  /**
   * Remove the last element in a list, append it to another list and return it
   * List
   *
   * @param source0
   * @param destination1
   * @return BulkReply
   */
  public Promise<BulkReply> rpoplpush(Object source0, Object destination1) {
    if (version < RPOPLPUSH_VERSION) return new Promise<>(new RedisException("Server does not support RPOPLPUSH"));
    return execute(BulkReply.class, new Command(RPOPLPUSH_BYTES, source0, destination1));
  }

  private static final String RPUSH = "RPUSH";
  private static final byte[] RPUSH_BYTES = RPUSH.getBytes(Charsets.US_ASCII);
  private static final int RPUSH_VERSION = parseVersion("1.0.0");

  /**
   * Append one or multiple values to a list
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public Promise<IntegerReply> rpush(Object key0, Object[] value1) {
    if (version < RPUSH_VERSION) return new Promise<>(new RedisException("Server does not support RPUSH"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, value1);
    return execute(IntegerReply.class, new Command(RPUSH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> rpush_(Object... arguments) {
    if (version < RPUSH_VERSION) return new Promise<>(new RedisException("Server does not support RPUSH"));
    return execute(IntegerReply.class, new Command(RPUSH_BYTES, arguments));
  }

  private static final String RPUSHX = "RPUSHX";
  private static final byte[] RPUSHX_BYTES = RPUSHX.getBytes(Charsets.US_ASCII);
  private static final int RPUSHX_VERSION = parseVersion("2.2.0");

  /**
   * Append a value to a list, only if the list exists
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public Promise<IntegerReply> rpushx(Object key0, Object value1) {
    if (version < RPUSHX_VERSION) return new Promise<>(new RedisException("Server does not support RPUSHX"));
    return execute(IntegerReply.class, new Command(RPUSHX_BYTES, key0, value1));
  }

  private static final String DEL = "DEL";
  private static final byte[] DEL_BYTES = DEL.getBytes(Charsets.US_ASCII);
  private static final int DEL_VERSION = parseVersion("1.0.0");

  /**
   * Delete a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> del(Object[] key0) {
    if (version < DEL_VERSION) return new Promise<>(new RedisException("Server does not support DEL"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(IntegerReply.class, new Command(DEL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> del_(Object... arguments) {
    if (version < DEL_VERSION) return new Promise<>(new RedisException("Server does not support DEL"));
    return execute(IntegerReply.class, new Command(DEL_BYTES, arguments));
  }

  private static final String DUMP = "DUMP";
  private static final byte[] DUMP_BYTES = DUMP.getBytes(Charsets.US_ASCII);
  private static final int DUMP_VERSION = parseVersion("2.6.0");

  /**
   * Return a serialized version of the value stored at the specified key.
   * Generic
   *
   * @param key0
   * @return BulkReply
   */
  public Promise<BulkReply> dump(Object key0) {
    if (version < DUMP_VERSION) return new Promise<>(new RedisException("Server does not support DUMP"));
    return execute(BulkReply.class, new Command(DUMP_BYTES, key0));
  }

  private static final String EXISTS = "EXISTS";
  private static final byte[] EXISTS_BYTES = EXISTS.getBytes(Charsets.US_ASCII);
  private static final int EXISTS_VERSION = parseVersion("1.0.0");

  /**
   * Determine if a key exists
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> exists(Object key0) {
    if (version < EXISTS_VERSION) return new Promise<>(new RedisException("Server does not support EXISTS"));
    return execute(IntegerReply.class, new Command(EXISTS_BYTES, key0));
  }

  private static final String EXPIRE = "EXPIRE";
  private static final byte[] EXPIRE_BYTES = EXPIRE.getBytes(Charsets.US_ASCII);
  private static final int EXPIRE_VERSION = parseVersion("1.0.0");

  /**
   * Set a key's time to live in seconds
   * Generic
   *
   * @param key0
   * @param seconds1
   * @return IntegerReply
   */
  public Promise<IntegerReply> expire(Object key0, Object seconds1) {
    if (version < EXPIRE_VERSION) return new Promise<>(new RedisException("Server does not support EXPIRE"));
    return execute(IntegerReply.class, new Command(EXPIRE_BYTES, key0, seconds1));
  }

  private static final String EXPIREAT = "EXPIREAT";
  private static final byte[] EXPIREAT_BYTES = EXPIREAT.getBytes(Charsets.US_ASCII);
  private static final int EXPIREAT_VERSION = parseVersion("1.2.0");

  /**
   * Set the expiration for a key as a UNIX timestamp
   * Generic
   *
   * @param key0
   * @param timestamp1
   * @return IntegerReply
   */
  public Promise<IntegerReply> expireat(Object key0, Object timestamp1) {
    if (version < EXPIREAT_VERSION) return new Promise<>(new RedisException("Server does not support EXPIREAT"));
    return execute(IntegerReply.class, new Command(EXPIREAT_BYTES, key0, timestamp1));
  }

  private static final String KEYS = "KEYS";
  private static final byte[] KEYS_BYTES = KEYS.getBytes(Charsets.US_ASCII);
  private static final int KEYS_VERSION = parseVersion("1.0.0");

  /**
   * Find all keys matching the given pattern
   * Generic
   *
   * @param pattern0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> keys(Object pattern0) {
    if (version < KEYS_VERSION) return new Promise<>(new RedisException("Server does not support KEYS"));
    return execute(MultiBulkReply.class, new Command(KEYS_BYTES, pattern0));
  }

  private static final String MIGRATE = "MIGRATE";
  private static final byte[] MIGRATE_BYTES = MIGRATE.getBytes(Charsets.US_ASCII);
  private static final int MIGRATE_VERSION = parseVersion("2.6.0");

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
  public Promise<StatusReply> migrate(Object host0, Object port1, Object key2, Object destination_db3, Object timeout4) {
    if (version < MIGRATE_VERSION) return new Promise<>(new RedisException("Server does not support MIGRATE"));
    List<Object> list = new ArrayList<>();
    list.add(host0);
    list.add(port1);
    list.add(key2);
    list.add(destination_db3);
    list.add(timeout4);
    return execute(StatusReply.class, new Command(MIGRATE_BYTES, list.toArray(new Object[list.size()])));
  }

  private static final String MOVE = "MOVE";
  private static final byte[] MOVE_BYTES = MOVE.getBytes(Charsets.US_ASCII);
  private static final int MOVE_VERSION = parseVersion("1.0.0");

  /**
   * Move a key to another database
   * Generic
   *
   * @param key0
   * @param db1
   * @return IntegerReply
   */
  public Promise<IntegerReply> move(Object key0, Object db1) {
    if (version < MOVE_VERSION) return new Promise<>(new RedisException("Server does not support MOVE"));
    return execute(IntegerReply.class, new Command(MOVE_BYTES, key0, db1));
  }

  private static final String OBJECT = "OBJECT";
  private static final byte[] OBJECT_BYTES = OBJECT.getBytes(Charsets.US_ASCII);
  private static final int OBJECT_VERSION = parseVersion("2.2.3");

  /**
   * Inspect the internals of Redis objects
   * Generic
   *
   * @param subcommand0
   * @param arguments1
   * @return Reply
   */
  public Promise<Reply> object(Object subcommand0, Object[] arguments1) {
    if (version < OBJECT_VERSION) return new Promise<>(new RedisException("Server does not support OBJECT"));
    List<Object> list = new ArrayList<>();
    list.add(subcommand0);
    Collections.addAll(list, arguments1);
    return execute(Reply.class, new Command(OBJECT_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<Reply> object_(Object... arguments) {
    if (version < OBJECT_VERSION) return new Promise<>(new RedisException("Server does not support OBJECT"));
    return execute(Reply.class, new Command(OBJECT_BYTES, arguments));
  }

  private static final String PERSIST = "PERSIST";
  private static final byte[] PERSIST_BYTES = PERSIST.getBytes(Charsets.US_ASCII);
  private static final int PERSIST_VERSION = parseVersion("2.2.0");

  /**
   * Remove the expiration from a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> persist(Object key0) {
    if (version < PERSIST_VERSION) return new Promise<>(new RedisException("Server does not support PERSIST"));
    return execute(IntegerReply.class, new Command(PERSIST_BYTES, key0));
  }

  private static final String PEXPIRE = "PEXPIRE";
  private static final byte[] PEXPIRE_BYTES = PEXPIRE.getBytes(Charsets.US_ASCII);
  private static final int PEXPIRE_VERSION = parseVersion("2.6.0");

  /**
   * Set a key's time to live in milliseconds
   * Generic
   *
   * @param key0
   * @param milliseconds1
   * @return IntegerReply
   */
  public Promise<IntegerReply> pexpire(Object key0, Object milliseconds1) {
    if (version < PEXPIRE_VERSION) return new Promise<>(new RedisException("Server does not support PEXPIRE"));
    return execute(IntegerReply.class, new Command(PEXPIRE_BYTES, key0, milliseconds1));
  }

  private static final String PEXPIREAT = "PEXPIREAT";
  private static final byte[] PEXPIREAT_BYTES = PEXPIREAT.getBytes(Charsets.US_ASCII);
  private static final int PEXPIREAT_VERSION = parseVersion("2.6.0");

  /**
   * Set the expiration for a key as a UNIX timestamp specified in milliseconds
   * Generic
   *
   * @param key0
   * @param milliseconds_timestamp1
   * @return IntegerReply
   */
  public Promise<IntegerReply> pexpireat(Object key0, Object milliseconds_timestamp1) {
    if (version < PEXPIREAT_VERSION) return new Promise<>(new RedisException("Server does not support PEXPIREAT"));
    return execute(IntegerReply.class, new Command(PEXPIREAT_BYTES, key0, milliseconds_timestamp1));
  }

  private static final String PTTL = "PTTL";
  private static final byte[] PTTL_BYTES = PTTL.getBytes(Charsets.US_ASCII);
  private static final int PTTL_VERSION = parseVersion("2.6.0");

  /**
   * Get the time to live for a key in milliseconds
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> pttl(Object key0) {
    if (version < PTTL_VERSION) return new Promise<>(new RedisException("Server does not support PTTL"));
    return execute(IntegerReply.class, new Command(PTTL_BYTES, key0));
  }

  private static final String RANDOMKEY = "RANDOMKEY";
  private static final byte[] RANDOMKEY_BYTES = RANDOMKEY.getBytes(Charsets.US_ASCII);
  private static final int RANDOMKEY_VERSION = parseVersion("1.0.0");

  /**
   * Return a random key from the keyspace
   * Generic
   *
   * @return BulkReply
   */
  public Promise<BulkReply> randomkey() {
    if (version < RANDOMKEY_VERSION) return new Promise<>(new RedisException("Server does not support RANDOMKEY"));
    return execute(BulkReply.class, new Command(RANDOMKEY_BYTES));
  }

  private static final String RENAME = "RENAME";
  private static final byte[] RENAME_BYTES = RENAME.getBytes(Charsets.US_ASCII);
  private static final int RENAME_VERSION = parseVersion("1.0.0");

  /**
   * Rename a key
   * Generic
   *
   * @param key0
   * @param newkey1
   * @return StatusReply
   */
  public Promise<StatusReply> rename(Object key0, Object newkey1) {
    if (version < RENAME_VERSION) return new Promise<>(new RedisException("Server does not support RENAME"));
    return execute(StatusReply.class, new Command(RENAME_BYTES, key0, newkey1));
  }

  private static final String RENAMENX = "RENAMENX";
  private static final byte[] RENAMENX_BYTES = RENAMENX.getBytes(Charsets.US_ASCII);
  private static final int RENAMENX_VERSION = parseVersion("1.0.0");

  /**
   * Rename a key, only if the new key does not exist
   * Generic
   *
   * @param key0
   * @param newkey1
   * @return IntegerReply
   */
  public Promise<IntegerReply> renamenx(Object key0, Object newkey1) {
    if (version < RENAMENX_VERSION) return new Promise<>(new RedisException("Server does not support RENAMENX"));
    return execute(IntegerReply.class, new Command(RENAMENX_BYTES, key0, newkey1));
  }

  private static final String RESTORE = "RESTORE";
  private static final byte[] RESTORE_BYTES = RESTORE.getBytes(Charsets.US_ASCII);
  private static final int RESTORE_VERSION = parseVersion("2.6.0");

  /**
   * Create a key using the provided serialized value, previously obtained using DUMP.
   * Generic
   *
   * @param key0
   * @param ttl1
   * @param serialized_value2
   * @return StatusReply
   */
  public Promise<StatusReply> restore(Object key0, Object ttl1, Object serialized_value2) {
    if (version < RESTORE_VERSION) return new Promise<>(new RedisException("Server does not support RESTORE"));
    return execute(StatusReply.class, new Command(RESTORE_BYTES, key0, ttl1, serialized_value2));
  }

  private static final String SORT = "SORT";
  private static final byte[] SORT_BYTES = SORT.getBytes(Charsets.US_ASCII);
  private static final int SORT_VERSION = parseVersion("1.0.0");

  /**
   * Sort the elements in a list, set or sorted set
   * Generic
   *
   * @param key0
   * @param pattern1
   * @return Reply
   */
  public Promise<Reply> sort(Object key0, Object[] pattern1) {
    if (version < SORT_VERSION) return new Promise<>(new RedisException("Server does not support SORT"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, pattern1);
    return execute(Reply.class, new Command(SORT_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<Reply> sort_(Object... arguments) {
    if (version < SORT_VERSION) return new Promise<>(new RedisException("Server does not support SORT"));
    return execute(Reply.class, new Command(SORT_BYTES, arguments));
  }

  private static final String TTL = "TTL";
  private static final byte[] TTL_BYTES = TTL.getBytes(Charsets.US_ASCII);
  private static final int TTL_VERSION = parseVersion("1.0.0");

  /**
   * Get the time to live for a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> ttl(Object key0) {
    if (version < TTL_VERSION) return new Promise<>(new RedisException("Server does not support TTL"));
    return execute(IntegerReply.class, new Command(TTL_BYTES, key0));
  }

  private static final String TYPE = "TYPE";
  private static final byte[] TYPE_BYTES = TYPE.getBytes(Charsets.US_ASCII);
  private static final int TYPE_VERSION = parseVersion("1.0.0");

  /**
   * Determine the type stored at key
   * Generic
   *
   * @param key0
   * @return StatusReply
   */
  public Promise<StatusReply> type(Object key0) {
    if (version < TYPE_VERSION) return new Promise<>(new RedisException("Server does not support TYPE"));
    return execute(StatusReply.class, new Command(TYPE_BYTES, key0));
  }

  private static final String UNWATCH = "UNWATCH";
  private static final byte[] UNWATCH_BYTES = UNWATCH.getBytes(Charsets.US_ASCII);
  private static final int UNWATCH_VERSION = parseVersion("2.2.0");

  /**
   * Forget about all watched keys
   * Transactions
   *
   * @return StatusReply
   */
  public Promise<StatusReply> unwatch() {
    if (version < UNWATCH_VERSION) return new Promise<>(new RedisException("Server does not support UNWATCH"));
    return execute(StatusReply.class, new Command(UNWATCH_BYTES));
  }

  private static final String WATCH = "WATCH";
  private static final byte[] WATCH_BYTES = WATCH.getBytes(Charsets.US_ASCII);
  private static final int WATCH_VERSION = parseVersion("2.2.0");

  /**
   * Watch the given keys to determine execution of the MULTI/EXEC block
   * Transactions
   *
   * @param key0
   * @return StatusReply
   */
  public Promise<StatusReply> watch(Object[] key0) {
    if (version < WATCH_VERSION) return new Promise<>(new RedisException("Server does not support WATCH"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(StatusReply.class, new Command(WATCH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<StatusReply> watch_(Object... arguments) {
    if (version < WATCH_VERSION) return new Promise<>(new RedisException("Server does not support WATCH"));
    return execute(StatusReply.class, new Command(WATCH_BYTES, arguments));
  }

  private static final String EVAL = "EVAL";
  private static final byte[] EVAL_BYTES = EVAL.getBytes(Charsets.US_ASCII);
  private static final int EVAL_VERSION = parseVersion("2.6.0");

  /**
   * Execute a Lua script server side
   * Scripting
   *
   * @param script0
   * @param numkeys1
   * @param key2
   * @return Reply
   */
  public Promise<Reply> eval(Object script0, Object numkeys1, Object[] key2) {
    if (version < EVAL_VERSION) return new Promise<>(new RedisException("Server does not support EVAL"));
    List<Object> list = new ArrayList<>();
    list.add(script0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return execute(Reply.class, new Command(EVAL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<Reply> eval_(Object... arguments) {
    if (version < EVAL_VERSION) return new Promise<>(new RedisException("Server does not support EVAL"));
    return execute(Reply.class, new Command(EVAL_BYTES, arguments));
  }

  private static final String EVALSHA = "EVALSHA";
  private static final byte[] EVALSHA_BYTES = EVALSHA.getBytes(Charsets.US_ASCII);
  private static final int EVALSHA_VERSION = parseVersion("2.6.0");

  /**
   * Execute a Lua script server side
   * Scripting
   *
   * @param sha10
   * @param numkeys1
   * @param key2
   * @return Reply
   */
  public Promise<Reply> evalsha(Object sha10, Object numkeys1, Object[] key2) {
    if (version < EVALSHA_VERSION) return new Promise<>(new RedisException("Server does not support EVALSHA"));
    List<Object> list = new ArrayList<>();
    list.add(sha10);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return execute(Reply.class, new Command(EVALSHA_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<Reply> evalsha_(Object... arguments) {
    if (version < EVALSHA_VERSION) return new Promise<>(new RedisException("Server does not support EVALSHA"));
    return execute(Reply.class, new Command(EVALSHA_BYTES, arguments));
  }

  private static final String SCRIPT_EXISTS = "SCRIPT";
  private static final String SCRIPT_EXISTS2 = "EXISTS";
  private static final byte[] SCRIPT_EXISTS2_BYTES = SCRIPT_EXISTS2.getBytes(Charsets.US_ASCII);
  private static final byte[] SCRIPT_EXISTS_BYTES = SCRIPT_EXISTS.getBytes(Charsets.US_ASCII);
  private static final int SCRIPT_EXISTS_VERSION = parseVersion("2.6.0");

  /**
   * Check existence of scripts in the script cache.
   * Scripting
   *
   * @param script0
   * @return Reply
   */
  public Promise<Reply> script_exists(Object[] script0) {
    if (version < SCRIPT_EXISTS_VERSION) return new Promise<>(new RedisException("Server does not support SCRIPT_EXISTS"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, script0);
    return execute(Reply.class, new Command(SCRIPT_EXISTS_BYTES, SCRIPT_EXISTS2_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<Reply> script_exists_(Object... arguments) {
    if (version < SCRIPT_EXISTS_VERSION) return new Promise<>(new RedisException("Server does not support SCRIPT_EXISTS"));
    return execute(Reply.class, new Command(SCRIPT_EXISTS_BYTES, SCRIPT_EXISTS2_BYTES, arguments));
  }

  private static final String SCRIPT_FLUSH = "SCRIPT";
  private static final String SCRIPT_FLUSH2 = "FLUSH";
  private static final byte[] SCRIPT_FLUSH2_BYTES = SCRIPT_FLUSH2.getBytes(Charsets.US_ASCII);
  private static final byte[] SCRIPT_FLUSH_BYTES = SCRIPT_FLUSH.getBytes(Charsets.US_ASCII);
  private static final int SCRIPT_FLUSH_VERSION = parseVersion("2.6.0");

  /**
   * Remove all the scripts from the script cache.
   * Scripting
   *
   * @return Reply
   */
  public Promise<Reply> script_flush() {
    if (version < SCRIPT_FLUSH_VERSION) return new Promise<>(new RedisException("Server does not support SCRIPT_FLUSH"));
    return execute(Reply.class, new Command(SCRIPT_FLUSH_BYTES, SCRIPT_FLUSH2_BYTES));
  }

  private static final String SCRIPT_KILL = "SCRIPT";
  private static final String SCRIPT_KILL2 = "KILL";
  private static final byte[] SCRIPT_KILL2_BYTES = SCRIPT_KILL2.getBytes(Charsets.US_ASCII);
  private static final byte[] SCRIPT_KILL_BYTES = SCRIPT_KILL.getBytes(Charsets.US_ASCII);
  private static final int SCRIPT_KILL_VERSION = parseVersion("2.6.0");

  /**
   * Kill the script currently in execution.
   * Scripting
   *
   * @return Reply
   */
  public Promise<Reply> script_kill() {
    if (version < SCRIPT_KILL_VERSION) return new Promise<>(new RedisException("Server does not support SCRIPT_KILL"));
    return execute(Reply.class, new Command(SCRIPT_KILL_BYTES, SCRIPT_KILL2_BYTES));
  }

  private static final String SCRIPT_LOAD = "SCRIPT";
  private static final String SCRIPT_LOAD2 = "LOAD";
  private static final byte[] SCRIPT_LOAD2_BYTES = SCRIPT_LOAD2.getBytes(Charsets.US_ASCII);
  private static final byte[] SCRIPT_LOAD_BYTES = SCRIPT_LOAD.getBytes(Charsets.US_ASCII);
  private static final int SCRIPT_LOAD_VERSION = parseVersion("2.6.0");

  /**
   * Load the specified Lua script into the script cache.
   * Scripting
   *
   * @param script0
   * @return Reply
   */
  public Promise<Reply> script_load(Object script0) {
    if (version < SCRIPT_LOAD_VERSION) return new Promise<>(new RedisException("Server does not support SCRIPT_LOAD"));
    return execute(Reply.class, new Command(SCRIPT_LOAD_BYTES, SCRIPT_LOAD2_BYTES, script0));
  }

  private static final String HDEL = "HDEL";
  private static final byte[] HDEL_BYTES = HDEL.getBytes(Charsets.US_ASCII);
  private static final int HDEL_VERSION = parseVersion("2.0.0");

  /**
   * Delete one or more hash fields
   * Hash
   *
   * @param key0
   * @param field1
   * @return IntegerReply
   */
  public Promise<IntegerReply> hdel(Object key0, Object[] field1) {
    if (version < HDEL_VERSION) return new Promise<>(new RedisException("Server does not support HDEL"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, field1);
    return execute(IntegerReply.class, new Command(HDEL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> hdel_(Object... arguments) {
    if (version < HDEL_VERSION) return new Promise<>(new RedisException("Server does not support HDEL"));
    return execute(IntegerReply.class, new Command(HDEL_BYTES, arguments));
  }

  private static final String HEXISTS = "HEXISTS";
  private static final byte[] HEXISTS_BYTES = HEXISTS.getBytes(Charsets.US_ASCII);
  private static final int HEXISTS_VERSION = parseVersion("2.0.0");

  /**
   * Determine if a hash field exists
   * Hash
   *
   * @param key0
   * @param field1
   * @return IntegerReply
   */
  public Promise<IntegerReply> hexists(Object key0, Object field1) {
    if (version < HEXISTS_VERSION) return new Promise<>(new RedisException("Server does not support HEXISTS"));
    return execute(IntegerReply.class, new Command(HEXISTS_BYTES, key0, field1));
  }

  private static final String HGET = "HGET";
  private static final byte[] HGET_BYTES = HGET.getBytes(Charsets.US_ASCII);
  private static final int HGET_VERSION = parseVersion("2.0.0");

  /**
   * Get the value of a hash field
   * Hash
   *
   * @param key0
   * @param field1
   * @return BulkReply
   */
  public Promise<BulkReply> hget(Object key0, Object field1) {
    if (version < HGET_VERSION) return new Promise<>(new RedisException("Server does not support HGET"));
    return execute(BulkReply.class, new Command(HGET_BYTES, key0, field1));
  }

  private static final String HGETALL = "HGETALL";
  private static final byte[] HGETALL_BYTES = HGETALL.getBytes(Charsets.US_ASCII);
  private static final int HGETALL_VERSION = parseVersion("2.0.0");

  /**
   * Get all the fields and values in a hash
   * Hash
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> hgetall(Object key0) {
    if (version < HGETALL_VERSION) return new Promise<>(new RedisException("Server does not support HGETALL"));
    return execute(MultiBulkReply.class, new Command(HGETALL_BYTES, key0));
  }

  private static final String HINCRBY = "HINCRBY";
  private static final byte[] HINCRBY_BYTES = HINCRBY.getBytes(Charsets.US_ASCII);
  private static final int HINCRBY_VERSION = parseVersion("2.0.0");

  /**
   * Increment the integer value of a hash field by the given number
   * Hash
   *
   * @param key0
   * @param field1
   * @param increment2
   * @return IntegerReply
   */
  public Promise<IntegerReply> hincrby(Object key0, Object field1, Object increment2) {
    if (version < HINCRBY_VERSION) return new Promise<>(new RedisException("Server does not support HINCRBY"));
    return execute(IntegerReply.class, new Command(HINCRBY_BYTES, key0, field1, increment2));
  }

  private static final String HINCRBYFLOAT = "HINCRBYFLOAT";
  private static final byte[] HINCRBYFLOAT_BYTES = HINCRBYFLOAT.getBytes(Charsets.US_ASCII);
  private static final int HINCRBYFLOAT_VERSION = parseVersion("2.6.0");

  /**
   * Increment the float value of a hash field by the given amount
   * Hash
   *
   * @param key0
   * @param field1
   * @param increment2
   * @return BulkReply
   */
  public Promise<BulkReply> hincrbyfloat(Object key0, Object field1, Object increment2) {
    if (version < HINCRBYFLOAT_VERSION) return new Promise<>(new RedisException("Server does not support HINCRBYFLOAT"));
    return execute(BulkReply.class, new Command(HINCRBYFLOAT_BYTES, key0, field1, increment2));
  }

  private static final String HKEYS = "HKEYS";
  private static final byte[] HKEYS_BYTES = HKEYS.getBytes(Charsets.US_ASCII);
  private static final int HKEYS_VERSION = parseVersion("2.0.0");

  /**
   * Get all the fields in a hash
   * Hash
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> hkeys(Object key0) {
    if (version < HKEYS_VERSION) return new Promise<>(new RedisException("Server does not support HKEYS"));
    return execute(MultiBulkReply.class, new Command(HKEYS_BYTES, key0));
  }

  private static final String HLEN = "HLEN";
  private static final byte[] HLEN_BYTES = HLEN.getBytes(Charsets.US_ASCII);
  private static final int HLEN_VERSION = parseVersion("2.0.0");

  /**
   * Get the number of fields in a hash
   * Hash
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> hlen(Object key0) {
    if (version < HLEN_VERSION) return new Promise<>(new RedisException("Server does not support HLEN"));
    return execute(IntegerReply.class, new Command(HLEN_BYTES, key0));
  }

  private static final String HMGET = "HMGET";
  private static final byte[] HMGET_BYTES = HMGET.getBytes(Charsets.US_ASCII);
  private static final int HMGET_VERSION = parseVersion("2.0.0");

  /**
   * Get the values of all the given hash fields
   * Hash
   *
   * @param key0
   * @param field1
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> hmget(Object key0, Object[] field1) {
    if (version < HMGET_VERSION) return new Promise<>(new RedisException("Server does not support HMGET"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, field1);
    return execute(MultiBulkReply.class, new Command(HMGET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> hmget_(Object... arguments) {
    if (version < HMGET_VERSION) return new Promise<>(new RedisException("Server does not support HMGET"));
    return execute(MultiBulkReply.class, new Command(HMGET_BYTES, arguments));
  }

  private static final String HMSET = "HMSET";
  private static final byte[] HMSET_BYTES = HMSET.getBytes(Charsets.US_ASCII);
  private static final int HMSET_VERSION = parseVersion("2.0.0");

  /**
   * Set multiple hash fields to multiple values
   * Hash
   *
   * @param key0
   * @param field_or_value1
   * @return StatusReply
   */
  public Promise<StatusReply> hmset(Object key0, Object[] field_or_value1) {
    if (version < HMSET_VERSION) return new Promise<>(new RedisException("Server does not support HMSET"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, field_or_value1);
    return execute(StatusReply.class, new Command(HMSET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<StatusReply> hmset_(Object... arguments) {
    if (version < HMSET_VERSION) return new Promise<>(new RedisException("Server does not support HMSET"));
    return execute(StatusReply.class, new Command(HMSET_BYTES, arguments));
  }

  private static final String HSET = "HSET";
  private static final byte[] HSET_BYTES = HSET.getBytes(Charsets.US_ASCII);
  private static final int HSET_VERSION = parseVersion("2.0.0");

  /**
   * Set the string value of a hash field
   * Hash
   *
   * @param key0
   * @param field1
   * @param value2
   * @return IntegerReply
   */
  public Promise<IntegerReply> hset(Object key0, Object field1, Object value2) {
    if (version < HSET_VERSION) return new Promise<>(new RedisException("Server does not support HSET"));
    return execute(IntegerReply.class, new Command(HSET_BYTES, key0, field1, value2));
  }

  private static final String HSETNX = "HSETNX";
  private static final byte[] HSETNX_BYTES = HSETNX.getBytes(Charsets.US_ASCII);
  private static final int HSETNX_VERSION = parseVersion("2.0.0");

  /**
   * Set the value of a hash field, only if the field does not exist
   * Hash
   *
   * @param key0
   * @param field1
   * @param value2
   * @return IntegerReply
   */
  public Promise<IntegerReply> hsetnx(Object key0, Object field1, Object value2) {
    if (version < HSETNX_VERSION) return new Promise<>(new RedisException("Server does not support HSETNX"));
    return execute(IntegerReply.class, new Command(HSETNX_BYTES, key0, field1, value2));
  }

  private static final String HVALS = "HVALS";
  private static final byte[] HVALS_BYTES = HVALS.getBytes(Charsets.US_ASCII);
  private static final int HVALS_VERSION = parseVersion("2.0.0");

  /**
   * Get all the values in a hash
   * Hash
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> hvals(Object key0) {
    if (version < HVALS_VERSION) return new Promise<>(new RedisException("Server does not support HVALS"));
    return execute(MultiBulkReply.class, new Command(HVALS_BYTES, key0));
  }

  private static final String PUBLISH = "PUBLISH";
  private static final byte[] PUBLISH_BYTES = PUBLISH.getBytes(Charsets.US_ASCII);
  private static final int PUBLISH_VERSION = parseVersion("2.0.0");

  /**
   * Post a message to a channel
   * Pubsub
   *
   * @param channel0
   * @param message1
   * @return IntegerReply
   */
  public Promise<IntegerReply> publish(Object channel0, Object message1) {
    if (version < PUBLISH_VERSION) return new Promise<>(new RedisException("Server does not support PUBLISH"));
    return execute(IntegerReply.class, new Command(PUBLISH_BYTES, channel0, message1));
  }

  private static final String SADD = "SADD";
  private static final byte[] SADD_BYTES = SADD.getBytes(Charsets.US_ASCII);
  private static final int SADD_VERSION = parseVersion("1.0.0");

  /**
   * Add one or more members to a set
   * Set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  public Promise<IntegerReply> sadd(Object key0, Object[] member1) {
    if (version < SADD_VERSION) return new Promise<>(new RedisException("Server does not support SADD"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, member1);
    return execute(IntegerReply.class, new Command(SADD_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> sadd_(Object... arguments) {
    if (version < SADD_VERSION) return new Promise<>(new RedisException("Server does not support SADD"));
    return execute(IntegerReply.class, new Command(SADD_BYTES, arguments));
  }

  private static final String SCARD = "SCARD";
  private static final byte[] SCARD_BYTES = SCARD.getBytes(Charsets.US_ASCII);
  private static final int SCARD_VERSION = parseVersion("1.0.0");

  /**
   * Get the number of members in a set
   * Set
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> scard(Object key0) {
    if (version < SCARD_VERSION) return new Promise<>(new RedisException("Server does not support SCARD"));
    return execute(IntegerReply.class, new Command(SCARD_BYTES, key0));
  }

  private static final String SDIFF = "SDIFF";
  private static final byte[] SDIFF_BYTES = SDIFF.getBytes(Charsets.US_ASCII);
  private static final int SDIFF_VERSION = parseVersion("1.0.0");

  /**
   * Subtract multiple sets
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> sdiff(Object[] key0) {
    if (version < SDIFF_VERSION) return new Promise<>(new RedisException("Server does not support SDIFF"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(MultiBulkReply.class, new Command(SDIFF_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> sdiff_(Object... arguments) {
    if (version < SDIFF_VERSION) return new Promise<>(new RedisException("Server does not support SDIFF"));
    return execute(MultiBulkReply.class, new Command(SDIFF_BYTES, arguments));
  }

  private static final String SDIFFSTORE = "SDIFFSTORE";
  private static final byte[] SDIFFSTORE_BYTES = SDIFFSTORE.getBytes(Charsets.US_ASCII);
  private static final int SDIFFSTORE_VERSION = parseVersion("1.0.0");

  /**
   * Subtract multiple sets and store the resulting set in a key
   * Set
   *
   * @param destination0
   * @param key1
   * @return IntegerReply
   */
  public Promise<IntegerReply> sdiffstore(Object destination0, Object[] key1) {
    if (version < SDIFFSTORE_VERSION) return new Promise<>(new RedisException("Server does not support SDIFFSTORE"));
    List<Object> list = new ArrayList<>();
    list.add(destination0);
    Collections.addAll(list, key1);
    return execute(IntegerReply.class, new Command(SDIFFSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> sdiffstore_(Object... arguments) {
    if (version < SDIFFSTORE_VERSION) return new Promise<>(new RedisException("Server does not support SDIFFSTORE"));
    return execute(IntegerReply.class, new Command(SDIFFSTORE_BYTES, arguments));
  }

  private static final String SINTER = "SINTER";
  private static final byte[] SINTER_BYTES = SINTER.getBytes(Charsets.US_ASCII);
  private static final int SINTER_VERSION = parseVersion("1.0.0");

  /**
   * Intersect multiple sets
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> sinter(Object[] key0) {
    if (version < SINTER_VERSION) return new Promise<>(new RedisException("Server does not support SINTER"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(MultiBulkReply.class, new Command(SINTER_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> sinter_(Object... arguments) {
    if (version < SINTER_VERSION) return new Promise<>(new RedisException("Server does not support SINTER"));
    return execute(MultiBulkReply.class, new Command(SINTER_BYTES, arguments));
  }

  private static final String SINTERSTORE = "SINTERSTORE";
  private static final byte[] SINTERSTORE_BYTES = SINTERSTORE.getBytes(Charsets.US_ASCII);
  private static final int SINTERSTORE_VERSION = parseVersion("1.0.0");

  /**
   * Intersect multiple sets and store the resulting set in a key
   * Set
   *
   * @param destination0
   * @param key1
   * @return IntegerReply
   */
  public Promise<IntegerReply> sinterstore(Object destination0, Object[] key1) {
    if (version < SINTERSTORE_VERSION) return new Promise<>(new RedisException("Server does not support SINTERSTORE"));
    List<Object> list = new ArrayList<>();
    list.add(destination0);
    Collections.addAll(list, key1);
    return execute(IntegerReply.class, new Command(SINTERSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> sinterstore_(Object... arguments) {
    if (version < SINTERSTORE_VERSION) return new Promise<>(new RedisException("Server does not support SINTERSTORE"));
    return execute(IntegerReply.class, new Command(SINTERSTORE_BYTES, arguments));
  }

  private static final String SISMEMBER = "SISMEMBER";
  private static final byte[] SISMEMBER_BYTES = SISMEMBER.getBytes(Charsets.US_ASCII);
  private static final int SISMEMBER_VERSION = parseVersion("1.0.0");

  /**
   * Determine if a given value is a member of a set
   * Set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  public Promise<IntegerReply> sismember(Object key0, Object member1) {
    if (version < SISMEMBER_VERSION) return new Promise<>(new RedisException("Server does not support SISMEMBER"));
    return execute(IntegerReply.class, new Command(SISMEMBER_BYTES, key0, member1));
  }

  private static final String SMEMBERS = "SMEMBERS";
  private static final byte[] SMEMBERS_BYTES = SMEMBERS.getBytes(Charsets.US_ASCII);
  private static final int SMEMBERS_VERSION = parseVersion("1.0.0");

  /**
   * Get all the members in a set
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> smembers(Object key0) {
    if (version < SMEMBERS_VERSION) return new Promise<>(new RedisException("Server does not support SMEMBERS"));
    return execute(MultiBulkReply.class, new Command(SMEMBERS_BYTES, key0));
  }

  private static final String SMOVE = "SMOVE";
  private static final byte[] SMOVE_BYTES = SMOVE.getBytes(Charsets.US_ASCII);
  private static final int SMOVE_VERSION = parseVersion("1.0.0");

  /**
   * Move a member from one set to another
   * Set
   *
   * @param source0
   * @param destination1
   * @param member2
   * @return IntegerReply
   */
  public Promise<IntegerReply> smove(Object source0, Object destination1, Object member2) {
    if (version < SMOVE_VERSION) return new Promise<>(new RedisException("Server does not support SMOVE"));
    return execute(IntegerReply.class, new Command(SMOVE_BYTES, source0, destination1, member2));
  }

  private static final String SPOP = "SPOP";
  private static final byte[] SPOP_BYTES = SPOP.getBytes(Charsets.US_ASCII);
  private static final int SPOP_VERSION = parseVersion("1.0.0");

  /**
   * Remove and return a random member from a set
   * Set
   *
   * @param key0
   * @return BulkReply
   */
  public Promise<BulkReply> spop(Object key0) {
    if (version < SPOP_VERSION) return new Promise<>(new RedisException("Server does not support SPOP"));
    return execute(BulkReply.class, new Command(SPOP_BYTES, key0));
  }

  private static final String SRANDMEMBER = "SRANDMEMBER";
  private static final byte[] SRANDMEMBER_BYTES = SRANDMEMBER.getBytes(Charsets.US_ASCII);
  private static final int SRANDMEMBER_VERSION = parseVersion("1.0.0");

  /**
   * Get one or multiple random members from a set
   * Set
   *
   * @param key0
   * @param count1
   * @return Reply
   */
  public Promise<Reply> srandmember(Object key0, Object count1) {
    if (version < SRANDMEMBER_VERSION) return new Promise<>(new RedisException("Server does not support SRANDMEMBER"));
    return execute(Reply.class, new Command(SRANDMEMBER_BYTES, key0, count1));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<Reply> srandmember_(Object... arguments) {
    if (version < SRANDMEMBER_VERSION) return new Promise<>(new RedisException("Server does not support SRANDMEMBER"));
    return execute(Reply.class, new Command(SRANDMEMBER_BYTES, arguments));
  }

  private static final String SREM = "SREM";
  private static final byte[] SREM_BYTES = SREM.getBytes(Charsets.US_ASCII);
  private static final int SREM_VERSION = parseVersion("1.0.0");

  /**
   * Remove one or more members from a set
   * Set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  public Promise<IntegerReply> srem(Object key0, Object[] member1) {
    if (version < SREM_VERSION) return new Promise<>(new RedisException("Server does not support SREM"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, member1);
    return execute(IntegerReply.class, new Command(SREM_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> srem_(Object... arguments) {
    if (version < SREM_VERSION) return new Promise<>(new RedisException("Server does not support SREM"));
    return execute(IntegerReply.class, new Command(SREM_BYTES, arguments));
  }

  private static final String SUNION = "SUNION";
  private static final byte[] SUNION_BYTES = SUNION.getBytes(Charsets.US_ASCII);
  private static final int SUNION_VERSION = parseVersion("1.0.0");

  /**
   * Add multiple sets
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  public Promise<MultiBulkReply> sunion(Object[] key0) {
    if (version < SUNION_VERSION) return new Promise<>(new RedisException("Server does not support SUNION"));
    List<Object> list = new ArrayList<>();
    Collections.addAll(list, key0);
    return execute(MultiBulkReply.class, new Command(SUNION_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> sunion_(Object... arguments) {
    if (version < SUNION_VERSION) return new Promise<>(new RedisException("Server does not support SUNION"));
    return execute(MultiBulkReply.class, new Command(SUNION_BYTES, arguments));
  }

  private static final String SUNIONSTORE = "SUNIONSTORE";
  private static final byte[] SUNIONSTORE_BYTES = SUNIONSTORE.getBytes(Charsets.US_ASCII);
  private static final int SUNIONSTORE_VERSION = parseVersion("1.0.0");

  /**
   * Add multiple sets and store the resulting set in a key
   * Set
   *
   * @param destination0
   * @param key1
   * @return IntegerReply
   */
  public Promise<IntegerReply> sunionstore(Object destination0, Object[] key1) {
    if (version < SUNIONSTORE_VERSION) return new Promise<>(new RedisException("Server does not support SUNIONSTORE"));
    List<Object> list = new ArrayList<>();
    list.add(destination0);
    Collections.addAll(list, key1);
    return execute(IntegerReply.class, new Command(SUNIONSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> sunionstore_(Object... arguments) {
    if (version < SUNIONSTORE_VERSION) return new Promise<>(new RedisException("Server does not support SUNIONSTORE"));
    return execute(IntegerReply.class, new Command(SUNIONSTORE_BYTES, arguments));
  }

  private static final String ZADD = "ZADD";
  private static final byte[] ZADD_BYTES = ZADD.getBytes(Charsets.US_ASCII);
  private static final int ZADD_VERSION = parseVersion("1.2.0");

  /**
   * Add one or more members to a sorted set, or update its score if it already exists
   * Sorted_set
   *
   * @param args
   * @return IntegerReply
   */
  public Promise<IntegerReply> zadd(Object[] args) {
    if (version < ZADD_VERSION) return new Promise<>(new RedisException("Server does not support ZADD"));
    return execute(IntegerReply.class, new Command(ZADD_BYTES, args));
  }

  private static final String ZCARD = "ZCARD";
  private static final byte[] ZCARD_BYTES = ZCARD.getBytes(Charsets.US_ASCII);
  private static final int ZCARD_VERSION = parseVersion("1.2.0");

  /**
   * Get the number of members in a sorted set
   * Sorted_set
   *
   * @param key0
   * @return IntegerReply
   */
  public Promise<IntegerReply> zcard(Object key0) {
    if (version < ZCARD_VERSION) return new Promise<>(new RedisException("Server does not support ZCARD"));
    return execute(IntegerReply.class, new Command(ZCARD_BYTES, key0));
  }

  private static final String ZCOUNT = "ZCOUNT";
  private static final byte[] ZCOUNT_BYTES = ZCOUNT.getBytes(Charsets.US_ASCII);
  private static final int ZCOUNT_VERSION = parseVersion("2.0.0");

  /**
   * Count the members in a sorted set with scores within the given values
   * Sorted_set
   *
   * @param key0
   * @param min1
   * @param max2
   * @return IntegerReply
   */
  public Promise<IntegerReply> zcount(Object key0, Object min1, Object max2) {
    if (version < ZCOUNT_VERSION) return new Promise<>(new RedisException("Server does not support ZCOUNT"));
    return execute(IntegerReply.class, new Command(ZCOUNT_BYTES, key0, min1, max2));
  }

  private static final String ZINCRBY = "ZINCRBY";
  private static final byte[] ZINCRBY_BYTES = ZINCRBY.getBytes(Charsets.US_ASCII);
  private static final int ZINCRBY_VERSION = parseVersion("1.2.0");

  /**
   * Increment the score of a member in a sorted set
   * Sorted_set
   *
   * @param key0
   * @param increment1
   * @param member2
   * @return BulkReply
   */
  public Promise<BulkReply> zincrby(Object key0, Object increment1, Object member2) {
    if (version < ZINCRBY_VERSION) return new Promise<>(new RedisException("Server does not support ZINCRBY"));
    return execute(BulkReply.class, new Command(ZINCRBY_BYTES, key0, increment1, member2));
  }

  private static final String ZINTERSTORE = "ZINTERSTORE";
  private static final byte[] ZINTERSTORE_BYTES = ZINTERSTORE.getBytes(Charsets.US_ASCII);
  private static final int ZINTERSTORE_VERSION = parseVersion("2.0.0");

  /**
   * Intersect multiple sorted sets and store the resulting sorted set in a new key
   * Sorted_set
   *
   * @param destination0
   * @param numkeys1
   * @param key2
   * @return IntegerReply
   */
  public Promise<IntegerReply> zinterstore(Object destination0, Object numkeys1, Object[] key2) {
    if (version < ZINTERSTORE_VERSION) return new Promise<>(new RedisException("Server does not support ZINTERSTORE"));
    List<Object> list = new ArrayList<>();
    list.add(destination0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return execute(IntegerReply.class, new Command(ZINTERSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> zinterstore_(Object... arguments) {
    if (version < ZINTERSTORE_VERSION) return new Promise<>(new RedisException("Server does not support ZINTERSTORE"));
    return execute(IntegerReply.class, new Command(ZINTERSTORE_BYTES, arguments));
  }

  private static final String ZRANGE = "ZRANGE";
  private static final byte[] ZRANGE_BYTES = ZRANGE.getBytes(Charsets.US_ASCII);
  private static final int ZRANGE_VERSION = parseVersion("1.2.0");

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
  public Promise<MultiBulkReply> zrange(Object key0, Object start1, Object stop2, Object withscores3) {
    if (version < ZRANGE_VERSION) return new Promise<>(new RedisException("Server does not support ZRANGE"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    list.add(start1);
    list.add(stop2);
    if (withscores3 != null) list.add(withscores3);
    return execute(MultiBulkReply.class, new Command(ZRANGE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> zrange_(Object... arguments) {
    if (version < ZRANGE_VERSION) return new Promise<>(new RedisException("Server does not support ZRANGE"));
    return execute(MultiBulkReply.class, new Command(ZRANGE_BYTES, arguments));
  }

  private static final String ZRANGEBYSCORE = "ZRANGEBYSCORE";
  private static final byte[] ZRANGEBYSCORE_BYTES = ZRANGEBYSCORE.getBytes(Charsets.US_ASCII);
  private static final int ZRANGEBYSCORE_VERSION = parseVersion("1.0.5");

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
  public Promise<MultiBulkReply> zrangebyscore(Object key0, Object min1, Object max2, Object withscores3, Object[] offset_or_count4) {
    if (version < ZRANGEBYSCORE_VERSION) return new Promise<>(new RedisException("Server does not support ZRANGEBYSCORE"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    list.add(min1);
    list.add(max2);
    if (withscores3 != null) list.add(withscores3);
    Collections.addAll(list, offset_or_count4);
    return execute(MultiBulkReply.class, new Command(ZRANGEBYSCORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> zrangebyscore_(Object... arguments) {
    if (version < ZRANGEBYSCORE_VERSION) return new Promise<>(new RedisException("Server does not support ZRANGEBYSCORE"));
    return execute(MultiBulkReply.class, new Command(ZRANGEBYSCORE_BYTES, arguments));
  }

  private static final String ZRANK = "ZRANK";
  private static final byte[] ZRANK_BYTES = ZRANK.getBytes(Charsets.US_ASCII);
  private static final int ZRANK_VERSION = parseVersion("2.0.0");

  /**
   * Determine the index of a member in a sorted set
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return Reply
   */
  public Promise<Reply> zrank(Object key0, Object member1) {
    if (version < ZRANK_VERSION) return new Promise<>(new RedisException("Server does not support ZRANK"));
    return execute(Reply.class, new Command(ZRANK_BYTES, key0, member1));
  }

  private static final String ZREM = "ZREM";
  private static final byte[] ZREM_BYTES = ZREM.getBytes(Charsets.US_ASCII);
  private static final int ZREM_VERSION = parseVersion("1.2.0");

  /**
   * Remove one or more members from a sorted set
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  public Promise<IntegerReply> zrem(Object key0, Object[] member1) {
    if (version < ZREM_VERSION) return new Promise<>(new RedisException("Server does not support ZREM"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    Collections.addAll(list, member1);
    return execute(IntegerReply.class, new Command(ZREM_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> zrem_(Object... arguments) {
    if (version < ZREM_VERSION) return new Promise<>(new RedisException("Server does not support ZREM"));
    return execute(IntegerReply.class, new Command(ZREM_BYTES, arguments));
  }

  private static final String ZREMRANGEBYRANK = "ZREMRANGEBYRANK";
  private static final byte[] ZREMRANGEBYRANK_BYTES = ZREMRANGEBYRANK.getBytes(Charsets.US_ASCII);
  private static final int ZREMRANGEBYRANK_VERSION = parseVersion("2.0.0");

  /**
   * Remove all members in a sorted set within the given indexes
   * Sorted_set
   *
   * @param key0
   * @param start1
   * @param stop2
   * @return IntegerReply
   */
  public Promise<IntegerReply> zremrangebyrank(Object key0, Object start1, Object stop2) {
    if (version < ZREMRANGEBYRANK_VERSION) return new Promise<>(new RedisException("Server does not support ZREMRANGEBYRANK"));
    return execute(IntegerReply.class, new Command(ZREMRANGEBYRANK_BYTES, key0, start1, stop2));
  }

  private static final String ZREMRANGEBYSCORE = "ZREMRANGEBYSCORE";
  private static final byte[] ZREMRANGEBYSCORE_BYTES = ZREMRANGEBYSCORE.getBytes(Charsets.US_ASCII);
  private static final int ZREMRANGEBYSCORE_VERSION = parseVersion("1.2.0");

  /**
   * Remove all members in a sorted set within the given scores
   * Sorted_set
   *
   * @param key0
   * @param min1
   * @param max2
   * @return IntegerReply
   */
  public Promise<IntegerReply> zremrangebyscore(Object key0, Object min1, Object max2) {
    if (version < ZREMRANGEBYSCORE_VERSION) return new Promise<>(new RedisException("Server does not support ZREMRANGEBYSCORE"));
    return execute(IntegerReply.class, new Command(ZREMRANGEBYSCORE_BYTES, key0, min1, max2));
  }

  private static final String ZREVRANGE = "ZREVRANGE";
  private static final byte[] ZREVRANGE_BYTES = ZREVRANGE.getBytes(Charsets.US_ASCII);
  private static final int ZREVRANGE_VERSION = parseVersion("1.2.0");

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
  public Promise<MultiBulkReply> zrevrange(Object key0, Object start1, Object stop2, Object withscores3) {
    if (version < ZREVRANGE_VERSION) return new Promise<>(new RedisException("Server does not support ZREVRANGE"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    list.add(start1);
    list.add(stop2);
    if (withscores3 != null) list.add(withscores3);
    return execute(MultiBulkReply.class, new Command(ZREVRANGE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> zrevrange_(Object... arguments) {
    if (version < ZREVRANGE_VERSION) return new Promise<>(new RedisException("Server does not support ZREVRANGE"));
    return execute(MultiBulkReply.class, new Command(ZREVRANGE_BYTES, arguments));
  }

  private static final String ZREVRANGEBYSCORE = "ZREVRANGEBYSCORE";
  private static final byte[] ZREVRANGEBYSCORE_BYTES = ZREVRANGEBYSCORE.getBytes(Charsets.US_ASCII);
  private static final int ZREVRANGEBYSCORE_VERSION = parseVersion("2.2.0");

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
  public Promise<MultiBulkReply> zrevrangebyscore(Object key0, Object max1, Object min2, Object withscores3, Object[] offset_or_count4) {
    if (version < ZREVRANGEBYSCORE_VERSION) return new Promise<>(new RedisException("Server does not support ZREVRANGEBYSCORE"));
    List<Object> list = new ArrayList<>();
    list.add(key0);
    list.add(max1);
    list.add(min2);
    if (withscores3 != null) list.add(withscores3);
    Collections.addAll(list, offset_or_count4);
    return execute(MultiBulkReply.class, new Command(ZREVRANGEBYSCORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<MultiBulkReply> zrevrangebyscore_(Object... arguments) {
    if (version < ZREVRANGEBYSCORE_VERSION) return new Promise<>(new RedisException("Server does not support ZREVRANGEBYSCORE"));
    return execute(MultiBulkReply.class, new Command(ZREVRANGEBYSCORE_BYTES, arguments));
  }

  private static final String ZREVRANK = "ZREVRANK";
  private static final byte[] ZREVRANK_BYTES = ZREVRANK.getBytes(Charsets.US_ASCII);
  private static final int ZREVRANK_VERSION = parseVersion("2.0.0");

  /**
   * Determine the index of a member in a sorted set, with scores ordered from high to low
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return Reply
   */
  public Promise<Reply> zrevrank(Object key0, Object member1) {
    if (version < ZREVRANK_VERSION) return new Promise<>(new RedisException("Server does not support ZREVRANK"));
    return execute(Reply.class, new Command(ZREVRANK_BYTES, key0, member1));
  }

  private static final String ZSCORE = "ZSCORE";
  private static final byte[] ZSCORE_BYTES = ZSCORE.getBytes(Charsets.US_ASCII);
  private static final int ZSCORE_VERSION = parseVersion("1.2.0");

  /**
   * Get the score associated with the given member in a sorted set
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return BulkReply
   */
  public Promise<BulkReply> zscore(Object key0, Object member1) {
    if (version < ZSCORE_VERSION) return new Promise<>(new RedisException("Server does not support ZSCORE"));
    return execute(BulkReply.class, new Command(ZSCORE_BYTES, key0, member1));
  }

  private static final String ZUNIONSTORE = "ZUNIONSTORE";
  private static final byte[] ZUNIONSTORE_BYTES = ZUNIONSTORE.getBytes(Charsets.US_ASCII);
  private static final int ZUNIONSTORE_VERSION = parseVersion("2.0.0");

  /**
   * Add multiple sorted sets and store the resulting sorted set in a new key
   * Sorted_set
   *
   * @param destination0
   * @param numkeys1
   * @param key2
   * @return IntegerReply
   */
  public Promise<IntegerReply> zunionstore(Object destination0, Object numkeys1, Object[] key2) {
    if (version < ZUNIONSTORE_VERSION) return new Promise<>(new RedisException("Server does not support ZUNIONSTORE"));
    List<Object> list = new ArrayList<>();
    list.add(destination0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return execute(IntegerReply.class, new Command(ZUNIONSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Promise<IntegerReply> zunionstore_(Object... arguments) {
    if (version < ZUNIONSTORE_VERSION) return new Promise<>(new RedisException("Server does not support ZUNIONSTORE"));
    return execute(IntegerReply.class, new Command(ZUNIONSTORE_BYTES, arguments));
  }
}
