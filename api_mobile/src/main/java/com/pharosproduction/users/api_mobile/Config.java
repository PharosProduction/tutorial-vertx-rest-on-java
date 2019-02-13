package com.pharosproduction.users.api_mobile;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;

import java.util.List;

class Config {

  // Constants

  private static final String HTTP = "http";
  private static final String HOST = "host";
  private static final String PORT = "port";
  private static final int PORT_DEFAULT = 8080;

  // Variables

  private final HttpServerOptions mHttpOptions;

  // Constructors

  Config(JsonObject config) {
    JsonObject httpConfig = config.getJsonObject(HTTP);
    String host = httpConfig.getString(HOST);
    int port = httpConfig.getInteger(PORT, PORT_DEFAULT);
    List<HttpVersion> alpns = List.of(HttpVersion.HTTP_1_1, HttpVersion.HTTP_2);

    mHttpOptions = new HttpServerOptions();
    mHttpOptions.setAlpnVersions(alpns);
    mHttpOptions.setHost(host);
    mHttpOptions.setPort(port);
  }

  // Getters

  HttpServerOptions getHttpOptions() {
    return mHttpOptions;
  }
}
