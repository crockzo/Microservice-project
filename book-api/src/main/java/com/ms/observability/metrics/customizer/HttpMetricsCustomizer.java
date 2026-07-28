package com.ms.observability.metrics.customizer;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import java.time.Duration;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.micrometer.metrics.autoconfigure.MeterRegistryCustomizer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpMetricsCustomizer implements MeterRegistryCustomizer<MeterRegistry> {

  @Override
  public void customize(MeterRegistry registry) {
    double[] sloMillis = {
      10, 25, 50, 75, 100, 150, 200, 300, 500, 750, 1000, 1500, 2000, 3000, 5000
    };
    log.info(
        "✅ Configuring HTTP metrics with percentiles (p50, p95, p99) and {} SLO buckets",
        sloMillis.length);

    // Convert millis to nanos for SLO configuration
    double[] sloNanos =
        Arrays.stream(sloMillis)
            .map(millis -> Duration.ofMillis((long) millis).toNanos())
            .toArray();
    registry
        .config()
        .meterFilter(
            new MeterFilter() {
              @Override
              public DistributionStatisticConfig configure(
                  Meter.Id id, DistributionStatisticConfig config) {
                if (id.getName().startsWith("http.server.requests")) {
                  return DistributionStatisticConfig.builder()
                      .percentiles(0.5, 0.9, 0.95, 0.99)
                      .serviceLevelObjectives(sloNanos)
                      .build()
                      .merge(config);
                }
                return config;
              }
            });
    log.info("✅ HTTP metrics configured: latency percentiles, status codes, SLO buckets");
  }
}
