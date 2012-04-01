package redis.reply;

import com.google.common.base.Charsets;

/**
* Created by IntelliJ IDEA.
* User: sam
* Date: 7/29/11
* Time: 10:23 AM
* To change this template use File | Settings | File Templates.
*/
public class ErrorReply implements Reply<String> {
  public static final char MARKER = '-';
  private static final byte[] ERR = "ERR ".getBytes(Charsets.UTF_8);
  private final String error;

  public ErrorReply(String error) {
    this.error = error;
  }

  @Override
  public String data() {
    return error;
  }
}
