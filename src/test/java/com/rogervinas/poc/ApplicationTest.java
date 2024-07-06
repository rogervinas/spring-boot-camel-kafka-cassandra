package com.rogervinas.poc;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startable;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ExtendWith(SnapshotExtension.class)
class ApplicationTest {

  static final Duration THIRTY_SECONDS = Duration.ofSeconds(30);

  @Container
  static Startable container = DockerComposeHelper.createContainer();

  Expect expect;

  @Value("${kafka.hostname}:${kafka.port}")
  String kafkaBroker;

  @Value("${kafka.topic}")
  String kafkaTopic;

  KafkaProducerHelper kafkaProducerHelper;

  @Value("${cassandra.keyspace}")
  String cassandraKeyspace;

  @Value("${cassandra.table}")
  String cassandraTable;

  @Autowired
  CqlSessionBuilder cassandraSessionBuilder;

  @BeforeEach
  void beforeEach() {
    kafkaProducerHelper = new KafkaProducerHelper(kafkaBroker);
  }

  @Test
  void shouldProcessInputMessage() throws Exception {
    truncateCassandraTable();

    var kafkaMessage = IOUtils.resourceToString("message.json", UTF_8, getSystemClassLoader());
    kafkaProducerHelper.send(kafkaTopic, kafkaMessage);

    await().atMost(THIRTY_SECONDS).untilAsserted(() -> {
      List<Map<String, Object>> rows = selectFromCassandraTable();
      assertThat(rows).hasSize(8);
      expect.serializer("json").toMatchSnapshot(rows);
    });
  }

  @Test
  void shouldProcessTwoInputMessagesUpdatingSameRow() throws Exception {
    truncateCassandraTable();

    var kafkaMessage1 = IOUtils.resourceToString("message-1.json", UTF_8, getSystemClassLoader());
    kafkaProducerHelper.send(kafkaTopic, kafkaMessage1);
    var kafkaMessage2 = IOUtils.resourceToString("message-2.json", UTF_8, getSystemClassLoader());
    kafkaProducerHelper.send(kafkaTopic, kafkaMessage2);

    await().atMost(THIRTY_SECONDS).untilAsserted(() -> {
      List<Map<String, Object>> rows = selectFromCassandraTable();
      assertThat(rows).hasSize(1);
      expect.serializer("json").toMatchSnapshot(rows);
    });
  }

  private void truncateCassandraTable() {
    try (CqlSession session = cassandraSessionBuilder.build()) {
      session.execute(STR."TRUNCATE \{cassandraKeyspace}.\{cassandraTable}");
    }
  }

  private List<Map<String, Object>> selectFromCassandraTable() {
    try (CqlSession session = cassandraSessionBuilder.build()) {
      return session.execute(STR."SELECT id, source from \{cassandraKeyspace}.\{cassandraTable}")
        .all().stream().map(r ->
          Map.of(
            "id", r.getString("id"),
            "source", r.getMap("source", String.class, String.class)
          )
        ).toList();
    }
  }
}
