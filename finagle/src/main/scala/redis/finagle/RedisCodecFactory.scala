package redis.finagle

import org.jboss.netty.handler.codec.redis.{RedisDecoder, RedisEncoder, Reply, Command}
import org.jboss.netty.channel.{ChannelPipelineFactory, Channels}
import com.twitter.finagle.tracing.ClientRequestTracingFilter
import com.twitter.util.Future
import com.twitter.finagle.{Service, Codec, CodecFactory}

class RedisCodecFactory extends CodecFactory[Command, Reply] {
  def client = Function.const {
    new Codec[Command, Reply] {

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

      override def prepareService(underlying: Service[Command, Reply]) = {
        Future.value((new RedisTracingFilter()) andThen underlying)
      }

    }
  }

  def server = Function.const {
    new Codec[Command, Reply] {
      def pipelineFactory = new ChannelPipelineFactory {
        def getPipeline() = {
          throw new UnsupportedOperationException
        }
      }
    }
  }
}

private class RedisTracingFilter extends ClientRequestTracingFilter[Command, Reply] {
  val serviceName = "redis"

  def methodName(command: Command): String = {
    new String(command.getName)
  }
}
