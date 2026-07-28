package com.ms.observability.metrics;

import com.ms.observability.metrics.customizer.CommonMetricsCustomizer;
import com.ms.observability.metrics.customizer.HttpMetricsCustomizer;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmClassLoadingMeterConventions;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmCpuMeterConventions;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmMemoryMeterConventions;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmThreadMeterConventions;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration for Micrometer metrics.
 *
 * <p>Enables: - @Timed annotation support (method-level timing metrics) - @Counted annotation
 * support (method-level counters) - JVM metrics (memory, GC, threads) via JvmMetricsConfiguration -
 * HTTP metrics (requests, responses, latency) via HttpMetricsConfiguration - Database metrics
 * (connection pool, query performance) via DatabaseMetricsConfiguration
 *
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(
    prefix = "observability.metrics",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Slf4j
public class MetricsConfiguration {

  /** Enable @Timed annotation support for method-level metrics. */
  @Bean
  @ConditionalOnClass(TimedAspect.class)
  public TimedAspect timedAspect(MeterRegistry registry) {
    log.info("✅ @Timed annotation support enabled");
    return new TimedAspect(registry);
  }

  /** Enable @Counted annotation support for method-level counters. */
  @Bean
  @ConditionalOnClass(CountedAspect.class)
  public CountedAspect countedAspect(MeterRegistry registry) {
    log.info("✅ @Counted annotation support enabled");
    return new CountedAspect(registry);
  }

  @Bean
  ProcessorMetrics processorMetrics() {
    return new ProcessorMetrics(List.of(), new OpenTelemetryJvmCpuMeterConventions(Tags.empty()));
  }

  @Bean
  JvmMemoryMetrics jvmMemoryMetrics() {
    return new JvmMemoryMetrics(
        List.of(), new OpenTelemetryJvmMemoryMeterConventions(Tags.empty()));
  }

  @Bean
  JvmThreadMetrics jvmThreadMetrics() {
    return new JvmThreadMetrics(
        List.of(), new OpenTelemetryJvmThreadMeterConventions(Tags.empty()));
  }

  @Bean
  ClassLoaderMetrics classLoaderMetrics() {
    return new ClassLoaderMetrics(new OpenTelemetryJvmClassLoadingMeterConventions());
  }

  @Bean
  public CommonMetricsCustomizer commonMetricsCustomizer(Environment environment) {
    return new CommonMetricsCustomizer(environment);
  }

  @Bean
  public HttpMetricsCustomizer httpMetricsCustomizer() {
    return new HttpMetricsCustomizer();
  }
}
