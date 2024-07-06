package com.rogervinas.poc;

import org.apache.camel.builder.RouteBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CassandraRouteBuilder extends RouteBuilder {

  private final String uriFrom;
  private final String uriTo;

  public CassandraRouteBuilder(String uriFrom, String uriTo) {
    this.uriFrom = uriFrom;
    this.uriTo = uriTo;
  }

  @Override
  public void configure() {
    from(uriFrom)
      .split(jsonpath("$.hits.hits"))
      .log("FROM ${body}")
      .transform().body(Map.class, this::mapBody)
      .log("TO ${body}")
      .to(uriTo);
  }

  private Object mapBody(Map<String, Object> body) {
    return List.of(
      body.get("_id"),
      flatten((Map<String, Object>) body.get("_source"))
    );
  }

  private Map<String, String> flatten(Map<String, Object> map) {
    Map<String, String> flatMap = new HashMap<>();
    flatten("", map, flatMap);
    return flatMap;
  }

  private void flatten(String currentPath, Map<String, Object> map, Map<String, String> flatMap) {
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      String newPath = currentPath.isEmpty() ? key : STR."\{currentPath}.\{key}";
      if (value instanceof Map) {
        flatten(newPath, (Map<String, Object>) value, flatMap);
      } else {
        flatMap.put(newPath, STR."\{value}");
      }
    }
  }
}
