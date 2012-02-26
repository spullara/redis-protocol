package redis.finagle

import org.specs.Specification
import com.twitter.finagle.builder.ClientBuilder
import redis.client.RedisClient

class RedisTest extends Specification {
  "The redis client" should {

    var client: RedisClient = null

    doBefore {
      RedisCluster.start(1)
      val service = ClientBuilder()
        .codec(new RedisCodecFactory)
        .hosts(RedisCluster.hostAddresses())
        .hostConnectionLimit(1)
        .build()
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
      val CALLS = 1000000;
      val start = System.currentTimeMillis();
      for (i <- 0 to CALLS) {
        client.set(String.valueOf(i), "value")()
      }
      val end = System.currentTimeMillis();
      System.out.println(CALLS * 1000 / (end - start) + " calls per second");
    }
  }
}
