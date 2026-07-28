package com.ms.service;

import com.ms.dto.UserDTO;

import java.util.List;

public interface UserService {

  List<UserDTO> getAll();

  UserDTO get(String username);

  UserDTO add(UserDTO user);

  String delete(String username);

  UserDTO update(String username, UserDTO user);

}
