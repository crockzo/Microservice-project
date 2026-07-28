package com.ms.security.autoconfigure;

import com.ms.observability.autoconfigure.ObservabilityAutoConfiguration;
import com.ms.security.jwt.JWTCookieService;
import com.ms.security.jwt.JwtFilter;
import com.ms.security.jwt.JwtProperties;
import com.ms.security.jwt.TokenProvider;
import com.ms.security.service.SecurityMetersService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AutoConfiguration(after = {SecurityAutoConfiguration.class, ObservabilityAutoConfiguration.class})
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(value = JwtProperties.class)
public class MicroServiceSecurityAutoConfiguration {

  @Bean
  @ConditionalOnBean(value = {MeterRegistry.class})
  public SecurityMetersService securityMetersService(MeterRegistry meterRegistry) {
    log.info(
        "✅ Configuring SecurityMetersService for JWT token based validation exception metrics. {}",
        meterRegistry.getClass().getSimpleName());
    return new SecurityMetersService(meterRegistry);
  }

  @Bean
  @ConditionalOnBean(value = {SecurityMetersService.class})
  public TokenProvider tokenProvider(
      JwtProperties jwtProperties, SecurityMetersService securityMetersService) {
    log.info("✅ Configuring TokenProvider used by JwtFilter to create/validate token.");
    return new TokenProvider(securityMetersService, jwtProperties);
  }

  @Bean
  @ConditionalOnBean(value = {TokenProvider.class})
  public JWTCookieService jwtCookieService(Environment environment, TokenProvider tokenProvider) {
    log.info("✅ Configuring JWTCookieService to build cookie based authentication.");
    return new JWTCookieService(tokenProvider, environment);
  }

  @Bean
  @ConditionalOnBean(value = {TokenProvider.class, JWTCookieService.class})
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, TokenProvider tokenProvider, JWTCookieService jwtCookieService) {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/actuator", "/actuator/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(
            new JwtFilter(tokenProvider, jwtCookieService),
            UsernamePasswordAuthenticationFilter.class);
    log.info("✅ Configuring SecurityFilterChain for JWT based authentication and authorization.");
    return http.build();
  }
}
