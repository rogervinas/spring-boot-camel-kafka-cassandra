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
    @Value("${cassandra.port}") int port
  ) {
    return new CassandraInitializer(hostname, port);
  }

  @Bean
  public RouteBuilder cassandraRouteBuilder(
    @Value("${cassandra.route.from}") String uriFrom,
    @Value("${cassandra.route.to}") String uriTo
  ) {
    return new CassandraRouteBuilder(uriFrom, uriTo);
  }
}
