package redis.client

import com.google.common.base.Charsets

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder

import redis.finagle._
import org.jboss.netty.handler.codec.redis._

object RedisClient {

  val APPEND = "APPEND";
  val APPEND_BYTES = APPEND.getBytes(Charsets.US_ASCII);

  val AUTH = "AUTH";
  val AUTH_BYTES = AUTH.getBytes(Charsets.US_ASCII);

  val BGREWRITEAOF = "BGREWRITEAOF";
  val BGREWRITEAOF_BYTES = BGREWRITEAOF.getBytes(Charsets.US_ASCII);

  val BGSAVE = "BGSAVE";
  val BGSAVE_BYTES = BGSAVE.getBytes(Charsets.US_ASCII);

  val BLPOP = "BLPOP";
  val BLPOP_BYTES = BLPOP.getBytes(Charsets.US_ASCII);

  val BRPOP = "BRPOP";
  val BRPOP_BYTES = BRPOP.getBytes(Charsets.US_ASCII);

  val BRPOPLPUSH = "BRPOPLPUSH";
  val BRPOPLPUSH_BYTES = BRPOPLPUSH.getBytes(Charsets.US_ASCII);

  val CONFIG_GET = "CONFIG_GET";
  val CONFIG_GET_BYTES = CONFIG_GET.getBytes(Charsets.US_ASCII);

  val CONFIG_SET = "CONFIG_SET";
  val CONFIG_SET_BYTES = CONFIG_SET.getBytes(Charsets.US_ASCII);

  val CONFIG_RESETSTAT = "CONFIG_RESETSTAT";
  val CONFIG_RESETSTAT_BYTES = CONFIG_RESETSTAT.getBytes(Charsets.US_ASCII);

  val DBSIZE = "DBSIZE";
  val DBSIZE_BYTES = DBSIZE.getBytes(Charsets.US_ASCII);

  val DEBUG_OBJECT = "DEBUG_OBJECT";
  val DEBUG_OBJECT_BYTES = DEBUG_OBJECT.getBytes(Charsets.US_ASCII);

  val DEBUG_SEGFAULT = "DEBUG_SEGFAULT";
  val DEBUG_SEGFAULT_BYTES = DEBUG_SEGFAULT.getBytes(Charsets.US_ASCII);

  val DECR = "DECR";
  val DECR_BYTES = DECR.getBytes(Charsets.US_ASCII);

  val DECRBY = "DECRBY";
  val DECRBY_BYTES = DECRBY.getBytes(Charsets.US_ASCII);

  val DEL = "DEL";
  val DEL_BYTES = DEL.getBytes(Charsets.US_ASCII);

  val DISCARD = "DISCARD";
  val DISCARD_BYTES = DISCARD.getBytes(Charsets.US_ASCII);

  val ECHO = "ECHO";
  val ECHO_BYTES = ECHO.getBytes(Charsets.US_ASCII);

  val EXEC = "EXEC";
  val EXEC_BYTES = EXEC.getBytes(Charsets.US_ASCII);

  val EXISTS = "EXISTS";
  val EXISTS_BYTES = EXISTS.getBytes(Charsets.US_ASCII);

  val EXPIRE = "EXPIRE";
  val EXPIRE_BYTES = EXPIRE.getBytes(Charsets.US_ASCII);

  val EXPIREAT = "EXPIREAT";
  val EXPIREAT_BYTES = EXPIREAT.getBytes(Charsets.US_ASCII);

  val FLUSHALL = "FLUSHALL";
  val FLUSHALL_BYTES = FLUSHALL.getBytes(Charsets.US_ASCII);

  val FLUSHDB = "FLUSHDB";
  val FLUSHDB_BYTES = FLUSHDB.getBytes(Charsets.US_ASCII);

  val GET = "GET";
  val GET_BYTES = GET.getBytes(Charsets.US_ASCII);

  val GETBIT = "GETBIT";
  val GETBIT_BYTES = GETBIT.getBytes(Charsets.US_ASCII);

  val GETRANGE = "GETRANGE";
  val GETRANGE_BYTES = GETRANGE.getBytes(Charsets.US_ASCII);

  val GETSET = "GETSET";
  val GETSET_BYTES = GETSET.getBytes(Charsets.US_ASCII);

  val HDEL = "HDEL";
  val HDEL_BYTES = HDEL.getBytes(Charsets.US_ASCII);

  val HEXISTS = "HEXISTS";
  val HEXISTS_BYTES = HEXISTS.getBytes(Charsets.US_ASCII);

  val HGET = "HGET";
  val HGET_BYTES = HGET.getBytes(Charsets.US_ASCII);

  val HGETALL = "HGETALL";
  val HGETALL_BYTES = HGETALL.getBytes(Charsets.US_ASCII);

  val HINCRBY = "HINCRBY";
  val HINCRBY_BYTES = HINCRBY.getBytes(Charsets.US_ASCII);

  val HKEYS = "HKEYS";
  val HKEYS_BYTES = HKEYS.getBytes(Charsets.US_ASCII);

  val HLEN = "HLEN";
  val HLEN_BYTES = HLEN.getBytes(Charsets.US_ASCII);

  val HMGET = "HMGET";
  val HMGET_BYTES = HMGET.getBytes(Charsets.US_ASCII);

  val HMSET = "HMSET";
  val HMSET_BYTES = HMSET.getBytes(Charsets.US_ASCII);

  val HSET = "HSET";
  val HSET_BYTES = HSET.getBytes(Charsets.US_ASCII);

  val HSETNX = "HSETNX";
  val HSETNX_BYTES = HSETNX.getBytes(Charsets.US_ASCII);

  val HVALS = "HVALS";
  val HVALS_BYTES = HVALS.getBytes(Charsets.US_ASCII);

  val INCR = "INCR";
  val INCR_BYTES = INCR.getBytes(Charsets.US_ASCII);

  val INCRBY = "INCRBY";
  val INCRBY_BYTES = INCRBY.getBytes(Charsets.US_ASCII);

  val INFO = "INFO";
  val INFO_BYTES = INFO.getBytes(Charsets.US_ASCII);

  val KEYS = "KEYS";
  val KEYS_BYTES = KEYS.getBytes(Charsets.US_ASCII);

  val LASTSAVE = "LASTSAVE";
  val LASTSAVE_BYTES = LASTSAVE.getBytes(Charsets.US_ASCII);

  val LINDEX = "LINDEX";
  val LINDEX_BYTES = LINDEX.getBytes(Charsets.US_ASCII);

  val LINSERT = "LINSERT";
  val LINSERT_BYTES = LINSERT.getBytes(Charsets.US_ASCII);

  val LLEN = "LLEN";
  val LLEN_BYTES = LLEN.getBytes(Charsets.US_ASCII);

  val LPOP = "LPOP";
  val LPOP_BYTES = LPOP.getBytes(Charsets.US_ASCII);

  val LPUSH = "LPUSH";
  val LPUSH_BYTES = LPUSH.getBytes(Charsets.US_ASCII);

  val LPUSHX = "LPUSHX";
  val LPUSHX_BYTES = LPUSHX.getBytes(Charsets.US_ASCII);

  val LRANGE = "LRANGE";
  val LRANGE_BYTES = LRANGE.getBytes(Charsets.US_ASCII);

  val LREM = "LREM";
  val LREM_BYTES = LREM.getBytes(Charsets.US_ASCII);

  val LSET = "LSET";
  val LSET_BYTES = LSET.getBytes(Charsets.US_ASCII);

  val LTRIM = "LTRIM";
  val LTRIM_BYTES = LTRIM.getBytes(Charsets.US_ASCII);

  val MGET = "MGET";
  val MGET_BYTES = MGET.getBytes(Charsets.US_ASCII);

  val MONITOR = "MONITOR";
  val MONITOR_BYTES = MONITOR.getBytes(Charsets.US_ASCII);

  val MOVE = "MOVE";
  val MOVE_BYTES = MOVE.getBytes(Charsets.US_ASCII);

  val MSET = "MSET";
  val MSET_BYTES = MSET.getBytes(Charsets.US_ASCII);

  val MSETNX = "MSETNX";
  val MSETNX_BYTES = MSETNX.getBytes(Charsets.US_ASCII);

  val MULTI = "MULTI";
  val MULTI_BYTES = MULTI.getBytes(Charsets.US_ASCII);

  val OBJECT = "OBJECT";
  val OBJECT_BYTES = OBJECT.getBytes(Charsets.US_ASCII);

  val PERSIST = "PERSIST";
  val PERSIST_BYTES = PERSIST.getBytes(Charsets.US_ASCII);

  val PING = "PING";
  val PING_BYTES = PING.getBytes(Charsets.US_ASCII);

  val PSUBSCRIBE = "PSUBSCRIBE";
  val PSUBSCRIBE_BYTES = PSUBSCRIBE.getBytes(Charsets.US_ASCII);

  val PUBLISH = "PUBLISH";
  val PUBLISH_BYTES = PUBLISH.getBytes(Charsets.US_ASCII);

  val PUNSUBSCRIBE = "PUNSUBSCRIBE";
  val PUNSUBSCRIBE_BYTES = PUNSUBSCRIBE.getBytes(Charsets.US_ASCII);

  val QUIT = "QUIT";
  val QUIT_BYTES = QUIT.getBytes(Charsets.US_ASCII);

  val RANDOMKEY = "RANDOMKEY";
  val RANDOMKEY_BYTES = RANDOMKEY.getBytes(Charsets.US_ASCII);

  val RENAME = "RENAME";
  val RENAME_BYTES = RENAME.getBytes(Charsets.US_ASCII);

  val RENAMENX = "RENAMENX";
  val RENAMENX_BYTES = RENAMENX.getBytes(Charsets.US_ASCII);

  val RPOP = "RPOP";
  val RPOP_BYTES = RPOP.getBytes(Charsets.US_ASCII);

  val RPOPLPUSH = "RPOPLPUSH";
  val RPOPLPUSH_BYTES = RPOPLPUSH.getBytes(Charsets.US_ASCII);

  val RPUSH = "RPUSH";
  val RPUSH_BYTES = RPUSH.getBytes(Charsets.US_ASCII);

  val RPUSHX = "RPUSHX";
  val RPUSHX_BYTES = RPUSHX.getBytes(Charsets.US_ASCII);

  val SADD = "SADD";
  val SADD_BYTES = SADD.getBytes(Charsets.US_ASCII);

  val SAVE = "SAVE";
  val SAVE_BYTES = SAVE.getBytes(Charsets.US_ASCII);

  val SCARD = "SCARD";
  val SCARD_BYTES = SCARD.getBytes(Charsets.US_ASCII);

  val SDIFF = "SDIFF";
  val SDIFF_BYTES = SDIFF.getBytes(Charsets.US_ASCII);

  val SDIFFSTORE = "SDIFFSTORE";
  val SDIFFSTORE_BYTES = SDIFFSTORE.getBytes(Charsets.US_ASCII);

  val SELECT = "SELECT";
  val SELECT_BYTES = SELECT.getBytes(Charsets.US_ASCII);

  val SET = "SET";
  val SET_BYTES = SET.getBytes(Charsets.US_ASCII);

  val SETBIT = "SETBIT";
  val SETBIT_BYTES = SETBIT.getBytes(Charsets.US_ASCII);

  val SETEX = "SETEX";
  val SETEX_BYTES = SETEX.getBytes(Charsets.US_ASCII);

  val SETNX = "SETNX";
  val SETNX_BYTES = SETNX.getBytes(Charsets.US_ASCII);

  val SETRANGE = "SETRANGE";
  val SETRANGE_BYTES = SETRANGE.getBytes(Charsets.US_ASCII);

  val SHUTDOWN = "SHUTDOWN";
  val SHUTDOWN_BYTES = SHUTDOWN.getBytes(Charsets.US_ASCII);

  val SINTER = "SINTER";
  val SINTER_BYTES = SINTER.getBytes(Charsets.US_ASCII);

  val SINTERSTORE = "SINTERSTORE";
  val SINTERSTORE_BYTES = SINTERSTORE.getBytes(Charsets.US_ASCII);

  val SISMEMBER = "SISMEMBER";
  val SISMEMBER_BYTES = SISMEMBER.getBytes(Charsets.US_ASCII);

  val SLAVEOF = "SLAVEOF";
  val SLAVEOF_BYTES = SLAVEOF.getBytes(Charsets.US_ASCII);

  val SLOWLOG = "SLOWLOG";
  val SLOWLOG_BYTES = SLOWLOG.getBytes(Charsets.US_ASCII);

  val SMEMBERS = "SMEMBERS";
  val SMEMBERS_BYTES = SMEMBERS.getBytes(Charsets.US_ASCII);

  val SMOVE = "SMOVE";
  val SMOVE_BYTES = SMOVE.getBytes(Charsets.US_ASCII);

  val SORT = "SORT";
  val SORT_BYTES = SORT.getBytes(Charsets.US_ASCII);

  val SPOP = "SPOP";
  val SPOP_BYTES = SPOP.getBytes(Charsets.US_ASCII);

  val SRANDMEMBER = "SRANDMEMBER";
  val SRANDMEMBER_BYTES = SRANDMEMBER.getBytes(Charsets.US_ASCII);

  val SREM = "SREM";
  val SREM_BYTES = SREM.getBytes(Charsets.US_ASCII);

  val STRLEN = "STRLEN";
  val STRLEN_BYTES = STRLEN.getBytes(Charsets.US_ASCII);

  val SUBSCRIBE = "SUBSCRIBE";
  val SUBSCRIBE_BYTES = SUBSCRIBE.getBytes(Charsets.US_ASCII);

  val SUNION = "SUNION";
  val SUNION_BYTES = SUNION.getBytes(Charsets.US_ASCII);

  val SUNIONSTORE = "SUNIONSTORE";
  val SUNIONSTORE_BYTES = SUNIONSTORE.getBytes(Charsets.US_ASCII);

  val SYNC = "SYNC";
  val SYNC_BYTES = SYNC.getBytes(Charsets.US_ASCII);

  val TTL = "TTL";
  val TTL_BYTES = TTL.getBytes(Charsets.US_ASCII);

  val TYPE = "TYPE";
  val TYPE_BYTES = TYPE.getBytes(Charsets.US_ASCII);

  val UNSUBSCRIBE = "UNSUBSCRIBE";
  val UNSUBSCRIBE_BYTES = UNSUBSCRIBE.getBytes(Charsets.US_ASCII);

  val UNWATCH = "UNWATCH";
  val UNWATCH_BYTES = UNWATCH.getBytes(Charsets.US_ASCII);

  val WATCH = "WATCH";
  val WATCH_BYTES = WATCH.getBytes(Charsets.US_ASCII);

  val ZADD = "ZADD";
  val ZADD_BYTES = ZADD.getBytes(Charsets.US_ASCII);

  val ZCARD = "ZCARD";
  val ZCARD_BYTES = ZCARD.getBytes(Charsets.US_ASCII);

  val ZCOUNT = "ZCOUNT";
  val ZCOUNT_BYTES = ZCOUNT.getBytes(Charsets.US_ASCII);

  val ZINCRBY = "ZINCRBY";
  val ZINCRBY_BYTES = ZINCRBY.getBytes(Charsets.US_ASCII);

  val ZINTERSTORE = "ZINTERSTORE";
  val ZINTERSTORE_BYTES = ZINTERSTORE.getBytes(Charsets.US_ASCII);

  val ZRANGE = "ZRANGE";
  val ZRANGE_BYTES = ZRANGE.getBytes(Charsets.US_ASCII);

  val ZRANGEBYSCORE = "ZRANGEBYSCORE";
  val ZRANGEBYSCORE_BYTES = ZRANGEBYSCORE.getBytes(Charsets.US_ASCII);

  val ZRANK = "ZRANK";
  val ZRANK_BYTES = ZRANK.getBytes(Charsets.US_ASCII);

  val ZREM = "ZREM";
  val ZREM_BYTES = ZREM.getBytes(Charsets.US_ASCII);

  val ZREMRANGEBYRANK = "ZREMRANGEBYRANK";
  val ZREMRANGEBYRANK_BYTES = ZREMRANGEBYRANK.getBytes(Charsets.US_ASCII);

  val ZREMRANGEBYSCORE = "ZREMRANGEBYSCORE";
  val ZREMRANGEBYSCORE_BYTES = ZREMRANGEBYSCORE.getBytes(Charsets.US_ASCII);

  val ZREVRANGE = "ZREVRANGE";
  val ZREVRANGE_BYTES = ZREVRANGE.getBytes(Charsets.US_ASCII);

  val ZREVRANGEBYSCORE = "ZREVRANGEBYSCORE";
  val ZREVRANGEBYSCORE_BYTES = ZREVRANGEBYSCORE.getBytes(Charsets.US_ASCII);

  val ZREVRANK = "ZREVRANK";
  val ZREVRANK_BYTES = ZREVRANK.getBytes(Charsets.US_ASCII);

  val ZSCORE = "ZSCORE";
  val ZSCORE_BYTES = ZSCORE.getBytes(Charsets.US_ASCII);

  val ZUNIONSTORE = "ZUNIONSTORE";
  val ZUNIONSTORE_BYTES = ZUNIONSTORE.getBytes(Charsets.US_ASCII);

  val EVAL = "EVAL";
  val EVAL_BYTES = EVAL.getBytes(Charsets.US_ASCII);


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
  def apply(service: Service[Command, Reply]): RedisClient = new RedisClient(service)

}

class RedisException(message:String) extends RuntimeException(message) {
}

class RedisClient(service: Service[Command, Reply]) {

  // Append a value to a key
  def append(key0: Object, value1: Object) = {
    service(new Command(RedisClient.APPEND_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Authenticate to the server
  def auth(password0: Object) = {
    service(new Command(RedisClient.AUTH_BYTES, password0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Asynchronously rewrite the append-only file
  def bgrewriteaof() = {
    service(new Command(RedisClient.BGREWRITEAOF_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Asynchronously save the dataset to disk
  def bgsave() = {
    service(new Command(RedisClient.BGSAVE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Remove and get the first element in a list, or block until one is available
  def blpop(key0: Object*) = {
    service(new Command(RedisClient.BLPOP_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Remove and get the last element in a list, or block until one is available
  def brpop(key0: Object*) = {
    service(new Command(RedisClient.BRPOP_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Pop a value from a list, push it to another list and return it; or block until one is available
  def brpoplpush(source0: Object, destination1: Object, timeout2: Object) = {
    service(new Command(RedisClient.BRPOPLPUSH_BYTES, source0, destination1, timeout2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Get the value of a configuration parameter
  def config_get(parameter0: Object) = {
    service(new Command(RedisClient.CONFIG_GET_BYTES, parameter0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Set a configuration parameter to the given value
  def config_set(parameter0: Object, value1: Object) = {
    service(new Command(RedisClient.CONFIG_SET_BYTES, parameter0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Reset the stats returned by INFO
  def config_resetstat() = {
    service(new Command(RedisClient.CONFIG_RESETSTAT_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Return the number of keys in the selected database
  def dbsize() = {
    service(new Command(RedisClient.DBSIZE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get debugging information about a key
  def debug_object(key0: Object) = {
    service(new Command(RedisClient.DEBUG_OBJECT_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Make the server crash
  def debug_segfault() = {
    service(new Command(RedisClient.DEBUG_SEGFAULT_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Decrement the integer value of a key by one
  def decr(key0: Object) = {
    service(new Command(RedisClient.DECR_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Decrement the integer value of a key by the given number
  def decrby(key0: Object, decrement1: Object) = {
    service(new Command(RedisClient.DECRBY_BYTES, key0, decrement1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Delete a key
  def del(key0: Object*) = {
    service(new Command(RedisClient.DEL_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Discard all commands issued after MULTI
  def discard() = {
    service(new Command(RedisClient.DISCARD_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Echo the given string
  def echo(message0: Object) = {
    service(new Command(RedisClient.ECHO_BYTES, message0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Execute all commands issued after MULTI
  def exec() = {
    service(new Command(RedisClient.EXEC_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Determine if a key exists
  def exists(key0: Object) = {
    service(new Command(RedisClient.EXISTS_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Set a key's time to live in seconds
  def expire(key0: Object, seconds1: Object) = {
    service(new Command(RedisClient.EXPIRE_BYTES, key0, seconds1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Set the expiration for a key as a UNIX timestamp
  def expireat(key0: Object, timestamp1: Object) = {
    service(new Command(RedisClient.EXPIREAT_BYTES, key0, timestamp1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove all keys from all databases
  def flushall() = {
    service(new Command(RedisClient.FLUSHALL_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Remove all keys from the current database
  def flushdb() = {
    service(new Command(RedisClient.FLUSHDB_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Get the value of a key
  def get(key0: Object) = {
    service(new Command(RedisClient.GET_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Returns the bit value at offset in the string value stored at key
  def getbit(key0: Object, offset1: Object) = {
    service(new Command(RedisClient.GETBIT_BYTES, key0, offset1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get a substring of the string stored at a key
  def getrange(key0: Object, start1: Object, end2: Object) = {
    service(new Command(RedisClient.GETRANGE_BYTES, key0, start1, end2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Set the string value of a key and return its old value
  def getset(key0: Object, value1: Object) = {
    service(new Command(RedisClient.GETSET_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Delete one or more hash fields
  def hdel(key0: Object, field1: Object*) = {
    service(new Command(RedisClient.HDEL_BYTES, key0, field1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Determine if a hash field exists
  def hexists(key0: Object, field1: Object) = {
    service(new Command(RedisClient.HEXISTS_BYTES, key0, field1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get the value of a hash field
  def hget(key0: Object, field1: Object) = {
    service(new Command(RedisClient.HGET_BYTES, key0, field1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Get all the fields and values in a hash
  def hgetall(key0: Object) = {
    service(new Command(RedisClient.HGETALL_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Increment the integer value of a hash field by the given number
  def hincrby(key0: Object, field1: Object, increment2: Object) = {
    service(new Command(RedisClient.HINCRBY_BYTES, key0, field1, increment2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get all the fields in a hash
  def hkeys(key0: Object) = {
    service(new Command(RedisClient.HKEYS_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Get the number of fields in a hash
  def hlen(key0: Object) = {
    service(new Command(RedisClient.HLEN_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get the values of all the given hash fields
  def hmget(key0: Object, field1: Object*) = {
    service(new Command(RedisClient.HMGET_BYTES, key0, field1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Set multiple hash fields to multiple values
  def hmset(key0: Object, field_or_value1: Object*) = {
    service(new Command(RedisClient.HMSET_BYTES, key0, field_or_value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Set the string value of a hash field
  def hset(key0: Object, field1: Object, value2: Object) = {
    service(new Command(RedisClient.HSET_BYTES, key0, field1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Set the value of a hash field, only if the field does not exist
  def hsetnx(key0: Object, field1: Object, value2: Object) = {
    service(new Command(RedisClient.HSETNX_BYTES, key0, field1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get all the values in a hash
  def hvals(key0: Object) = {
    service(new Command(RedisClient.HVALS_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Increment the integer value of a key by one
  def incr(key0: Object) = {
    service(new Command(RedisClient.INCR_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Increment the integer value of a key by the given number
  def incrby(key0: Object, increment1: Object) = {
    service(new Command(RedisClient.INCRBY_BYTES, key0, increment1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get information and statistics about the server
  def info() = {
    service(new Command(RedisClient.INFO_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Find all keys matching the given pattern
  def keys(pattern0: Object) = {
    service(new Command(RedisClient.KEYS_BYTES, pattern0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Get the UNIX time stamp of the last successful save to disk
  def lastsave() = {
    service(new Command(RedisClient.LASTSAVE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get an element from a list by its index
  def lindex(key0: Object, index1: Object) = {
    service(new Command(RedisClient.LINDEX_BYTES, key0, index1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Insert an element before or after another element in a list
  def linsert(key0: Object, where1: Object, pivot2: Object, value3: Object) = {
    service(new Command(RedisClient.LINSERT_BYTES, key0, where1, pivot2, value3)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get the length of a list
  def llen(key0: Object) = {
    service(new Command(RedisClient.LLEN_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove and get the first element in a list
  def lpop(key0: Object) = {
    service(new Command(RedisClient.LPOP_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Prepend one or multiple values to a list
  def lpush(key0: Object, value1: Object*) = {
    service(new Command(RedisClient.LPUSH_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Prepend a value to a list, only if the list exists
  def lpushx(key0: Object, value1: Object) = {
    service(new Command(RedisClient.LPUSHX_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get a range of elements from a list
  def lrange(key0: Object, start1: Object, stop2: Object) = {
    service(new Command(RedisClient.LRANGE_BYTES, key0, start1, stop2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Remove elements from a list
  def lrem(key0: Object, count1: Object, value2: Object) = {
    service(new Command(RedisClient.LREM_BYTES, key0, count1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Set the value of an element in a list by its index
  def lset(key0: Object, index1: Object, value2: Object) = {
    service(new Command(RedisClient.LSET_BYTES, key0, index1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Trim a list to the specified range
  def ltrim(key0: Object, start1: Object, stop2: Object) = {
    service(new Command(RedisClient.LTRIM_BYTES, key0, start1, stop2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Get the values of all the given keys
  def mget(key0: Object*) = {
    service(new Command(RedisClient.MGET_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Listen for all requests received by the server in real time
  def monitor() = {
    service(new Command(RedisClient.MONITOR_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Move a key to another database
  def move(key0: Object, db1: Object) = {
    service(new Command(RedisClient.MOVE_BYTES, key0, db1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Set multiple keys to multiple values
  def mset(key_or_value0: Object*) = {
    service(new Command(RedisClient.MSET_BYTES, key_or_value0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Set multiple keys to multiple values, only if none of the keys exist
  def msetnx(key_or_value0: Object*) = {
    service(new Command(RedisClient.MSETNX_BYTES, key_or_value0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Mark the start of a transaction block
  def multi() = {
    service(new Command(RedisClient.MULTI_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Inspect the internals of Redis objects
  def `object`(subcommand0: Object, arguments1: Object*) = {
    service(new Command(RedisClient.OBJECT_BYTES, subcommand0, arguments1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Remove the expiration from a key
  def persist(key0: Object) = {
    service(new Command(RedisClient.PERSIST_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Ping the server
  def ping() = {
    service(new Command(RedisClient.PING_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Listen for messages published to channels matching the given patterns
  def psubscribe(pattern0: Object*) = {
    service(new Command(RedisClient.PSUBSCRIBE_BYTES, pattern0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Post a message to a channel
  def publish(channel0: Object, message1: Object) = {
    service(new Command(RedisClient.PUBLISH_BYTES, channel0, message1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Stop listening for messages posted to channels matching the given patterns
  def punsubscribe(pattern0: Object*) = {
    service(new Command(RedisClient.PUNSUBSCRIBE_BYTES, pattern0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Close the connection
  def quit() = {
    service(new Command(RedisClient.QUIT_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Return a random key from the keyspace
  def randomkey() = {
    service(new Command(RedisClient.RANDOMKEY_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Rename a key
  def rename(key0: Object, newkey1: Object) = {
    service(new Command(RedisClient.RENAME_BYTES, key0, newkey1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Rename a key, only if the new key does not exist
  def renamenx(key0: Object, newkey1: Object) = {
    service(new Command(RedisClient.RENAMENX_BYTES, key0, newkey1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove and get the last element in a list
  def rpop(key0: Object) = {
    service(new Command(RedisClient.RPOP_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Remove the last element in a list, append it to another list and return it
  def rpoplpush(source0: Object, destination1: Object) = {
    service(new Command(RedisClient.RPOPLPUSH_BYTES, source0, destination1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Append one or multiple values to a list
  def rpush(key0: Object, value1: Object*) = {
    service(new Command(RedisClient.RPUSH_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Append a value to a list, only if the list exists
  def rpushx(key0: Object, value1: Object) = {
    service(new Command(RedisClient.RPUSHX_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Add one or more members to a set
  def sadd(key0: Object, member1: Object*) = {
    service(new Command(RedisClient.SADD_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Synchronously save the dataset to disk
  def save() = {
    service(new Command(RedisClient.SAVE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Get the number of members in a set
  def scard(key0: Object) = {
    service(new Command(RedisClient.SCARD_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Subtract multiple sets
  def sdiff(key0: Object*) = {
    service(new Command(RedisClient.SDIFF_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Subtract multiple sets and store the resulting set in a key
  def sdiffstore(destination0: Object, key1: Object*) = {
    service(new Command(RedisClient.SDIFFSTORE_BYTES, destination0, key1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Change the selected database for the current connection
  def select(index0: Object) = {
    service(new Command(RedisClient.SELECT_BYTES, index0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Set the string value of a key
  def set(key0: Object, value1: Object) = {
    service(new Command(RedisClient.SET_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Sets or clears the bit at offset in the string value stored at key
  def setbit(key0: Object, offset1: Object, value2: Object) = {
    service(new Command(RedisClient.SETBIT_BYTES, key0, offset1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Set the value and expiration of a key
  def setex(key0: Object, seconds1: Object, value2: Object) = {
    service(new Command(RedisClient.SETEX_BYTES, key0, seconds1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Set the value of a key, only if the key does not exist
  def setnx(key0: Object, value1: Object) = {
    service(new Command(RedisClient.SETNX_BYTES, key0, value1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Overwrite part of a string at key starting at the specified offset
  def setrange(key0: Object, offset1: Object, value2: Object) = {
    service(new Command(RedisClient.SETRANGE_BYTES, key0, offset1, value2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Synchronously save the dataset to disk and then shut down the server
  def shutdown() = {
    service(new Command(RedisClient.SHUTDOWN_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Intersect multiple sets
  def sinter(key0: Object*) = {
    service(new Command(RedisClient.SINTER_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Intersect multiple sets and store the resulting set in a key
  def sinterstore(destination0: Object, key1: Object*) = {
    service(new Command(RedisClient.SINTERSTORE_BYTES, destination0, key1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Determine if a given value is a member of a set
  def sismember(key0: Object, member1: Object) = {
    service(new Command(RedisClient.SISMEMBER_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Make the server a slave of another instance, or promote it as master
  def slaveof(host0: Object, port1: Object) = {
    service(new Command(RedisClient.SLAVEOF_BYTES, host0, port1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Manages the Redis slow queries log
  def slowlog(subcommand0: Object, argument1: Object) = {
    service(new Command(RedisClient.SLOWLOG_BYTES, subcommand0, argument1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Get all the members in a set
  def smembers(key0: Object) = {
    service(new Command(RedisClient.SMEMBERS_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Move a member from one set to another
  def smove(source0: Object, destination1: Object, member2: Object) = {
    service(new Command(RedisClient.SMOVE_BYTES, source0, destination1, member2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Sort the elements in a list, set or sorted set
  def sort(key0: Object, pattern1: Object, offset_or_count2: Object, pattern3: Object*) = {
    service(new Command(RedisClient.SORT_BYTES, key0, pattern1, offset_or_count2, pattern3)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Remove and return a random member from a set
  def spop(key0: Object) = {
    service(new Command(RedisClient.SPOP_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Get a random member from a set
  def srandmember(key0: Object) = {
    service(new Command(RedisClient.SRANDMEMBER_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Remove one or more members from a set
  def srem(key0: Object, member1: Object*) = {
    service(new Command(RedisClient.SREM_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get the length of the value stored in a key
  def strlen(key0: Object) = {
    service(new Command(RedisClient.STRLEN_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Listen for messages published to the given channels
  def subscribe(channel0: Object*) = {
    service(new Command(RedisClient.SUBSCRIBE_BYTES, channel0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Add multiple sets
  def sunion(key0: Object*) = {
    service(new Command(RedisClient.SUNION_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Add multiple sets and store the resulting set in a key
  def sunionstore(destination0: Object, key1: Object*) = {
    service(new Command(RedisClient.SUNIONSTORE_BYTES, destination0, key1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Internal command used for replication
  def sync() = {
    service(new Command(RedisClient.SYNC_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Get the time to live for a key
  def ttl(key0: Object) = {
    service(new Command(RedisClient.TTL_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Determine the type stored at key
  def `type`(key0: Object) = {
    service(new Command(RedisClient.TYPE_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Stop listening for messages posted to the given channels
  def unsubscribe(channel0: Object*) = {
    service(new Command(RedisClient.UNSUBSCRIBE_BYTES, channel0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }

  // Forget about all watched keys
  def unwatch() = {
    service(new Command(RedisClient.UNWATCH_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Watch the given keys to determine execution of the MULTI/EXEC block
  def watch(key0: Object*) = {
    service(new Command(RedisClient.WATCH_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: StatusReply => reply
      }
    }
  }

  // Add one or more members to a sorted set, or update its score if it already exists
  def zadd(key0: Object, score1: Object, member2: Object, score3: Object, member4: Object) = {
    service(new Command(RedisClient.ZADD_BYTES, key0, score1, member2, score3, member4)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get the number of members in a sorted set
  def zcard(key0: Object) = {
    service(new Command(RedisClient.ZCARD_BYTES, key0)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Count the members in a sorted set with scores within the given values
  def zcount(key0: Object, min1: Object, max2: Object) = {
    service(new Command(RedisClient.ZCOUNT_BYTES, key0, min1, max2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Increment the score of a member in a sorted set
  def zincrby(key0: Object, increment1: Object, member2: Object) = {
    service(new Command(RedisClient.ZINCRBY_BYTES, key0, increment1, member2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Intersect multiple sorted sets and store the resulting sorted set in a new key
  def zinterstore(destination0: Object, numkeys1: Object, key2: Object*) = {
    service(new Command(RedisClient.ZINTERSTORE_BYTES, destination0, numkeys1, key2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Return a range of members in a sorted set, by index
  def zrange(key0: Object, start1: Object, stop2: Object, withscores3: Object) = {
    service(new Command(RedisClient.ZRANGE_BYTES, key0, start1, stop2, withscores3)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Return a range of members in a sorted set, by score
  def zrangebyscore(key0: Object, min1: Object, max2: Object, withscores3: Object, offset_or_count4: Object) = {
    service(new Command(RedisClient.ZRANGEBYSCORE_BYTES, key0, min1, max2, withscores3, offset_or_count4)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Determine the index of a member in a sorted set
  def zrank(key0: Object, member1: Object) = {
    service(new Command(RedisClient.ZRANK_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove one or more members from a sorted set
  def zrem(key0: Object, member1: Object*) = {
    service(new Command(RedisClient.ZREM_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove all members in a sorted set within the given indexes
  def zremrangebyrank(key0: Object, start1: Object, stop2: Object) = {
    service(new Command(RedisClient.ZREMRANGEBYRANK_BYTES, key0, start1, stop2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove all members in a sorted set within the given scores
  def zremrangebyscore(key0: Object, min1: Object, max2: Object) = {
    service(new Command(RedisClient.ZREMRANGEBYSCORE_BYTES, key0, min1, max2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Return a range of members in a sorted set, by index, with scores ordered from high to low
  def zrevrange(key0: Object, start1: Object, stop2: Object, withscores3: Object) = {
    service(new Command(RedisClient.ZREVRANGE_BYTES, key0, start1, stop2, withscores3)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Return a range of members in a sorted set, by score, with scores ordered from high to low
  def zrevrangebyscore(key0: Object, max1: Object, min2: Object, withscores3: Object, offset_or_count4: Object) = {
    service(new Command(RedisClient.ZREVRANGEBYSCORE_BYTES, key0, max1, min2, withscores3, offset_or_count4)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Determine the index of a member in a sorted set, with scores ordered from high to low
  def zrevrank(key0: Object, member1: Object) = {
    service(new Command(RedisClient.ZREVRANK_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Get the score associated with the given member in a sorted set
  def zscore(key0: Object, member1: Object) = {
    service(new Command(RedisClient.ZSCORE_BYTES, key0, member1)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: BulkReply => reply
      }
    }
  }

  // Add multiple sorted sets and store the resulting sorted set in a new key
  def zunionstore(destination0: Object, numkeys1: Object, key2: Object*) = {
    service(new Command(RedisClient.ZUNIONSTORE_BYTES, destination0, numkeys1, key2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: IntegerReply => reply
      }
    }
  }

  // Execute a Lua script server side
  def eval(script0: Object, numkeys1: Object, key2: Object*) = {
    service(new Command(RedisClient.EVAL_BYTES, script0, numkeys1, key2)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error.toString(Charsets.US_ASCII))
        case reply: Reply => reply
      }
    }
  }
}
