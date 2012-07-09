package com.twitter.finagle.redis

import com.twitter.finagle.builder.ClientBuilder

object Test {

  def main(args: Array[String]) {
    val service = ClientBuilder()
            .codec(new RedisCodecFactory)
            .hosts("localhost:6379")
            .hostConnectionLimit(1)
            .build()
    val client = RedisClient(service)

    client.set("test", "test") foreach { reply =>
      println(reply)
      System.exit(0)
    }
  }
}