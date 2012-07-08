package com.twitter.finagle.redis

import com.google.common.base.Charsets

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.util.Future

import org.jboss.netty.buffer.ChannelBuffers

import redis._
import redis.netty._

import scala.collection.mutable.ArrayBuffer

object RedisClient {

  /**
   * Construct a client from a single host.
   * @param host a String of host:port combination.
   */
  def apply(host: String): RedisClient = RedisClient(
    ClientBuilder()
      .hosts(host)
      .hostConnectionLimit(1)
      .codec(new RedisCodecFactory)
      .build())

  /**
   * Construct a client from a single Service.
   */
  def apply(service: Service[Command, Reply[_]]): RedisClient = new RedisClient(service)

  val APPEND = "APPEND"
  val APPEND_BYTES = ChannelBuffers.copiedBuffer(APPEND, Charsets.US_ASCII)

  val AUTH = "AUTH"
  val AUTH_BYTES = ChannelBuffers.copiedBuffer(AUTH, Charsets.US_ASCII)

  val BGREWRITEAOF = "BGREWRITEAOF"
  val BGREWRITEAOF_BYTES = ChannelBuffers.copiedBuffer(BGREWRITEAOF, Charsets.US_ASCII)

  val BGSAVE = "BGSAVE"
  val BGSAVE_BYTES = ChannelBuffers.copiedBuffer(BGSAVE, Charsets.US_ASCII)

  val BITCOUNT = "BITCOUNT"
  val BITCOUNT_BYTES = ChannelBuffers.copiedBuffer(BITCOUNT, Charsets.US_ASCII)

  val BITOP = "BITOP"
  val BITOP_BYTES = ChannelBuffers.copiedBuffer(BITOP, Charsets.US_ASCII)

  val BLPOP = "BLPOP"
  val BLPOP_BYTES = ChannelBuffers.copiedBuffer(BLPOP, Charsets.US_ASCII)

  val BRPOP = "BRPOP"
  val BRPOP_BYTES = ChannelBuffers.copiedBuffer(BRPOP, Charsets.US_ASCII)

  val BRPOPLPUSH = "BRPOPLPUSH"
  val BRPOPLPUSH_BYTES = ChannelBuffers.copiedBuffer(BRPOPLPUSH, Charsets.US_ASCII)

  val CONFIG_GET = "CONFIG_GET"
  val CONFIG_GET_BYTES = ChannelBuffers.copiedBuffer(CONFIG_GET, Charsets.US_ASCII)

  val CONFIG_SET = "CONFIG_SET"
  val CONFIG_SET_BYTES = ChannelBuffers.copiedBuffer(CONFIG_SET, Charsets.US_ASCII)

  val CONFIG_RESETSTAT = "CONFIG_RESETSTAT"
  val CONFIG_RESETSTAT_BYTES = ChannelBuffers.copiedBuffer(CONFIG_RESETSTAT, Charsets.US_ASCII)

  val DBSIZE = "DBSIZE"
  val DBSIZE_BYTES = ChannelBuffers.copiedBuffer(DBSIZE, Charsets.US_ASCII)

  val DEBUG_OBJECT = "DEBUG_OBJECT"
  val DEBUG_OBJECT_BYTES = ChannelBuffers.copiedBuffer(DEBUG_OBJECT, Charsets.US_ASCII)

  val DEBUG_SEGFAULT = "DEBUG_SEGFAULT"
  val DEBUG_SEGFAULT_BYTES = ChannelBuffers.copiedBuffer(DEBUG_SEGFAULT, Charsets.US_ASCII)

  val DECR = "DECR"
  val DECR_BYTES = ChannelBuffers.copiedBuffer(DECR, Charsets.US_ASCII)

  val DECRBY = "DECRBY"
  val DECRBY_BYTES = ChannelBuffers.copiedBuffer(DECRBY, Charsets.US_ASCII)

  val DEL = "DEL"
  val DEL_BYTES = ChannelBuffers.copiedBuffer(DEL, Charsets.US_ASCII)

  val DUMP = "DUMP"
  val DUMP_BYTES = ChannelBuffers.copiedBuffer(DUMP, Charsets.US_ASCII)

  val ECHO = "ECHO"
  val ECHO_BYTES = ChannelBuffers.copiedBuffer(ECHO, Charsets.US_ASCII)

  val EVAL = "EVAL"
  val EVAL_BYTES = ChannelBuffers.copiedBuffer(EVAL, Charsets.US_ASCII)

  val EVALSHA = "EVALSHA"
  val EVALSHA_BYTES = ChannelBuffers.copiedBuffer(EVALSHA, Charsets.US_ASCII)

  val EXISTS = "EXISTS"
  val EXISTS_BYTES = ChannelBuffers.copiedBuffer(EXISTS, Charsets.US_ASCII)

  val EXPIRE = "EXPIRE"
  val EXPIRE_BYTES = ChannelBuffers.copiedBuffer(EXPIRE, Charsets.US_ASCII)

  val EXPIREAT = "EXPIREAT"
  val EXPIREAT_BYTES = ChannelBuffers.copiedBuffer(EXPIREAT, Charsets.US_ASCII)

  val FLUSHALL = "FLUSHALL"
  val FLUSHALL_BYTES = ChannelBuffers.copiedBuffer(FLUSHALL, Charsets.US_ASCII)

  val FLUSHDB = "FLUSHDB"
  val FLUSHDB_BYTES = ChannelBuffers.copiedBuffer(FLUSHDB, Charsets.US_ASCII)

  val GET = "GET"
  val GET_BYTES = ChannelBuffers.copiedBuffer(GET, Charsets.US_ASCII)

  val GETBIT = "GETBIT"
  val GETBIT_BYTES = ChannelBuffers.copiedBuffer(GETBIT, Charsets.US_ASCII)

  val GETRANGE = "GETRANGE"
  val GETRANGE_BYTES = ChannelBuffers.copiedBuffer(GETRANGE, Charsets.US_ASCII)

  val GETSET = "GETSET"
  val GETSET_BYTES = ChannelBuffers.copiedBuffer(GETSET, Charsets.US_ASCII)

  val HDEL = "HDEL"
  val HDEL_BYTES = ChannelBuffers.copiedBuffer(HDEL, Charsets.US_ASCII)

  val HEXISTS = "HEXISTS"
  val HEXISTS_BYTES = ChannelBuffers.copiedBuffer(HEXISTS, Charsets.US_ASCII)

  val HGET = "HGET"
  val HGET_BYTES = ChannelBuffers.copiedBuffer(HGET, Charsets.US_ASCII)

  val HGETALL = "HGETALL"
  val HGETALL_BYTES = ChannelBuffers.copiedBuffer(HGETALL, Charsets.US_ASCII)

  val HINCRBY = "HINCRBY"
  val HINCRBY_BYTES = ChannelBuffers.copiedBuffer(HINCRBY, Charsets.US_ASCII)

  val HINCRBYFLOAT = "HINCRBYFLOAT"
  val HINCRBYFLOAT_BYTES = ChannelBuffers.copiedBuffer(HINCRBYFLOAT, Charsets.US_ASCII)

  val HKEYS = "HKEYS"
  val HKEYS_BYTES = ChannelBuffers.copiedBuffer(HKEYS, Charsets.US_ASCII)

  val HLEN = "HLEN"
  val HLEN_BYTES = ChannelBuffers.copiedBuffer(HLEN, Charsets.US_ASCII)

  val HMGET = "HMGET"
  val HMGET_BYTES = ChannelBuffers.copiedBuffer(HMGET, Charsets.US_ASCII)

  val HMSET = "HMSET"
  val HMSET_BYTES = ChannelBuffers.copiedBuffer(HMSET, Charsets.US_ASCII)

  val HSET = "HSET"
  val HSET_BYTES = ChannelBuffers.copiedBuffer(HSET, Charsets.US_ASCII)

  val HSETNX = "HSETNX"
  val HSETNX_BYTES = ChannelBuffers.copiedBuffer(HSETNX, Charsets.US_ASCII)

  val HVALS = "HVALS"
  val HVALS_BYTES = ChannelBuffers.copiedBuffer(HVALS, Charsets.US_ASCII)

  val INCR = "INCR"
  val INCR_BYTES = ChannelBuffers.copiedBuffer(INCR, Charsets.US_ASCII)

  val INCRBY = "INCRBY"
  val INCRBY_BYTES = ChannelBuffers.copiedBuffer(INCRBY, Charsets.US_ASCII)

  val INCRBYFLOAT = "INCRBYFLOAT"
  val INCRBYFLOAT_BYTES = ChannelBuffers.copiedBuffer(INCRBYFLOAT, Charsets.US_ASCII)

  val INFO = "INFO"
  val INFO_BYTES = ChannelBuffers.copiedBuffer(INFO, Charsets.US_ASCII)

  val KEYS = "KEYS"
  val KEYS_BYTES = ChannelBuffers.copiedBuffer(KEYS, Charsets.US_ASCII)

  val LASTSAVE = "LASTSAVE"
  val LASTSAVE_BYTES = ChannelBuffers.copiedBuffer(LASTSAVE, Charsets.US_ASCII)

  val LINDEX = "LINDEX"
  val LINDEX_BYTES = ChannelBuffers.copiedBuffer(LINDEX, Charsets.US_ASCII)

  val LINSERT = "LINSERT"
  val LINSERT_BYTES = ChannelBuffers.copiedBuffer(LINSERT, Charsets.US_ASCII)

  val LLEN = "LLEN"
  val LLEN_BYTES = ChannelBuffers.copiedBuffer(LLEN, Charsets.US_ASCII)

  val LPOP = "LPOP"
  val LPOP_BYTES = ChannelBuffers.copiedBuffer(LPOP, Charsets.US_ASCII)

  val LPUSH = "LPUSH"
  val LPUSH_BYTES = ChannelBuffers.copiedBuffer(LPUSH, Charsets.US_ASCII)

  val LPUSHX = "LPUSHX"
  val LPUSHX_BYTES = ChannelBuffers.copiedBuffer(LPUSHX, Charsets.US_ASCII)

  val LRANGE = "LRANGE"
  val LRANGE_BYTES = ChannelBuffers.copiedBuffer(LRANGE, Charsets.US_ASCII)

  val LREM = "LREM"
  val LREM_BYTES = ChannelBuffers.copiedBuffer(LREM, Charsets.US_ASCII)

  val LSET = "LSET"
  val LSET_BYTES = ChannelBuffers.copiedBuffer(LSET, Charsets.US_ASCII)

  val LTRIM = "LTRIM"
  val LTRIM_BYTES = ChannelBuffers.copiedBuffer(LTRIM, Charsets.US_ASCII)

  val MGET = "MGET"
  val MGET_BYTES = ChannelBuffers.copiedBuffer(MGET, Charsets.US_ASCII)

  val MIGRATE = "MIGRATE"
  val MIGRATE_BYTES = ChannelBuffers.copiedBuffer(MIGRATE, Charsets.US_ASCII)

  val MONITOR = "MONITOR"
  val MONITOR_BYTES = ChannelBuffers.copiedBuffer(MONITOR, Charsets.US_ASCII)

  val MOVE = "MOVE"
  val MOVE_BYTES = ChannelBuffers.copiedBuffer(MOVE, Charsets.US_ASCII)

  val MSET = "MSET"
  val MSET_BYTES = ChannelBuffers.copiedBuffer(MSET, Charsets.US_ASCII)

  val MSETNX = "MSETNX"
  val MSETNX_BYTES = ChannelBuffers.copiedBuffer(MSETNX, Charsets.US_ASCII)

  val OBJECT = "OBJECT"
  val OBJECT_BYTES = ChannelBuffers.copiedBuffer(OBJECT, Charsets.US_ASCII)

  val PERSIST = "PERSIST"
  val PERSIST_BYTES = ChannelBuffers.copiedBuffer(PERSIST, Charsets.US_ASCII)

  val PEXPIRE = "PEXPIRE"
  val PEXPIRE_BYTES = ChannelBuffers.copiedBuffer(PEXPIRE, Charsets.US_ASCII)

  val PEXPIREAT = "PEXPIREAT"
  val PEXPIREAT_BYTES = ChannelBuffers.copiedBuffer(PEXPIREAT, Charsets.US_ASCII)

  val PING = "PING"
  val PING_BYTES = ChannelBuffers.copiedBuffer(PING, Charsets.US_ASCII)

  val PSETEX = "PSETEX"
  val PSETEX_BYTES = ChannelBuffers.copiedBuffer(PSETEX, Charsets.US_ASCII)

  val PTTL = "PTTL"
  val PTTL_BYTES = ChannelBuffers.copiedBuffer(PTTL, Charsets.US_ASCII)

  val PUBLISH = "PUBLISH"
  val PUBLISH_BYTES = ChannelBuffers.copiedBuffer(PUBLISH, Charsets.US_ASCII)

  val QUIT = "QUIT"
  val QUIT_BYTES = ChannelBuffers.copiedBuffer(QUIT, Charsets.US_ASCII)

  val RANDOMKEY = "RANDOMKEY"
  val RANDOMKEY_BYTES = ChannelBuffers.copiedBuffer(RANDOMKEY, Charsets.US_ASCII)

  val RENAME = "RENAME"
  val RENAME_BYTES = ChannelBuffers.copiedBuffer(RENAME, Charsets.US_ASCII)

  val RENAMENX = "RENAMENX"
  val RENAMENX_BYTES = ChannelBuffers.copiedBuffer(RENAMENX, Charsets.US_ASCII)

  val RESTORE = "RESTORE"
  val RESTORE_BYTES = ChannelBuffers.copiedBuffer(RESTORE, Charsets.US_ASCII)

  val RPOP = "RPOP"
  val RPOP_BYTES = ChannelBuffers.copiedBuffer(RPOP, Charsets.US_ASCII)

  val RPOPLPUSH = "RPOPLPUSH"
  val RPOPLPUSH_BYTES = ChannelBuffers.copiedBuffer(RPOPLPUSH, Charsets.US_ASCII)

  val RPUSH = "RPUSH"
  val RPUSH_BYTES = ChannelBuffers.copiedBuffer(RPUSH, Charsets.US_ASCII)

  val RPUSHX = "RPUSHX"
  val RPUSHX_BYTES = ChannelBuffers.copiedBuffer(RPUSHX, Charsets.US_ASCII)

  val SADD = "SADD"
  val SADD_BYTES = ChannelBuffers.copiedBuffer(SADD, Charsets.US_ASCII)

  val SAVE = "SAVE"
  val SAVE_BYTES = ChannelBuffers.copiedBuffer(SAVE, Charsets.US_ASCII)

  val SCARD = "SCARD"
  val SCARD_BYTES = ChannelBuffers.copiedBuffer(SCARD, Charsets.US_ASCII)

  val SCRIPT_EXISTS = "SCRIPT_EXISTS"
  val SCRIPT_EXISTS_BYTES = ChannelBuffers.copiedBuffer(SCRIPT_EXISTS, Charsets.US_ASCII)

  val SCRIPT_FLUSH = "SCRIPT_FLUSH"
  val SCRIPT_FLUSH_BYTES = ChannelBuffers.copiedBuffer(SCRIPT_FLUSH, Charsets.US_ASCII)

  val SCRIPT_KILL = "SCRIPT_KILL"
  val SCRIPT_KILL_BYTES = ChannelBuffers.copiedBuffer(SCRIPT_KILL, Charsets.US_ASCII)

  val SCRIPT_LOAD = "SCRIPT_LOAD"
  val SCRIPT_LOAD_BYTES = ChannelBuffers.copiedBuffer(SCRIPT_LOAD, Charsets.US_ASCII)

  val SDIFF = "SDIFF"
  val SDIFF_BYTES = ChannelBuffers.copiedBuffer(SDIFF, Charsets.US_ASCII)

  val SDIFFSTORE = "SDIFFSTORE"
  val SDIFFSTORE_BYTES = ChannelBuffers.copiedBuffer(SDIFFSTORE, Charsets.US_ASCII)

  val SELECT = "SELECT"
  val SELECT_BYTES = ChannelBuffers.copiedBuffer(SELECT, Charsets.US_ASCII)

  val SET = "SET"
  val SET_BYTES = ChannelBuffers.copiedBuffer(SET, Charsets.US_ASCII)

  val SETBIT = "SETBIT"
  val SETBIT_BYTES = ChannelBuffers.copiedBuffer(SETBIT, Charsets.US_ASCII)

  val SETEX = "SETEX"
  val SETEX_BYTES = ChannelBuffers.copiedBuffer(SETEX, Charsets.US_ASCII)

  val SETNX = "SETNX"
  val SETNX_BYTES = ChannelBuffers.copiedBuffer(SETNX, Charsets.US_ASCII)

  val SETRANGE = "SETRANGE"
  val SETRANGE_BYTES = ChannelBuffers.copiedBuffer(SETRANGE, Charsets.US_ASCII)

  val SHUTDOWN = "SHUTDOWN"
  val SHUTDOWN_BYTES = ChannelBuffers.copiedBuffer(SHUTDOWN, Charsets.US_ASCII)

  val SINTER = "SINTER"
  val SINTER_BYTES = ChannelBuffers.copiedBuffer(SINTER, Charsets.US_ASCII)

  val SINTERSTORE = "SINTERSTORE"
  val SINTERSTORE_BYTES = ChannelBuffers.copiedBuffer(SINTERSTORE, Charsets.US_ASCII)

  val SISMEMBER = "SISMEMBER"
  val SISMEMBER_BYTES = ChannelBuffers.copiedBuffer(SISMEMBER, Charsets.US_ASCII)

  val SLAVEOF = "SLAVEOF"
  val SLAVEOF_BYTES = ChannelBuffers.copiedBuffer(SLAVEOF, Charsets.US_ASCII)

  val SLOWLOG = "SLOWLOG"
  val SLOWLOG_BYTES = ChannelBuffers.copiedBuffer(SLOWLOG, Charsets.US_ASCII)

  val SMEMBERS = "SMEMBERS"
  val SMEMBERS_BYTES = ChannelBuffers.copiedBuffer(SMEMBERS, Charsets.US_ASCII)

  val SMOVE = "SMOVE"
  val SMOVE_BYTES = ChannelBuffers.copiedBuffer(SMOVE, Charsets.US_ASCII)

  val SORT = "SORT"
  val SORT_BYTES = ChannelBuffers.copiedBuffer(SORT, Charsets.US_ASCII)

  val SPOP = "SPOP"
  val SPOP_BYTES = ChannelBuffers.copiedBuffer(SPOP, Charsets.US_ASCII)

  val SRANDMEMBER = "SRANDMEMBER"
  val SRANDMEMBER_BYTES = ChannelBuffers.copiedBuffer(SRANDMEMBER, Charsets.US_ASCII)

  val SREM = "SREM"
  val SREM_BYTES = ChannelBuffers.copiedBuffer(SREM, Charsets.US_ASCII)

  val STRLEN = "STRLEN"
  val STRLEN_BYTES = ChannelBuffers.copiedBuffer(STRLEN, Charsets.US_ASCII)

  val SUNION = "SUNION"
  val SUNION_BYTES = ChannelBuffers.copiedBuffer(SUNION, Charsets.US_ASCII)

  val SUNIONSTORE = "SUNIONSTORE"
  val SUNIONSTORE_BYTES = ChannelBuffers.copiedBuffer(SUNIONSTORE, Charsets.US_ASCII)

  val SYNC = "SYNC"
  val SYNC_BYTES = ChannelBuffers.copiedBuffer(SYNC, Charsets.US_ASCII)

  val TIME = "TIME"
  val TIME_BYTES = ChannelBuffers.copiedBuffer(TIME, Charsets.US_ASCII)

  val TTL = "TTL"
  val TTL_BYTES = ChannelBuffers.copiedBuffer(TTL, Charsets.US_ASCII)

  val TYPE = "TYPE"
  val TYPE_BYTES = ChannelBuffers.copiedBuffer(TYPE, Charsets.US_ASCII)

  val UNWATCH = "UNWATCH"
  val UNWATCH_BYTES = ChannelBuffers.copiedBuffer(UNWATCH, Charsets.US_ASCII)

  val WATCH = "WATCH"
  val WATCH_BYTES = ChannelBuffers.copiedBuffer(WATCH, Charsets.US_ASCII)

  val ZADD = "ZADD"
  val ZADD_BYTES = ChannelBuffers.copiedBuffer(ZADD, Charsets.US_ASCII)

  val ZCARD = "ZCARD"
  val ZCARD_BYTES = ChannelBuffers.copiedBuffer(ZCARD, Charsets.US_ASCII)

  val ZCOUNT = "ZCOUNT"
  val ZCOUNT_BYTES = ChannelBuffers.copiedBuffer(ZCOUNT, Charsets.US_ASCII)

  val ZINCRBY = "ZINCRBY"
  val ZINCRBY_BYTES = ChannelBuffers.copiedBuffer(ZINCRBY, Charsets.US_ASCII)

  val ZINTERSTORE = "ZINTERSTORE"
  val ZINTERSTORE_BYTES = ChannelBuffers.copiedBuffer(ZINTERSTORE, Charsets.US_ASCII)

  val ZRANGE = "ZRANGE"
  val ZRANGE_BYTES = ChannelBuffers.copiedBuffer(ZRANGE, Charsets.US_ASCII)

  val ZRANGEBYSCORE = "ZRANGEBYSCORE"
  val ZRANGEBYSCORE_BYTES = ChannelBuffers.copiedBuffer(ZRANGEBYSCORE, Charsets.US_ASCII)

  val ZRANK = "ZRANK"
  val ZRANK_BYTES = ChannelBuffers.copiedBuffer(ZRANK, Charsets.US_ASCII)

  val ZREM = "ZREM"
  val ZREM_BYTES = ChannelBuffers.copiedBuffer(ZREM, Charsets.US_ASCII)

  val ZREMRANGEBYRANK = "ZREMRANGEBYRANK"
  val ZREMRANGEBYRANK_BYTES = ChannelBuffers.copiedBuffer(ZREMRANGEBYRANK, Charsets.US_ASCII)

  val ZREMRANGEBYSCORE = "ZREMRANGEBYSCORE"
  val ZREMRANGEBYSCORE_BYTES = ChannelBuffers.copiedBuffer(ZREMRANGEBYSCORE, Charsets.US_ASCII)

  val ZREVRANGE = "ZREVRANGE"
  val ZREVRANGE_BYTES = ChannelBuffers.copiedBuffer(ZREVRANGE, Charsets.US_ASCII)

  val ZREVRANGEBYSCORE = "ZREVRANGEBYSCORE"
  val ZREVRANGEBYSCORE_BYTES = ChannelBuffers.copiedBuffer(ZREVRANGEBYSCORE, Charsets.US_ASCII)

  val ZREVRANK = "ZREVRANK"
  val ZREVRANK_BYTES = ChannelBuffers.copiedBuffer(ZREVRANK, Charsets.US_ASCII)

  val ZSCORE = "ZSCORE"
  val ZSCORE_BYTES = ChannelBuffers.copiedBuffer(ZSCORE, Charsets.US_ASCII)

  val ZUNIONSTORE = "ZUNIONSTORE"
  val ZUNIONSTORE_BYTES = ChannelBuffers.copiedBuffer(ZUNIONSTORE, Charsets.US_ASCII)

}

case class RedisException(message:String) extends RuntimeException(message)

class RedisClient(service: Service[Command, Reply[_]]) {

  /**
   * Append a value to a key
   */
  def append(key0: Object, value1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.APPEND_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from APPEND: " + other)
      }
    }
  }

  /**
   * Authenticate to the server
   */
  def auth(password0: Object): Future[StatusReply] = {
    service(new Command(RedisClient.AUTH_BYTES, password0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from AUTH: " + other)
      }
    }
  }

  /**
   * Asynchronously rewrite the append-only file
   */
  def bgrewriteaof(): Future[StatusReply] = {
    service(new Command(RedisClient.BGREWRITEAOF_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from BGREWRITEAOF: " + other)
      }
    }
  }

  /**
   * Asynchronously save the dataset to disk
   */
  def bgsave(): Future[StatusReply] = {
    service(new Command(RedisClient.BGSAVE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from BGSAVE: " + other)
      }
    }
  }

  /**
   * Count set bits in a string
   */
  def bitcount(key0: Object, start1: Object, end2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.BITCOUNT_BYTES, key0, start1, end2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from BITCOUNT: " + other)
      }
    }
  }

  /**
   * Perform bitwise operations between strings
   */
  def bitop(operation0: Object, destkey1: Object, key2: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += operation0
    arguments += destkey1
    arguments = arguments ++ key2
    service(new Command(RedisClient.BITOP_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from BITOP: " + other)
      }
    }
  }

  /**
   * Remove and get the first element in a list, or block until one is available
   */
  def blpop(key0: Object*): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key0
    service(new Command(RedisClient.BLPOP_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from BLPOP: " + other)
      }
    }
  }

  /**
   * Remove and get the last element in a list, or block until one is available
   */
  def brpop(key0: Object*): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key0
    service(new Command(RedisClient.BRPOP_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from BRPOP: " + other)
      }
    }
  }

  /**
   * Pop a value from a list, push it to another list and return it; or block until one is available
   */
  def brpoplpush(source0: Object, destination1: Object, timeout2: Object): Future[BulkReply] = {
    service(new Command(RedisClient.BRPOPLPUSH_BYTES, source0, destination1, timeout2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from BRPOPLPUSH: " + other)
      }
    }
  }

  /**
   * Get the value of a configuration parameter
   */
  def config_get(parameter0: Object): Future[Reply[_]] = {
    service(new Command(RedisClient.CONFIG_GET_BYTES, parameter0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from CONFIG_GET: " + other)
      }
    }
  }

  /**
   * Set a configuration parameter to the given value
   */
  def config_set(parameter0: Object, value1: Object): Future[Reply[_]] = {
    service(new Command(RedisClient.CONFIG_SET_BYTES, parameter0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from CONFIG_SET: " + other)
      }
    }
  }

  /**
   * Reset the stats returned by INFO
   */
  def config_resetstat(): Future[Reply[_]] = {
    service(new Command(RedisClient.CONFIG_RESETSTAT_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from CONFIG_RESETSTAT: " + other)
      }
    }
  }

  /**
   * Return the number of keys in the selected database
   */
  def dbsize(): Future[IntegerReply] = {
    service(new Command(RedisClient.DBSIZE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from DBSIZE: " + other)
      }
    }
  }

  /**
   * Get debugging information about a key
   */
  def debug_object(key0: Object): Future[Reply[_]] = {
    service(new Command(RedisClient.DEBUG_OBJECT_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from DEBUG_OBJECT: " + other)
      }
    }
  }

  /**
   * Make the server crash
   */
  def debug_segfault(): Future[Reply[_]] = {
    service(new Command(RedisClient.DEBUG_SEGFAULT_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from DEBUG_SEGFAULT: " + other)
      }
    }
  }

  /**
   * Decrement the integer value of a key by one
   */
  def decr(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.DECR_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from DECR: " + other)
      }
    }
  }

  /**
   * Decrement the integer value of a key by the given number
   */
  def decrby(key0: Object, decrement1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.DECRBY_BYTES, key0, decrement1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from DECRBY: " + other)
      }
    }
  }

  /**
   * Delete a key
   */
  def del(key0: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key0
    service(new Command(RedisClient.DEL_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from DEL: " + other)
      }
    }
  }

  /**
   * Return a serialized version of the value stored at the specified key.
   */
  def dump(key0: Object): Future[BulkReply] = {
    service(new Command(RedisClient.DUMP_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from DUMP: " + other)
      }
    }
  }

  /**
   * Echo the given string
   */
  def echo(message0: Object): Future[BulkReply] = {
    service(new Command(RedisClient.ECHO_BYTES, message0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from ECHO: " + other)
      }
    }
  }

  /**
   * Execute a Lua script server side
   */
  def eval(script0: Object, numkeys1: Object, key2: Object*): Future[Reply[_]] = {
    var arguments = ArrayBuffer[Object]()
    arguments += script0
    arguments += numkeys1
    arguments = arguments ++ key2
    service(new Command(RedisClient.EVAL_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from EVAL: " + other)
      }
    }
  }

  /**
   * Execute a Lua script server side
   */
  def evalsha(sha10: Object, numkeys1: Object, key2: Object*): Future[Reply[_]] = {
    var arguments = ArrayBuffer[Object]()
    arguments += sha10
    arguments += numkeys1
    arguments = arguments ++ key2
    service(new Command(RedisClient.EVALSHA_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from EVALSHA: " + other)
      }
    }
  }

  /**
   * Determine if a key exists
   */
  def exists(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.EXISTS_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from EXISTS: " + other)
      }
    }
  }

  /**
   * Set a key's time to live in seconds
   */
  def expire(key0: Object, seconds1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.EXPIRE_BYTES, key0, seconds1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from EXPIRE: " + other)
      }
    }
  }

  /**
   * Set the expiration for a key as a UNIX timestamp
   */
  def expireat(key0: Object, timestamp1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.EXPIREAT_BYTES, key0, timestamp1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from EXPIREAT: " + other)
      }
    }
  }

  /**
   * Remove all keys from all databases
   */
  def flushall(): Future[StatusReply] = {
    service(new Command(RedisClient.FLUSHALL_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from FLUSHALL: " + other)
      }
    }
  }

  /**
   * Remove all keys from the current database
   */
  def flushdb(): Future[StatusReply] = {
    service(new Command(RedisClient.FLUSHDB_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from FLUSHDB: " + other)
      }
    }
  }

  /**
   * Get the value of a key
   */
  def get(key0: Object): Future[BulkReply] = {
    service(new Command(RedisClient.GET_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from GET: " + other)
      }
    }
  }

  /**
   * Returns the bit value at offset in the string value stored at key
   */
  def getbit(key0: Object, offset1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.GETBIT_BYTES, key0, offset1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from GETBIT: " + other)
      }
    }
  }

  /**
   * Get a substring of the string stored at a key
   */
  def getrange(key0: Object, start1: Object, end2: Object): Future[BulkReply] = {
    service(new Command(RedisClient.GETRANGE_BYTES, key0, start1, end2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from GETRANGE: " + other)
      }
    }
  }

  /**
   * Set the string value of a key and return its old value
   */
  def getset(key0: Object, value1: Object): Future[BulkReply] = {
    service(new Command(RedisClient.GETSET_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from GETSET: " + other)
      }
    }
  }

  /**
   * Delete one or more hash fields
   */
  def hdel(key0: Object, field1: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments = arguments ++ field1
    service(new Command(RedisClient.HDEL_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from HDEL: " + other)
      }
    }
  }

  /**
   * Determine if a hash field exists
   */
  def hexists(key0: Object, field1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.HEXISTS_BYTES, key0, field1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from HEXISTS: " + other)
      }
    }
  }

  /**
   * Get the value of a hash field
   */
  def hget(key0: Object, field1: Object): Future[BulkReply] = {
    service(new Command(RedisClient.HGET_BYTES, key0, field1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from HGET: " + other)
      }
    }
  }

  /**
   * Get all the fields and values in a hash
   */
  def hgetall(key0: Object): Future[MultiBulkReply] = {
    service(new Command(RedisClient.HGETALL_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from HGETALL: " + other)
      }
    }
  }

  /**
   * Increment the integer value of a hash field by the given number
   */
  def hincrby(key0: Object, field1: Object, increment2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.HINCRBY_BYTES, key0, field1, increment2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from HINCRBY: " + other)
      }
    }
  }

  /**
   * Increment the float value of a hash field by the given amount
   */
  def hincrbyfloat(key0: Object, field1: Object, increment2: Object): Future[BulkReply] = {
    service(new Command(RedisClient.HINCRBYFLOAT_BYTES, key0, field1, increment2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from HINCRBYFLOAT: " + other)
      }
    }
  }

  /**
   * Get all the fields in a hash
   */
  def hkeys(key0: Object): Future[MultiBulkReply] = {
    service(new Command(RedisClient.HKEYS_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from HKEYS: " + other)
      }
    }
  }

  /**
   * Get the number of fields in a hash
   */
  def hlen(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.HLEN_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from HLEN: " + other)
      }
    }
  }

  /**
   * Get the values of all the given hash fields
   */
  def hmget(key0: Object, field1: Object*): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments = arguments ++ field1
    service(new Command(RedisClient.HMGET_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from HMGET: " + other)
      }
    }
  }

  /**
   * Set multiple hash fields to multiple values
   */
  def hmset(key0: Object, field_or_value1: Object*): Future[StatusReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments = arguments ++ field_or_value1
    service(new Command(RedisClient.HMSET_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from HMSET: " + other)
      }
    }
  }

  /**
   * Set the string value of a hash field
   */
  def hset(key0: Object, field1: Object, value2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.HSET_BYTES, key0, field1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from HSET: " + other)
      }
    }
  }

  /**
   * Set the value of a hash field, only if the field does not exist
   */
  def hsetnx(key0: Object, field1: Object, value2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.HSETNX_BYTES, key0, field1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from HSETNX: " + other)
      }
    }
  }

  /**
   * Get all the values in a hash
   */
  def hvals(key0: Object): Future[MultiBulkReply] = {
    service(new Command(RedisClient.HVALS_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from HVALS: " + other)
      }
    }
  }

  /**
   * Increment the integer value of a key by one
   */
  def incr(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.INCR_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from INCR: " + other)
      }
    }
  }

  /**
   * Increment the integer value of a key by the given amount
   */
  def incrby(key0: Object, increment1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.INCRBY_BYTES, key0, increment1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from INCRBY: " + other)
      }
    }
  }

  /**
   * Increment the float value of a key by the given amount
   */
  def incrbyfloat(key0: Object, increment1: Object): Future[BulkReply] = {
    service(new Command(RedisClient.INCRBYFLOAT_BYTES, key0, increment1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from INCRBYFLOAT: " + other)
      }
    }
  }

  /**
   * Get information and statistics about the server
   */
  def info(): Future[BulkReply] = {
    service(new Command(RedisClient.INFO_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from INFO: " + other)
      }
    }
  }

  /**
   * Find all keys matching the given pattern
   */
  def keys(pattern0: Object): Future[MultiBulkReply] = {
    service(new Command(RedisClient.KEYS_BYTES, pattern0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from KEYS: " + other)
      }
    }
  }

  /**
   * Get the UNIX time stamp of the last successful save to disk
   */
  def lastsave(): Future[IntegerReply] = {
    service(new Command(RedisClient.LASTSAVE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from LASTSAVE: " + other)
      }
    }
  }

  /**
   * Get an element from a list by its index
   */
  def lindex(key0: Object, index1: Object): Future[BulkReply] = {
    service(new Command(RedisClient.LINDEX_BYTES, key0, index1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from LINDEX: " + other)
      }
    }
  }

  /**
   * Insert an element before or after another element in a list
   */
  def linsert(key0: Object, where1: Object, pivot2: Object, value3: Object): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments += where1
    arguments += pivot2
    arguments += value3
    service(new Command(RedisClient.LINSERT_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from LINSERT: " + other)
      }
    }
  }

  /**
   * Get the length of a list
   */
  def llen(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.LLEN_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from LLEN: " + other)
      }
    }
  }

  /**
   * Remove and get the first element in a list
   */
  def lpop(key0: Object): Future[BulkReply] = {
    service(new Command(RedisClient.LPOP_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from LPOP: " + other)
      }
    }
  }

  /**
   * Prepend one or multiple values to a list
   */
  def lpush(key0: Object, value1: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments = arguments ++ value1
    service(new Command(RedisClient.LPUSH_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from LPUSH: " + other)
      }
    }
  }

  /**
   * Prepend a value to a list, only if the list exists
   */
  def lpushx(key0: Object, value1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.LPUSHX_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from LPUSHX: " + other)
      }
    }
  }

  /**
   * Get a range of elements from a list
   */
  def lrange(key0: Object, start1: Object, stop2: Object): Future[MultiBulkReply] = {
    service(new Command(RedisClient.LRANGE_BYTES, key0, start1, stop2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from LRANGE: " + other)
      }
    }
  }

  /**
   * Remove elements from a list
   */
  def lrem(key0: Object, count1: Object, value2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.LREM_BYTES, key0, count1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from LREM: " + other)
      }
    }
  }

  /**
   * Set the value of an element in a list by its index
   */
  def lset(key0: Object, index1: Object, value2: Object): Future[StatusReply] = {
    service(new Command(RedisClient.LSET_BYTES, key0, index1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from LSET: " + other)
      }
    }
  }

  /**
   * Trim a list to the specified range
   */
  def ltrim(key0: Object, start1: Object, stop2: Object): Future[StatusReply] = {
    service(new Command(RedisClient.LTRIM_BYTES, key0, start1, stop2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from LTRIM: " + other)
      }
    }
  }

  /**
   * Get the values of all the given keys
   */
  def mget(key0: Object*): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key0
    service(new Command(RedisClient.MGET_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from MGET: " + other)
      }
    }
  }

  /**
   * Atomically transfer a key from a Redis instance to another one.
   */
  def migrate(host0: Object, port1: Object, key2: Object, destination_db3: Object, timeout4: Object): Future[StatusReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += host0
    arguments += port1
    arguments += key2
    arguments += destination_db3
    arguments += timeout4
    service(new Command(RedisClient.MIGRATE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from MIGRATE: " + other)
      }
    }
  }

  /**
   * Listen for all requests received by the server in real time
   */
  def monitor(): Future[Reply[_]] = {
    service(new Command(RedisClient.MONITOR_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from MONITOR: " + other)
      }
    }
  }

  /**
   * Move a key to another database
   */
  def move(key0: Object, db1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.MOVE_BYTES, key0, db1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from MOVE: " + other)
      }
    }
  }

  /**
   * Set multiple keys to multiple values
   */
  def mset(key_or_value0: Object*): Future[StatusReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key_or_value0
    service(new Command(RedisClient.MSET_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from MSET: " + other)
      }
    }
  }

  /**
   * Set multiple keys to multiple values, only if none of the keys exist
   */
  def msetnx(key_or_value0: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key_or_value0
    service(new Command(RedisClient.MSETNX_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from MSETNX: " + other)
      }
    }
  }

  /**
   * Inspect the internals of Redis objects
   */
  def `object`(subcommand0: Object, arguments1: Object*): Future[Reply[_]] = {
    var arguments = ArrayBuffer[Object]()
    arguments += subcommand0
    arguments = arguments ++ arguments1
    service(new Command(RedisClient.OBJECT_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from OBJECT: " + other)
      }
    }
  }

  /**
   * Remove the expiration from a key
   */
  def persist(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.PERSIST_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from PERSIST: " + other)
      }
    }
  }

  /**
   * Set a key's time to live in milliseconds
   */
  def pexpire(key0: Object, milliseconds1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.PEXPIRE_BYTES, key0, milliseconds1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from PEXPIRE: " + other)
      }
    }
  }

  /**
   * Set the expiration for a key as a UNIX timestamp specified in milliseconds
   */
  def pexpireat(key0: Object, milliseconds_timestamp1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.PEXPIREAT_BYTES, key0, milliseconds_timestamp1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from PEXPIREAT: " + other)
      }
    }
  }

  /**
   * Ping the server
   */
  def ping(): Future[StatusReply] = {
    service(new Command(RedisClient.PING_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from PING: " + other)
      }
    }
  }

  /**
   * Set the value and expiration in milliseconds of a key
   */
  def psetex(key0: Object, milliseconds1: Object, value2: Object): Future[Reply[_]] = {
    service(new Command(RedisClient.PSETEX_BYTES, key0, milliseconds1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from PSETEX: " + other)
      }
    }
  }

  /**
   * Get the time to live for a key in milliseconds
   */
  def pttl(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.PTTL_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from PTTL: " + other)
      }
    }
  }

  /**
   * Post a message to a channel
   */
  def publish(channel0: Object, message1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.PUBLISH_BYTES, channel0, message1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from PUBLISH: " + other)
      }
    }
  }

  /**
   * Close the connection
   */
  def quit(): Future[StatusReply] = {
    service(new Command(RedisClient.QUIT_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from QUIT: " + other)
      }
    }
  }

  /**
   * Return a random key from the keyspace
   */
  def randomkey(): Future[BulkReply] = {
    service(new Command(RedisClient.RANDOMKEY_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from RANDOMKEY: " + other)
      }
    }
  }

  /**
   * Rename a key
   */
  def rename(key0: Object, newkey1: Object): Future[StatusReply] = {
    service(new Command(RedisClient.RENAME_BYTES, key0, newkey1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from RENAME: " + other)
      }
    }
  }

  /**
   * Rename a key, only if the new key does not exist
   */
  def renamenx(key0: Object, newkey1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.RENAMENX_BYTES, key0, newkey1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from RENAMENX: " + other)
      }
    }
  }

  /**
   * Create a key using the provided serialized value, previously obtained using DUMP.
   */
  def restore(key0: Object, ttl1: Object, serialized_value2: Object): Future[StatusReply] = {
    service(new Command(RedisClient.RESTORE_BYTES, key0, ttl1, serialized_value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from RESTORE: " + other)
      }
    }
  }

  /**
   * Remove and get the last element in a list
   */
  def rpop(key0: Object): Future[BulkReply] = {
    service(new Command(RedisClient.RPOP_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from RPOP: " + other)
      }
    }
  }

  /**
   * Remove the last element in a list, append it to another list and return it
   */
  def rpoplpush(source0: Object, destination1: Object): Future[BulkReply] = {
    service(new Command(RedisClient.RPOPLPUSH_BYTES, source0, destination1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from RPOPLPUSH: " + other)
      }
    }
  }

  /**
   * Append one or multiple values to a list
   */
  def rpush(key0: Object, value1: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments = arguments ++ value1
    service(new Command(RedisClient.RPUSH_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from RPUSH: " + other)
      }
    }
  }

  /**
   * Append a value to a list, only if the list exists
   */
  def rpushx(key0: Object, value1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.RPUSHX_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from RPUSHX: " + other)
      }
    }
  }

  /**
   * Add one or more members to a set
   */
  def sadd(key0: Object, member1: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments = arguments ++ member1
    service(new Command(RedisClient.SADD_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SADD: " + other)
      }
    }
  }

  /**
   * Synchronously save the dataset to disk
   */
  def save(): Future[StatusReply] = {
    service(new Command(RedisClient.SAVE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from SAVE: " + other)
      }
    }
  }

  /**
   * Get the number of members in a set
   */
  def scard(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.SCARD_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SCARD: " + other)
      }
    }
  }

  /**
   * Check existence of scripts in the script cache.
   */
  def script_exists(script0: Object*): Future[Reply[_]] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ script0
    service(new Command(RedisClient.SCRIPT_EXISTS_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from SCRIPT_EXISTS: " + other)
      }
    }
  }

  /**
   * Remove all the scripts from the script cache.
   */
  def script_flush(): Future[Reply[_]] = {
    service(new Command(RedisClient.SCRIPT_FLUSH_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from SCRIPT_FLUSH: " + other)
      }
    }
  }

  /**
   * Kill the script currently in execution.
   */
  def script_kill(): Future[Reply[_]] = {
    service(new Command(RedisClient.SCRIPT_KILL_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from SCRIPT_KILL: " + other)
      }
    }
  }

  /**
   * Load the specified Lua script into the script cache.
   */
  def script_load(script0: Object): Future[Reply[_]] = {
    service(new Command(RedisClient.SCRIPT_LOAD_BYTES, script0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from SCRIPT_LOAD: " + other)
      }
    }
  }

  /**
   * Subtract multiple sets
   */
  def sdiff(key0: Object*): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key0
    service(new Command(RedisClient.SDIFF_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from SDIFF: " + other)
      }
    }
  }

  /**
   * Subtract multiple sets and store the resulting set in a key
   */
  def sdiffstore(destination0: Object, key1: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += destination0
    arguments = arguments ++ key1
    service(new Command(RedisClient.SDIFFSTORE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SDIFFSTORE: " + other)
      }
    }
  }

  /**
   * Change the selected database for the current connection
   */
  def select(index0: Object): Future[StatusReply] = {
    service(new Command(RedisClient.SELECT_BYTES, index0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from SELECT: " + other)
      }
    }
  }

  /**
   * Set the string value of a key
   */
  def set(key0: Object, value1: Object): Future[StatusReply] = {
    service(new Command(RedisClient.SET_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from SET: " + other)
      }
    }
  }

  /**
   * Sets or clears the bit at offset in the string value stored at key
   */
  def setbit(key0: Object, offset1: Object, value2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.SETBIT_BYTES, key0, offset1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SETBIT: " + other)
      }
    }
  }

  /**
   * Set the value and expiration of a key
   */
  def setex(key0: Object, seconds1: Object, value2: Object): Future[StatusReply] = {
    service(new Command(RedisClient.SETEX_BYTES, key0, seconds1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from SETEX: " + other)
      }
    }
  }

  /**
   * Set the value of a key, only if the key does not exist
   */
  def setnx(key0: Object, value1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.SETNX_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SETNX: " + other)
      }
    }
  }

  /**
   * Overwrite part of a string at key starting at the specified offset
   */
  def setrange(key0: Object, offset1: Object, value2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.SETRANGE_BYTES, key0, offset1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SETRANGE: " + other)
      }
    }
  }

  /**
   * Synchronously save the dataset to disk and then shut down the server
   */
  def shutdown(NOSAVE0: Object, SAVE1: Object): Future[StatusReply] = {
    service(new Command(RedisClient.SHUTDOWN_BYTES, NOSAVE0, SAVE1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from SHUTDOWN: " + other)
      }
    }
  }

  /**
   * Intersect multiple sets
   */
  def sinter(key0: Object*): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key0
    service(new Command(RedisClient.SINTER_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from SINTER: " + other)
      }
    }
  }

  /**
   * Intersect multiple sets and store the resulting set in a key
   */
  def sinterstore(destination0: Object, key1: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += destination0
    arguments = arguments ++ key1
    service(new Command(RedisClient.SINTERSTORE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SINTERSTORE: " + other)
      }
    }
  }

  /**
   * Determine if a given value is a member of a set
   */
  def sismember(key0: Object, member1: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.SISMEMBER_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SISMEMBER: " + other)
      }
    }
  }

  /**
   * Make the server a slave of another instance, or promote it as master
   */
  def slaveof(host0: Object, port1: Object): Future[StatusReply] = {
    service(new Command(RedisClient.SLAVEOF_BYTES, host0, port1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from SLAVEOF: " + other)
      }
    }
  }

  /**
   * Manages the Redis slow queries log
   */
  def slowlog(subcommand0: Object, argument1: Object): Future[Reply[_]] = {
    service(new Command(RedisClient.SLOWLOG_BYTES, subcommand0, argument1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from SLOWLOG: " + other)
      }
    }
  }

  /**
   * Get all the members in a set
   */
  def smembers(key0: Object): Future[MultiBulkReply] = {
    service(new Command(RedisClient.SMEMBERS_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from SMEMBERS: " + other)
      }
    }
  }

  /**
   * Move a member from one set to another
   */
  def smove(source0: Object, destination1: Object, member2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.SMOVE_BYTES, source0, destination1, member2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SMOVE: " + other)
      }
    }
  }

  /**
   * Sort the elements in a list, set or sorted set
   */
  def sort(key0: Object, pattern1: Object, offset_or_count2: Object, pattern3: Object*): Future[Reply[_]] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments += pattern1
    arguments += offset_or_count2
    arguments = arguments ++ pattern3
    service(new Command(RedisClient.SORT_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from SORT: " + other)
      }
    }
  }

  /**
   * Remove and return a random member from a set
   */
  def spop(key0: Object): Future[BulkReply] = {
    service(new Command(RedisClient.SPOP_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from SPOP: " + other)
      }
    }
  }

  /**
   * Get a random member from a set
   */
  def srandmember(key0: Object): Future[BulkReply] = {
    service(new Command(RedisClient.SRANDMEMBER_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from SRANDMEMBER: " + other)
      }
    }
  }

  /**
   * Remove one or more members from a set
   */
  def srem(key0: Object, member1: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments = arguments ++ member1
    service(new Command(RedisClient.SREM_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SREM: " + other)
      }
    }
  }

  /**
   * Get the length of the value stored in a key
   */
  def strlen(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.STRLEN_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from STRLEN: " + other)
      }
    }
  }

  /**
   * Add multiple sets
   */
  def sunion(key0: Object*): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key0
    service(new Command(RedisClient.SUNION_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from SUNION: " + other)
      }
    }
  }

  /**
   * Add multiple sets and store the resulting set in a key
   */
  def sunionstore(destination0: Object, key1: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += destination0
    arguments = arguments ++ key1
    service(new Command(RedisClient.SUNIONSTORE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from SUNIONSTORE: " + other)
      }
    }
  }

  /**
   * Internal command used for replication
   */
  def sync(): Future[Reply[_]] = {
    service(new Command(RedisClient.SYNC_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from SYNC: " + other)
      }
    }
  }

  /**
   * Return the current server time
   */
  def time(): Future[MultiBulkReply] = {
    service(new Command(RedisClient.TIME_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from TIME: " + other)
      }
    }
  }

  /**
   * Get the time to live for a key
   */
  def ttl(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.TTL_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from TTL: " + other)
      }
    }
  }

  /**
   * Determine the type stored at key
   */
  def `type`(key0: Object): Future[StatusReply] = {
    service(new Command(RedisClient.TYPE_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from TYPE: " + other)
      }
    }
  }

  /**
   * Forget about all watched keys
   */
  def unwatch(): Future[StatusReply] = {
    service(new Command(RedisClient.UNWATCH_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from UNWATCH: " + other)
      }
    }
  }

  /**
   * Watch the given keys to determine execution of the MULTI/EXEC block
   */
  def watch(key0: Object*): Future[StatusReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ key0
    service(new Command(RedisClient.WATCH_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: StatusReply => reply
        case other => throw new RedisException("Unexpected reply from WATCH: " + other)
      }
    }
  }

  /**
   * Add one or more members to a sorted set, or update its score if it already exists
   */
  def zadd(args: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ args
    service(new Command(RedisClient.ZADD_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from ZADD: " + other)
      }
    }
  }

  /**
   * Get the number of members in a sorted set
   */
  def zcard(key0: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.ZCARD_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from ZCARD: " + other)
      }
    }
  }

  /**
   * Count the members in a sorted set with scores within the given values
   */
  def zcount(key0: Object, min1: Object, max2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.ZCOUNT_BYTES, key0, min1, max2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from ZCOUNT: " + other)
      }
    }
  }

  /**
   * Increment the score of a member in a sorted set
   */
  def zincrby(key0: Object, increment1: Object, member2: Object): Future[BulkReply] = {
    service(new Command(RedisClient.ZINCRBY_BYTES, key0, increment1, member2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from ZINCRBY: " + other)
      }
    }
  }

  /**
   * Intersect multiple sorted sets and store the resulting sorted set in a new key
   */
  def zinterstore(args: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments = arguments ++ args
    service(new Command(RedisClient.ZINTERSTORE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from ZINTERSTORE: " + other)
      }
    }
  }

  /**
   * Return a range of members in a sorted set, by index
   */
  def zrange(key0: Object, start1: Object, stop2: Object, withscores3: Object): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments += start1
    arguments += stop2
    arguments += withscores3
    service(new Command(RedisClient.ZRANGE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from ZRANGE: " + other)
      }
    }
  }

  /**
   * Return a range of members in a sorted set, by score
   */
  def zrangebyscore(key0: Object, min1: Object, max2: Object, withscores3: Object, offset_or_count4: Object): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments += min1
    arguments += max2
    arguments += withscores3
    arguments += offset_or_count4
    service(new Command(RedisClient.ZRANGEBYSCORE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from ZRANGEBYSCORE: " + other)
      }
    }
  }

  /**
   * Determine the index of a member in a sorted set
   */
  def zrank(key0: Object, member1: Object): Future[Reply[_]] = {
    service(new Command(RedisClient.ZRANK_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from ZRANK: " + other)
      }
    }
  }

  /**
   * Remove one or more members from a sorted set
   */
  def zrem(key0: Object, member1: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments = arguments ++ member1
    service(new Command(RedisClient.ZREM_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from ZREM: " + other)
      }
    }
  }

  /**
   * Remove all members in a sorted set within the given indexes
   */
  def zremrangebyrank(key0: Object, start1: Object, stop2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.ZREMRANGEBYRANK_BYTES, key0, start1, stop2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from ZREMRANGEBYRANK: " + other)
      }
    }
  }

  /**
   * Remove all members in a sorted set within the given scores
   */
  def zremrangebyscore(key0: Object, min1: Object, max2: Object): Future[IntegerReply] = {
    service(new Command(RedisClient.ZREMRANGEBYSCORE_BYTES, key0, min1, max2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from ZREMRANGEBYSCORE: " + other)
      }
    }
  }

  /**
   * Return a range of members in a sorted set, by index, with scores ordered from high to low
   */
  def zrevrange(key0: Object, start1: Object, stop2: Object, withscores3: Object): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments += start1
    arguments += stop2
    arguments += withscores3
    service(new Command(RedisClient.ZREVRANGE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from ZREVRANGE: " + other)
      }
    }
  }

  /**
   * Return a range of members in a sorted set, by score, with scores ordered from high to low
   */
  def zrevrangebyscore(key0: Object, max1: Object, min2: Object, withscores3: Object, offset_or_count4: Object): Future[MultiBulkReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += key0
    arguments += max1
    arguments += min2
    arguments += withscores3
    arguments += offset_or_count4
    service(new Command(RedisClient.ZREVRANGEBYSCORE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: MultiBulkReply => reply
        case other => throw new RedisException("Unexpected reply from ZREVRANGEBYSCORE: " + other)
      }
    }
  }

  /**
   * Determine the index of a member in a sorted set, with scores ordered from high to low
   */
  def zrevrank(key0: Object, member1: Object): Future[Reply[_]] = {
    service(new Command(RedisClient.ZREVRANK_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: Reply[_] => reply
        case other => throw new RedisException("Unexpected reply from ZREVRANK: " + other)
      }
    }
  }

  /**
   * Get the score associated with the given member in a sorted set
   */
  def zscore(key0: Object, member1: Object): Future[BulkReply] = {
    service(new Command(RedisClient.ZSCORE_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: BulkReply => reply
        case other => throw new RedisException("Unexpected reply from ZSCORE: " + other)
      }
    }
  }

  /**
   * Add multiple sorted sets and store the resulting sorted set in a new key
   */
  def zunionstore(destination0: Object, numkeys1: Object, key2: Object*): Future[IntegerReply] = {
    var arguments = ArrayBuffer[Object]()
    arguments += destination0
    arguments += numkeys1
    arguments = arguments ++ key2
    service(new Command(RedisClient.ZUNIONSTORE_BYTES, arguments.toArray)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.data())
        case reply: IntegerReply => reply
        case other => throw new RedisException("Unexpected reply from ZUNIONSTORE: " + other)
      }
    }
  }

  def release() {
    service.release()
  }
}
