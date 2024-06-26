package com.rogervinas.poc;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public CassandraInitializer cassandraInitializer(
    @Value("${cassandra.hostname}") String hostname,
    @Value("${cassandra.port}") int port,
    @Value("${cassandra.keyspace}") String keyspace,
    @Value("${cassandra.table}") String table
  ) {
    return new CassandraInitializer(hostname, port, keyspace, table);
  }

  @Bean
  public RouteBuilder cassandraRouteBuilder(
    @Value("${cassandra.route.from}") String uriFrom,
    @Value("${cassandra.route.to}") String uriTo
  ) {
    return new CassandraRouteBuilder(uriFrom, uriTo);
  }
}
