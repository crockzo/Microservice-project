package com.ms.service;

import com.ms.dto.BookDTO;

import java.util.List;

public interface BookService {

  List<BookDTO> getAll();

  BookDTO get(int id);

  BookDTO add(BookDTO book);

  String delete(int id);

  public BookDTO update(int id, BookDTO book);
}
