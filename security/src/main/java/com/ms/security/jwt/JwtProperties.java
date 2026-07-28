package com.ms.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "openuplearning.jwt")
public class JwtProperties {

  private String secret = "";

  private String base64Secret = "";

  private long tokenValidityInMilliSeconds = 1800_000; // 30 minutes
}
