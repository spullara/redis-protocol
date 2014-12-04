package redis.netty4;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandResp {

  private final Command command;
  private final Promise<Reply> replyPromise;
  private final Class<?> replyClass;
  private Reply replyForDebug = null;
  private long replyDate;

  private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  public CommandResp(Command command, Class<?> clazz, Promise<Reply> replyPromise) {
    this.command = command;
    this.replyPromise = replyPromise;
    this.replyClass = clazz;
  }

  public Command getCommand() {
    return command;
  }

  public <T> void map(final Promise<T> promise, final Class<T> clazz) {
    if (replyClass == null || !replyClass.equals(clazz)) {
      throw new RedisException("bad type : replyClass Name should be " + replyClass.getName() + " but is " + clazz.getName());
    }
    replyPromise.addListener(new GenericFutureListener<Future<Reply>>() {
      @Override
      public void operationComplete(Future<Reply> future) throws Exception {
        if (future.isSuccess()) {
          Reply result = future.get();
          T castedResult = null;
          try {
            castedResult = clazz.cast(result);
            promise.setSuccess(castedResult);
          } catch (ClassCastException e) {
            promise.setFailure(e);
          }
        } else if (future.isCancelled()) {
          promise.cancel(false);
        } else {
          promise.setFailure(future.cause());
        }
      }
    });
  }

  public void setReply(Reply reply) {
    replyDate = System.currentTimeMillis();
    replyForDebug = reply;
    if (reply instanceof ErrorReply) {
      replyPromise.setFailure(new RedisException(reply.toString() + " Command was " + command));
    } else {
      if (replyClass == null || reply == null || reply.getClass() == null || !replyClass.isAssignableFrom(reply.getClass())) {
        throw new RedisException("bad type : replyClassName should be " + replyClass.getName() + " but is " + reply.getClass().getName() + " Command was " + command);
      }
      replyPromise.setSuccess(reply);
    }
  }

  public void setFailure(Throwable cause) {
    replyPromise.setFailure(cause);
  }

  @Override
  public String toString() {
    return df.format(new Date(replyDate)) + " CommandResp [command=" + command + ", reply=" + replyForDebug + ", replyClassName=" + replyClass.getName() + "]";
  }

}
