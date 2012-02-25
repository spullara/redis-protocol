package redis.finagle

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.google.common.base.Charsets
import redis.netty._

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
  def apply(service: Service[Command, Reply]): RedisClient = new RedisClient(service)

}

class RedisClient(service: Service[Command, Reply]) {

  def set(name: String, value: String) = {
    service(new Command("SET", name, value)) map {
      _ match {
        case status: StatusReply => status.status
      }
    }
  }

  def get(name: String) = {
    service(new Command("GET", name)) map {
      _ match {
        case bulk: BulkReply => new String(bulk.bytes, Charsets.UTF_8)
      }
    }
  }
}
