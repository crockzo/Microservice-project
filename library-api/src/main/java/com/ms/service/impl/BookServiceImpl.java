package com.ms.service.impl;

import com.ms.client.webclient.BookWebClientUtil;
import com.ms.dto.BookDTO;
import com.ms.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
  //  public final BookClient bookClient;
  public final BookWebClientUtil bookClient;

  @Override
  public List<BookDTO> getAll() {
    log.info("Calling Book service to get all books.");
    List<BookDTO> books = bookClient.getAll();
    log.info("Total books found : {}", books.size());
    return books;
  }

  @Override
  public BookDTO get(int id) {
    log.info("Calling book service to get book by id : {}", id);
    BookDTO book = bookClient.get(id);
    log.info("Book by book id {} found.", id);
    return book;
  }

  @Override
  public BookDTO add(BookDTO book) {
    log.info("Calling book service to create new book.");
    BookDTO newBook = bookClient.add(book);
    log.info("New Book created with id : {}", newBook.getId());
    return newBook;
  }

  @Override
  public String delete(int id) {
    log.info("Calling book service to delete book id : {}.", id);
    String response = bookClient.delete(id);
    log.info("Response from book delete by id endpoint : {}", response);
    return response;
  }

  @Override
  public BookDTO update(int id, BookDTO book) {
    log.info("Calling book service to update book by book id : {}.", id);
    BookDTO updatedBook = bookClient.update(id, book);
    log.info("Book with book id : {} updated successfully.", updatedBook.getId());
    return updatedBook;
  }
}
