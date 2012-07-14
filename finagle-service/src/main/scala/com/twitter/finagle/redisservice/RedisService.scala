package com.twitter.finagle.redisservice

import com.twitter.finagle.{ClientConnection, ServiceFactory, Service}
import redis.reply._
import redis.{RedisProtocol, Command}
import com.twitter.util.{Future, FuturePool}
import java.util.concurrent.Executors
import java.net.Socket

class RedisService(redisProtocol: RedisProtocol) extends Service[Command, Reply[Any]] {
  val futurePool = FuturePool(Executors.newSingleThreadExecutor())

  def apply(request: Command): Future[Reply[Any]] = {
    this.synchronized {
      redisProtocol.sendAsync(request)
      futurePool {
        redisProtocol.receiveAsync() match {
          case e: ErrorReply => throw new RuntimeException(e.data())
          case r: Reply[Any] => r
          case o => throw new RuntimeException("Unexpected reply: " + o)
        }
      }
    }
  }
}

class RedisServiceFactory(host: String, port: Int) extends ServiceFactory[Command, Reply[_]] {

  def apply(conn: ClientConnection) = {
    Redis.futurePool {
      val socket: Socket = new Socket(host, port)
      val protocol: RedisProtocol = new RedisProtocol(socket)
      new RedisService(protocol)
    }
  }

  def close() {}
}

object Redis {
  val futurePool = FuturePool(Executors.newCachedThreadPool())
}