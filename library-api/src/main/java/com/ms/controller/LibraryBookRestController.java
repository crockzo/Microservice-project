package com.ms.controller;

import java.util.List;

import com.ms.service.BookService;
import jakarta.validation.Valid;

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

import com.ms.dto.BookDTO;

@RestController
@RequestMapping("/library/books")
public class LibraryBookRestController {

  @Autowired
  private BookService bookService;

  @GetMapping
  public ResponseEntity<List<BookDTO>> getAllBook() {
    return new ResponseEntity<>(bookService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/{bookId}")
  public ResponseEntity<BookDTO> get(@PathVariable("bookId") int id) {

    return new ResponseEntity<>(bookService.get(id), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<BookDTO> add(@Valid @RequestBody BookDTO book) {
    return new ResponseEntity<>(bookService.add(book), HttpStatus.OK);
  }

  @DeleteMapping("/{bookId}")
  public ResponseEntity<String> delete(@PathVariable("bookId") int id) {
    return new ResponseEntity<>(bookService.delete(id), HttpStatus.OK);
  }

  @PutMapping("/{bookId}")
  public ResponseEntity<BookDTO> update(@PathVariable("bookId") int id, @Valid @RequestBody BookDTO book) {
    book.setId(id);
    return new ResponseEntity<>(bookService.update(id, book), HttpStatus.OK);
  }

}
