package com.rogervinas.poc;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

class KafkaProducerHelper {
  private final Producer<String, String> producer;

  public KafkaProducerHelper(String bootstrapServers) {
    var config = new Properties();
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    producer = new KafkaProducer<>(config);
  }

  public void send(String topic, String body) throws Exception {
    producer.send(new ProducerRecord<>(topic, body)).get();
    producer.flush();
  }
}
