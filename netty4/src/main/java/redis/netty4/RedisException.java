package redis.netty4;

/**
 * Runtime errors
 * <p/>
 * User: sam Date: 11/5/11 Time: 10:09 PM
 */
public class RedisException extends RuntimeException {
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  public RedisException() {
    super();
  }

  public RedisException(Throwable cause) {
    super(cause);
  }

  public RedisException(String message) {
    super(message);
  }

  public RedisException(String message, Throwable cause) {
    super(message, cause);
  }
}