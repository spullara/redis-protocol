package com.twitter.finagle.redis

import com.twitter.finagle.{ServiceFactory, Codec, CodecFactory}

import org.jboss.netty.channel.{ChannelPipelineFactory, Channels}
import redis.Command
import redis.netty.Reply
import redis.netty.{RedisDecoder, RedisEncoder}

class RedisCodecFactory extends CodecFactory[Command, Reply[_]] { config =>

  def client = Function.const {
    new Codec[Command, Reply[_]] {

      def pipelineFactory = new ChannelPipelineFactory {
        def getPipeline = {
          val pipeline = Channels.pipeline()
          pipeline.addLast("encoder", new RedisEncoder)
          pipeline.addLast("decoder", new RedisDecoder)
          pipeline
        }
      }
    }
  }

  def server = Function.const {
    new Codec[Command, Reply[_]] {
      def pipelineFactory = new ChannelPipelineFactory {
        def getPipeline = {
          throw new UnsupportedOperationException
        }
      }
    }
  }
}
