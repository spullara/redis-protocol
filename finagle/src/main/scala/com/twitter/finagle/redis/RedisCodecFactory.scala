package com.twitter.finagle.redis

import com.twitter.finagle.{Codec, CodecFactory}

import org.jboss.netty.channel.{ChannelPipelineFactory, Channels}
import redis.Command
import redis.netty.Reply
import redis.netty.{RedisDecoder, RedisEncoder}

class RedisCodecFactory extends CodecFactory[Command, Reply[_]] {
  def client = Function.const {
    new Codec[Command, Reply[_ <: Any]] {

      def pipelineFactory = new ChannelPipelineFactory {
        def getPipeline() = {
          val pipeline = Channels.pipeline()
          val commandCodec = new RedisEncoder
          val replyCodec = new RedisDecoder

          pipeline.addLast("encoder", commandCodec)
          pipeline.addLast("decoder", replyCodec)
          pipeline
        }
      }
    }
  }

  def server = Function.const {
    new Codec[Command, Reply[_]] {
      def pipelineFactory = new ChannelPipelineFactory {
        def getPipeline() = {
          throw new UnsupportedOperationException
        }
      }
    }
  }
}
