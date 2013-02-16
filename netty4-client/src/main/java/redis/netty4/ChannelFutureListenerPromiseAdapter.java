package redis.netty4;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import spullara.util.concurrent.Promise;

/**
 * Adapts a ChannelFutureListener to a Promise.
 */
public class ChannelFutureListenerPromiseAdapter<T> implements ChannelFutureListener {
  private final Promise<T> promise;
  private final T client;


  public ChannelFutureListenerPromiseAdapter(Promise<T> promise, T client) {
    this.promise = promise;
    this.client = client;
  }

  @Override
  public void operationComplete(ChannelFuture channelFuture) throws Exception {
    if (channelFuture.isSuccess()) {
      promise.set(client);
    } else {
      promise.setException(channelFuture.cause());
    }
  }
}
