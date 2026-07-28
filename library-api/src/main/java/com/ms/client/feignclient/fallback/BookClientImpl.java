package com.ms.client.feignclient.fallback;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Service;

import com.ms.dto.BookDTO;
import com.ms.client.feignclient.BookClient;

@Service
public class BookClientImpl implements BookClient{

	@Override
	public List<BookDTO> getAll() {
		return List.of(getDummyBook());
	}

	@Override
	public BookDTO get(int id) {
		return getDummyBook();
	}

	@Override
	public BookDTO add(BookDTO book) {
		return getDummyBook();
	}

	@Override
	public String delete(int id) {
		return "Not delete - Fallback";
	}

	@Override
	public BookDTO update(int id, @Valid BookDTO book) {
		return getDummyBook();
	}
	
	private BookDTO getDummyBook() {
		BookDTO book = new BookDTO();
		book.setAuthor("Fallback Author");
		book.setName("Fallback name");
		book.setPublisher("Fallback publisher");
		book.setId(0);
		return book;
	}
	
}
