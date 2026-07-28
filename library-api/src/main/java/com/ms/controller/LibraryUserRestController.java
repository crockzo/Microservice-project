package com.ms.controller;

import com.ms.dto.BookDTO;
import com.ms.dto.UserDTO;
import com.ms.service.BookService;
import com.ms.service.LibraryService;
import com.ms.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/library/users")
@Slf4j
public class LibraryUserRestController {

  @Autowired
  private UserService userService;

  @Autowired
  private BookService bookService;

  @Autowired
  private LibraryService libraryService;

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAll() {
    return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/{username}")
  public ResponseEntity<UserDTO> get(@PathVariable String username) {
    UserDTO user = userService.get(username);

    Set<BookDTO> books = libraryService.findBookIdByUsername(username).stream().map(bookService::get)
        .collect(Collectors.toSet());

    user.setBooks(books);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<UserDTO> add(@Valid @RequestBody UserDTO user) {
    return new ResponseEntity<>(userService.add(user), HttpStatus.OK);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<String> delete(@PathVariable String username) {

    return new ResponseEntity<>(userService.delete(username), HttpStatus.OK);
  }

  @PutMapping("/{username}")
  public ResponseEntity<UserDTO> update(@PathVariable("username") String username, @Valid @RequestBody UserDTO user) {
    return new ResponseEntity<>(userService.update(username, user), HttpStatus.OK);
  }

  @PostMapping("/{username}/books/{bookId}")
  public ResponseEntity<String> allocatedBookToUser(@PathVariable String username, @PathVariable int bookId) {

    userService.get(username);
    libraryService.issuedBookCount(username);
    bookService.get(bookId);

    return new ResponseEntity<>(libraryService.issueBookToUser(bookId, username), HttpStatus.OK);
  }

  @DeleteMapping("/{username}/books/{bookId}")
  public ResponseEntity<String> releaseBookFromUser(@PathVariable String username, @PathVariable int bookId) {
    return new ResponseEntity<>(libraryService.releaseBookFromUser(bookId, username), HttpStatus.OK);
  }


}
