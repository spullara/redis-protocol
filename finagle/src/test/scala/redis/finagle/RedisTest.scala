package redis.finagle

import org.specs.Specification
import com.twitter.finagle.builder.ClientBuilder

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
      client.get("test")() mustEqual "value"
    }
  }
}
