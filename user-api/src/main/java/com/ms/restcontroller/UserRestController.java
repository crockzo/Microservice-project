package com.ms.restcontroller;

import com.ms.dto.UserDTO;
import com.ms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

  private final UserService service;

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAll() {
    return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
  }

  @GetMapping("/{username}")
  public ResponseEntity<UserDTO> get(@PathVariable String username) {
    return new ResponseEntity<>(service.find(username), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<UserDTO> add(@Valid @RequestBody UserDTO user) {
    return new ResponseEntity<>(service.save(user), HttpStatus.CREATED);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<String> delete(@PathVariable String username) {
    return new ResponseEntity<>(service.delete(username), HttpStatus.OK);
  }

  @PutMapping("/{username}")
  public ResponseEntity<UserDTO> update(@PathVariable("username") String username, @Valid @RequestBody UserDTO user) {
    return new ResponseEntity<>(service.update(user), HttpStatus.OK);
  }
}
