package redis.client;

/**
 * Runtime errors
 * <p/>
 * User: sam
 * Date: 11/5/11
 * Time: 10:09 PM
 */
public class RedisException extends RuntimeException {
  public RedisException() {
    super();    //To change body of overridden methods use File | Settings | File Templates.
  }

  public RedisException(Throwable cause) {
    super(cause);    //To change body of overridden methods use File | Settings | File Templates.
  }

  public RedisException(String message) {
    super(message);    //To change body of overridden methods use File | Settings | File Templates.
  }

  public RedisException(String message, Throwable cause) {
    super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
  }
}
