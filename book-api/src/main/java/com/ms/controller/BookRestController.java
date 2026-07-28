package com.ms.controller;

import com.ms.dto.BookDTO;
import com.ms.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@RestController
@RequestMapping("/books")
@Slf4j
@RequiredArgsConstructor
public class BookRestController {

  private final BookService service;

  @GetMapping
  public ResponseEntity<List<BookDTO>> getAll() {
    return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
  }

  @GetMapping("/{book_id}")
  public ResponseEntity<BookDTO> get(@PathVariable("book_id") int id) {
    log.info("Fetch all the books from MySql DB.");
    return new ResponseEntity<>(service.find(id), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<BookDTO> add(@Valid @RequestBody BookDTO book) {
    return new ResponseEntity<>(service.save(book), HttpStatus.OK);
  }

  @DeleteMapping("/{book_id}")
  public ResponseEntity<String> delete(@PathVariable("book_id") int id) {
    return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
  }

  @PutMapping("/{book_id}")
  public ResponseEntity<BookDTO> update(
      @PathVariable("book_id") int id, @Valid @RequestBody BookDTO book) {
    book.setId(id);
    return new ResponseEntity<>(service.update(book), HttpStatus.OK);
  }
}
