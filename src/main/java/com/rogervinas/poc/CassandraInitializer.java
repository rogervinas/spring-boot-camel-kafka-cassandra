package com.rogervinas.poc;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class CassandraInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(CassandraInitializer.class);

  public CassandraInitializer(String hostname, int port) {
    CqlSessionBuilder builder = CqlSession
      .builder()
      .withLocalDatacenter("datacenter1")
      .addContactPoint(new InetSocketAddress(hostname, port));
    try (CqlSession session = builder.build()) {
      session.execute("""
        CREATE KEYSPACE IF NOT EXISTS my_keyspace
        WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
        """
      );
      session.execute("""
        CREATE TABLE IF NOT EXISTS my_keyspace.users (
          user_id UUID PRIMARY KEY,
          name TEXT,
          age INT
        );
        """
      );
      LOGGER.info("Cassandra initialized!");
    } catch (Exception e) {
      LOGGER.error("Cassandra initialization error", e);
      throw e;
    }
  }
}
