package com.pharosproduction.users.common;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Launcher extends io.vertx.core.Launcher {

  // Main

  public static void main(String[] args) {
    new Launcher().dispatch(args);
  }

  // Constants

  private static final String configPath = "conf/config.json";

  // Overrides

  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    super.beforeDeployingVerticle(deploymentOptions);

    defaultOptions(deploymentOptions);

    try {
      readConfig(deploymentOptions);
    } catch (FileNotFoundException e) {
      System.out.println("Unable to read config: " + e.getMessage());
    }
  }

  // Private

  private void defaultOptions(DeploymentOptions options) {
    if (options.getConfig() != null) return;

    options.setConfig(new JsonObject());
  }

  private void readConfig(DeploymentOptions options) throws FileNotFoundException {
    File configFile = new File(configPath);
    JsonObject config = getConfig(configFile);
    options.getConfig().mergeIn(config);
  }

  private JsonObject getConfig(File config) throws FileNotFoundException {
    if (!config.isFile()) return new JsonObject();

    Scanner scanner = new Scanner(config).useDelimiter("\\A");
    String confStr = scanner.next();

    return new JsonObject(confStr);
  }
}
