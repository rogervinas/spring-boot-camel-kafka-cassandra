package com.rogervinas.poc;

import org.apache.camel.builder.RouteBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.apache.camel.component.cassandra.CassandraConstants.CQL_QUERY;

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
      .log("Inserting into my_table")
      .setBody(constant(newBody()))
      .setHeader(CQL_QUERY, constant("INSERT INTO my_table (id, name, age, values) values (?, ?, ?, ?)"))
      .to(uriTo);
  }

  private Object newBody() {
    return List.of(
      UUID.randomUUID(),
      "some name",
      35,
      Map.of("k1", "v1", "k2", "v2")
    );
  }
}
