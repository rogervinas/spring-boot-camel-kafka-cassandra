package com.rogervinas;

import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public abstract class CassandraDatabase {
  public static void init(String hostname, int port) {
    try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(hostname, port)).build()) {
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
    }
  }
}
