package com.ms.client.webclient;

import com.ms.dto.ExceptionResponse;
import com.ms.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class UserWebClientUtil {

  private final static String USER_SERVICE_BASE_URL = "http://localhost:7070/users";

  private final WebClient userClient;

  public UserWebClientUtil(WebClient.Builder builder) {
    userClient = builder.baseUrl(USER_SERVICE_BASE_URL).build();
  }

  public UserDTO get(String username) {
    Object response = userClient.get().uri("/{username}", username).exchangeToMono(resp -> {
      if (resp.statusCode().equals(HttpStatus.OK)) {
        return resp.bodyToMono(UserDTO.class);
      } else {
        return resp.bodyToMono(ExceptionResponse.class);
      }
    }).block();

    checkIfResponseIsException(response);
    return (UserDTO) response;
  }

  public UserDTO add(UserDTO user) {
    Object response = userClient.post().bodyValue(user).exchangeToMono(resp -> {
      if (resp.statusCode().equals(HttpStatus.CREATED)) {
        return resp.bodyToMono(UserDTO.class);
      } else {
        return resp.bodyToMono(ExceptionResponse.class);
      }
    }).block();

    checkIfResponseIsException(response);
    return (UserDTO) response;
  }

  public List<UserDTO> getAll() {
    return userClient.get().retrieve().bodyToFlux(UserDTO.class).collectList().block();
  }

  public UserDTO update(String username, UserDTO user) {
    Object response = userClient.put().uri("/{username}", username).bodyValue(user).exchangeToMono(resp -> {
      if (resp.statusCode().equals(HttpStatus.OK)) {
        return resp.bodyToMono(UserDTO.class);
      } else {
        return resp.bodyToMono(ExceptionResponse.class);
      }
    }).block();

    checkIfResponseIsException(response);
    return (UserDTO) response;
  }

  public String delete(String username) {

    Object response = userClient.delete().uri("/{username}", username).exchangeToMono(resp -> {
      if (resp.statusCode().equals(HttpStatus.OK)) {
        return resp.bodyToMono(UserDTO.class);
      } else {
        return resp.bodyToMono(ExceptionResponse.class);
      }
    }).block();

    checkIfResponseIsException(response);

    return (String) response;
  }

  private void checkIfResponseIsException(Object response) {
    if (response instanceof ExceptionResponse)
      throw new RuntimeException(((ExceptionResponse) response).getError());
  }

}
