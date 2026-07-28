package com.ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{

	
}
