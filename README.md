[![CI](https://github.com/rogervinas/spring-boot-camel-kafka-cassandra/actions/workflows/gradle.yml/badge.svg)](https://github.com/rogervinas/spring-boot-camel-kafka-cassandra/actions/workflows/gradle.yml)
![Java](https://img.shields.io/badge/Java-21-blue?labelColor=black)
![SpringBoot](https://img.shields.io/badge/SpringBoot-3.3.2-blue?labelColor=black)
![ApacheCamel](https://img.shields.io/badge/ApacheCamel-4.7.0-blue?labelColor=black)
![Cassandra](https://img.shields.io/badge/Cassandra-5.0-blue?labelColor=black)
![ConfluentKafka](https://img.shields.io/badge/ConfluentKafka-7.6.1-blue?labelColor=black)

# Spring Boot + Apache Camel + Kafka + Cassandra

Documentation:
* [Cassandra CQL Camel Component](https://camel.apache.org/components/4.4.x/cql-component.html)

# Test
```shell
./gradlew check
```

# Run
```shell
docker compose up -d
./gradlew bootRun
docker compose down
```

# Build and Run Jar
```shell
docker compose up -d
./gradlew build
java -jar build/libs/spring-boot-camel-kafka-cassandra-0.0.1-SNAPSHOT.jar
docker compose down
```

# CQL shell
```shell
docker compose up -d
docker exec -it spring-boot-camel-kafka-cassandra-cassandra-1 cqlsh cassandra 9042 --cqlversion='3.4.7'
use test_keyspace;
select * from hits;
```

# Send to Kafka using [kcat](https://github.com/edenhill/kcat)
```shell
kcat -b localhost:9094 -t hits -P src/test/resources/message.json
```
