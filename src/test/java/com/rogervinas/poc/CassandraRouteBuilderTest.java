package com.rogervinas.poc;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.nio.charset.StandardCharsets.UTF_8;

@ExtendWith(SnapshotExtension.class)
class CassandraRouteBuilderTest extends CamelTestSupport {

  static final String URI_FROM = "direct:from";
  static final String URI_TO = "mock:to";

  Expect expect;

  @EndpointInject(URI_FROM)
  ProducerTemplate from;
  @EndpointInject(URI_TO)
  MockEndpoint to;

  @Test
  void shouldProcessInputMessage() throws IOException {
    from.sendBody(IOUtils.resourceToString("message.json", UTF_8, getSystemClassLoader()));

    var receivedBodies = to.getReceivedExchanges().stream().map(x -> x.getMessage().getBody()).toList();
    expect.serializer("json").toMatchSnapshot(receivedBodies);
  }

  @Override
  protected RoutesBuilder createRouteBuilder() {
    return new CassandraRouteBuilder(URI_FROM, URI_TO);
  }
}
