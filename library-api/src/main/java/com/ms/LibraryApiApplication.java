package com.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LibraryApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(LibraryApiApplication.class, args);
  }

}
