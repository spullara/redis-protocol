package com.twitter.finagle.redis

import org.specs.SpecificationWithJUnit
import redis.util.Encoding
import com.twitter.util.Promise
import redis.reply.{StatusReply, Reply}
import com.twitter.finagle.redisservice.RedisClient

class RedisSpec extends SpecificationWithJUnit {
  val value = "value".getBytes
  val CALLS = 1000000

  "redis service factory" should {

    "some commands" in {
      val client = RedisClient("localhost", 6379)
      val promise = new Promise[StatusReply]()
      client.del("test").get()
      client.hmset("test", "test1", "value1", "test2", "value2") onSuccess {
        reply =>
          promise.setValue(reply)
      } onFailure {
        promise.setException(_)
      }
      promise.get().data() mustEqual "OK"
    }

    "low level benchmark" in {
      var i = 0
      val start = System.currentTimeMillis()
      val promise = new Promise[StatusReply]()
      val client = RedisClient("localhost", 6379)
      def call() {
        client.set(Encoding.numToBytes(i, false), value) onSuccess {
          reply =>
            if (i == CALLS) {
              println(CALLS * 1000l / (System.currentTimeMillis() - start))
              promise.setValue(reply)
            } else {
              i += 1
              call()
            }
        }
      }
      call()
      promise.get().data() mustEqual "OK"
      i mustEqual CALLS
    }
  }
}
