package com.twitter.finagle.redis

import org.specs.SpecificationWithJUnit
import redis.util.Encoding
import com.twitter.util.Promise
import redis.reply.Reply
import com.twitter.finagle.redisservice.RedisServiceFactory

class RedisSpec extends SpecificationWithJUnit {
  "redis service factory" should {
    "low levl benchmark" in {
      val CALLS = 1000000
      var i = 0
      val value = "value".getBytes
      val factory = new RedisServiceFactory("localhost", 6379)
      val start = System.currentTimeMillis()
      val promise = new Promise[Reply[_]]()
      factory() foreach { service =>
        val client = new RedisClient(service)
        def call() {
          client.set(Encoding.numToBytes(i, false), value) onSuccess { reply =>
            i += 1
            if (i == CALLS) {
              println(CALLS * 1000l / (System.currentTimeMillis() - start))
              promise.setValue(reply)
            } else {
              call()
            }
          }
        }
        call()
      }
      promise.get().asInstanceOf[Reply[String]].data() mustEqual "OK"
      i mustEqual CALLS
    }
  }
}
