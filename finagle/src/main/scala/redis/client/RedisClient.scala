package redis.client

import com.google.common.base.Charsets

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder

import redis.finagle._
import redis.netty._

object RedisClient {

  val APPEND = "APPEND";
  val APPEND_BYTES = APPEND.getBytes(Charsets.US_ASCII);

  val AUTH = "AUTH";
  val AUTH_BYTES = AUTH.getBytes(Charsets.US_ASCII);

  val BGREWRITEAOF = "BGREWRITEAOF";
  val BGREWRITEAOF_BYTES = BGREWRITEAOF.getBytes(Charsets.US_ASCII);

  val BGSAVE = "BGSAVE";
  val BGSAVE_BYTES = BGSAVE.getBytes(Charsets.US_ASCII);

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

  val LLEN = "LLEN";
  val LLEN_BYTES = LLEN.getBytes(Charsets.US_ASCII);

  val LPOP = "LPOP";
  val LPOP_BYTES = LPOP.getBytes(Charsets.US_ASCII);

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

  val MONITOR = "MONITOR";
  val MONITOR_BYTES = MONITOR.getBytes(Charsets.US_ASCII);

  val MOVE = "MOVE";
  val MOVE_BYTES = MOVE.getBytes(Charsets.US_ASCII);

  val MULTI = "MULTI";
  val MULTI_BYTES = MULTI.getBytes(Charsets.US_ASCII);

  val PERSIST = "PERSIST";
  val PERSIST_BYTES = PERSIST.getBytes(Charsets.US_ASCII);

  val PING = "PING";
  val PING_BYTES = PING.getBytes(Charsets.US_ASCII);

  val PUBLISH = "PUBLISH";
  val PUBLISH_BYTES = PUBLISH.getBytes(Charsets.US_ASCII);

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

  val RPUSHX = "RPUSHX";
  val RPUSHX_BYTES = RPUSHX.getBytes(Charsets.US_ASCII);

  val SAVE = "SAVE";
  val SAVE_BYTES = SAVE.getBytes(Charsets.US_ASCII);

  val SCARD = "SCARD";
  val SCARD_BYTES = SCARD.getBytes(Charsets.US_ASCII);

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

  val SISMEMBER = "SISMEMBER";
  val SISMEMBER_BYTES = SISMEMBER.getBytes(Charsets.US_ASCII);

  val SLAVEOF = "SLAVEOF";
  val SLAVEOF_BYTES = SLAVEOF.getBytes(Charsets.US_ASCII);

  val SMEMBERS = "SMEMBERS";
  val SMEMBERS_BYTES = SMEMBERS.getBytes(Charsets.US_ASCII);

  val SMOVE = "SMOVE";
  val SMOVE_BYTES = SMOVE.getBytes(Charsets.US_ASCII);

  val SPOP = "SPOP";
  val SPOP_BYTES = SPOP.getBytes(Charsets.US_ASCII);

  val SRANDMEMBER = "SRANDMEMBER";
  val SRANDMEMBER_BYTES = SRANDMEMBER.getBytes(Charsets.US_ASCII);

  val STRLEN = "STRLEN";
  val STRLEN_BYTES = STRLEN.getBytes(Charsets.US_ASCII);

  val SYNC = "SYNC";
  val SYNC_BYTES = SYNC.getBytes(Charsets.US_ASCII);

  val TTL = "TTL";
  val TTL_BYTES = TTL.getBytes(Charsets.US_ASCII);

  val TYPE = "TYPE";
  val TYPE_BYTES = TYPE.getBytes(Charsets.US_ASCII);

  val UNWATCH = "UNWATCH";
  val UNWATCH_BYTES = UNWATCH.getBytes(Charsets.US_ASCII);

  val ZCARD = "ZCARD";
  val ZCARD_BYTES = ZCARD.getBytes(Charsets.US_ASCII);

  val ZCOUNT = "ZCOUNT";
  val ZCOUNT_BYTES = ZCOUNT.getBytes(Charsets.US_ASCII);

  val ZINCRBY = "ZINCRBY";
  val ZINCRBY_BYTES = ZINCRBY.getBytes(Charsets.US_ASCII);

  val ZRANK = "ZRANK";
  val ZRANK_BYTES = ZRANK.getBytes(Charsets.US_ASCII);

  val ZREMRANGEBYRANK = "ZREMRANGEBYRANK";
  val ZREMRANGEBYRANK_BYTES = ZREMRANGEBYRANK.getBytes(Charsets.US_ASCII);

  val ZREMRANGEBYSCORE = "ZREMRANGEBYSCORE";
  val ZREMRANGEBYSCORE_BYTES = ZREMRANGEBYSCORE.getBytes(Charsets.US_ASCII);

  val ZREVRANK = "ZREVRANK";
  val ZREVRANK_BYTES = ZREVRANK.getBytes(Charsets.US_ASCII);

  val ZSCORE = "ZSCORE";
  val ZSCORE_BYTES = ZSCORE.getBytes(Charsets.US_ASCII);


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
  def append(key: Object, value: Object) = {
    service(new Command(RedisClient.APPEND_BYTES, key, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Authenticate to the server
  def auth(password: Object) = {
    service(new Command(RedisClient.AUTH_BYTES, password)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Asynchronously rewrite the append-only file
  def bgrewriteaof() = {
    service(new Command(RedisClient.BGREWRITEAOF_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Asynchronously save the dataset to disk
  def bgsave() = {
    service(new Command(RedisClient.BGSAVE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Pop a value from a list, push it to another list and return it; or block until one is available
  def brpoplpush(source: Object, destination: Object, timeout: Object) = {
    service(new Command(RedisClient.BRPOPLPUSH_BYTES, source, destination, timeout)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Get the value of a configuration parameter
  def config_get(parameter: Object) = {
    service(new Command(RedisClient.CONFIG_GET_BYTES, parameter)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: Reply => reply
      }
    }
  }

  // Set a configuration parameter to the given value
  def config_set(parameter: Object, value: Object) = {
    service(new Command(RedisClient.CONFIG_SET_BYTES, parameter, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: Reply => reply
      }
    }
  }

  // Reset the stats returned by INFO
  def config_resetstat() = {
    service(new Command(RedisClient.CONFIG_RESETSTAT_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: Reply => reply
      }
    }
  }

  // Return the number of keys in the selected database
  def dbsize() = {
    service(new Command(RedisClient.DBSIZE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Get debugging information about a key
  def debug_object(key: Object) = {
    service(new Command(RedisClient.DEBUG_OBJECT_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: Reply => reply
      }
    }
  }

  // Make the server crash
  def debug_segfault() = {
    service(new Command(RedisClient.DEBUG_SEGFAULT_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: Reply => reply
      }
    }
  }

  // Decrement the integer value of a key by one
  def decr(key: Object) = {
    service(new Command(RedisClient.DECR_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Decrement the integer value of a key by the given number
  def decrby(key: Object, decrement: Object) = {
    service(new Command(RedisClient.DECRBY_BYTES, key, decrement)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Discard all commands issued after MULTI
  def discard() = {
    service(new Command(RedisClient.DISCARD_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Echo the given string
  def echo(message: Object) = {
    service(new Command(RedisClient.ECHO_BYTES, message)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Execute all commands issued after MULTI
  def exec() = {
    service(new Command(RedisClient.EXEC_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Determine if a key exists
  def exists(key: Object) = {
    service(new Command(RedisClient.EXISTS_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Set a key's time to live in seconds
  def expire(key: Object, seconds: Object) = {
    service(new Command(RedisClient.EXPIRE_BYTES, key, seconds)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Set the expiration for a key as a UNIX timestamp
  def expireat(key: Object, timestamp: Object) = {
    service(new Command(RedisClient.EXPIREAT_BYTES, key, timestamp)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove all keys from all databases
  def flushall() = {
    service(new Command(RedisClient.FLUSHALL_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Remove all keys from the current database
  def flushdb() = {
    service(new Command(RedisClient.FLUSHDB_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Get the value of a key
  def get(key: Object) = {
    service(new Command(RedisClient.GET_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Returns the bit value at offset in the string value stored at key
  def getbit(key: Object, offset: Object) = {
    service(new Command(RedisClient.GETBIT_BYTES, key, offset)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Get a substring of the string stored at a key
  def getrange(key: Object, start: Object, end: Object) = {
    service(new Command(RedisClient.GETRANGE_BYTES, key, start, end)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Set the string value of a key and return its old value
  def getset(key: Object, value: Object) = {
    service(new Command(RedisClient.GETSET_BYTES, key, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Determine if a hash field exists
  def hexists(key: Object, field: Object) = {
    service(new Command(RedisClient.HEXISTS_BYTES, key, field)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Get the value of a hash field
  def hget(key: Object, field: Object) = {
    service(new Command(RedisClient.HGET_BYTES, key, field)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Get all the fields and values in a hash
  def hgetall(key: Object) = {
    service(new Command(RedisClient.HGETALL_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Increment the integer value of a hash field by the given number
  def hincrby(key: Object, field: Object, increment: Object) = {
    service(new Command(RedisClient.HINCRBY_BYTES, key, field, increment)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Get all the fields in a hash
  def hkeys(key: Object) = {
    service(new Command(RedisClient.HKEYS_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Get the number of fields in a hash
  def hlen(key: Object) = {
    service(new Command(RedisClient.HLEN_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Set the string value of a hash field
  def hset(key: Object, field: Object, value: Object) = {
    service(new Command(RedisClient.HSET_BYTES, key, field, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Set the value of a hash field, only if the field does not exist
  def hsetnx(key: Object, field: Object, value: Object) = {
    service(new Command(RedisClient.HSETNX_BYTES, key, field, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Get all the values in a hash
  def hvals(key: Object) = {
    service(new Command(RedisClient.HVALS_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Increment the integer value of a key by one
  def incr(key: Object) = {
    service(new Command(RedisClient.INCR_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Increment the integer value of a key by the given number
  def incrby(key: Object, increment: Object) = {
    service(new Command(RedisClient.INCRBY_BYTES, key, increment)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Get information and statistics about the server
  def info() = {
    service(new Command(RedisClient.INFO_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Find all keys matching the given pattern
  def keys(pattern: Object) = {
    service(new Command(RedisClient.KEYS_BYTES, pattern)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Get the UNIX time stamp of the last successful save to disk
  def lastsave() = {
    service(new Command(RedisClient.LASTSAVE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Get an element from a list by its index
  def lindex(key: Object, index: Object) = {
    service(new Command(RedisClient.LINDEX_BYTES, key, index)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Get the length of a list
  def llen(key: Object) = {
    service(new Command(RedisClient.LLEN_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove and get the first element in a list
  def lpop(key: Object) = {
    service(new Command(RedisClient.LPOP_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Prepend a value to a list, only if the list exists
  def lpushx(key: Object, value: Object) = {
    service(new Command(RedisClient.LPUSHX_BYTES, key, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Get a range of elements from a list
  def lrange(key: Object, start: Object, stop: Object) = {
    service(new Command(RedisClient.LRANGE_BYTES, key, start, stop)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Remove elements from a list
  def lrem(key: Object, count: Object, value: Object) = {
    service(new Command(RedisClient.LREM_BYTES, key, count, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Set the value of an element in a list by its index
  def lset(key: Object, index: Object, value: Object) = {
    service(new Command(RedisClient.LSET_BYTES, key, index, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Trim a list to the specified range
  def ltrim(key: Object, start: Object, stop: Object) = {
    service(new Command(RedisClient.LTRIM_BYTES, key, start, stop)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Listen for all requests received by the server in real time
  def monitor() = {
    service(new Command(RedisClient.MONITOR_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: Reply => reply
      }
    }
  }

  // Move a key to another database
  def move(key: Object, db: Object) = {
    service(new Command(RedisClient.MOVE_BYTES, key, db)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Mark the start of a transaction block
  def multi() = {
    service(new Command(RedisClient.MULTI_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Remove the expiration from a key
  def persist(key: Object) = {
    service(new Command(RedisClient.PERSIST_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Ping the server
  def ping() = {
    service(new Command(RedisClient.PING_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Post a message to a channel
  def publish(channel: Object, message: Object) = {
    service(new Command(RedisClient.PUBLISH_BYTES, channel, message)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Close the connection
  def quit() = {
    service(new Command(RedisClient.QUIT_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Return a random key from the keyspace
  def randomkey() = {
    service(new Command(RedisClient.RANDOMKEY_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Rename a key
  def rename(key: Object, newkey: Object) = {
    service(new Command(RedisClient.RENAME_BYTES, key, newkey)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Rename a key, only if the new key does not exist
  def renamenx(key: Object, newkey: Object) = {
    service(new Command(RedisClient.RENAMENX_BYTES, key, newkey)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove and get the last element in a list
  def rpop(key: Object) = {
    service(new Command(RedisClient.RPOP_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Remove the last element in a list, append it to another list and return it
  def rpoplpush(source: Object, destination: Object) = {
    service(new Command(RedisClient.RPOPLPUSH_BYTES, source, destination)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Append a value to a list, only if the list exists
  def rpushx(key: Object, value: Object) = {
    service(new Command(RedisClient.RPUSHX_BYTES, key, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Synchronously save the dataset to disk
  def save() = {
    service(new Command(RedisClient.SAVE_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: Reply => reply
      }
    }
  }

  // Get the number of members in a set
  def scard(key: Object) = {
    service(new Command(RedisClient.SCARD_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Change the selected database for the current connection
  def select(index: Object) = {
    service(new Command(RedisClient.SELECT_BYTES, index)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Set the string value of a key
  def set(key: Object, value: Object) = {
    service(new Command(RedisClient.SET_BYTES, key, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Sets or clears the bit at offset in the string value stored at key
  def setbit(key: Object, offset: Object, value: Object) = {
    service(new Command(RedisClient.SETBIT_BYTES, key, offset, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Set the value and expiration of a key
  def setex(key: Object, seconds: Object, value: Object) = {
    service(new Command(RedisClient.SETEX_BYTES, key, seconds, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Set the value of a key, only if the key does not exist
  def setnx(key: Object, value: Object) = {
    service(new Command(RedisClient.SETNX_BYTES, key, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Overwrite part of a string at key starting at the specified offset
  def setrange(key: Object, offset: Object, value: Object) = {
    service(new Command(RedisClient.SETRANGE_BYTES, key, offset, value)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Synchronously save the dataset to disk and then shut down the server
  def shutdown() = {
    service(new Command(RedisClient.SHUTDOWN_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Determine if a given value is a member of a set
  def sismember(key: Object, member: Object) = {
    service(new Command(RedisClient.SISMEMBER_BYTES, key, member)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Make the server a slave of another instance, or promote it as master
  def slaveof(host: Object, port: Object) = {
    service(new Command(RedisClient.SLAVEOF_BYTES, host, port)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Get all the members in a set
  def smembers(key: Object) = {
    service(new Command(RedisClient.SMEMBERS_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: MultiBulkReply => reply
      }
    }
  }

  // Move a member from one set to another
  def smove(source: Object, destination: Object, member: Object) = {
    service(new Command(RedisClient.SMOVE_BYTES, source, destination, member)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove and return a random member from a set
  def spop(key: Object) = {
    service(new Command(RedisClient.SPOP_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Get a random member from a set
  def srandmember(key: Object) = {
    service(new Command(RedisClient.SRANDMEMBER_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Get the length of the value stored in a key
  def strlen(key: Object) = {
    service(new Command(RedisClient.STRLEN_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Internal command used for replication
  def sync() = {
    service(new Command(RedisClient.SYNC_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: Reply => reply
      }
    }
  }

  // Get the time to live for a key
  def ttl(key: Object) = {
    service(new Command(RedisClient.TTL_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Determine the type stored at key
  def `type`(key: Object) = {
    service(new Command(RedisClient.TYPE_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Forget about all watched keys
  def unwatch() = {
    service(new Command(RedisClient.UNWATCH_BYTES)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: StatusReply => reply
      }
    }
  }

  // Get the number of members in a sorted set
  def zcard(key: Object) = {
    service(new Command(RedisClient.ZCARD_BYTES, key)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Count the members in a sorted set with scores within the given values
  def zcount(key: Object, min: Object, max: Object) = {
    service(new Command(RedisClient.ZCOUNT_BYTES, key, min, max)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Increment the score of a member in a sorted set
  def zincrby(key: Object, increment: Object, member: Object) = {
    service(new Command(RedisClient.ZINCRBY_BYTES, key, increment, member)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }

  // Determine the index of a member in a sorted set
  def zrank(key: Object, member: Object) = {
    service(new Command(RedisClient.ZRANK_BYTES, key, member)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove all members in a sorted set within the given indexes
  def zremrangebyrank(key: Object, start: Object, stop: Object) = {
    service(new Command(RedisClient.ZREMRANGEBYRANK_BYTES, key, start, stop)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Remove all members in a sorted set within the given scores
  def zremrangebyscore(key: Object, min: Object, max: Object) = {
    service(new Command(RedisClient.ZREMRANGEBYSCORE_BYTES, key, min, max)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Determine the index of a member in a sorted set, with scores ordered from high to low
  def zrevrank(key: Object, member: Object) = {
    service(new Command(RedisClient.ZREVRANK_BYTES, key, member)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: IntegerReply => reply
      }
    }
  }

  // Get the score associated with the given member in a sorted set
  def zscore(key: Object, member: Object) = {
    service(new Command(RedisClient.ZSCORE_BYTES, key, member)) map {
      _ match {
        case error: ErrorReply => throw new RedisException(error.error)
        case reply: BulkReply => reply
      }
    }
  }
}
