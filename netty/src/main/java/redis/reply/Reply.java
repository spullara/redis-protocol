package redis.reply;

import java.io.IOException;
import java.io.OutputStream;

import com.google.common.base.Charsets;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
* Replies.
* User: sam
* Date: 7/27/11
* Time: 3:04 PM
* To change this template use File | Settings | File Templates.
*/
public abstract class Reply {

  public abstract void write(ChannelBuffer os) throws IOException;

  public String toString() {
    ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
    try {
      write(channelBuffer);
    } catch (IOException e) {
      throw new AssertionError("Trustin says this won't happen either");
    }
    return channelBuffer.toString(Charsets.UTF_8);
  }
  
}
