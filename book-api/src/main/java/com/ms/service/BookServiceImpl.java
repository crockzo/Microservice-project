package com.ms.service;

import com.ms.dto.BookDTO;
import com.ms.exception.BookNotFoundException;
import com.ms.model.Book;
import com.ms.observability.annotation.Observed;
import com.ms.repository.BookRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.MeterTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository repository;

  private final ModelMapper mapper;

  @Override
  public BookDTO save(BookDTO book) {
    log.info("Saving new book to db : {}", book);
    Book bookEntity = toEntity(book);
    BookDTO newBook = toDto(repository.save(bookEntity));
    log.info("Book saved successfully.");
    return newBook;
  }

  @Override
  @Observed(name = "book.fetch.observed")
  public BookDTO find(int id) {
    log.info("Finding book by id :{} from db.", id);
    BookDTO book = toDto(getBookById(id));
    log.info("Found book by id :{} from db.", book);
    return (book);
  }

  @Override
  public List<BookDTO> findAll() {
    log.info("Find all books from db.");
    List<BookDTO> books = repository.findAll().stream().map(this::toDto).toList();
    log.info("Found {} books from db.", books.size());
    return books;
  }

  @Override
  public String delete(int id) {
    log.info("Deleting book from db by id : {}", id);
    if (!repository.existsById(id)) {
      log.error("Book by book id {} not found in db.", id);
      throw new BookNotFoundException("Book by given id not found.");
    }

    repository.deleteById(id);
    log.info("Book deleted successfully.");
    return "Book Deleted";
  }

  @Override
  public BookDTO update(BookDTO book) {
    Book bookById = getBookById(book.getId());
    log.info("Book details before updating : {}", bookById);
    Book bookEntity = repository.save(toEntity(book));
    BookDTO updatedBook = toDto(bookEntity);
    log.info("Book details after updating : {}", updatedBook);
    return updatedBook;
  }


  private Book getBookById(int id) {
    return repository.findById(id).orElseThrow(() -> new BookNotFoundException("Book by given id not found."));
  }

  private BookDTO toDto(Book book) {
    return mapper.map(book, BookDTO.class);
  }

  private Book toEntity(BookDTO dto) {
    return mapper.map(dto, Book.class);
  }
}
