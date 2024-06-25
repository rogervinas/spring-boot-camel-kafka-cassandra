[![CI](https://github.com/rogervinas/spring-boot-camel-kafka-cassandra/actions/workflows/gradle.yml/badge.svg)](https://github.com/rogervinas/spring-boot-camel-kafka-cassandra/actions/workflows/gradle.yml)

# Spring Boot + Apache Camel + Kafka + Cassandra

Documentation:

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
docker exec -it cassandra-cassandra-1 cqlsh cassandra 9042 --cqlversion='3.4.7'
```