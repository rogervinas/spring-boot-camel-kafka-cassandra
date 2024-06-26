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
        );
    }
    return container;
  }

  public static void setSystemProperties() {
    if (isCI()) {
      setSystemPropertiesV1();
    } else {
      setSystemPropertiesV2();
    }
  }

  private static void setSystemPropertiesV1() {
    DockerComposeContainer containerV1 = (DockerComposeContainer) container;
    var cassandraPort = containerV1.getServicePort(CASSANDRA, CASSANDRA_PORT);
    System.setProperty("cassandra.port", cassandraPort.toString());
  }

  private static void setSystemPropertiesV2() {
    ComposeContainer containerV1 = (ComposeContainer) container;
    var cassandraPort = containerV1.getServicePort(CASSANDRA, CASSANDRA_PORT);
    System.setProperty("cassandra.port", cassandraPort.toString());
  }
}
