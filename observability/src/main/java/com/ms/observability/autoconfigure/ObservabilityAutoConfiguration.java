package com.ms.observability.autoconfigure;

import com.ms.observability.configuration.ObservabilityProperties;
import com.ms.observability.health.ObservabilityHealthIndicator;
import com.ms.observability.logging.OpenTelemetryLoggerAppender;
import com.ms.observability.metrics.MetricsConfiguration;
import com.ms.observability.tracing.TracingConfiguration;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.micrometer.metrics.autoconfigure.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.micrometer.metrics.autoconfigure.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.opentelemetry.autoconfigure.OpenTelemetrySdkAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

@AutoConfiguration(
    after = {
      PrometheusMetricsExportAutoConfiguration.class,
      CompositeMeterRegistryAutoConfiguration.class,
      OpenTelemetrySdkAutoConfiguration.class
    })
@EnableConfigurationProperties(ObservabilityProperties.class)
@ConditionalOnProperty(
    prefix = "observability",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Import({MetricsConfiguration.class, TracingConfiguration.class})
public class ObservabilityAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(value = ObservabilityHealthIndicator.class)
  @ConditionalOnBean({MeterRegistry.class})
  public ObservabilityHealthIndicator observabilityHealthIndicator(
      MeterRegistry meterRegistry, Environment environment) {
    return new ObservabilityHealthIndicator(meterRegistry, environment);
  }

  @Bean
  @ConditionalOnMissingBean(value = OpenTelemetryLoggerAppender.class)
  @ConditionalOnBean({OpenTelemetry.class})
  public OpenTelemetryLoggerAppender openTelemetryLoggerAppender(OpenTelemetry openTelemetry) {
    return new OpenTelemetryLoggerAppender(openTelemetry);
  }
}
