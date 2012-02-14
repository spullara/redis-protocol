package redis.reply;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.io.OutputStream;

/**
* Replies.
* User: sam
* Date: 7/27/11
* Time: 3:04 PM
* To change this template use File | Settings | File Templates.
*/
public abstract class Reply {

  public abstract void write(OutputStream os) throws IOException;

}
