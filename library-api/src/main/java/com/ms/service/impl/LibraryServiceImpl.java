package com.ms.service.impl;

import com.ms.exception.BookAlreadyIssuedException;
import com.ms.exception.BookIssueLimitPerUserException;
import com.ms.exception.BookNotAllocatedToUserException;
import com.ms.model.Library;
import com.ms.repository.LibraryRepository;
import com.ms.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

  private final LibraryRepository repo;

  @Override
  @Transactional
  public void deleteByBookId(int id) {
    repo.deleteByBookId(id);
  }

  @Override
  @Transactional
  public void deleteByUsername(String username) {
    repo.deleteByUsername(username);
  }

  @Override
  public String issueBookToUser(int bookId, String username) {
    if (repo.existsByUsernameAndBookId(username, bookId))
      throw new BookAlreadyIssuedException("User already have this book allocated");
    log.info("Allocating book id : {} to username {},", bookId, username);
    repo.save(new Library(bookId, username));
    log.info("Book allocated to user successfully.");
    return "Book Issued to User";
  }

  @Override
  @Transactional
  public String releaseBookFromUser(int bookId, String username) {
    if (!repo.existsByUsernameAndBookId(username, bookId))
      throw new BookNotAllocatedToUserException("Book is not issued to the user");
    log.info("Releasing book id : {} from username {},", bookId, username);
    repo.deleteByUsernameAndBookId(username, bookId);
    log.info("Book released from user.");
    return "Book Release from user";
  }

  @Override
  public List<Integer> findBookIdByUsername(String username) {

    return repo.getBookIdByUsername(username);
  }

  @Override
  public int issuedBookCount(String username) {
    log.info("Checking db to get books count allocated to the user.");
    int bookCount = repo.countByUsername(username);
    log.info("user by username : {} has {} books", username, bookCount);
    if (bookCount >= 3) throw new BookIssueLimitPerUserException("Already have 3 book issued");
    return bookCount;
  }


}
