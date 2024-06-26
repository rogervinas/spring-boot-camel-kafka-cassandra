package com.rogervinas.poc;

import org.apache.camel.builder.RouteBuilder;

public class CassandraRouteBuilder extends RouteBuilder {

  private final String uriFrom;
  private final String uriTo;

  public CassandraRouteBuilder(String uriFrom, String uriTo) {
    this.uriFrom = uriFrom;
    this.uriTo = uriTo;
  }

  @Override
  public void configure() throws Exception {
    from(uriFrom)
      .to(uriTo);
  }
}
