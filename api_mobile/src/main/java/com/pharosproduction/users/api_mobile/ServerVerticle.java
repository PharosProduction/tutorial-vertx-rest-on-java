package com.pharosproduction.users.api_mobile;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;

public class ServerVerticle extends AbstractVerticle {

  // Variables

  private final User mUser = new User("Vasja", "Ivanov");

  // Overrides

  @Override
  public void start() throws Exception {
    super.start();

    createServer();
  }

  // Private

  private void createServer() {
    Config config = new Config(config());
    HttpServerOptions options = config.getHttpOptions();
    int port = config.getHttpOptions().getPort();

    vertx.createHttpServer(options)
      .requestHandler(this::handleRequest)
      .listen(port, this::handleListener);
  }

  private void handleRequest(HttpServerRequest request) {
    HttpServerResponse response = request.response()
      .putHeader("content-type", "application/json");

    String json = Json.encodePrettily(mUser);
    response.end(json);
  }

  private void handleListener(AsyncResult<HttpServer> ar) {
    if (ar.succeeded()) {
      System.out.println("Server started");
    } else {
      System.out.println("Cannot start server: " + ar.cause());
    }
  }
}
