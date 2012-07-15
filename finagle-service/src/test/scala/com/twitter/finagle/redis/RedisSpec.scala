package com.twitter.finagle.redis

import org.specs.SpecificationWithJUnit
import redis.util.Encoding
import com.twitter.util.Promise
import redis.reply.{StatusReply, Reply}
import com.twitter.finagle.redisservice.{RedisClient, RedisServiceFactory}

class RedisSpec extends SpecificationWithJUnit {
  "redis service factory" should {

    "some commands" in {
      val client = RedisClient("localhost", 6379)
      val promise = new Promise[StatusReply]()
      client.del("test").get()
      client.hmset("test", "test1", "value1", "test2", "value2") onSuccess { reply =>
        promise.setValue(reply)
      } onFailure {
        promise.setException(_)
      }
      promise.get().data() mustEqual "OK"
    }

    "low levl benchmark" in {
      val CALLS = 100000
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
