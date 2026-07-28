package com.ms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookDTO {

  private int id;

  @NotBlank(message = "Provide book name")
  private String name;

  @NotBlank(message = "Provide publisher")
  private String publisher;

  @NotBlank(message = "Provide author")
  private String author;

}
