package com.ms.observability.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "observability")
public class ObservabilityProperties {

  /** Enable/disable observability features globally. */
  private boolean enabled = true;

  /** Metrics configuration. */
  private Metrics metrics = new Metrics();

  /** Tracing configuration. */
  private Tracing tracing = new Tracing();

  /** Logging configuration. */
  private Logging logging = new Logging();

  @Data
  public static class Metrics {
    /** Enable Prometheus metrics. */
    private boolean enabled = true;
  }

  @Data
  public static class Tracing {
    /** Enable distributed tracing. */
    private boolean enabled = true;
  }

  @Data
  public static class Logging {
    private boolean enabled = true;
  }
}
