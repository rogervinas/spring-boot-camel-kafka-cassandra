package com.rogervinas.poc;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public CqlSessionBuilder cassandraSessionBuilder(
    @Value("${cassandra.hostname}") String hostname,
    @Value("${cassandra.port}") int port,
    @Value("${cassandra.datacenter}") String datacenter
  ) {
    return CqlSession
      .builder()
      .withLocalDatacenter(datacenter)
      .addContactPoint(new InetSocketAddress(hostname, port));
  }

  @Bean
  public CassandraInitializer cassandraInitializer(
    CqlSessionBuilder builder,
    @Value("${cassandra.keyspace}") String keyspace,
    @Value("${cassandra.table}") String table
  ) {
    return new CassandraInitializer(builder, keyspace, table);
  }

  @Bean
  public RouteBuilder cassandraRouteBuilder(
    @Value("${route.from}") String uriFrom,
    @Value("${route.to}") String uriTo
  ) {
    return new CassandraRouteBuilder(uriFrom, uriTo);
  }
}
