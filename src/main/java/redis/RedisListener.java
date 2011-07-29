package redis;

/**
 * Created by IntelliJ IDEA.
 * User: sam
 * Date: 7/29/11
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RedisListener {
  public void received(Command command);
}
