package com.ms.observability.health;

import io.micrometer.core.instrument.MeterRegistry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.core.env.Environment;

/**
 * Health indicator that reports the status of observability components.
 *
 * <p>Reports: - Whether metrics collection is active and meter count - Tracing configuration
 * (service name, sampling, endpoint) - Logging format and trace correlation status - Stack export
 * status
 *
 * @since 1.3.0
 */
@RequiredArgsConstructor
public class ObservabilityHealthIndicator implements HealthIndicator {

  private final MeterRegistry meterRegistry;
  private final Environment environment;

  @Override
  public Health health() {
    Health.Builder builder = Health.up();

    builder.withDetail("metrics.registeredMeters", meterRegistry.getMeters().size());
    String tracingSamplingProbability =
        Optional.ofNullable(environment.getProperty("management.tracing.sampling.probability"))
            .orElse("UNKNOWN");
    builder.withDetail("tracing.samplingProbability", tracingSamplingProbability);
    String loggingPatternCorrelation =
        Optional.ofNullable(environment.getProperty("logging.pattern.correlation"))
            .orElse("UNKNOWN");
    builder.withDetail("logging.pattern.correlation", loggingPatternCorrelation);
    return builder.build();
  }
}
