package com.pharosproduction.users.common;

import io.vertx.core.*;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MicroserviceVerticle extends AbstractVerticle {

  // Variables

  private ServiceDiscovery mDiscovery;
  private Set<Record> mRegisteredRecords = new ConcurrentHashSet<>();

  // Overrides

  @Override
  public void start() throws Exception {
    super.start();

    createServiceDiscovery();
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    super.stop(stopFuture);

    List<Future> futures = mRegisteredRecords
      .stream()
      .map(this::unpublish)
      .collect(Collectors.toList());

    if (futures.isEmpty()) {
      stopDiscovery(stopFuture);
    } else {
      stopServices(futures, stopFuture);
    }
  }

  // Public

  protected void publishHttpEndpoint(String endpoint, String host, int port, Handler<AsyncResult<Void>> completion) throws Exception {
    Record record = HttpEndpoint.createRecord(endpoint, host, port, "/");
    publish(record, completion);
  }

  // Private

  private void createServiceDiscovery() {
    JsonObject config = config();
    ServiceDiscoveryOptions opts = new ServiceDiscoveryOptions().setBackendConfiguration(config);
    mDiscovery = ServiceDiscovery.create(vertx, opts);
  }

  private void publish(Record record, Handler<AsyncResult<Void>> completion) throws Exception {
    if (mDiscovery == null) start();

    mDiscovery.publish(record, ar -> {
      if (ar.succeeded()) mRegisteredRecords.add(record);

      completion.handle(ar.map((Void)null));
    });
  }

  private Future<Void> unpublish(Record record) {
    Future<Void> unregisteringFuture = Future.future();
    mDiscovery.unpublish(record.getRegistration(), unregisteringFuture);

    return unregisteringFuture;
  }

  private void stopDiscovery(Future<Void> stopFuture) {
    mDiscovery.close();
    stopFuture.complete();
  }

  private void stopServices(List<Future> futures, Future<Void> stopFuture) {
    CompositeFuture composite = CompositeFuture.all(futures);
    composite.setHandler(ar -> {
      mDiscovery.close();

      if (ar.failed()) {
        stopFuture.fail(ar.cause());
      } else {
        stopFuture.complete();
      }
    });
  }
}

