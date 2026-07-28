package com.ms.dto;

import jakarta.validation.constraints.NotBlank;

public class BookDTO {

	private int id;

	@NotBlank(message = "Provide book name")
	private String name;

	@NotBlank(message = "Provide publisher")
	private String publisher;

	@NotBlank(message = "Provide author")
	private String author;

	public BookDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
