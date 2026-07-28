package com.ms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ms.model.Library;

public interface LibraryRepository extends JpaRepository<Library, Integer>{

	@Query("select bookId from Library where username = ?1")
	List<Integer> getBookIdByUsername(String username);
	
	@Modifying
	void deleteByUsername(String name);
	
	@Modifying
	void deleteByBookId(int id);
	
	boolean existsByUsernameAndBookId(String username, int bookId);
	
	@Modifying
	void deleteByUsernameAndBookId(String username, int bookId);
	
	int countByUsername(String username);
}
