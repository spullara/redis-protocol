package com.twitter.finagle.redis

import java.net.InetSocketAddress

object RedisCluster {
  self =>

  import collection.mutable.{Stack => MutableStack}

  val instanceStack = MutableStack[ExternalRedis]()

  def address: Option[InetSocketAddress] = instanceStack.head.address

  def address(i: Int) = instanceStack(i).address

  def addresses: Seq[Option[InetSocketAddress]] = instanceStack.map {
    i => i.address
  }

  def hostAddresses(): String = {
    require(instanceStack.length > 0)
    addresses.map {
      address =>
        val addy = address.get
        "%s:%d".format(addy.getHostName(), addy.getPort())
    }.sorted.mkString(",")
  }

  def start(count: Int = 1) {
    0 until count foreach {
      i =>
        val instance = new ExternalRedis()
        instance.start()
        instanceStack.push(instance)
    }
  }

  def stop() {
    instanceStack.pop().stop()
  }

  def stopAll() {
    instanceStack.foreach {
      i => i.stop()
    }
    instanceStack.clear
  }

  // Make sure the process is always killed eventually
  Runtime.getRuntime().addShutdownHook(new Thread {
    override def run() {
      self.instanceStack.foreach {
        instance => instance.stop()
      }
    }
  });
}
