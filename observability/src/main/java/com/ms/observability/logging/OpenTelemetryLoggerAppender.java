package com.ms.observability.logging;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;

@RequiredArgsConstructor
public class OpenTelemetryLoggerAppender implements InitializingBean {

  private final OpenTelemetry openTelemetry;

  @Override
  public void afterPropertiesSet() {
    OpenTelemetryAppender.install(this.openTelemetry);
  }
}
