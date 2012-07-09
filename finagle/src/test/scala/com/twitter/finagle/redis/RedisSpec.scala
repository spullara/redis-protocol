package com.twitter.finagle.redis

import com.twitter.util.Promise
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory
import java.util.concurrent.Executors
import com.twitter.finagle.builder.{ReferenceCountedChannelFactory, ClientBuilder}
import org.specs.SpecificationWithJUnit
import com.google.common.base.Charsets
import redis.netty.BulkReply
import redis.Command
import redis.util.Encoding

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
        val oioFactory = new OioClientSocketChannelFactory(Executors.newCachedThreadPool())
        RedisCluster.start(1)
        val service = ClientBuilder()
                .codec(new RedisCodecFactory)
                .channelFactory(new ReferenceCountedChannelFactory(oioFactory))
                .hosts(RedisCluster.hostAddresses())
                .hostConnectionLimit(1)
                .build()
        client = RedisClient(service)
      }
    }

    doAfter {
      ifDevelopment {
        RedisCluster.stop()
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
        val CALLS = 100000
        var i = 0
        val promise = new Promise[String]()
        def call() {
          client.set(Encoding.numToBytes(i, false), value) onSuccess {
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
        val result = promise.get()
        println(result)
        result mustNotBe null
      }
    }
  }
}
