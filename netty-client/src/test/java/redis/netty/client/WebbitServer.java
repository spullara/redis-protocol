package redis.netty.client;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;
import org.webbitserver.WebServers;
import redis.netty.BulkReply;
import spullara.util.functions.Block;

import java.util.concurrent.ExecutionException;

public class WebbitServer {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    final RedisClient redis = RedisClient.connect("localhost", 6379).get();
    WebServers.createWebServer(8080).add(new HttpHandler() {
      public void handleHttpRequest(final HttpRequest request, final HttpResponse response, final HttpControl control) throws Exception {
        System.out.println(request.uri());
        redis.get(request.uri()).onSuccess(new Block<BulkReply>() {
          public void apply(final BulkReply bulkReply) {
            control.execute(new Runnable() {
              public void run() {
                response.content(bulkReply.data().array()).end();
              }
            });
          }
        });
      }
    }).start();
    System.out.println("Serving from Redis on 8080");
  }
}
