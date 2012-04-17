package redis.reply;

import java.io.IOException;
import java.io.OutputStream;

import redis.RedisProtocol;

/**
* Replies.
* User: sam
* Date: 7/27/11
* Time: 3:04 PM
* To change this template use File | Settings | File Templates.
*/
public interface Reply<T> {
  byte[] CRLF = new byte[] { RedisProtocol.CR, RedisProtocol.LF };

  T data();
  void write(OutputStream os) throws IOException;
}
