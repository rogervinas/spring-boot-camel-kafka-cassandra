package com.rogervinas.poc;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

class KafkaProducerHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTest.class);

  private final Producer<String, String> producer;

  public KafkaProducerHelper(String bootstrapServers) {
    var config = new Properties();
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    producer = new KafkaProducer<>(config);
  }

  public void send(String topic, String body) throws Exception {
    var metadata = producer.send(new ProducerRecord<>(topic, body)).get();
    LOGGER.info("Message sent topic={} offset={}", metadata.topic(), metadata.offset());
    producer.flush();
  }
}
