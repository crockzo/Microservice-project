package com.ms.observability.metrics.customizer;

import io.micrometer.core.instrument.MeterRegistry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.micrometer.metrics.autoconfigure.MeterRegistryCustomizer;
import org.springframework.core.env.Environment;

@Slf4j
@RequiredArgsConstructor
public class CommonMetricsCustomizer implements MeterRegistryCustomizer<MeterRegistry> {

  private final Environment environment;

  @Override
  public void customize(MeterRegistry registry) {
    String[] activeProfiles = environment.getActiveProfiles();
    String activeEnvironment = activeProfiles.length > 0 ? activeProfiles[0] : "development";
    String serviceName =
        Optional.ofNullable(environment.getProperty("spring.application.name")).orElse("book-api");

    registry.config().commonTags("service", serviceName, "environment", activeEnvironment);
    log.info(
        "✅ Metrics common tags configured: service={}, environment={}",
        serviceName,
        activeEnvironment);
  }
}
