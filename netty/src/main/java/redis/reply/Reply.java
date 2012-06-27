package redis.reply;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import redis.netty.RedisDecoder;

/**
* Replies.
* User: sam
* Date: 7/27/11
* Time: 3:04 PM
* To change this template use File | Settings | File Templates.
*/
public interface Reply<T> {
  byte[] CRLF = new byte[] { RedisDecoder.CR, RedisDecoder.LF };

  T data();
  void write(ChannelBuffer os) throws IOException;
}
