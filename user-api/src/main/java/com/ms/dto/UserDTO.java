package com.ms.dto;

import jakarta.validation.constraints.NotBlank;

public class UserDTO {

	@NotBlank(message ="Provide Username")
	private String username;
	
	@NotBlank(message = "Provide name")
	private String name;
	
	@NotBlank(message = "Provide email")
	private String email;

	public UserDTO() {}
	
	

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
