import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

plugins {
  java
  id("org.springframework.boot") version "3.5.3"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "com.rogervinas"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
}

val javaSnapshotTestingVersion = "4.0.8"

dependencies {
  implementation(platform("org.apache.camel.springboot:camel-spring-boot-dependencies:4.13.0"))
  implementation("org.apache.camel.springboot:camel-spring-boot-starter")

  implementation("org.apache.camel.springboot:camel-kafka-starter")
  implementation("org.apache.camel.springboot:camel-cassandraql-starter")
  implementation("org.apache.camel.springboot:camel-jsonpath-starter")

  implementation("org.apache.cassandra:java-driver-core:4.19.0")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.apache.camel:camel-test-junit5")
  testImplementation("commons-io:commons-io:2.20.0")
  testImplementation("org.awaitility:awaitility:4.3.0")
  testImplementation("org.testcontainers:junit-jupiter:1.21.3")

  testImplementation("io.github.origin-energy:java-snapshot-testing-junit5:$javaSnapshotTestingVersion")
  testImplementation("io.github.origin-energy:java-snapshot-testing-plugin-jackson:$javaSnapshotTestingVersion")
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

tasks.withType<JavaCompile> {
  options.compilerArgs.addAll(listOf("--enable-preview", "--release", "21"))
}

tasks.withType<JavaExec> {
  jvmArgs = listOf("--enable-preview")
}

tasks.withType<Test> {
  useJUnitPlatform()
  jvmArgs = listOf("--enable-preview")
  testLogging {
    events(PASSED, SKIPPED, FAILED)
  }
}
