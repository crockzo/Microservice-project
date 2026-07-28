package com.ms.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public class UserDTO {

	@NotBlank(message = "Provide Username")
	private String username;

	@NotBlank(message = "Provide name")
	private String name;

	@NotBlank(message = "Provide email")
	private String email;

	private Set<BookDTO> books = new HashSet<>();

	public Set<BookDTO> getBooks() {
		return books;
	}

	public void setBooks(Set<BookDTO> books) {
		this.books = books;
	}

	public UserDTO() {
	}

	public UserDTO(@NotBlank(message = "Provide Username") String username,
			@NotBlank(message = "Provide name") String name, @NotBlank(message = "Provide email") String email) {
		super();
		this.username = username;
		this.name = name;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
