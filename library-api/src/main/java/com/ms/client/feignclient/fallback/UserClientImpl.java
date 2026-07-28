package com.ms.client.feignclient.fallback;

import com.ms.client.feignclient.UserClient;
import com.ms.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserClientImpl implements UserClient {

  @Override
  public List<UserDTO> getAll() {
    return List.of(getDummyUser());
  }

  @Override
  public UserDTO get(String username) {
    return getDummyUser();
  }

  @Override
  public UserDTO add(UserDTO user) {
    return getDummyUser();
  }

  @Override
  public String delete(String username) {
    return "Not Deleted - Fallback";
  }

  @Override
  public UserDTO update(String username, UserDTO user) {
    return getDummyUser();
  }

  private UserDTO getDummyUser() {
    UserDTO user = new UserDTO();

    user.setUsername("Fallback username");
    user.setEmail("Fallback@email");
    user.setName("Fallback");

    return user;
  }
}
