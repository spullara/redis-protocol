package redis.finagle

import org.specs.Specification
import com.twitter.finagle.builder.ClientBuilder
import redis.client.RedisClient
import com.twitter.util.Promise

class RedisTest extends Specification {
  "The redis client" should {

    var client: RedisClient = null

    doBefore {
      RedisCluster.start(1)
      val service = ClientBuilder()
        .codec(new RedisCodecFactory)
        .hosts(RedisCluster.hostAddresses())
        .hostConnectionLimit(1)
        .buildFactory().make()()
      client = RedisClient(service)
    }

    doAfter {
      RedisCluster.stop()
    }

    "perform simple commands" in {
      client.set("test", "value")()
      new String(client.get("test")().bytes) mustEqual "value"
    }

    "benchmark" in {
      val value = "value".getBytes
      val start = System.currentTimeMillis();
      val CALLS = 1000000;
      var i = 0;
      val promise = new Promise[String]()
      def call(): Unit = {
        client.set(String.valueOf(CALLS), value) onSuccess { reply =>
          i = i + 1
          if (i == CALLS) {
            val end = System.currentTimeMillis()
            promise.setValue(CALLS * 1000 / (end - start) + " calls per second")
          } else {
            call();
          }
        }
      }
      call()
      println(promise.get())
    }
  }
}
