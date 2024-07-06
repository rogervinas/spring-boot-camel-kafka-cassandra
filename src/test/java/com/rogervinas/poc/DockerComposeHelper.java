package com.rogervinas.poc;

import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.lifecycle.Startable;

import java.io.File;
import java.time.Duration;

import static org.testcontainers.containers.wait.strategy.Wait.forListeningPort;
import static org.testcontainers.containers.wait.strategy.Wait.forListeningPorts;
import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage;
import static org.testcontainers.containers.wait.strategy.WaitAllStrategy.Mode.WITH_INDIVIDUAL_TIMEOUTS_ONLY;
import static org.testcontainers.containers.wait.strategy.WaitAllStrategy.Mode.WITH_OUTER_TIMEOUT;

public class DockerComposeHelper {

  private static final Duration STARTUP_TIMEOUT = Duration.ofMinutes(5);

  private static final String CASSANDRA = "cassandra";
  private static final int CASSANDRA_PORT = 9042;

  private static final String KAFKA = "kafka";
  private static final int KAFKA_PORT = 9094;

  private static final String ZOOKEEPER = "zookeeper";
  private static final int ZOOKEEPER_PORT = 2181;

  private static Startable container = null;

  private static boolean isCI() {
    return "true".equals(System.getenv("CI"));
  }

  public static Startable createContainer() {
    if (isCI()) {
      return createContainerV1();
    } else {
      return createContainerV2();
    }
  }

  private static Startable createContainerV1() {
    if (container == null) {
      container = new DockerComposeContainer(new File("docker-compose.yml"))
        .withLocalCompose(true)
        .withExposedService(
          CASSANDRA,
          CASSANDRA_PORT,
          forLogMessage(".*Startup complete.*", 1).withStartupTimeout(STARTUP_TIMEOUT)
        )
        .withExposedService(
          KAFKA,
          KAFKA_PORT,
          forLogMessage(".*started.*", 1).withStartupTimeout(STARTUP_TIMEOUT)
        )
        .withExposedService(
          ZOOKEEPER,
          ZOOKEEPER_PORT,
          forLogMessage(".*Started.*", 1).withStartupTimeout(STARTUP_TIMEOUT)
        );
    }
    return container;
  }

  private static Startable createContainerV2() {
    if (container == null) {
      container = new ComposeContainer(new File("docker-compose.yml"))
        .withLocalCompose(true)
        .withExposedService(
          CASSANDRA,
          CASSANDRA_PORT,
          forLogMessage(".*Startup complete.*", 1).withStartupTimeout(STARTUP_TIMEOUT)
        )
        .withExposedService(
          KAFKA,
          KAFKA_PORT,
          forLogMessage(".*started.*", 1).withStartupTimeout(STARTUP_TIMEOUT)
        )
        .withExposedService(
          ZOOKEEPER,
          ZOOKEEPER_PORT,
          forLogMessage(".*Started.*", 1).withStartupTimeout(STARTUP_TIMEOUT)
        );
    }
    return container;
  }
}
