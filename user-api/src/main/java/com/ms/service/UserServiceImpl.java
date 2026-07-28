package com.ms.service;

import com.ms.dto.UserDTO;
import com.ms.exception.UserNameAlreadyExistException;
import com.ms.exception.UserNotFoundException;
import com.ms.model.User;
import com.ms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private final ModelMapper mapper;

  @Override
  public UserDTO save(UserDTO user) {

    if (repository.findByUsername(user.getUsername()).isPresent()) {
      throw new UserNameAlreadyExistException("Username not available");
    }
    log.info("Saving new user to user database.");
    User userEntity = toEntity(user);
    UserDTO newUser = toDto(repository.save(userEntity));
    log.info("Newly save user : {}", newUser);
    return newUser;
  }

  @Override
  public UserDTO find(String username) {
    log.info("Find user by username from user database.");
    User user = repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User by given username not found."));
    log.info("Found user with username : {}", user.getUsername());
    return toDto(user);
  }

  @Override
  public List<UserDTO> findAll() {
    log.info("Fetching all the information from user database.");
    List<UserDTO> users = repository.findAll().stream().map(this::toDto).toList();
    log.info("Found total : {} users", users.size());
    return users;
  }

  @Override
  @Transactional
  public String delete(String username) {
    if (!repository.existsByUsername(username)) throw new UserNotFoundException("User by given username not found.");
    log.info("Deleting user by username from user database.");
    repository.deleteByUsername(username);
    log.info("User deleted successfully.");
    return "User deleted";
  }


  @Override
  public UserDTO update(UserDTO user) {
    User userEntity = repository.findByUsername(user.getUsername()).orElseThrow(() -> new UserNotFoundException("User by given username not found."));
    log.info("User details before updating : {}", user);
    userEntity.setEmail(user.getEmail());
    userEntity.setName(user.getName());
    UserDTO updatedUser = toDto(repository.save(userEntity));
    log.info("User details after updating : {}", updatedUser);
    return updatedUser;
  }

  private UserDTO toDto(User user) {
    return mapper.map(user, UserDTO.class);
  }

  private User toEntity(UserDTO dto) {
    return mapper.map(dto, User.class);
  }

}
