package com.ms.service.impl;

import com.ms.client.webclient.UserWebClientUtil;
import com.ms.dto.UserDTO;
import com.ms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  //  private final UserClient userClient;
  private final UserWebClientUtil userClient;

  @Override
  public List<UserDTO> getAll() {
    log.info("Calling User service to get all users.");
    List<UserDTO> users = userClient.getAll();
    log.info("Total users found = {}", users.size());
    return users;
  }

  @Override
  public UserDTO get(String username) {
    log.info("Calling user service to get user by username : {}", username);
    UserDTO user = userClient.get(username);
    log.info("User by username {} found.", username);
    return user;
  }

  @Override
  public UserDTO add(UserDTO user) {
    log.info("Calling user service to create new user.");
    UserDTO newUser = userClient.add(user);
    log.info("New User created with id = {}", newUser.getUsername());
    return newUser;
  }

  @Override
  public String delete(String username) {
    log.info("Calling user service to delete user username : {}.", username);
    String response = userClient.delete(username);
    log.info("Response from user delete by username endpoint : {}", response);
    return response;
  }

  @Override
  public UserDTO update(String username, UserDTO user) {
    log.info("Calling user service to update user by username : {}.", username);
    UserDTO updatedUser = userClient.update(username, user);
    log.info("User with username = {} updated successfully.", updatedUser.getUsername());
    return updatedUser;
  }

}
