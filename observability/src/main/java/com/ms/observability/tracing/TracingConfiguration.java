package com.ms.observability.tracing;

import io.opentelemetry.api.trace.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
    prefix = "observability.tracing",
    value = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Slf4j
public class TracingConfiguration {

  @Bean
  @ConditionalOnBean(Tracer.class)
  @ConditionalOnMissingBean(value = TracingHelper.class)
  public TracingHelper tracingHelper(Tracer tracer) {
    log.info("TracingHelper bean available for manual instrumentation");
    return new TracingHelper(tracer);
  }
}
