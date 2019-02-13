package com.pharosproduction.users.api_mobile;

import com.pharosproduction.users.common.MicroserviceVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

public class ApiMobileVerticle extends MicroserviceVerticle {

  // Overrides

  @Override
  public void start() throws Exception {
    super.start();

    JsonObject configObj = config();
    deployServer(configObj);
    publishEndpoint(configObj);
  }

  // Private

  private void deployServer(JsonObject configObj) {
    String className = ServerVerticle.class.getName();
    DeploymentOptions options = new DeploymentOptions()
      .setConfig(configObj);

    vertx.deployVerticle(className, options);
  }

  private void publishEndpoint(JsonObject configObj) throws Exception {
    Config config = new Config(configObj);
    String host = config.getHttpOptions().getHost();
    int port = config.getHttpOptions().getPort();

    publishHttpEndpoint("users", host, port, this::publishHandler);
  }

  private void publishHandler(AsyncResult<Void> ar) {
    if (ar.failed()) {
      ar.cause().printStackTrace();
    } else {
      System.out.println("REST: " + ar.succeeded());
    }
  }
}
