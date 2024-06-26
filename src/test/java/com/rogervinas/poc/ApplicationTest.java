package com.rogervinas.poc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startable;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ExtendWith(OutputCaptureExtension.class)
class ApplicationTest {

  @Container
  static Startable container = DockerComposeHelper.createContainer();

  @BeforeAll
  static void beforeAll() {
    DockerComposeHelper.setSystemProperties();
  }

  @Test
  void shouldDoSomething(CapturedOutput output) {
    await().atMost(Duration.ofSeconds(30)).untilAsserted(() -> {
      assertThat(output.getOut()).contains("Table my_keyspace.my_table created");
    });
  }
}
