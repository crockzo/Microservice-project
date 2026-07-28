package com.ms.service;

import java.util.List;

public interface LibraryService {

	void deleteByBookId(int id);
	
	void deleteByUsername(String username);
	
	String issueBookToUser(int bookId, String username);
	
	String releaseBookFromUser(int bookId, String username);
	
	List<Integer> findBookIdByUsername(String username);
	
	int issuedBookCount(String username);
}
