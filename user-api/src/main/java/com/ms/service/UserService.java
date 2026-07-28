package com.ms.service;

import java.util.List;

import com.ms.dto.UserDTO;

public interface UserService {

	UserDTO save(UserDTO user);
	
	List<UserDTO> findAll();
	
	UserDTO find(String username);
	
	String delete(String username);
	
	UserDTO update(UserDTO user);
	
	
}
