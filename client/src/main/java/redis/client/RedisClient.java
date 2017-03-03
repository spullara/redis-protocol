package redis.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.base.Charsets;

import redis.Command;
import redis.reply.BulkReply;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;
import redis.reply.StatusReply;

public class RedisClient extends RedisClientBase {
  protected Pipeline pipeline = new Pipeline();

  public RedisClient(String host, int port) throws IOException {
    this(host, port, 0, null, Executors.newSingleThreadExecutor());
  }

  public RedisClient(String host, int port, int db, String passwd) throws IOException {
    this(host, port, db, passwd, Executors.newSingleThreadExecutor());
  }

  public RedisClient(String host, int port, int db, String passwd, ExecutorService es) throws IOException {
    super(host, port, db, passwd, es);
  }

  public Pipeline pipeline() {
    return pipeline;
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
  public IntegerReply append(Object key0, Object value1) throws RedisException {
    if (version < APPEND_VERSION) throw new RedisException("Server does not support APPEND");
    return (IntegerReply) execute(APPEND, new Command(APPEND_BYTES, key0, value1));
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
  public IntegerReply bitcount(Object key0, Object start1, Object end2) throws RedisException {
    if (version < BITCOUNT_VERSION) throw new RedisException("Server does not support BITCOUNT");
    return (IntegerReply) execute(BITCOUNT, new Command(BITCOUNT_BYTES, key0, start1, end2));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply bitcount_(Object... arguments) throws RedisException {
    if (version < BITCOUNT_VERSION) throw new RedisException("Server does not support BITCOUNT");
    return (IntegerReply) execute(BITCOUNT, new Command(BITCOUNT_BYTES, arguments));
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
  public IntegerReply bitop(Object operation0, Object destkey1, Object... key2) throws RedisException {
    if (version < BITOP_VERSION) throw new RedisException("Server does not support BITOP");
    List list = new ArrayList();
    list.add(operation0);
    list.add(destkey1);
    Collections.addAll(list, key2);
    return (IntegerReply) execute(BITOP, new Command(BITOP_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply bitop_(Object... arguments) throws RedisException {
    if (version < BITOP_VERSION) throw new RedisException("Server does not support BITOP");
    return (IntegerReply) execute(BITOP, new Command(BITOP_BYTES, arguments));
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
  public IntegerReply decr(Object key0) throws RedisException {
    if (version < DECR_VERSION) throw new RedisException("Server does not support DECR");
    return (IntegerReply) execute(DECR, new Command(DECR_BYTES, key0));
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
  public IntegerReply decrby(Object key0, Object decrement1) throws RedisException {
    if (version < DECRBY_VERSION) throw new RedisException("Server does not support DECRBY");
    return (IntegerReply) execute(DECRBY, new Command(DECRBY_BYTES, key0, decrement1));
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
  public BulkReply get(Object key0) throws RedisException {
    if (version < GET_VERSION) throw new RedisException("Server does not support GET");
    return (BulkReply) execute(GET, new Command(GET_BYTES, key0));
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
  public IntegerReply getbit(Object key0, Object offset1) throws RedisException {
    if (version < GETBIT_VERSION) throw new RedisException("Server does not support GETBIT");
    return (IntegerReply) execute(GETBIT, new Command(GETBIT_BYTES, key0, offset1));
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
  public BulkReply getrange(Object key0, Object start1, Object end2) throws RedisException {
    if (version < GETRANGE_VERSION) throw new RedisException("Server does not support GETRANGE");
    return (BulkReply) execute(GETRANGE, new Command(GETRANGE_BYTES, key0, start1, end2));
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
  public BulkReply getset(Object key0, Object value1) throws RedisException {
    if (version < GETSET_VERSION) throw new RedisException("Server does not support GETSET");
    return (BulkReply) execute(GETSET, new Command(GETSET_BYTES, key0, value1));
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
  public IntegerReply incr(Object key0) throws RedisException {
    if (version < INCR_VERSION) throw new RedisException("Server does not support INCR");
    return (IntegerReply) execute(INCR, new Command(INCR_BYTES, key0));
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
  public IntegerReply incrby(Object key0, Object increment1) throws RedisException {
    if (version < INCRBY_VERSION) throw new RedisException("Server does not support INCRBY");
    return (IntegerReply) execute(INCRBY, new Command(INCRBY_BYTES, key0, increment1));
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
  public BulkReply incrbyfloat(Object key0, Object increment1) throws RedisException {
    if (version < INCRBYFLOAT_VERSION) throw new RedisException("Server does not support INCRBYFLOAT");
    return (BulkReply) execute(INCRBYFLOAT, new Command(INCRBYFLOAT_BYTES, key0, increment1));
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
  public MultiBulkReply mget(Object... key0) throws RedisException {
    if (version < MGET_VERSION) throw new RedisException("Server does not support MGET");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (MultiBulkReply) execute(MGET, new Command(MGET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply mget_(Object... arguments) throws RedisException {
    if (version < MGET_VERSION) throw new RedisException("Server does not support MGET");
    return (MultiBulkReply) execute(MGET, new Command(MGET_BYTES, arguments));
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
  public StatusReply mset(Object... key_or_value0) throws RedisException {
    if (version < MSET_VERSION) throw new RedisException("Server does not support MSET");
    List list = new ArrayList();
    Collections.addAll(list, key_or_value0);
    return (StatusReply) execute(MSET, new Command(MSET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public StatusReply mset_(Object... arguments) throws RedisException {
    if (version < MSET_VERSION) throw new RedisException("Server does not support MSET");
    return (StatusReply) execute(MSET, new Command(MSET_BYTES, arguments));
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
  public IntegerReply msetnx(Object... key_or_value0) throws RedisException {
    if (version < MSETNX_VERSION) throw new RedisException("Server does not support MSETNX");
    List list = new ArrayList();
    Collections.addAll(list, key_or_value0);
    return (IntegerReply) execute(MSETNX, new Command(MSETNX_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply msetnx_(Object... arguments) throws RedisException {
    if (version < MSETNX_VERSION) throw new RedisException("Server does not support MSETNX");
    return (IntegerReply) execute(MSETNX, new Command(MSETNX_BYTES, arguments));
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
  public Reply psetex(Object key0, Object milliseconds1, Object value2) throws RedisException {
    if (version < PSETEX_VERSION) throw new RedisException("Server does not support PSETEX");
    return (Reply) execute(PSETEX, new Command(PSETEX_BYTES, key0, milliseconds1, value2));
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
   * @param seconds2
   * @return StatusReply
   */
  public StatusReply set(Object key0, Object value1, Object... seconds2) throws RedisException {
    if (version < SET_VERSION) throw new RedisException("Server does not support SET");
    List list = new ArrayList();
    list.add(key0);
    list.add(value1);
    Collections.addAll(list, seconds2);
    return (StatusReply) execute(SET, new Command(SET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public StatusReply set_(Object... arguments) throws RedisException {
    if (version < SET_VERSION) throw new RedisException("Server does not support SET");
    return (StatusReply) execute(SET, new Command(SET_BYTES, arguments));
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
  public IntegerReply setbit(Object key0, Object offset1, Object value2) throws RedisException {
    if (version < SETBIT_VERSION) throw new RedisException("Server does not support SETBIT");
    return (IntegerReply) execute(SETBIT, new Command(SETBIT_BYTES, key0, offset1, value2));
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
  public StatusReply setex(Object key0, Object seconds1, Object value2) throws RedisException {
    if (version < SETEX_VERSION) throw new RedisException("Server does not support SETEX");
    return (StatusReply) execute(SETEX, new Command(SETEX_BYTES, key0, seconds1, value2));
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
  public IntegerReply setnx(Object key0, Object value1) throws RedisException {
    if (version < SETNX_VERSION) throw new RedisException("Server does not support SETNX");
    return (IntegerReply) execute(SETNX, new Command(SETNX_BYTES, key0, value1));
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
  public IntegerReply setrange(Object key0, Object offset1, Object value2) throws RedisException {
    if (version < SETRANGE_VERSION) throw new RedisException("Server does not support SETRANGE");
    return (IntegerReply) execute(SETRANGE, new Command(SETRANGE_BYTES, key0, offset1, value2));
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
  public IntegerReply strlen(Object key0) throws RedisException {
    if (version < STRLEN_VERSION) throw new RedisException("Server does not support STRLEN");
    return (IntegerReply) execute(STRLEN, new Command(STRLEN_BYTES, key0));
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
  public BulkReply echo(Object message0) throws RedisException {
    if (version < ECHO_VERSION) throw new RedisException("Server does not support ECHO");
    return (BulkReply) execute(ECHO, new Command(ECHO_BYTES, message0));
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
  public StatusReply ping() throws RedisException {
    if (version < PING_VERSION) throw new RedisException("Server does not support PING");
    return (StatusReply) execute(PING, new Command(PING_BYTES));
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
  public StatusReply quit() throws RedisException {
    if (version < QUIT_VERSION) throw new RedisException("Server does not support QUIT");
    return (StatusReply) execute(QUIT, new Command(QUIT_BYTES));
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
  public StatusReply bgrewriteaof() throws RedisException {
    if (version < BGREWRITEAOF_VERSION) throw new RedisException("Server does not support BGREWRITEAOF");
    return (StatusReply) execute(BGREWRITEAOF, new Command(BGREWRITEAOF_BYTES));
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
  public StatusReply bgsave() throws RedisException {
    if (version < BGSAVE_VERSION) throw new RedisException("Server does not support BGSAVE");
    return (StatusReply) execute(BGSAVE, new Command(BGSAVE_BYTES));
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
  public Reply client_kill(Object ip_port0) throws RedisException {
    if (version < CLIENT_KILL_VERSION) throw new RedisException("Server does not support CLIENT_KILL");
    return (Reply) execute(CLIENT_KILL, new Command(CLIENT_KILL_BYTES, CLIENT_KILL2_BYTES, ip_port0));
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
  public Reply client_list() throws RedisException {
    if (version < CLIENT_LIST_VERSION) throw new RedisException("Server does not support CLIENT_LIST");
    return (Reply) execute(CLIENT_LIST, new Command(CLIENT_LIST_BYTES, CLIENT_LIST2_BYTES));
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
  public Reply client_getname() throws RedisException {
    if (version < CLIENT_GETNAME_VERSION) throw new RedisException("Server does not support CLIENT_GETNAME");
    return (Reply) execute(CLIENT_GETNAME, new Command(CLIENT_GETNAME_BYTES, CLIENT_GETNAME2_BYTES));
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
  public Reply client_setname(Object connection_name0) throws RedisException {
    if (version < CLIENT_SETNAME_VERSION) throw new RedisException("Server does not support CLIENT_SETNAME");
    return (Reply) execute(CLIENT_SETNAME, new Command(CLIENT_SETNAME_BYTES, CLIENT_SETNAME2_BYTES, connection_name0));
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
  public Reply config_get(Object parameter0) throws RedisException {
    if (version < CONFIG_GET_VERSION) throw new RedisException("Server does not support CONFIG_GET");
    return (Reply) execute(CONFIG_GET, new Command(CONFIG_GET_BYTES, CONFIG_GET2_BYTES, parameter0));
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
  public Reply config_set(Object parameter0, Object value1) throws RedisException {
    if (version < CONFIG_SET_VERSION) throw new RedisException("Server does not support CONFIG_SET");
    return (Reply) execute(CONFIG_SET, new Command(CONFIG_SET_BYTES, CONFIG_SET2_BYTES, parameter0, value1));
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
  public Reply config_resetstat() throws RedisException {
    if (version < CONFIG_RESETSTAT_VERSION) throw new RedisException("Server does not support CONFIG_RESETSTAT");
    return (Reply) execute(CONFIG_RESETSTAT, new Command(CONFIG_RESETSTAT_BYTES, CONFIG_RESETSTAT2_BYTES));
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
  public IntegerReply dbsize() throws RedisException {
    if (version < DBSIZE_VERSION) throw new RedisException("Server does not support DBSIZE");
    return (IntegerReply) execute(DBSIZE, new Command(DBSIZE_BYTES));
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
  public Reply debug_object(Object key0) throws RedisException {
    if (version < DEBUG_OBJECT_VERSION) throw new RedisException("Server does not support DEBUG_OBJECT");
    return (Reply) execute(DEBUG_OBJECT, new Command(DEBUG_OBJECT_BYTES, DEBUG_OBJECT2_BYTES, key0));
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
  public Reply debug_segfault() throws RedisException {
    if (version < DEBUG_SEGFAULT_VERSION) throw new RedisException("Server does not support DEBUG_SEGFAULT");
    return (Reply) execute(DEBUG_SEGFAULT, new Command(DEBUG_SEGFAULT_BYTES, DEBUG_SEGFAULT2_BYTES));
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
  public StatusReply flushall() throws RedisException {
    if (version < FLUSHALL_VERSION) throw new RedisException("Server does not support FLUSHALL");
    return (StatusReply) execute(FLUSHALL, new Command(FLUSHALL_BYTES));
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
  public StatusReply flushdb() throws RedisException {
    if (version < FLUSHDB_VERSION) throw new RedisException("Server does not support FLUSHDB");
    return (StatusReply) execute(FLUSHDB, new Command(FLUSHDB_BYTES));
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
  public BulkReply info(Object section0) throws RedisException {
    if (version < INFO_VERSION) throw new RedisException("Server does not support INFO");
    return (BulkReply) execute(INFO, new Command(INFO_BYTES, section0));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public BulkReply info_(Object... arguments) throws RedisException {
    if (version < INFO_VERSION) throw new RedisException("Server does not support INFO");
    return (BulkReply) execute(INFO, new Command(INFO_BYTES, arguments));
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
  public IntegerReply lastsave() throws RedisException {
    if (version < LASTSAVE_VERSION) throw new RedisException("Server does not support LASTSAVE");
    return (IntegerReply) execute(LASTSAVE, new Command(LASTSAVE_BYTES));
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
  public Reply monitor() throws RedisException {
    if (version < MONITOR_VERSION) throw new RedisException("Server does not support MONITOR");
    return (Reply) execute(MONITOR, new Command(MONITOR_BYTES));
  }
  
  private static final String SAVE = "SAVE";
  private static final byte[] SAVE_BYTES = SAVE.getBytes(Charsets.US_ASCII);
  private static final int SAVE_VERSION = parseVersion("1.0.0");

  /**
   * Synchronously save the dataset to disk
   * Server
   *
   * @return StatusReply
   */
  public StatusReply save() throws RedisException {
    if (version < SAVE_VERSION) throw new RedisException("Server does not support SAVE");
    return (StatusReply) execute(SAVE, new Command(SAVE_BYTES));
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
  public StatusReply shutdown(Object NOSAVE0, Object SAVE1) throws RedisException {
    if (version < SHUTDOWN_VERSION) throw new RedisException("Server does not support SHUTDOWN");
    return (StatusReply) execute(SHUTDOWN, new Command(SHUTDOWN_BYTES, NOSAVE0, SAVE1));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public StatusReply shutdown_(Object... arguments) throws RedisException {
    if (version < SHUTDOWN_VERSION) throw new RedisException("Server does not support SHUTDOWN");
    return (StatusReply) execute(SHUTDOWN, new Command(SHUTDOWN_BYTES, arguments));
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
  public StatusReply slaveof(Object host0, Object port1) throws RedisException {
    if (version < SLAVEOF_VERSION) throw new RedisException("Server does not support SLAVEOF");
    return (StatusReply) execute(SLAVEOF, new Command(SLAVEOF_BYTES, host0, port1));
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
  public Reply slowlog(Object subcommand0, Object argument1) throws RedisException {
    if (version < SLOWLOG_VERSION) throw new RedisException("Server does not support SLOWLOG");
    return (Reply) execute(SLOWLOG, new Command(SLOWLOG_BYTES, subcommand0, argument1));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Reply slowlog_(Object... arguments) throws RedisException {
    if (version < SLOWLOG_VERSION) throw new RedisException("Server does not support SLOWLOG");
    return (Reply) execute(SLOWLOG, new Command(SLOWLOG_BYTES, arguments));
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
  public Reply sync() throws RedisException {
    if (version < SYNC_VERSION) throw new RedisException("Server does not support SYNC");
    return (Reply) execute(SYNC, new Command(SYNC_BYTES));
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
  public MultiBulkReply time() throws RedisException {
    if (version < TIME_VERSION) throw new RedisException("Server does not support TIME");
    return (MultiBulkReply) execute(TIME, new Command(TIME_BYTES));
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
  public MultiBulkReply blpop(Object... key0) throws RedisException {
    if (version < BLPOP_VERSION) throw new RedisException("Server does not support BLPOP");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (MultiBulkReply) execute(BLPOP, new Command(BLPOP_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply blpop_(Object... arguments) throws RedisException {
    if (version < BLPOP_VERSION) throw new RedisException("Server does not support BLPOP");
    return (MultiBulkReply) execute(BLPOP, new Command(BLPOP_BYTES, arguments));
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
  public MultiBulkReply brpop(Object... key0) throws RedisException {
    if (version < BRPOP_VERSION) throw new RedisException("Server does not support BRPOP");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (MultiBulkReply) execute(BRPOP, new Command(BRPOP_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply brpop_(Object... arguments) throws RedisException {
    if (version < BRPOP_VERSION) throw new RedisException("Server does not support BRPOP");
    return (MultiBulkReply) execute(BRPOP, new Command(BRPOP_BYTES, arguments));
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
   * @return Reply
   */
  public Reply brpoplpush(Object source0, Object destination1, Object timeout2) throws RedisException {
    if (version < BRPOPLPUSH_VERSION) throw new RedisException("Server does not support BRPOPLPUSH");
    return (Reply) execute(BRPOPLPUSH, new Command(BRPOPLPUSH_BYTES, source0, destination1, timeout2));
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
  public BulkReply lindex(Object key0, Object index1) throws RedisException {
    if (version < LINDEX_VERSION) throw new RedisException("Server does not support LINDEX");
    return (BulkReply) execute(LINDEX, new Command(LINDEX_BYTES, key0, index1));
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
  public IntegerReply linsert(Object key0, Object where1, Object pivot2, Object value3) throws RedisException {
    if (version < LINSERT_VERSION) throw new RedisException("Server does not support LINSERT");
    List list = new ArrayList();
    list.add(key0);
    list.add(where1);
    list.add(pivot2);
    list.add(value3);
    return (IntegerReply) execute(LINSERT, new Command(LINSERT_BYTES, list.toArray(new Object[list.size()])));
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
  public IntegerReply llen(Object key0) throws RedisException {
    if (version < LLEN_VERSION) throw new RedisException("Server does not support LLEN");
    return (IntegerReply) execute(LLEN, new Command(LLEN_BYTES, key0));
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
  public BulkReply lpop(Object key0) throws RedisException {
    if (version < LPOP_VERSION) throw new RedisException("Server does not support LPOP");
    return (BulkReply) execute(LPOP, new Command(LPOP_BYTES, key0));
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
  public IntegerReply lpush(Object key0, Object... value1) throws RedisException {
    if (version < LPUSH_VERSION) throw new RedisException("Server does not support LPUSH");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, value1);
    return (IntegerReply) execute(LPUSH, new Command(LPUSH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply lpush_(Object... arguments) throws RedisException {
    if (version < LPUSH_VERSION) throw new RedisException("Server does not support LPUSH");
    return (IntegerReply) execute(LPUSH, new Command(LPUSH_BYTES, arguments));
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
  public IntegerReply lpushx(Object key0, Object value1) throws RedisException {
    if (version < LPUSHX_VERSION) throw new RedisException("Server does not support LPUSHX");
    return (IntegerReply) execute(LPUSHX, new Command(LPUSHX_BYTES, key0, value1));
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
  public MultiBulkReply lrange(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < LRANGE_VERSION) throw new RedisException("Server does not support LRANGE");
    return (MultiBulkReply) execute(LRANGE, new Command(LRANGE_BYTES, key0, start1, stop2));
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
  public IntegerReply lrem(Object key0, Object count1, Object value2) throws RedisException {
    if (version < LREM_VERSION) throw new RedisException("Server does not support LREM");
    return (IntegerReply) execute(LREM, new Command(LREM_BYTES, key0, count1, value2));
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
  public StatusReply lset(Object key0, Object index1, Object value2) throws RedisException {
    if (version < LSET_VERSION) throw new RedisException("Server does not support LSET");
    return (StatusReply) execute(LSET, new Command(LSET_BYTES, key0, index1, value2));
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
  public StatusReply ltrim(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < LTRIM_VERSION) throw new RedisException("Server does not support LTRIM");
    return (StatusReply) execute(LTRIM, new Command(LTRIM_BYTES, key0, start1, stop2));
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
  public BulkReply rpop(Object key0) throws RedisException {
    if (version < RPOP_VERSION) throw new RedisException("Server does not support RPOP");
    return (BulkReply) execute(RPOP, new Command(RPOP_BYTES, key0));
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
  public BulkReply rpoplpush(Object source0, Object destination1) throws RedisException {
    if (version < RPOPLPUSH_VERSION) throw new RedisException("Server does not support RPOPLPUSH");
    return (BulkReply) execute(RPOPLPUSH, new Command(RPOPLPUSH_BYTES, source0, destination1));
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
  public IntegerReply rpush(Object key0, Object... value1) throws RedisException {
    if (version < RPUSH_VERSION) throw new RedisException("Server does not support RPUSH");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, value1);
    return (IntegerReply) execute(RPUSH, new Command(RPUSH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply rpush_(Object... arguments) throws RedisException {
    if (version < RPUSH_VERSION) throw new RedisException("Server does not support RPUSH");
    return (IntegerReply) execute(RPUSH, new Command(RPUSH_BYTES, arguments));
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
  public IntegerReply rpushx(Object key0, Object value1) throws RedisException {
    if (version < RPUSHX_VERSION) throw new RedisException("Server does not support RPUSHX");
    return (IntegerReply) execute(RPUSHX, new Command(RPUSHX_BYTES, key0, value1));
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
  public IntegerReply del(Object... key0) throws RedisException {
    if (version < DEL_VERSION) throw new RedisException("Server does not support DEL");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (IntegerReply) execute(DEL, new Command(DEL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply del_(Object... arguments) throws RedisException {
    if (version < DEL_VERSION) throw new RedisException("Server does not support DEL");
    return (IntegerReply) execute(DEL, new Command(DEL_BYTES, arguments));
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
  public BulkReply dump(Object key0) throws RedisException {
    if (version < DUMP_VERSION) throw new RedisException("Server does not support DUMP");
    return (BulkReply) execute(DUMP, new Command(DUMP_BYTES, key0));
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
  public IntegerReply exists(Object key0) throws RedisException {
    if (version < EXISTS_VERSION) throw new RedisException("Server does not support EXISTS");
    return (IntegerReply) execute(EXISTS, new Command(EXISTS_BYTES, key0));
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
  public IntegerReply expire(Object key0, Object seconds1) throws RedisException {
    if (version < EXPIRE_VERSION) throw new RedisException("Server does not support EXPIRE");
    return (IntegerReply) execute(EXPIRE, new Command(EXPIRE_BYTES, key0, seconds1));
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
  public IntegerReply expireat(Object key0, Object timestamp1) throws RedisException {
    if (version < EXPIREAT_VERSION) throw new RedisException("Server does not support EXPIREAT");
    return (IntegerReply) execute(EXPIREAT, new Command(EXPIREAT_BYTES, key0, timestamp1));
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
  public MultiBulkReply keys(Object pattern0) throws RedisException {
    if (version < KEYS_VERSION) throw new RedisException("Server does not support KEYS");
    return (MultiBulkReply) execute(KEYS, new Command(KEYS_BYTES, pattern0));
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
  public StatusReply migrate(Object host0, Object port1, Object key2, Object destination_db3, Object timeout4) throws RedisException {
    if (version < MIGRATE_VERSION) throw new RedisException("Server does not support MIGRATE");
    List list = new ArrayList();
    list.add(host0);
    list.add(port1);
    list.add(key2);
    list.add(destination_db3);
    list.add(timeout4);
    return (StatusReply) execute(MIGRATE, new Command(MIGRATE_BYTES, list.toArray(new Object[list.size()])));
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
  public IntegerReply move(Object key0, Object db1) throws RedisException {
    if (version < MOVE_VERSION) throw new RedisException("Server does not support MOVE");
    return (IntegerReply) execute(MOVE, new Command(MOVE_BYTES, key0, db1));
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
  public Reply object(Object subcommand0, Object... arguments1) throws RedisException {
    if (version < OBJECT_VERSION) throw new RedisException("Server does not support OBJECT");
    List list = new ArrayList();
    list.add(subcommand0);
    Collections.addAll(list, arguments1);
    return (Reply) execute(OBJECT, new Command(OBJECT_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Reply object_(Object... arguments) throws RedisException {
    if (version < OBJECT_VERSION) throw new RedisException("Server does not support OBJECT");
    return (Reply) execute(OBJECT, new Command(OBJECT_BYTES, arguments));
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
  public IntegerReply persist(Object key0) throws RedisException {
    if (version < PERSIST_VERSION) throw new RedisException("Server does not support PERSIST");
    return (IntegerReply) execute(PERSIST, new Command(PERSIST_BYTES, key0));
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
  public IntegerReply pexpire(Object key0, Object milliseconds1) throws RedisException {
    if (version < PEXPIRE_VERSION) throw new RedisException("Server does not support PEXPIRE");
    return (IntegerReply) execute(PEXPIRE, new Command(PEXPIRE_BYTES, key0, milliseconds1));
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
  public IntegerReply pexpireat(Object key0, Object milliseconds_timestamp1) throws RedisException {
    if (version < PEXPIREAT_VERSION) throw new RedisException("Server does not support PEXPIREAT");
    return (IntegerReply) execute(PEXPIREAT, new Command(PEXPIREAT_BYTES, key0, milliseconds_timestamp1));
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
  public IntegerReply pttl(Object key0) throws RedisException {
    if (version < PTTL_VERSION) throw new RedisException("Server does not support PTTL");
    return (IntegerReply) execute(PTTL, new Command(PTTL_BYTES, key0));
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
  public BulkReply randomkey() throws RedisException {
    if (version < RANDOMKEY_VERSION) throw new RedisException("Server does not support RANDOMKEY");
    return (BulkReply) execute(RANDOMKEY, new Command(RANDOMKEY_BYTES));
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
  public StatusReply rename(Object key0, Object newkey1) throws RedisException {
    if (version < RENAME_VERSION) throw new RedisException("Server does not support RENAME");
    return (StatusReply) execute(RENAME, new Command(RENAME_BYTES, key0, newkey1));
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
  public IntegerReply renamenx(Object key0, Object newkey1) throws RedisException {
    if (version < RENAMENX_VERSION) throw new RedisException("Server does not support RENAMENX");
    return (IntegerReply) execute(RENAMENX, new Command(RENAMENX_BYTES, key0, newkey1));
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
  public StatusReply restore(Object key0, Object ttl1, Object serialized_value2) throws RedisException {
    if (version < RESTORE_VERSION) throw new RedisException("Server does not support RESTORE");
    return (StatusReply) execute(RESTORE, new Command(RESTORE_BYTES, key0, ttl1, serialized_value2));
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
  public Reply sort(Object key0, Object... pattern1) throws RedisException {
    if (version < SORT_VERSION) throw new RedisException("Server does not support SORT");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, pattern1);
    return (Reply) execute(SORT, new Command(SORT_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Reply sort_(Object... arguments) throws RedisException {
    if (version < SORT_VERSION) throw new RedisException("Server does not support SORT");
    return (Reply) execute(SORT, new Command(SORT_BYTES, arguments));
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
  public IntegerReply ttl(Object key0) throws RedisException {
    if (version < TTL_VERSION) throw new RedisException("Server does not support TTL");
    return (IntegerReply) execute(TTL, new Command(TTL_BYTES, key0));
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
  public StatusReply type(Object key0) throws RedisException {
    if (version < TYPE_VERSION) throw new RedisException("Server does not support TYPE");
    return (StatusReply) execute(TYPE, new Command(TYPE_BYTES, key0));
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
  public StatusReply unwatch() throws RedisException {
    if (version < UNWATCH_VERSION) throw new RedisException("Server does not support UNWATCH");
    return (StatusReply) execute(UNWATCH, new Command(UNWATCH_BYTES));
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
  public StatusReply watch(Object... key0) throws RedisException {
    if (version < WATCH_VERSION) throw new RedisException("Server does not support WATCH");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (StatusReply) execute(WATCH, new Command(WATCH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public StatusReply watch_(Object... arguments) throws RedisException {
    if (version < WATCH_VERSION) throw new RedisException("Server does not support WATCH");
    return (StatusReply) execute(WATCH, new Command(WATCH_BYTES, arguments));
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
  public Reply eval(Object script0, Object numkeys1, Object... key2) throws RedisException {
    if (version < EVAL_VERSION) throw new RedisException("Server does not support EVAL");
    List list = new ArrayList();
    list.add(script0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return (Reply) execute(EVAL, new Command(EVAL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Reply eval_(Object... arguments) throws RedisException {
    if (version < EVAL_VERSION) throw new RedisException("Server does not support EVAL");
    return (Reply) execute(EVAL, new Command(EVAL_BYTES, arguments));
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
  public Reply evalsha(Object sha10, Object numkeys1, Object... key2) throws RedisException {
    if (version < EVALSHA_VERSION) throw new RedisException("Server does not support EVALSHA");
    List list = new ArrayList();
    list.add(sha10);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return (Reply) execute(EVALSHA, new Command(EVALSHA_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Reply evalsha_(Object... arguments) throws RedisException {
    if (version < EVALSHA_VERSION) throw new RedisException("Server does not support EVALSHA");
    return (Reply) execute(EVALSHA, new Command(EVALSHA_BYTES, arguments));
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
  public Reply script_exists(Object... script0) throws RedisException {
    if (version < SCRIPT_EXISTS_VERSION) throw new RedisException("Server does not support SCRIPT_EXISTS");
    List list = new ArrayList();
    Collections.addAll(list, script0);
    return (Reply) execute(SCRIPT_EXISTS, new Command(SCRIPT_EXISTS_BYTES, SCRIPT_EXISTS2_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Reply script_exists_(Object... arguments) throws RedisException {
    if (version < SCRIPT_EXISTS_VERSION) throw new RedisException("Server does not support SCRIPT_EXISTS");
    return (Reply) execute(SCRIPT_EXISTS, new Command(SCRIPT_EXISTS_BYTES, SCRIPT_EXISTS2_BYTES, arguments));
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
  public Reply script_flush() throws RedisException {
    if (version < SCRIPT_FLUSH_VERSION) throw new RedisException("Server does not support SCRIPT_FLUSH");
    return (Reply) execute(SCRIPT_FLUSH, new Command(SCRIPT_FLUSH_BYTES, SCRIPT_FLUSH2_BYTES));
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
  public Reply script_kill() throws RedisException {
    if (version < SCRIPT_KILL_VERSION) throw new RedisException("Server does not support SCRIPT_KILL");
    return (Reply) execute(SCRIPT_KILL, new Command(SCRIPT_KILL_BYTES, SCRIPT_KILL2_BYTES));
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
  public Reply script_load(Object script0) throws RedisException {
    if (version < SCRIPT_LOAD_VERSION) throw new RedisException("Server does not support SCRIPT_LOAD");
    return (Reply) execute(SCRIPT_LOAD, new Command(SCRIPT_LOAD_BYTES, SCRIPT_LOAD2_BYTES, script0));
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
  public IntegerReply hdel(Object key0, Object... field1) throws RedisException {
    if (version < HDEL_VERSION) throw new RedisException("Server does not support HDEL");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, field1);
    return (IntegerReply) execute(HDEL, new Command(HDEL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply hdel_(Object... arguments) throws RedisException {
    if (version < HDEL_VERSION) throw new RedisException("Server does not support HDEL");
    return (IntegerReply) execute(HDEL, new Command(HDEL_BYTES, arguments));
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
  public IntegerReply hexists(Object key0, Object field1) throws RedisException {
    if (version < HEXISTS_VERSION) throw new RedisException("Server does not support HEXISTS");
    return (IntegerReply) execute(HEXISTS, new Command(HEXISTS_BYTES, key0, field1));
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
  public BulkReply hget(Object key0, Object field1) throws RedisException {
    if (version < HGET_VERSION) throw new RedisException("Server does not support HGET");
    return (BulkReply) execute(HGET, new Command(HGET_BYTES, key0, field1));
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
  public MultiBulkReply hgetall(Object key0) throws RedisException {
    if (version < HGETALL_VERSION) throw new RedisException("Server does not support HGETALL");
    return (MultiBulkReply) execute(HGETALL, new Command(HGETALL_BYTES, key0));
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
  public IntegerReply hincrby(Object key0, Object field1, Object increment2) throws RedisException {
    if (version < HINCRBY_VERSION) throw new RedisException("Server does not support HINCRBY");
    return (IntegerReply) execute(HINCRBY, new Command(HINCRBY_BYTES, key0, field1, increment2));
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
  public BulkReply hincrbyfloat(Object key0, Object field1, Object increment2) throws RedisException {
    if (version < HINCRBYFLOAT_VERSION) throw new RedisException("Server does not support HINCRBYFLOAT");
    return (BulkReply) execute(HINCRBYFLOAT, new Command(HINCRBYFLOAT_BYTES, key0, field1, increment2));
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
  public MultiBulkReply hkeys(Object key0) throws RedisException {
    if (version < HKEYS_VERSION) throw new RedisException("Server does not support HKEYS");
    return (MultiBulkReply) execute(HKEYS, new Command(HKEYS_BYTES, key0));
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
  public IntegerReply hlen(Object key0) throws RedisException {
    if (version < HLEN_VERSION) throw new RedisException("Server does not support HLEN");
    return (IntegerReply) execute(HLEN, new Command(HLEN_BYTES, key0));
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
  public MultiBulkReply hmget(Object key0, Object... field1) throws RedisException {
    if (version < HMGET_VERSION) throw new RedisException("Server does not support HMGET");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, field1);
    return (MultiBulkReply) execute(HMGET, new Command(HMGET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply hmget_(Object... arguments) throws RedisException {
    if (version < HMGET_VERSION) throw new RedisException("Server does not support HMGET");
    return (MultiBulkReply) execute(HMGET, new Command(HMGET_BYTES, arguments));
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
  public StatusReply hmset(Object key0, Object... field_or_value1) throws RedisException {
    if (version < HMSET_VERSION) throw new RedisException("Server does not support HMSET");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, field_or_value1);
    return (StatusReply) execute(HMSET, new Command(HMSET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public StatusReply hmset_(Object... arguments) throws RedisException {
    if (version < HMSET_VERSION) throw new RedisException("Server does not support HMSET");
    return (StatusReply) execute(HMSET, new Command(HMSET_BYTES, arguments));
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
  public IntegerReply hset(Object key0, Object field1, Object value2) throws RedisException {
    if (version < HSET_VERSION) throw new RedisException("Server does not support HSET");
    return (IntegerReply) execute(HSET, new Command(HSET_BYTES, key0, field1, value2));
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
  public IntegerReply hsetnx(Object key0, Object field1, Object value2) throws RedisException {
    if (version < HSETNX_VERSION) throw new RedisException("Server does not support HSETNX");
    return (IntegerReply) execute(HSETNX, new Command(HSETNX_BYTES, key0, field1, value2));
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
  public MultiBulkReply hvals(Object key0) throws RedisException {
    if (version < HVALS_VERSION) throw new RedisException("Server does not support HVALS");
    return (MultiBulkReply) execute(HVALS, new Command(HVALS_BYTES, key0));
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
  public IntegerReply publish(Object channel0, Object message1) throws RedisException {
    if (version < PUBLISH_VERSION) throw new RedisException("Server does not support PUBLISH");
    return (IntegerReply) execute(PUBLISH, new Command(PUBLISH_BYTES, channel0, message1));
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
  public IntegerReply sadd(Object key0, Object... member1) throws RedisException {
    if (version < SADD_VERSION) throw new RedisException("Server does not support SADD");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, member1);
    return (IntegerReply) execute(SADD, new Command(SADD_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply sadd_(Object... arguments) throws RedisException {
    if (version < SADD_VERSION) throw new RedisException("Server does not support SADD");
    return (IntegerReply) execute(SADD, new Command(SADD_BYTES, arguments));
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
  public IntegerReply scard(Object key0) throws RedisException {
    if (version < SCARD_VERSION) throw new RedisException("Server does not support SCARD");
    return (IntegerReply) execute(SCARD, new Command(SCARD_BYTES, key0));
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
  public MultiBulkReply sdiff(Object... key0) throws RedisException {
    if (version < SDIFF_VERSION) throw new RedisException("Server does not support SDIFF");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (MultiBulkReply) execute(SDIFF, new Command(SDIFF_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply sdiff_(Object... arguments) throws RedisException {
    if (version < SDIFF_VERSION) throw new RedisException("Server does not support SDIFF");
    return (MultiBulkReply) execute(SDIFF, new Command(SDIFF_BYTES, arguments));
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
  public IntegerReply sdiffstore(Object destination0, Object... key1) throws RedisException {
    if (version < SDIFFSTORE_VERSION) throw new RedisException("Server does not support SDIFFSTORE");
    List list = new ArrayList();
    list.add(destination0);
    Collections.addAll(list, key1);
    return (IntegerReply) execute(SDIFFSTORE, new Command(SDIFFSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply sdiffstore_(Object... arguments) throws RedisException {
    if (version < SDIFFSTORE_VERSION) throw new RedisException("Server does not support SDIFFSTORE");
    return (IntegerReply) execute(SDIFFSTORE, new Command(SDIFFSTORE_BYTES, arguments));
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
  public MultiBulkReply sinter(Object... key0) throws RedisException {
    if (version < SINTER_VERSION) throw new RedisException("Server does not support SINTER");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (MultiBulkReply) execute(SINTER, new Command(SINTER_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply sinter_(Object... arguments) throws RedisException {
    if (version < SINTER_VERSION) throw new RedisException("Server does not support SINTER");
    return (MultiBulkReply) execute(SINTER, new Command(SINTER_BYTES, arguments));
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
  public IntegerReply sinterstore(Object destination0, Object... key1) throws RedisException {
    if (version < SINTERSTORE_VERSION) throw new RedisException("Server does not support SINTERSTORE");
    List list = new ArrayList();
    list.add(destination0);
    Collections.addAll(list, key1);
    return (IntegerReply) execute(SINTERSTORE, new Command(SINTERSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply sinterstore_(Object... arguments) throws RedisException {
    if (version < SINTERSTORE_VERSION) throw new RedisException("Server does not support SINTERSTORE");
    return (IntegerReply) execute(SINTERSTORE, new Command(SINTERSTORE_BYTES, arguments));
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
  public IntegerReply sismember(Object key0, Object member1) throws RedisException {
    if (version < SISMEMBER_VERSION) throw new RedisException("Server does not support SISMEMBER");
    return (IntegerReply) execute(SISMEMBER, new Command(SISMEMBER_BYTES, key0, member1));
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
  public MultiBulkReply smembers(Object key0) throws RedisException {
    if (version < SMEMBERS_VERSION) throw new RedisException("Server does not support SMEMBERS");
    return (MultiBulkReply) execute(SMEMBERS, new Command(SMEMBERS_BYTES, key0));
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
  public IntegerReply smove(Object source0, Object destination1, Object member2) throws RedisException {
    if (version < SMOVE_VERSION) throw new RedisException("Server does not support SMOVE");
    return (IntegerReply) execute(SMOVE, new Command(SMOVE_BYTES, source0, destination1, member2));
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
  public BulkReply spop(Object key0) throws RedisException {
    if (version < SPOP_VERSION) throw new RedisException("Server does not support SPOP");
    return (BulkReply) execute(SPOP, new Command(SPOP_BYTES, key0));
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
  public Reply srandmember(Object key0, Object count1) throws RedisException {
    if (version < SRANDMEMBER_VERSION) throw new RedisException("Server does not support SRANDMEMBER");
    return (Reply) execute(SRANDMEMBER, new Command(SRANDMEMBER_BYTES, key0, count1));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public Reply srandmember_(Object... arguments) throws RedisException {
    if (version < SRANDMEMBER_VERSION) throw new RedisException("Server does not support SRANDMEMBER");
    return (Reply) execute(SRANDMEMBER, new Command(SRANDMEMBER_BYTES, arguments));
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
  public IntegerReply srem(Object key0, Object... member1) throws RedisException {
    if (version < SREM_VERSION) throw new RedisException("Server does not support SREM");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, member1);
    return (IntegerReply) execute(SREM, new Command(SREM_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply srem_(Object... arguments) throws RedisException {
    if (version < SREM_VERSION) throw new RedisException("Server does not support SREM");
    return (IntegerReply) execute(SREM, new Command(SREM_BYTES, arguments));
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
  public MultiBulkReply sunion(Object... key0) throws RedisException {
    if (version < SUNION_VERSION) throw new RedisException("Server does not support SUNION");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (MultiBulkReply) execute(SUNION, new Command(SUNION_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply sunion_(Object... arguments) throws RedisException {
    if (version < SUNION_VERSION) throw new RedisException("Server does not support SUNION");
    return (MultiBulkReply) execute(SUNION, new Command(SUNION_BYTES, arguments));
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
  public IntegerReply sunionstore(Object destination0, Object... key1) throws RedisException {
    if (version < SUNIONSTORE_VERSION) throw new RedisException("Server does not support SUNIONSTORE");
    List list = new ArrayList();
    list.add(destination0);
    Collections.addAll(list, key1);
    return (IntegerReply) execute(SUNIONSTORE, new Command(SUNIONSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply sunionstore_(Object... arguments) throws RedisException {
    if (version < SUNIONSTORE_VERSION) throw new RedisException("Server does not support SUNIONSTORE");
    return (IntegerReply) execute(SUNIONSTORE, new Command(SUNIONSTORE_BYTES, arguments));
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
  public IntegerReply zadd(Object... args) throws RedisException {
    if (version < ZADD_VERSION) throw new RedisException("Server does not support ZADD");
    return (IntegerReply) execute(ZADD, new Command(ZADD_BYTES, args));
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
  public IntegerReply zcard(Object key0) throws RedisException {
    if (version < ZCARD_VERSION) throw new RedisException("Server does not support ZCARD");
    return (IntegerReply) execute(ZCARD, new Command(ZCARD_BYTES, key0));
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
  public IntegerReply zcount(Object key0, Object min1, Object max2) throws RedisException {
    if (version < ZCOUNT_VERSION) throw new RedisException("Server does not support ZCOUNT");
    return (IntegerReply) execute(ZCOUNT, new Command(ZCOUNT_BYTES, key0, min1, max2));
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
  public BulkReply zincrby(Object key0, Object increment1, Object member2) throws RedisException {
    if (version < ZINCRBY_VERSION) throw new RedisException("Server does not support ZINCRBY");
    return (BulkReply) execute(ZINCRBY, new Command(ZINCRBY_BYTES, key0, increment1, member2));
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
  public IntegerReply zinterstore(Object destination0, Object numkeys1, Object... key2) throws RedisException {
    if (version < ZINTERSTORE_VERSION) throw new RedisException("Server does not support ZINTERSTORE");
    List list = new ArrayList();
    list.add(destination0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return (IntegerReply) execute(ZINTERSTORE, new Command(ZINTERSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply zinterstore_(Object... arguments) throws RedisException {
    if (version < ZINTERSTORE_VERSION) throw new RedisException("Server does not support ZINTERSTORE");
    return (IntegerReply) execute(ZINTERSTORE, new Command(ZINTERSTORE_BYTES, arguments));
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
  public MultiBulkReply zrange(Object key0, Object start1, Object stop2, Object withscores3) throws RedisException {
    if (version < ZRANGE_VERSION) throw new RedisException("Server does not support ZRANGE");
    List list = new ArrayList();
    list.add(key0);
    list.add(start1);
    list.add(stop2);
    if (withscores3 != null) list.add(withscores3);
    return (MultiBulkReply) execute(ZRANGE, new Command(ZRANGE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply zrange_(Object... arguments) throws RedisException {
    if (version < ZRANGE_VERSION) throw new RedisException("Server does not support ZRANGE");
    return (MultiBulkReply) execute(ZRANGE, new Command(ZRANGE_BYTES, arguments));
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
  public MultiBulkReply zrangebyscore(Object key0, Object min1, Object max2, Object withscores3, Object... offset_or_count4) throws RedisException {
    if (version < ZRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZRANGEBYSCORE");
    List list = new ArrayList();
    list.add(key0);
    list.add(min1);
    list.add(max2);
    if (withscores3 != null) list.add(withscores3);
    Collections.addAll(list, offset_or_count4);
    return (MultiBulkReply) execute(ZRANGEBYSCORE, new Command(ZRANGEBYSCORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply zrangebyscore_(Object... arguments) throws RedisException {
    if (version < ZRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZRANGEBYSCORE");
    return (MultiBulkReply) execute(ZRANGEBYSCORE, new Command(ZRANGEBYSCORE_BYTES, arguments));
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
  public Reply zrank(Object key0, Object member1) throws RedisException {
    if (version < ZRANK_VERSION) throw new RedisException("Server does not support ZRANK");
    return (Reply) execute(ZRANK, new Command(ZRANK_BYTES, key0, member1));
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
  public IntegerReply zrem(Object key0, Object... member1) throws RedisException {
    if (version < ZREM_VERSION) throw new RedisException("Server does not support ZREM");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, member1);
    return (IntegerReply) execute(ZREM, new Command(ZREM_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply zrem_(Object... arguments) throws RedisException {
    if (version < ZREM_VERSION) throw new RedisException("Server does not support ZREM");
    return (IntegerReply) execute(ZREM, new Command(ZREM_BYTES, arguments));
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
  public IntegerReply zremrangebyrank(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < ZREMRANGEBYRANK_VERSION) throw new RedisException("Server does not support ZREMRANGEBYRANK");
    return (IntegerReply) execute(ZREMRANGEBYRANK, new Command(ZREMRANGEBYRANK_BYTES, key0, start1, stop2));
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
  public IntegerReply zremrangebyscore(Object key0, Object min1, Object max2) throws RedisException {
    if (version < ZREMRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREMRANGEBYSCORE");
    return (IntegerReply) execute(ZREMRANGEBYSCORE, new Command(ZREMRANGEBYSCORE_BYTES, key0, min1, max2));
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
  public MultiBulkReply zrevrange(Object key0, Object start1, Object stop2, Object withscores3) throws RedisException {
    if (version < ZREVRANGE_VERSION) throw new RedisException("Server does not support ZREVRANGE");
    List list = new ArrayList();
    list.add(key0);
    list.add(start1);
    list.add(stop2);
    if (withscores3 != null) list.add(withscores3);
    return (MultiBulkReply) execute(ZREVRANGE, new Command(ZREVRANGE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply zrevrange_(Object... arguments) throws RedisException {
    if (version < ZREVRANGE_VERSION) throw new RedisException("Server does not support ZREVRANGE");
    return (MultiBulkReply) execute(ZREVRANGE, new Command(ZREVRANGE_BYTES, arguments));
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
  public MultiBulkReply zrevrangebyscore(Object key0, Object max1, Object min2, Object withscores3, Object... offset_or_count4) throws RedisException {
    if (version < ZREVRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREVRANGEBYSCORE");
    List list = new ArrayList();
    list.add(key0);
    list.add(max1);
    list.add(min2);
    if (withscores3 != null) list.add(withscores3);
    Collections.addAll(list, offset_or_count4);
    return (MultiBulkReply) execute(ZREVRANGEBYSCORE, new Command(ZREVRANGEBYSCORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public MultiBulkReply zrevrangebyscore_(Object... arguments) throws RedisException {
    if (version < ZREVRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREVRANGEBYSCORE");
    return (MultiBulkReply) execute(ZREVRANGEBYSCORE, new Command(ZREVRANGEBYSCORE_BYTES, arguments));
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
  public Reply zrevrank(Object key0, Object member1) throws RedisException {
    if (version < ZREVRANK_VERSION) throw new RedisException("Server does not support ZREVRANK");
    return (Reply) execute(ZREVRANK, new Command(ZREVRANK_BYTES, key0, member1));
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
  public BulkReply zscore(Object key0, Object member1) throws RedisException {
    if (version < ZSCORE_VERSION) throw new RedisException("Server does not support ZSCORE");
    return (BulkReply) execute(ZSCORE, new Command(ZSCORE_BYTES, key0, member1));
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
  public IntegerReply zunionstore(Object destination0, Object numkeys1, Object... key2) throws RedisException {
    if (version < ZUNIONSTORE_VERSION) throw new RedisException("Server does not support ZUNIONSTORE");
    List list = new ArrayList();
    list.add(destination0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return (IntegerReply) execute(ZUNIONSTORE, new Command(ZUNIONSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public IntegerReply zunionstore_(Object... arguments) throws RedisException {
    if (version < ZUNIONSTORE_VERSION) throw new RedisException("Server does not support ZUNIONSTORE");
    return (IntegerReply) execute(ZUNIONSTORE, new Command(ZUNIONSTORE_BYTES, arguments));
  }

  public class Pipeline {

  /**
   * Append a value to a key
   * String
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> append(Object key0, Object value1) throws RedisException {
    if (version < APPEND_VERSION) throw new RedisException("Server does not support APPEND");
    return (CompletableFuture<IntegerReply>) pipeline(APPEND, new Command(APPEND_BYTES, key0, value1));
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
  public CompletableFuture<IntegerReply> bitcount(Object key0, Object start1, Object end2) throws RedisException {
    if (version < BITCOUNT_VERSION) throw new RedisException("Server does not support BITCOUNT");
    return (CompletableFuture<IntegerReply>) pipeline(BITCOUNT, new Command(BITCOUNT_BYTES, key0, start1, end2));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> bitcount_(Object... arguments) throws RedisException {
    if (version < BITCOUNT_VERSION) throw new RedisException("Server does not support BITCOUNT");
    return (CompletableFuture<IntegerReply>) pipeline(BITCOUNT, new Command(BITCOUNT_BYTES, arguments));
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
  public CompletableFuture<IntegerReply> bitop(Object operation0, Object destkey1, Object... key2) throws RedisException {
    if (version < BITOP_VERSION) throw new RedisException("Server does not support BITOP");
    List list = new ArrayList();
    list.add(operation0);
    list.add(destkey1);
    Collections.addAll(list, key2);
    return (CompletableFuture<IntegerReply>) pipeline(BITOP, new Command(BITOP_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> bitop_(Object... arguments) throws RedisException {
    if (version < BITOP_VERSION) throw new RedisException("Server does not support BITOP");
    return (CompletableFuture<IntegerReply>) pipeline(BITOP, new Command(BITOP_BYTES, arguments));
  }

  /**
   * Decrement the integer value of a key by one
   * String
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> decr(Object key0) throws RedisException {
    if (version < DECR_VERSION) throw new RedisException("Server does not support DECR");
    return (CompletableFuture<IntegerReply>) pipeline(DECR, new Command(DECR_BYTES, key0));
  }

  /**
   * Decrement the integer value of a key by the given number
   * String
   *
   * @param key0
   * @param decrement1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> decrby(Object key0, Object decrement1) throws RedisException {
    if (version < DECRBY_VERSION) throw new RedisException("Server does not support DECRBY");
    return (CompletableFuture<IntegerReply>) pipeline(DECRBY, new Command(DECRBY_BYTES, key0, decrement1));
  }

  /**
   * Get the value of a key
   * String
   *
   * @param key0
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> get(Object key0) throws RedisException {
    if (version < GET_VERSION) throw new RedisException("Server does not support GET");
    return (CompletableFuture<BulkReply>) pipeline(GET, new Command(GET_BYTES, key0));
  }

  /**
   * Returns the bit value at offset in the string value stored at key
   * String
   *
   * @param key0
   * @param offset1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> getbit(Object key0, Object offset1) throws RedisException {
    if (version < GETBIT_VERSION) throw new RedisException("Server does not support GETBIT");
    return (CompletableFuture<IntegerReply>) pipeline(GETBIT, new Command(GETBIT_BYTES, key0, offset1));
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
  public CompletableFuture<BulkReply> getrange(Object key0, Object start1, Object end2) throws RedisException {
    if (version < GETRANGE_VERSION) throw new RedisException("Server does not support GETRANGE");
    return (CompletableFuture<BulkReply>) pipeline(GETRANGE, new Command(GETRANGE_BYTES, key0, start1, end2));
  }

  /**
   * Set the string value of a key and return its old value
   * String
   *
   * @param key0
   * @param value1
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> getset(Object key0, Object value1) throws RedisException {
    if (version < GETSET_VERSION) throw new RedisException("Server does not support GETSET");
    return (CompletableFuture<BulkReply>) pipeline(GETSET, new Command(GETSET_BYTES, key0, value1));
  }

  /**
   * Increment the integer value of a key by one
   * String
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> incr(Object key0) throws RedisException {
    if (version < INCR_VERSION) throw new RedisException("Server does not support INCR");
    return (CompletableFuture<IntegerReply>) pipeline(INCR, new Command(INCR_BYTES, key0));
  }

  /**
   * Increment the integer value of a key by the given amount
   * String
   *
   * @param key0
   * @param increment1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> incrby(Object key0, Object increment1) throws RedisException {
    if (version < INCRBY_VERSION) throw new RedisException("Server does not support INCRBY");
    return (CompletableFuture<IntegerReply>) pipeline(INCRBY, new Command(INCRBY_BYTES, key0, increment1));
  }

  /**
   * Increment the float value of a key by the given amount
   * String
   *
   * @param key0
   * @param increment1
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> incrbyfloat(Object key0, Object increment1) throws RedisException {
    if (version < INCRBYFLOAT_VERSION) throw new RedisException("Server does not support INCRBYFLOAT");
    return (CompletableFuture<BulkReply>) pipeline(INCRBYFLOAT, new Command(INCRBYFLOAT_BYTES, key0, increment1));
  }

  /**
   * Get the values of all the given keys
   * String
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> mget(Object... key0) throws RedisException {
    if (version < MGET_VERSION) throw new RedisException("Server does not support MGET");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (CompletableFuture<MultiBulkReply>) pipeline(MGET, new Command(MGET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> mget_(Object... arguments) throws RedisException {
    if (version < MGET_VERSION) throw new RedisException("Server does not support MGET");
    return (CompletableFuture<MultiBulkReply>) pipeline(MGET, new Command(MGET_BYTES, arguments));
  }

  /**
   * Set multiple keys to multiple values
   * String
   *
   * @param key_or_value0
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> mset(Object... key_or_value0) throws RedisException {
    if (version < MSET_VERSION) throw new RedisException("Server does not support MSET");
    List list = new ArrayList();
    Collections.addAll(list, key_or_value0);
    return (CompletableFuture<StatusReply>) pipeline(MSET, new Command(MSET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<StatusReply> mset_(Object... arguments) throws RedisException {
    if (version < MSET_VERSION) throw new RedisException("Server does not support MSET");
    return (CompletableFuture<StatusReply>) pipeline(MSET, new Command(MSET_BYTES, arguments));
  }

  /**
   * Set multiple keys to multiple values, only if none of the keys exist
   * String
   *
   * @param key_or_value0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> msetnx(Object... key_or_value0) throws RedisException {
    if (version < MSETNX_VERSION) throw new RedisException("Server does not support MSETNX");
    List list = new ArrayList();
    Collections.addAll(list, key_or_value0);
    return (CompletableFuture<IntegerReply>) pipeline(MSETNX, new Command(MSETNX_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> msetnx_(Object... arguments) throws RedisException {
    if (version < MSETNX_VERSION) throw new RedisException("Server does not support MSETNX");
    return (CompletableFuture<IntegerReply>) pipeline(MSETNX, new Command(MSETNX_BYTES, arguments));
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
  public CompletableFuture<Reply> psetex(Object key0, Object milliseconds1, Object value2) throws RedisException {
    if (version < PSETEX_VERSION) throw new RedisException("Server does not support PSETEX");
    return (CompletableFuture<Reply>) pipeline(PSETEX, new Command(PSETEX_BYTES, key0, milliseconds1, value2));
  }

  /**
   * Set the string value of a key
   * String
   *
   * @param key0
   * @param value1
   * @param seconds2
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> set(Object key0, Object value1, Object... seconds2) throws RedisException {
    if (version < SET_VERSION) throw new RedisException("Server does not support SET");
    List list = new ArrayList();
    list.add(key0);
    list.add(value1);
    Collections.addAll(list, seconds2);
    return (CompletableFuture<StatusReply>) pipeline(SET, new Command(SET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<StatusReply> set_(Object... arguments) throws RedisException {
    if (version < SET_VERSION) throw new RedisException("Server does not support SET");
    return (CompletableFuture<StatusReply>) pipeline(SET, new Command(SET_BYTES, arguments));
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
  public CompletableFuture<IntegerReply> setbit(Object key0, Object offset1, Object value2) throws RedisException {
    if (version < SETBIT_VERSION) throw new RedisException("Server does not support SETBIT");
    return (CompletableFuture<IntegerReply>) pipeline(SETBIT, new Command(SETBIT_BYTES, key0, offset1, value2));
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
  public CompletableFuture<StatusReply> setex(Object key0, Object seconds1, Object value2) throws RedisException {
    if (version < SETEX_VERSION) throw new RedisException("Server does not support SETEX");
    return (CompletableFuture<StatusReply>) pipeline(SETEX, new Command(SETEX_BYTES, key0, seconds1, value2));
  }

  /**
   * Set the value of a key, only if the key does not exist
   * String
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> setnx(Object key0, Object value1) throws RedisException {
    if (version < SETNX_VERSION) throw new RedisException("Server does not support SETNX");
    return (CompletableFuture<IntegerReply>) pipeline(SETNX, new Command(SETNX_BYTES, key0, value1));
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
  public CompletableFuture<IntegerReply> setrange(Object key0, Object offset1, Object value2) throws RedisException {
    if (version < SETRANGE_VERSION) throw new RedisException("Server does not support SETRANGE");
    return (CompletableFuture<IntegerReply>) pipeline(SETRANGE, new Command(SETRANGE_BYTES, key0, offset1, value2));
  }

  /**
   * Get the length of the value stored in a key
   * String
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> strlen(Object key0) throws RedisException {
    if (version < STRLEN_VERSION) throw new RedisException("Server does not support STRLEN");
    return (CompletableFuture<IntegerReply>) pipeline(STRLEN, new Command(STRLEN_BYTES, key0));
  }

  /**
   * Echo the given string
   * Connection
   *
   * @param message0
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> echo(Object message0) throws RedisException {
    if (version < ECHO_VERSION) throw new RedisException("Server does not support ECHO");
    return (CompletableFuture<BulkReply>) pipeline(ECHO, new Command(ECHO_BYTES, message0));
  }

  /**
   * Ping the server
   * Connection
   *
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> ping() throws RedisException {
    if (version < PING_VERSION) throw new RedisException("Server does not support PING");
    return (CompletableFuture<StatusReply>) pipeline(PING, new Command(PING_BYTES));
  }

  /**
   * Close the connection
   * Connection
   *
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> quit() throws RedisException {
    if (version < QUIT_VERSION) throw new RedisException("Server does not support QUIT");
    return (CompletableFuture<StatusReply>) pipeline(QUIT, new Command(QUIT_BYTES));
  }

  /**
   * Change the selected database for the current connection
   * Connection
   *
   * @param index0
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> select(Object index0) throws RedisException {
    if (version < SELECT_VERSION) throw new RedisException("Server does not support SELECT");
    return (CompletableFuture<StatusReply>) pipeline(SELECT, new Command(SELECT_BYTES, index0));
  }

  /**
   * Asynchronously rewrite the append-only file
   * Server
   *
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> bgrewriteaof() throws RedisException {
    if (version < BGREWRITEAOF_VERSION) throw new RedisException("Server does not support BGREWRITEAOF");
    return (CompletableFuture<StatusReply>) pipeline(BGREWRITEAOF, new Command(BGREWRITEAOF_BYTES));
  }

  /**
   * Asynchronously save the dataset to disk
   * Server
   *
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> bgsave() throws RedisException {
    if (version < BGSAVE_VERSION) throw new RedisException("Server does not support BGSAVE");
    return (CompletableFuture<StatusReply>) pipeline(BGSAVE, new Command(BGSAVE_BYTES));
  }

  /**
   * Kill the connection of a client
   * Server
   *
   * @param ip_port0
   * @return Reply
   */
  public CompletableFuture<Reply> client_kill(Object ip_port0) throws RedisException {
    if (version < CLIENT_KILL_VERSION) throw new RedisException("Server does not support CLIENT_KILL");
    return (CompletableFuture<Reply>) pipeline(CLIENT_KILL, new Command(CLIENT_KILL_BYTES, CLIENT_KILL2_BYTES, ip_port0));
  }

  /**
   * Get the list of client connections
   * Server
   *
   * @return Reply
   */
  public CompletableFuture<Reply> client_list() throws RedisException {
    if (version < CLIENT_LIST_VERSION) throw new RedisException("Server does not support CLIENT_LIST");
    return (CompletableFuture<Reply>) pipeline(CLIENT_LIST, new Command(CLIENT_LIST_BYTES, CLIENT_LIST2_BYTES));
  }

  /**
   * Get the current connection name
   * Server
   *
   * @return Reply
   */
  public CompletableFuture<Reply> client_getname() throws RedisException {
    if (version < CLIENT_GETNAME_VERSION) throw new RedisException("Server does not support CLIENT_GETNAME");
    return (CompletableFuture<Reply>) pipeline(CLIENT_GETNAME, new Command(CLIENT_GETNAME_BYTES, CLIENT_GETNAME2_BYTES));
  }

  /**
   * Set the current connection name
   * Server
   *
   * @param connection_name0
   * @return Reply
   */
  public CompletableFuture<Reply> client_setname(Object connection_name0) throws RedisException {
    if (version < CLIENT_SETNAME_VERSION) throw new RedisException("Server does not support CLIENT_SETNAME");
    return (CompletableFuture<Reply>) pipeline(CLIENT_SETNAME, new Command(CLIENT_SETNAME_BYTES, CLIENT_SETNAME2_BYTES, connection_name0));
  }

  /**
   * Get the value of a configuration parameter
   * Server
   *
   * @param parameter0
   * @return Reply
   */
  public CompletableFuture<Reply> config_get(Object parameter0) throws RedisException {
    if (version < CONFIG_GET_VERSION) throw new RedisException("Server does not support CONFIG_GET");
    return (CompletableFuture<Reply>) pipeline(CONFIG_GET, new Command(CONFIG_GET_BYTES, CONFIG_GET2_BYTES, parameter0));
  }

  /**
   * Set a configuration parameter to the given value
   * Server
   *
   * @param parameter0
   * @param value1
   * @return Reply
   */
  public CompletableFuture<Reply> config_set(Object parameter0, Object value1) throws RedisException {
    if (version < CONFIG_SET_VERSION) throw new RedisException("Server does not support CONFIG_SET");
    return (CompletableFuture<Reply>) pipeline(CONFIG_SET, new Command(CONFIG_SET_BYTES, CONFIG_SET2_BYTES, parameter0, value1));
  }

  /**
   * Reset the stats returned by INFO
   * Server
   *
   * @return Reply
   */
  public CompletableFuture<Reply> config_resetstat() throws RedisException {
    if (version < CONFIG_RESETSTAT_VERSION) throw new RedisException("Server does not support CONFIG_RESETSTAT");
    return (CompletableFuture<Reply>) pipeline(CONFIG_RESETSTAT, new Command(CONFIG_RESETSTAT_BYTES, CONFIG_RESETSTAT2_BYTES));
  }

  /**
   * Return the number of keys in the selected database
   * Server
   *
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> dbsize() throws RedisException {
    if (version < DBSIZE_VERSION) throw new RedisException("Server does not support DBSIZE");
    return (CompletableFuture<IntegerReply>) pipeline(DBSIZE, new Command(DBSIZE_BYTES));
  }

  /**
   * Get debugging information about a key
   * Server
   *
   * @param key0
   * @return Reply
   */
  public CompletableFuture<Reply> debug_object(Object key0) throws RedisException {
    if (version < DEBUG_OBJECT_VERSION) throw new RedisException("Server does not support DEBUG_OBJECT");
    return (CompletableFuture<Reply>) pipeline(DEBUG_OBJECT, new Command(DEBUG_OBJECT_BYTES, DEBUG_OBJECT2_BYTES, key0));
  }

  /**
   * Make the server crash
   * Server
   *
   * @return Reply
   */
  public CompletableFuture<Reply> debug_segfault() throws RedisException {
    if (version < DEBUG_SEGFAULT_VERSION) throw new RedisException("Server does not support DEBUG_SEGFAULT");
    return (CompletableFuture<Reply>) pipeline(DEBUG_SEGFAULT, new Command(DEBUG_SEGFAULT_BYTES, DEBUG_SEGFAULT2_BYTES));
  }

  /**
   * Remove all keys from all databases
   * Server
   *
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> flushall() throws RedisException {
    if (version < FLUSHALL_VERSION) throw new RedisException("Server does not support FLUSHALL");
    return (CompletableFuture<StatusReply>) pipeline(FLUSHALL, new Command(FLUSHALL_BYTES));
  }

  /**
   * Remove all keys from the current database
   * Server
   *
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> flushdb() throws RedisException {
    if (version < FLUSHDB_VERSION) throw new RedisException("Server does not support FLUSHDB");
    return (CompletableFuture<StatusReply>) pipeline(FLUSHDB, new Command(FLUSHDB_BYTES));
  }

  /**
   * Get information and statistics about the server
   * Server
   *
   * @param section0
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> info(Object section0) throws RedisException {
    if (version < INFO_VERSION) throw new RedisException("Server does not support INFO");
    return (CompletableFuture<BulkReply>) pipeline(INFO, new Command(INFO_BYTES, section0));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<BulkReply> info_(Object... arguments) throws RedisException {
    if (version < INFO_VERSION) throw new RedisException("Server does not support INFO");
    return (CompletableFuture<BulkReply>) pipeline(INFO, new Command(INFO_BYTES, arguments));
  }

  /**
   * Get the UNIX time stamp of the last successful save to disk
   * Server
   *
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> lastsave() throws RedisException {
    if (version < LASTSAVE_VERSION) throw new RedisException("Server does not support LASTSAVE");
    return (CompletableFuture<IntegerReply>) pipeline(LASTSAVE, new Command(LASTSAVE_BYTES));
  }

  /**
   * Listen for all requests received by the server in real time
   * Server
   *
   * @return Reply
   */
  public CompletableFuture<Reply> monitor() throws RedisException {
    if (version < MONITOR_VERSION) throw new RedisException("Server does not support MONITOR");
    return (CompletableFuture<Reply>) pipeline(MONITOR, new Command(MONITOR_BYTES));
  }

  /**
   * Synchronously save the dataset to disk
   * Server
   *
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> save() throws RedisException {
    if (version < SAVE_VERSION) throw new RedisException("Server does not support SAVE");
    return (CompletableFuture<StatusReply>) pipeline(SAVE, new Command(SAVE_BYTES));
  }

  /**
   * Synchronously save the dataset to disk and then shut down the server
   * Server
   *
   * @param NOSAVE0
   * @param SAVE1
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> shutdown(Object NOSAVE0, Object SAVE1) throws RedisException {
    if (version < SHUTDOWN_VERSION) throw new RedisException("Server does not support SHUTDOWN");
    return (CompletableFuture<StatusReply>) pipeline(SHUTDOWN, new Command(SHUTDOWN_BYTES, NOSAVE0, SAVE1));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<StatusReply> shutdown_(Object... arguments) throws RedisException {
    if (version < SHUTDOWN_VERSION) throw new RedisException("Server does not support SHUTDOWN");
    return (CompletableFuture<StatusReply>) pipeline(SHUTDOWN, new Command(SHUTDOWN_BYTES, arguments));
  }

  /**
   * Make the server a slave of another instance, or promote it as master
   * Server
   *
   * @param host0
   * @param port1
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> slaveof(Object host0, Object port1) throws RedisException {
    if (version < SLAVEOF_VERSION) throw new RedisException("Server does not support SLAVEOF");
    return (CompletableFuture<StatusReply>) pipeline(SLAVEOF, new Command(SLAVEOF_BYTES, host0, port1));
  }

  /**
   * Manages the Redis slow queries log
   * Server
   *
   * @param subcommand0
   * @param argument1
   * @return Reply
   */
  public CompletableFuture<Reply> slowlog(Object subcommand0, Object argument1) throws RedisException {
    if (version < SLOWLOG_VERSION) throw new RedisException("Server does not support SLOWLOG");
    return (CompletableFuture<Reply>) pipeline(SLOWLOG, new Command(SLOWLOG_BYTES, subcommand0, argument1));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<Reply> slowlog_(Object... arguments) throws RedisException {
    if (version < SLOWLOG_VERSION) throw new RedisException("Server does not support SLOWLOG");
    return (CompletableFuture<Reply>) pipeline(SLOWLOG, new Command(SLOWLOG_BYTES, arguments));
  }

  /**
   * Internal command used for replication
   * Server
   *
   * @return Reply
   */
  public CompletableFuture<Reply> sync() throws RedisException {
    if (version < SYNC_VERSION) throw new RedisException("Server does not support SYNC");
    return (CompletableFuture<Reply>) pipeline(SYNC, new Command(SYNC_BYTES));
  }

  /**
   * Return the current server time
   * Server
   *
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> time() throws RedisException {
    if (version < TIME_VERSION) throw new RedisException("Server does not support TIME");
    return (CompletableFuture<MultiBulkReply>) pipeline(TIME, new Command(TIME_BYTES));
  }

  /**
   * Remove and get the first element in a list, or block until one is available
   * List
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> blpop(Object... key0) throws RedisException {
    if (version < BLPOP_VERSION) throw new RedisException("Server does not support BLPOP");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (CompletableFuture<MultiBulkReply>) pipeline(BLPOP, new Command(BLPOP_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> blpop_(Object... arguments) throws RedisException {
    if (version < BLPOP_VERSION) throw new RedisException("Server does not support BLPOP");
    return (CompletableFuture<MultiBulkReply>) pipeline(BLPOP, new Command(BLPOP_BYTES, arguments));
  }

  /**
   * Remove and get the last element in a list, or block until one is available
   * List
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> brpop(Object... key0) throws RedisException {
    if (version < BRPOP_VERSION) throw new RedisException("Server does not support BRPOP");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (CompletableFuture<MultiBulkReply>) pipeline(BRPOP, new Command(BRPOP_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> brpop_(Object... arguments) throws RedisException {
    if (version < BRPOP_VERSION) throw new RedisException("Server does not support BRPOP");
    return (CompletableFuture<MultiBulkReply>) pipeline(BRPOP, new Command(BRPOP_BYTES, arguments));
  }

  /**
   * Pop a value from a list, push it to another list and return it; or block until one is available
   * List
   *
   * @param source0
   * @param destination1
   * @param timeout2
   * @return Reply
   */
  public CompletableFuture<Reply> brpoplpush(Object source0, Object destination1, Object timeout2) throws RedisException {
    if (version < BRPOPLPUSH_VERSION) throw new RedisException("Server does not support BRPOPLPUSH");
    return (CompletableFuture<Reply>) pipeline(BRPOPLPUSH, new Command(BRPOPLPUSH_BYTES, source0, destination1, timeout2));
  }

  /**
   * Get an element from a list by its index
   * List
   *
   * @param key0
   * @param index1
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> lindex(Object key0, Object index1) throws RedisException {
    if (version < LINDEX_VERSION) throw new RedisException("Server does not support LINDEX");
    return (CompletableFuture<BulkReply>) pipeline(LINDEX, new Command(LINDEX_BYTES, key0, index1));
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
  public CompletableFuture<IntegerReply> linsert(Object key0, Object where1, Object pivot2, Object value3) throws RedisException {
    if (version < LINSERT_VERSION) throw new RedisException("Server does not support LINSERT");
    List list = new ArrayList();
    list.add(key0);
    list.add(where1);
    list.add(pivot2);
    list.add(value3);
    return (CompletableFuture<IntegerReply>) pipeline(LINSERT, new Command(LINSERT_BYTES, list.toArray(new Object[list.size()])));
  }

  /**
   * Get the length of a list
   * List
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> llen(Object key0) throws RedisException {
    if (version < LLEN_VERSION) throw new RedisException("Server does not support LLEN");
    return (CompletableFuture<IntegerReply>) pipeline(LLEN, new Command(LLEN_BYTES, key0));
  }

  /**
   * Remove and get the first element in a list
   * List
   *
   * @param key0
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> lpop(Object key0) throws RedisException {
    if (version < LPOP_VERSION) throw new RedisException("Server does not support LPOP");
    return (CompletableFuture<BulkReply>) pipeline(LPOP, new Command(LPOP_BYTES, key0));
  }

  /**
   * Prepend one or multiple values to a list
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> lpush(Object key0, Object... value1) throws RedisException {
    if (version < LPUSH_VERSION) throw new RedisException("Server does not support LPUSH");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, value1);
    return (CompletableFuture<IntegerReply>) pipeline(LPUSH, new Command(LPUSH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> lpush_(Object... arguments) throws RedisException {
    if (version < LPUSH_VERSION) throw new RedisException("Server does not support LPUSH");
    return (CompletableFuture<IntegerReply>) pipeline(LPUSH, new Command(LPUSH_BYTES, arguments));
  }

  /**
   * Prepend a value to a list, only if the list exists
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> lpushx(Object key0, Object value1) throws RedisException {
    if (version < LPUSHX_VERSION) throw new RedisException("Server does not support LPUSHX");
    return (CompletableFuture<IntegerReply>) pipeline(LPUSHX, new Command(LPUSHX_BYTES, key0, value1));
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
  public CompletableFuture<MultiBulkReply> lrange(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < LRANGE_VERSION) throw new RedisException("Server does not support LRANGE");
    return (CompletableFuture<MultiBulkReply>) pipeline(LRANGE, new Command(LRANGE_BYTES, key0, start1, stop2));
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
  public CompletableFuture<IntegerReply> lrem(Object key0, Object count1, Object value2) throws RedisException {
    if (version < LREM_VERSION) throw new RedisException("Server does not support LREM");
    return (CompletableFuture<IntegerReply>) pipeline(LREM, new Command(LREM_BYTES, key0, count1, value2));
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
  public CompletableFuture<StatusReply> lset(Object key0, Object index1, Object value2) throws RedisException {
    if (version < LSET_VERSION) throw new RedisException("Server does not support LSET");
    return (CompletableFuture<StatusReply>) pipeline(LSET, new Command(LSET_BYTES, key0, index1, value2));
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
  public CompletableFuture<StatusReply> ltrim(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < LTRIM_VERSION) throw new RedisException("Server does not support LTRIM");
    return (CompletableFuture<StatusReply>) pipeline(LTRIM, new Command(LTRIM_BYTES, key0, start1, stop2));
  }

  /**
   * Remove and get the last element in a list
   * List
   *
   * @param key0
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> rpop(Object key0) throws RedisException {
    if (version < RPOP_VERSION) throw new RedisException("Server does not support RPOP");
    return (CompletableFuture<BulkReply>) pipeline(RPOP, new Command(RPOP_BYTES, key0));
  }

  /**
   * Remove the last element in a list, append it to another list and return it
   * List
   *
   * @param source0
   * @param destination1
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> rpoplpush(Object source0, Object destination1) throws RedisException {
    if (version < RPOPLPUSH_VERSION) throw new RedisException("Server does not support RPOPLPUSH");
    return (CompletableFuture<BulkReply>) pipeline(RPOPLPUSH, new Command(RPOPLPUSH_BYTES, source0, destination1));
  }

  /**
   * Append one or multiple values to a list
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> rpush(Object key0, Object... value1) throws RedisException {
    if (version < RPUSH_VERSION) throw new RedisException("Server does not support RPUSH");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, value1);
    return (CompletableFuture<IntegerReply>) pipeline(RPUSH, new Command(RPUSH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> rpush_(Object... arguments) throws RedisException {
    if (version < RPUSH_VERSION) throw new RedisException("Server does not support RPUSH");
    return (CompletableFuture<IntegerReply>) pipeline(RPUSH, new Command(RPUSH_BYTES, arguments));
  }

  /**
   * Append a value to a list, only if the list exists
   * List
   *
   * @param key0
   * @param value1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> rpushx(Object key0, Object value1) throws RedisException {
    if (version < RPUSHX_VERSION) throw new RedisException("Server does not support RPUSHX");
    return (CompletableFuture<IntegerReply>) pipeline(RPUSHX, new Command(RPUSHX_BYTES, key0, value1));
  }

  /**
   * Delete a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> del(Object... key0) throws RedisException {
    if (version < DEL_VERSION) throw new RedisException("Server does not support DEL");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (CompletableFuture<IntegerReply>) pipeline(DEL, new Command(DEL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> del_(Object... arguments) throws RedisException {
    if (version < DEL_VERSION) throw new RedisException("Server does not support DEL");
    return (CompletableFuture<IntegerReply>) pipeline(DEL, new Command(DEL_BYTES, arguments));
  }

  /**
   * Return a serialized version of the value stored at the specified key.
   * Generic
   *
   * @param key0
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> dump(Object key0) throws RedisException {
    if (version < DUMP_VERSION) throw new RedisException("Server does not support DUMP");
    return (CompletableFuture<BulkReply>) pipeline(DUMP, new Command(DUMP_BYTES, key0));
  }

  /**
   * Determine if a key exists
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> exists(Object key0) throws RedisException {
    if (version < EXISTS_VERSION) throw new RedisException("Server does not support EXISTS");
    return (CompletableFuture<IntegerReply>) pipeline(EXISTS, new Command(EXISTS_BYTES, key0));
  }

  /**
   * Set a key's time to live in seconds
   * Generic
   *
   * @param key0
   * @param seconds1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> expire(Object key0, Object seconds1) throws RedisException {
    if (version < EXPIRE_VERSION) throw new RedisException("Server does not support EXPIRE");
    return (CompletableFuture<IntegerReply>) pipeline(EXPIRE, new Command(EXPIRE_BYTES, key0, seconds1));
  }

  /**
   * Set the expiration for a key as a UNIX timestamp
   * Generic
   *
   * @param key0
   * @param timestamp1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> expireat(Object key0, Object timestamp1) throws RedisException {
    if (version < EXPIREAT_VERSION) throw new RedisException("Server does not support EXPIREAT");
    return (CompletableFuture<IntegerReply>) pipeline(EXPIREAT, new Command(EXPIREAT_BYTES, key0, timestamp1));
  }

  /**
   * Find all keys matching the given pattern
   * Generic
   *
   * @param pattern0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> keys(Object pattern0) throws RedisException {
    if (version < KEYS_VERSION) throw new RedisException("Server does not support KEYS");
    return (CompletableFuture<MultiBulkReply>) pipeline(KEYS, new Command(KEYS_BYTES, pattern0));
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
  public CompletableFuture<StatusReply> migrate(Object host0, Object port1, Object key2, Object destination_db3, Object timeout4) throws RedisException {
    if (version < MIGRATE_VERSION) throw new RedisException("Server does not support MIGRATE");
    List list = new ArrayList();
    list.add(host0);
    list.add(port1);
    list.add(key2);
    list.add(destination_db3);
    list.add(timeout4);
    return (CompletableFuture<StatusReply>) pipeline(MIGRATE, new Command(MIGRATE_BYTES, list.toArray(new Object[list.size()])));
  }

  /**
   * Move a key to another database
   * Generic
   *
   * @param key0
   * @param db1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> move(Object key0, Object db1) throws RedisException {
    if (version < MOVE_VERSION) throw new RedisException("Server does not support MOVE");
    return (CompletableFuture<IntegerReply>) pipeline(MOVE, new Command(MOVE_BYTES, key0, db1));
  }

  /**
   * Inspect the internals of Redis objects
   * Generic
   *
   * @param subcommand0
   * @param arguments1
   * @return Reply
   */
  public CompletableFuture<Reply> object(Object subcommand0, Object... arguments1) throws RedisException {
    if (version < OBJECT_VERSION) throw new RedisException("Server does not support OBJECT");
    List list = new ArrayList();
    list.add(subcommand0);
    Collections.addAll(list, arguments1);
    return (CompletableFuture<Reply>) pipeline(OBJECT, new Command(OBJECT_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<Reply> object_(Object... arguments) throws RedisException {
    if (version < OBJECT_VERSION) throw new RedisException("Server does not support OBJECT");
    return (CompletableFuture<Reply>) pipeline(OBJECT, new Command(OBJECT_BYTES, arguments));
  }

  /**
   * Remove the expiration from a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> persist(Object key0) throws RedisException {
    if (version < PERSIST_VERSION) throw new RedisException("Server does not support PERSIST");
    return (CompletableFuture<IntegerReply>) pipeline(PERSIST, new Command(PERSIST_BYTES, key0));
  }

  /**
   * Set a key's time to live in milliseconds
   * Generic
   *
   * @param key0
   * @param milliseconds1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> pexpire(Object key0, Object milliseconds1) throws RedisException {
    if (version < PEXPIRE_VERSION) throw new RedisException("Server does not support PEXPIRE");
    return (CompletableFuture<IntegerReply>) pipeline(PEXPIRE, new Command(PEXPIRE_BYTES, key0, milliseconds1));
  }

  /**
   * Set the expiration for a key as a UNIX timestamp specified in milliseconds
   * Generic
   *
   * @param key0
   * @param milliseconds_timestamp1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> pexpireat(Object key0, Object milliseconds_timestamp1) throws RedisException {
    if (version < PEXPIREAT_VERSION) throw new RedisException("Server does not support PEXPIREAT");
    return (CompletableFuture<IntegerReply>) pipeline(PEXPIREAT, new Command(PEXPIREAT_BYTES, key0, milliseconds_timestamp1));
  }

  /**
   * Get the time to live for a key in milliseconds
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> pttl(Object key0) throws RedisException {
    if (version < PTTL_VERSION) throw new RedisException("Server does not support PTTL");
    return (CompletableFuture<IntegerReply>) pipeline(PTTL, new Command(PTTL_BYTES, key0));
  }

  /**
   * Return a random key from the keyspace
   * Generic
   *
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> randomkey() throws RedisException {
    if (version < RANDOMKEY_VERSION) throw new RedisException("Server does not support RANDOMKEY");
    return (CompletableFuture<BulkReply>) pipeline(RANDOMKEY, new Command(RANDOMKEY_BYTES));
  }

  /**
   * Rename a key
   * Generic
   *
   * @param key0
   * @param newkey1
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> rename(Object key0, Object newkey1) throws RedisException {
    if (version < RENAME_VERSION) throw new RedisException("Server does not support RENAME");
    return (CompletableFuture<StatusReply>) pipeline(RENAME, new Command(RENAME_BYTES, key0, newkey1));
  }

  /**
   * Rename a key, only if the new key does not exist
   * Generic
   *
   * @param key0
   * @param newkey1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> renamenx(Object key0, Object newkey1) throws RedisException {
    if (version < RENAMENX_VERSION) throw new RedisException("Server does not support RENAMENX");
    return (CompletableFuture<IntegerReply>) pipeline(RENAMENX, new Command(RENAMENX_BYTES, key0, newkey1));
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
  public CompletableFuture<StatusReply> restore(Object key0, Object ttl1, Object serialized_value2) throws RedisException {
    if (version < RESTORE_VERSION) throw new RedisException("Server does not support RESTORE");
    return (CompletableFuture<StatusReply>) pipeline(RESTORE, new Command(RESTORE_BYTES, key0, ttl1, serialized_value2));
  }

  /**
   * Sort the elements in a list, set or sorted set
   * Generic
   *
   * @param key0
   * @param pattern1
   * @return Reply
   */
  public CompletableFuture<Reply> sort(Object key0, Object... pattern1) throws RedisException {
    if (version < SORT_VERSION) throw new RedisException("Server does not support SORT");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, pattern1);
    return (CompletableFuture<Reply>) pipeline(SORT, new Command(SORT_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<Reply> sort_(Object... arguments) throws RedisException {
    if (version < SORT_VERSION) throw new RedisException("Server does not support SORT");
    return (CompletableFuture<Reply>) pipeline(SORT, new Command(SORT_BYTES, arguments));
  }

  /**
   * Get the time to live for a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> ttl(Object key0) throws RedisException {
    if (version < TTL_VERSION) throw new RedisException("Server does not support TTL");
    return (CompletableFuture<IntegerReply>) pipeline(TTL, new Command(TTL_BYTES, key0));
  }

  /**
   * Determine the type stored at key
   * Generic
   *
   * @param key0
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> type(Object key0) throws RedisException {
    if (version < TYPE_VERSION) throw new RedisException("Server does not support TYPE");
    return (CompletableFuture<StatusReply>) pipeline(TYPE, new Command(TYPE_BYTES, key0));
  }

  /**
   * Forget about all watched keys
   * Transactions
   *
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> unwatch() throws RedisException {
    if (version < UNWATCH_VERSION) throw new RedisException("Server does not support UNWATCH");
    return (CompletableFuture<StatusReply>) pipeline(UNWATCH, new Command(UNWATCH_BYTES));
  }

  /**
   * Watch the given keys to determine execution of the MULTI/EXEC block
   * Transactions
   *
   * @param key0
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> watch(Object... key0) throws RedisException {
    if (version < WATCH_VERSION) throw new RedisException("Server does not support WATCH");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (CompletableFuture<StatusReply>) pipeline(WATCH, new Command(WATCH_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<StatusReply> watch_(Object... arguments) throws RedisException {
    if (version < WATCH_VERSION) throw new RedisException("Server does not support WATCH");
    return (CompletableFuture<StatusReply>) pipeline(WATCH, new Command(WATCH_BYTES, arguments));
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
  public CompletableFuture<Reply> eval(Object script0, Object numkeys1, Object... key2) throws RedisException {
    if (version < EVAL_VERSION) throw new RedisException("Server does not support EVAL");
    List list = new ArrayList();
    list.add(script0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return (CompletableFuture<Reply>) pipeline(EVAL, new Command(EVAL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<Reply> eval_(Object... arguments) throws RedisException {
    if (version < EVAL_VERSION) throw new RedisException("Server does not support EVAL");
    return (CompletableFuture<Reply>) pipeline(EVAL, new Command(EVAL_BYTES, arguments));
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
  public CompletableFuture<Reply> evalsha(Object sha10, Object numkeys1, Object... key2) throws RedisException {
    if (version < EVALSHA_VERSION) throw new RedisException("Server does not support EVALSHA");
    List list = new ArrayList();
    list.add(sha10);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return (CompletableFuture<Reply>) pipeline(EVALSHA, new Command(EVALSHA_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<Reply> evalsha_(Object... arguments) throws RedisException {
    if (version < EVALSHA_VERSION) throw new RedisException("Server does not support EVALSHA");
    return (CompletableFuture<Reply>) pipeline(EVALSHA, new Command(EVALSHA_BYTES, arguments));
  }

  /**
   * Check existence of scripts in the script cache.
   * Scripting
   *
   * @param script0
   * @return Reply
   */
  public CompletableFuture<Reply> script_exists(Object... script0) throws RedisException {
    if (version < SCRIPT_EXISTS_VERSION) throw new RedisException("Server does not support SCRIPT_EXISTS");
    List list = new ArrayList();
    Collections.addAll(list, script0);
    return (CompletableFuture<Reply>) pipeline(SCRIPT_EXISTS, new Command(SCRIPT_EXISTS_BYTES, SCRIPT_EXISTS2_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<Reply> script_exists_(Object... arguments) throws RedisException {
    if (version < SCRIPT_EXISTS_VERSION) throw new RedisException("Server does not support SCRIPT_EXISTS");
    return (CompletableFuture<Reply>) pipeline(SCRIPT_EXISTS, new Command(SCRIPT_EXISTS_BYTES, SCRIPT_EXISTS2_BYTES, arguments));
  }

  /**
   * Remove all the scripts from the script cache.
   * Scripting
   *
   * @return Reply
   */
  public CompletableFuture<Reply> script_flush() throws RedisException {
    if (version < SCRIPT_FLUSH_VERSION) throw new RedisException("Server does not support SCRIPT_FLUSH");
    return (CompletableFuture<Reply>) pipeline(SCRIPT_FLUSH, new Command(SCRIPT_FLUSH_BYTES, SCRIPT_FLUSH2_BYTES));
  }

  /**
   * Kill the script currently in execution.
   * Scripting
   *
   * @return Reply
   */
  public CompletableFuture<Reply> script_kill() throws RedisException {
    if (version < SCRIPT_KILL_VERSION) throw new RedisException("Server does not support SCRIPT_KILL");
    return (CompletableFuture<Reply>) pipeline(SCRIPT_KILL, new Command(SCRIPT_KILL_BYTES, SCRIPT_KILL2_BYTES));
  }

  /**
   * Load the specified Lua script into the script cache.
   * Scripting
   *
   * @param script0
   * @return Reply
   */
  public CompletableFuture<Reply> script_load(Object script0) throws RedisException {
    if (version < SCRIPT_LOAD_VERSION) throw new RedisException("Server does not support SCRIPT_LOAD");
    return (CompletableFuture<Reply>) pipeline(SCRIPT_LOAD, new Command(SCRIPT_LOAD_BYTES, SCRIPT_LOAD2_BYTES, script0));
  }

  /**
   * Delete one or more hash fields
   * Hash
   *
   * @param key0
   * @param field1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> hdel(Object key0, Object... field1) throws RedisException {
    if (version < HDEL_VERSION) throw new RedisException("Server does not support HDEL");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, field1);
    return (CompletableFuture<IntegerReply>) pipeline(HDEL, new Command(HDEL_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> hdel_(Object... arguments) throws RedisException {
    if (version < HDEL_VERSION) throw new RedisException("Server does not support HDEL");
    return (CompletableFuture<IntegerReply>) pipeline(HDEL, new Command(HDEL_BYTES, arguments));
  }

  /**
   * Determine if a hash field exists
   * Hash
   *
   * @param key0
   * @param field1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> hexists(Object key0, Object field1) throws RedisException {
    if (version < HEXISTS_VERSION) throw new RedisException("Server does not support HEXISTS");
    return (CompletableFuture<IntegerReply>) pipeline(HEXISTS, new Command(HEXISTS_BYTES, key0, field1));
  }

  /**
   * Get the value of a hash field
   * Hash
   *
   * @param key0
   * @param field1
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> hget(Object key0, Object field1) throws RedisException {
    if (version < HGET_VERSION) throw new RedisException("Server does not support HGET");
    return (CompletableFuture<BulkReply>) pipeline(HGET, new Command(HGET_BYTES, key0, field1));
  }

  /**
   * Get all the fields and values in a hash
   * Hash
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> hgetall(Object key0) throws RedisException {
    if (version < HGETALL_VERSION) throw new RedisException("Server does not support HGETALL");
    return (CompletableFuture<MultiBulkReply>) pipeline(HGETALL, new Command(HGETALL_BYTES, key0));
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
  public CompletableFuture<IntegerReply> hincrby(Object key0, Object field1, Object increment2) throws RedisException {
    if (version < HINCRBY_VERSION) throw new RedisException("Server does not support HINCRBY");
    return (CompletableFuture<IntegerReply>) pipeline(HINCRBY, new Command(HINCRBY_BYTES, key0, field1, increment2));
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
  public CompletableFuture<BulkReply> hincrbyfloat(Object key0, Object field1, Object increment2) throws RedisException {
    if (version < HINCRBYFLOAT_VERSION) throw new RedisException("Server does not support HINCRBYFLOAT");
    return (CompletableFuture<BulkReply>) pipeline(HINCRBYFLOAT, new Command(HINCRBYFLOAT_BYTES, key0, field1, increment2));
  }

  /**
   * Get all the fields in a hash
   * Hash
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> hkeys(Object key0) throws RedisException {
    if (version < HKEYS_VERSION) throw new RedisException("Server does not support HKEYS");
    return (CompletableFuture<MultiBulkReply>) pipeline(HKEYS, new Command(HKEYS_BYTES, key0));
  }

  /**
   * Get the number of fields in a hash
   * Hash
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> hlen(Object key0) throws RedisException {
    if (version < HLEN_VERSION) throw new RedisException("Server does not support HLEN");
    return (CompletableFuture<IntegerReply>) pipeline(HLEN, new Command(HLEN_BYTES, key0));
  }

  /**
   * Get the values of all the given hash fields
   * Hash
   *
   * @param key0
   * @param field1
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> hmget(Object key0, Object... field1) throws RedisException {
    if (version < HMGET_VERSION) throw new RedisException("Server does not support HMGET");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, field1);
    return (CompletableFuture<MultiBulkReply>) pipeline(HMGET, new Command(HMGET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> hmget_(Object... arguments) throws RedisException {
    if (version < HMGET_VERSION) throw new RedisException("Server does not support HMGET");
    return (CompletableFuture<MultiBulkReply>) pipeline(HMGET, new Command(HMGET_BYTES, arguments));
  }

  /**
   * Set multiple hash fields to multiple values
   * Hash
   *
   * @param key0
   * @param field_or_value1
   * @return StatusReply
   */
  public CompletableFuture<StatusReply> hmset(Object key0, Object... field_or_value1) throws RedisException {
    if (version < HMSET_VERSION) throw new RedisException("Server does not support HMSET");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, field_or_value1);
    return (CompletableFuture<StatusReply>) pipeline(HMSET, new Command(HMSET_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<StatusReply> hmset_(Object... arguments) throws RedisException {
    if (version < HMSET_VERSION) throw new RedisException("Server does not support HMSET");
    return (CompletableFuture<StatusReply>) pipeline(HMSET, new Command(HMSET_BYTES, arguments));
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
  public CompletableFuture<IntegerReply> hset(Object key0, Object field1, Object value2) throws RedisException {
    if (version < HSET_VERSION) throw new RedisException("Server does not support HSET");
    return (CompletableFuture<IntegerReply>) pipeline(HSET, new Command(HSET_BYTES, key0, field1, value2));
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
  public CompletableFuture<IntegerReply> hsetnx(Object key0, Object field1, Object value2) throws RedisException {
    if (version < HSETNX_VERSION) throw new RedisException("Server does not support HSETNX");
    return (CompletableFuture<IntegerReply>) pipeline(HSETNX, new Command(HSETNX_BYTES, key0, field1, value2));
  }

  /**
   * Get all the values in a hash
   * Hash
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> hvals(Object key0) throws RedisException {
    if (version < HVALS_VERSION) throw new RedisException("Server does not support HVALS");
    return (CompletableFuture<MultiBulkReply>) pipeline(HVALS, new Command(HVALS_BYTES, key0));
  }

  /**
   * Post a message to a channel
   * Pubsub
   *
   * @param channel0
   * @param message1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> publish(Object channel0, Object message1) throws RedisException {
    if (version < PUBLISH_VERSION) throw new RedisException("Server does not support PUBLISH");
    return (CompletableFuture<IntegerReply>) pipeline(PUBLISH, new Command(PUBLISH_BYTES, channel0, message1));
  }

  /**
   * Add one or more members to a set
   * Set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> sadd(Object key0, Object... member1) throws RedisException {
    if (version < SADD_VERSION) throw new RedisException("Server does not support SADD");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, member1);
    return (CompletableFuture<IntegerReply>) pipeline(SADD, new Command(SADD_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> sadd_(Object... arguments) throws RedisException {
    if (version < SADD_VERSION) throw new RedisException("Server does not support SADD");
    return (CompletableFuture<IntegerReply>) pipeline(SADD, new Command(SADD_BYTES, arguments));
  }

  /**
   * Get the number of members in a set
   * Set
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> scard(Object key0) throws RedisException {
    if (version < SCARD_VERSION) throw new RedisException("Server does not support SCARD");
    return (CompletableFuture<IntegerReply>) pipeline(SCARD, new Command(SCARD_BYTES, key0));
  }

  /**
   * Subtract multiple sets
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> sdiff(Object... key0) throws RedisException {
    if (version < SDIFF_VERSION) throw new RedisException("Server does not support SDIFF");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (CompletableFuture<MultiBulkReply>) pipeline(SDIFF, new Command(SDIFF_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> sdiff_(Object... arguments) throws RedisException {
    if (version < SDIFF_VERSION) throw new RedisException("Server does not support SDIFF");
    return (CompletableFuture<MultiBulkReply>) pipeline(SDIFF, new Command(SDIFF_BYTES, arguments));
  }

  /**
   * Subtract multiple sets and store the resulting set in a key
   * Set
   *
   * @param destination0
   * @param key1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> sdiffstore(Object destination0, Object... key1) throws RedisException {
    if (version < SDIFFSTORE_VERSION) throw new RedisException("Server does not support SDIFFSTORE");
    List list = new ArrayList();
    list.add(destination0);
    Collections.addAll(list, key1);
    return (CompletableFuture<IntegerReply>) pipeline(SDIFFSTORE, new Command(SDIFFSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> sdiffstore_(Object... arguments) throws RedisException {
    if (version < SDIFFSTORE_VERSION) throw new RedisException("Server does not support SDIFFSTORE");
    return (CompletableFuture<IntegerReply>) pipeline(SDIFFSTORE, new Command(SDIFFSTORE_BYTES, arguments));
  }

  /**
   * Intersect multiple sets
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> sinter(Object... key0) throws RedisException {
    if (version < SINTER_VERSION) throw new RedisException("Server does not support SINTER");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (CompletableFuture<MultiBulkReply>) pipeline(SINTER, new Command(SINTER_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> sinter_(Object... arguments) throws RedisException {
    if (version < SINTER_VERSION) throw new RedisException("Server does not support SINTER");
    return (CompletableFuture<MultiBulkReply>) pipeline(SINTER, new Command(SINTER_BYTES, arguments));
  }

  /**
   * Intersect multiple sets and store the resulting set in a key
   * Set
   *
   * @param destination0
   * @param key1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> sinterstore(Object destination0, Object... key1) throws RedisException {
    if (version < SINTERSTORE_VERSION) throw new RedisException("Server does not support SINTERSTORE");
    List list = new ArrayList();
    list.add(destination0);
    Collections.addAll(list, key1);
    return (CompletableFuture<IntegerReply>) pipeline(SINTERSTORE, new Command(SINTERSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> sinterstore_(Object... arguments) throws RedisException {
    if (version < SINTERSTORE_VERSION) throw new RedisException("Server does not support SINTERSTORE");
    return (CompletableFuture<IntegerReply>) pipeline(SINTERSTORE, new Command(SINTERSTORE_BYTES, arguments));
  }

  /**
   * Determine if a given value is a member of a set
   * Set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> sismember(Object key0, Object member1) throws RedisException {
    if (version < SISMEMBER_VERSION) throw new RedisException("Server does not support SISMEMBER");
    return (CompletableFuture<IntegerReply>) pipeline(SISMEMBER, new Command(SISMEMBER_BYTES, key0, member1));
  }

  /**
   * Get all the members in a set
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> smembers(Object key0) throws RedisException {
    if (version < SMEMBERS_VERSION) throw new RedisException("Server does not support SMEMBERS");
    return (CompletableFuture<MultiBulkReply>) pipeline(SMEMBERS, new Command(SMEMBERS_BYTES, key0));
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
  public CompletableFuture<IntegerReply> smove(Object source0, Object destination1, Object member2) throws RedisException {
    if (version < SMOVE_VERSION) throw new RedisException("Server does not support SMOVE");
    return (CompletableFuture<IntegerReply>) pipeline(SMOVE, new Command(SMOVE_BYTES, source0, destination1, member2));
  }

  /**
   * Remove and return a random member from a set
   * Set
   *
   * @param key0
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> spop(Object key0) throws RedisException {
    if (version < SPOP_VERSION) throw new RedisException("Server does not support SPOP");
    return (CompletableFuture<BulkReply>) pipeline(SPOP, new Command(SPOP_BYTES, key0));
  }

  /**
   * Get one or multiple random members from a set
   * Set
   *
   * @param key0
   * @param count1
   * @return Reply
   */
  public CompletableFuture<Reply> srandmember(Object key0, Object count1) throws RedisException {
    if (version < SRANDMEMBER_VERSION) throw new RedisException("Server does not support SRANDMEMBER");
    return (CompletableFuture<Reply>) pipeline(SRANDMEMBER, new Command(SRANDMEMBER_BYTES, key0, count1));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<Reply> srandmember_(Object... arguments) throws RedisException {
    if (version < SRANDMEMBER_VERSION) throw new RedisException("Server does not support SRANDMEMBER");
    return (CompletableFuture<Reply>) pipeline(SRANDMEMBER, new Command(SRANDMEMBER_BYTES, arguments));
  }

  /**
   * Remove one or more members from a set
   * Set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> srem(Object key0, Object... member1) throws RedisException {
    if (version < SREM_VERSION) throw new RedisException("Server does not support SREM");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, member1);
    return (CompletableFuture<IntegerReply>) pipeline(SREM, new Command(SREM_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> srem_(Object... arguments) throws RedisException {
    if (version < SREM_VERSION) throw new RedisException("Server does not support SREM");
    return (CompletableFuture<IntegerReply>) pipeline(SREM, new Command(SREM_BYTES, arguments));
  }

  /**
   * Add multiple sets
   * Set
   *
   * @param key0
   * @return MultiBulkReply
   */
  public CompletableFuture<MultiBulkReply> sunion(Object... key0) throws RedisException {
    if (version < SUNION_VERSION) throw new RedisException("Server does not support SUNION");
    List list = new ArrayList();
    Collections.addAll(list, key0);
    return (CompletableFuture<MultiBulkReply>) pipeline(SUNION, new Command(SUNION_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> sunion_(Object... arguments) throws RedisException {
    if (version < SUNION_VERSION) throw new RedisException("Server does not support SUNION");
    return (CompletableFuture<MultiBulkReply>) pipeline(SUNION, new Command(SUNION_BYTES, arguments));
  }

  /**
   * Add multiple sets and store the resulting set in a key
   * Set
   *
   * @param destination0
   * @param key1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> sunionstore(Object destination0, Object... key1) throws RedisException {
    if (version < SUNIONSTORE_VERSION) throw new RedisException("Server does not support SUNIONSTORE");
    List list = new ArrayList();
    list.add(destination0);
    Collections.addAll(list, key1);
    return (CompletableFuture<IntegerReply>) pipeline(SUNIONSTORE, new Command(SUNIONSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> sunionstore_(Object... arguments) throws RedisException {
    if (version < SUNIONSTORE_VERSION) throw new RedisException("Server does not support SUNIONSTORE");
    return (CompletableFuture<IntegerReply>) pipeline(SUNIONSTORE, new Command(SUNIONSTORE_BYTES, arguments));
  }

  /**
   * Add one or more members to a sorted set, or update its score if it already exists
   * Sorted_set
   *
   * @param args
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> zadd(Object... args) throws RedisException {
    if (version < ZADD_VERSION) throw new RedisException("Server does not support ZADD");
    return (CompletableFuture<IntegerReply>) pipeline(ZADD, new Command(ZADD_BYTES, args));
  }

  /**
   * Get the number of members in a sorted set
   * Sorted_set
   *
   * @param key0
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> zcard(Object key0) throws RedisException {
    if (version < ZCARD_VERSION) throw new RedisException("Server does not support ZCARD");
    return (CompletableFuture<IntegerReply>) pipeline(ZCARD, new Command(ZCARD_BYTES, key0));
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
  public CompletableFuture<IntegerReply> zcount(Object key0, Object min1, Object max2) throws RedisException {
    if (version < ZCOUNT_VERSION) throw new RedisException("Server does not support ZCOUNT");
    return (CompletableFuture<IntegerReply>) pipeline(ZCOUNT, new Command(ZCOUNT_BYTES, key0, min1, max2));
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
  public CompletableFuture<BulkReply> zincrby(Object key0, Object increment1, Object member2) throws RedisException {
    if (version < ZINCRBY_VERSION) throw new RedisException("Server does not support ZINCRBY");
    return (CompletableFuture<BulkReply>) pipeline(ZINCRBY, new Command(ZINCRBY_BYTES, key0, increment1, member2));
  }

  /**
   * Intersect multiple sorted sets and store the resulting sorted set in a new key
   * Sorted_set
   *
   * @param destination0
   * @param numkeys1
   * @param key2
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> zinterstore(Object destination0, Object numkeys1, Object... key2) throws RedisException {
    if (version < ZINTERSTORE_VERSION) throw new RedisException("Server does not support ZINTERSTORE");
    List list = new ArrayList();
    list.add(destination0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return (CompletableFuture<IntegerReply>) pipeline(ZINTERSTORE, new Command(ZINTERSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> zinterstore_(Object... arguments) throws RedisException {
    if (version < ZINTERSTORE_VERSION) throw new RedisException("Server does not support ZINTERSTORE");
    return (CompletableFuture<IntegerReply>) pipeline(ZINTERSTORE, new Command(ZINTERSTORE_BYTES, arguments));
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
  public CompletableFuture<MultiBulkReply> zrange(Object key0, Object start1, Object stop2, Object withscores3) throws RedisException {
    if (version < ZRANGE_VERSION) throw new RedisException("Server does not support ZRANGE");
    List list = new ArrayList();
    list.add(key0);
    list.add(start1);
    list.add(stop2);
    if (withscores3 != null) list.add(withscores3);
    return (CompletableFuture<MultiBulkReply>) pipeline(ZRANGE, new Command(ZRANGE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> zrange_(Object... arguments) throws RedisException {
    if (version < ZRANGE_VERSION) throw new RedisException("Server does not support ZRANGE");
    return (CompletableFuture<MultiBulkReply>) pipeline(ZRANGE, new Command(ZRANGE_BYTES, arguments));
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
  public CompletableFuture<MultiBulkReply> zrangebyscore(Object key0, Object min1, Object max2, Object withscores3, Object... offset_or_count4) throws RedisException {
    if (version < ZRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZRANGEBYSCORE");
    List list = new ArrayList();
    list.add(key0);
    list.add(min1);
    list.add(max2);
    if (withscores3 != null) list.add(withscores3);
    Collections.addAll(list, offset_or_count4);
    return (CompletableFuture<MultiBulkReply>) pipeline(ZRANGEBYSCORE, new Command(ZRANGEBYSCORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> zrangebyscore_(Object... arguments) throws RedisException {
    if (version < ZRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZRANGEBYSCORE");
    return (CompletableFuture<MultiBulkReply>) pipeline(ZRANGEBYSCORE, new Command(ZRANGEBYSCORE_BYTES, arguments));
  }

  /**
   * Determine the index of a member in a sorted set
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return Reply
   */
  public CompletableFuture<Reply> zrank(Object key0, Object member1) throws RedisException {
    if (version < ZRANK_VERSION) throw new RedisException("Server does not support ZRANK");
    return (CompletableFuture<Reply>) pipeline(ZRANK, new Command(ZRANK_BYTES, key0, member1));
  }

  /**
   * Remove one or more members from a sorted set
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return IntegerReply
   */
  public CompletableFuture<IntegerReply> zrem(Object key0, Object... member1) throws RedisException {
    if (version < ZREM_VERSION) throw new RedisException("Server does not support ZREM");
    List list = new ArrayList();
    list.add(key0);
    Collections.addAll(list, member1);
    return (CompletableFuture<IntegerReply>) pipeline(ZREM, new Command(ZREM_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> zrem_(Object... arguments) throws RedisException {
    if (version < ZREM_VERSION) throw new RedisException("Server does not support ZREM");
    return (CompletableFuture<IntegerReply>) pipeline(ZREM, new Command(ZREM_BYTES, arguments));
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
  public CompletableFuture<IntegerReply> zremrangebyrank(Object key0, Object start1, Object stop2) throws RedisException {
    if (version < ZREMRANGEBYRANK_VERSION) throw new RedisException("Server does not support ZREMRANGEBYRANK");
    return (CompletableFuture<IntegerReply>) pipeline(ZREMRANGEBYRANK, new Command(ZREMRANGEBYRANK_BYTES, key0, start1, stop2));
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
  public CompletableFuture<IntegerReply> zremrangebyscore(Object key0, Object min1, Object max2) throws RedisException {
    if (version < ZREMRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREMRANGEBYSCORE");
    return (CompletableFuture<IntegerReply>) pipeline(ZREMRANGEBYSCORE, new Command(ZREMRANGEBYSCORE_BYTES, key0, min1, max2));
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
  public CompletableFuture<MultiBulkReply> zrevrange(Object key0, Object start1, Object stop2, Object withscores3) throws RedisException {
    if (version < ZREVRANGE_VERSION) throw new RedisException("Server does not support ZREVRANGE");
    List list = new ArrayList();
    list.add(key0);
    list.add(start1);
    list.add(stop2);
    if (withscores3 != null) list.add(withscores3);
    return (CompletableFuture<MultiBulkReply>) pipeline(ZREVRANGE, new Command(ZREVRANGE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> zrevrange_(Object... arguments) throws RedisException {
    if (version < ZREVRANGE_VERSION) throw new RedisException("Server does not support ZREVRANGE");
    return (CompletableFuture<MultiBulkReply>) pipeline(ZREVRANGE, new Command(ZREVRANGE_BYTES, arguments));
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
  public CompletableFuture<MultiBulkReply> zrevrangebyscore(Object key0, Object max1, Object min2, Object withscores3, Object... offset_or_count4) throws RedisException {
    if (version < ZREVRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREVRANGEBYSCORE");
    List list = new ArrayList();
    list.add(key0);
    list.add(max1);
    list.add(min2);
    if (withscores3 != null) list.add(withscores3);
    Collections.addAll(list, offset_or_count4);
    return (CompletableFuture<MultiBulkReply>) pipeline(ZREVRANGEBYSCORE, new Command(ZREVRANGEBYSCORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<MultiBulkReply> zrevrangebyscore_(Object... arguments) throws RedisException {
    if (version < ZREVRANGEBYSCORE_VERSION) throw new RedisException("Server does not support ZREVRANGEBYSCORE");
    return (CompletableFuture<MultiBulkReply>) pipeline(ZREVRANGEBYSCORE, new Command(ZREVRANGEBYSCORE_BYTES, arguments));
  }

  /**
   * Determine the index of a member in a sorted set, with scores ordered from high to low
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return Reply
   */
  public CompletableFuture<Reply> zrevrank(Object key0, Object member1) throws RedisException {
    if (version < ZREVRANK_VERSION) throw new RedisException("Server does not support ZREVRANK");
    return (CompletableFuture<Reply>) pipeline(ZREVRANK, new Command(ZREVRANK_BYTES, key0, member1));
  }

  /**
   * Get the score associated with the given member in a sorted set
   * Sorted_set
   *
   * @param key0
   * @param member1
   * @return BulkReply
   */
  public CompletableFuture<BulkReply> zscore(Object key0, Object member1) throws RedisException {
    if (version < ZSCORE_VERSION) throw new RedisException("Server does not support ZSCORE");
    return (CompletableFuture<BulkReply>) pipeline(ZSCORE, new Command(ZSCORE_BYTES, key0, member1));
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
  public CompletableFuture<IntegerReply> zunionstore(Object destination0, Object numkeys1, Object... key2) throws RedisException {
    if (version < ZUNIONSTORE_VERSION) throw new RedisException("Server does not support ZUNIONSTORE");
    List list = new ArrayList();
    list.add(destination0);
    list.add(numkeys1);
    Collections.addAll(list, key2);
    return (CompletableFuture<IntegerReply>) pipeline(ZUNIONSTORE, new Command(ZUNIONSTORE_BYTES, list.toArray(new Object[list.size()])));
  }

  // Varargs version to simplify commands with optional or multiple arguments
  public CompletableFuture<IntegerReply> zunionstore_(Object... arguments) throws RedisException {
    if (version < ZUNIONSTORE_VERSION) throw new RedisException("Server does not support ZUNIONSTORE");
    return (CompletableFuture<IntegerReply>) pipeline(ZUNIONSTORE, new Command(ZUNIONSTORE_BYTES, arguments));
  }
  }
}
