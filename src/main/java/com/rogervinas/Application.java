package com.rogervinas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    CassandraDatabase.init("localhost", 9042);
    SpringApplication.run(Application.class, args);
  }
}

