package com.rogervinas.poc;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class CassandraInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(CassandraInitializer.class);

  public CassandraInitializer(
    String hostname,
    int port,
    String keyspace,
    String table
  ) {
    CqlSessionBuilder builder = CqlSession
      .builder()
      .withLocalDatacenter("datacenter1")
      .addContactPoint(new InetSocketAddress(hostname, port));
    try (CqlSession session = builder.build()) {
      session.execute(STR."""
        CREATE KEYSPACE IF NOT EXISTS \{keyspace}
        WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
        """
      );
      session.execute(STR."""
        CREATE TABLE IF NOT EXISTS \{keyspace}.\{table} (
          id UUID PRIMARY KEY,
          name TEXT,
          age INT,
          values map<TEXT, TEXT>
        );
        """
      );
      LOGGER.info(STR."Table \{keyspace}.\{table} created");
    } catch (Exception e) {
      LOGGER.error(STR."Cannot create table \{keyspace}.\{table}", e);
      throw e;
    }
  }
}
