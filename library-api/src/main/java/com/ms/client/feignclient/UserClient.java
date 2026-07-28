package com.ms.client.feignclient;

import com.ms.client.feignclient.fallback.UserClientImpl;
import com.ms.dto.UserDTO;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "user-api", fallback = UserClientImpl.class)  //, url = "http://localhost:7070/users"
@LoadBalancerClient(name = "user-api")
public interface UserClient {

  @GetMapping("/users")
  List<UserDTO> getAll();

  @GetMapping("/users/{username}")
  UserDTO get(@PathVariable String username);

  @PostMapping("/users")
  UserDTO add(@RequestBody UserDTO user);

  @DeleteMapping("/users/{username}")
  String delete(@PathVariable String username);

  @PutMapping("/users/{username}")
  UserDTO update(@PathVariable("username") String username, @RequestBody UserDTO user);
}
