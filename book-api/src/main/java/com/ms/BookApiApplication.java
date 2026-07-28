package com.ms;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.micrometer.metrics.autoconfigure.export.otlp.OtlpMetricsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
public class BookApiApplication {

  @Autowired
  private OtlpMetricsProperties otlpMetricsProperties;

  public static void main(String[] args) {
    SpringApplication.run(BookApiApplication.class, args);
  }

  @Bean
  public ModelMapper mapper() {
    return new ModelMapper();
  }

  @EventListener(ApplicationReadyEvent.class)
  private void startup(){
    log.info("%%%%%%%%%%%%%%%% {}", otlpMetricsProperties);
  }

}
