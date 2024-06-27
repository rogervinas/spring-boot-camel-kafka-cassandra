package com.rogervinas.poc;

import org.apache.camel.builder.RouteBuilder;

import java.util.List;
import java.util.Map;

public class CassandraRouteBuilder extends RouteBuilder {

  private final String uriFrom;
  private final String uriTo;

  public CassandraRouteBuilder(String uriFrom, String uriTo) {
    this.uriFrom = uriFrom;
    this.uriTo = uriTo;
  }

  @Override
  public void configure() {
    from(uriFrom)
      .split(jsonpath("$.hits.hits"))
      .log("HIT ${body}")
      .transform().body(Map.class, this::mapBody)
      .to(uriTo);
  }

  private Object mapBody(Map<String, Object> body) {
    return List.of(
      body.get("_id"),
      body.get("_source")
    );
  }
}
