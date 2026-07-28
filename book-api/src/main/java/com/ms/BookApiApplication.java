package com.ms;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
public class BookApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookApiApplication.class, args);
  }

  @Bean
  public ModelMapper mapper() {
    return new ModelMapper();
  }
}
