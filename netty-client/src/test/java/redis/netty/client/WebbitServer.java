package redis.netty.client;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;
import org.webbitserver.WebServers;
import redis.netty.BulkReply;
import spullara.util.functions.Block;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WebbitServer {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    final Executor redisThread = Executors.newSingleThreadExecutor();
    final RedisClient redis = RedisClient.connect("localhost", 6379).get();
    WebServers.createWebServer(8080).add(new HttpHandler() {
      public void handleHttpRequest(final HttpRequest request, final HttpResponse response, final HttpControl control) throws Exception {
        System.out.println(request.uri());
        // Switch to Redis thread because we're about to run a blocking operation
        redisThread.execute(new Runnable() {
          @Override
          public void run() {
            // This blocks the current Thread, and that's ok - we're not on the Webbit thread.
            redis.get(request.uri()).onSuccess(new Block<BulkReply>() {
              public void apply(final BulkReply bulkReply) {
                // We're done with the blocking operations now. Switch back to Webbit thread.
                control.execute(new Runnable() {
                  public void run() {
                    response.content(bulkReply.data().array()).end();
                  }
                });
              }
            });
          }
        });
      }
    }).start();
    System.out.println("Serving from Redis on 8080");
  }
}
