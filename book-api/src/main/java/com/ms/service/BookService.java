package com.ms.service;

import java.util.List;

import com.ms.dto.BookDTO;

public interface BookService {

	BookDTO save(BookDTO book);
	
	BookDTO find(int id);
	
	List<BookDTO> findAll();
	
	String delete(int id);
	
	BookDTO update(BookDTO book);
	
}
