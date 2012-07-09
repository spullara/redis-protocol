package com.twitter.finagle.redis

import com.twitter.util.Promise
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory
import java.util.concurrent.Executors
import com.twitter.finagle.builder.{ReferenceCountedChannelFactory, ClientBuilder}
import org.specs.SpecificationWithJUnit
import com.google.common.base.Charsets
import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}
import redis.netty.BulkReply
import redis.Command

class RedisSpec extends SpecificationWithJUnit {
  def ifDevelopment[T](f: => T): Option[T] = {
    if (System.getenv.get("SBT_CI") == null && System.getProperty("SBT_CI") == null) {
      Some(f)
    } else {
      println("running in CI mode, skipping")
      1 must be_==(1)
      None
    }
  }

  "The redis client" should {

    var client: RedisClient = null

    doBefore {
      ifDevelopment {
        // RedisCluster.start(1)
        val service = ClientBuilder()
          .codec(new RedisCodecFactory)
          .hosts("localhost:6379")
          .hostConnectionLimit(1)
          .build()
        client = RedisClient(service)
      }
    }

    doAfter {
      ifDevelopment {
        // RedisCluster.stop()
      }
    }

    "perform simple commands" in {
      ifDevelopment {
        client.set("test", "value")()
        client.get("test")().data().toString(Charsets.UTF_8) mustEqual "value"
        client.mget("test")().data()(0) match {
          case br: BulkReply => br.asString(Charsets.UTF_8) mustEqual "value"
        }
      }
    }

    "benchmark" in {
      ifDevelopment {
        val value = "value".getBytes
        val start = System.currentTimeMillis()
        val CALLS = 1000000
        var i = 0
        val promise = new Promise[String]()
        def call() {
          client.set(Command.numToBytes(i, false), value) onSuccess {
            reply =>
              i = i + 1
              if (i == CALLS) {
                val end = System.currentTimeMillis()
                promise.setValue(CALLS * 1000 / (end - start) + " calls per second")
              } else {
                call()
              }
          } onFailure {
            case e => promise.setException(e)
          }
        }
        call()
        var result = promise.get()
        println(result)
        result mustNotBe null
      }
    }
  }
}
