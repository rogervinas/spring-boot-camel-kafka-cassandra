package com.rogervinas.poc;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class KafkaInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaInitializer.class);

  private static final int PARTITIONS = 1;
  private static final short REPLICATION = 1;

  public KafkaInitializer(
    String broker,
    String topic
  ) throws Exception {
    var config = new Properties();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
    try (var admin = AdminClient.create(config)) {
      if (!admin.listTopics().names().get().contains(topic)) {
        admin.createTopics(List.of(new NewTopic(topic, PARTITIONS, REPLICATION))).all().get();
        LOGGER.info(STR."Topic \{topic} created");
      } else {
        LOGGER.info(STR."Topic \{topic} already created");
      }
    } catch (Exception e) {
      LOGGER.error(STR."Cannot create topic \{topic}", e);
      throw e;
    }
  }
}
