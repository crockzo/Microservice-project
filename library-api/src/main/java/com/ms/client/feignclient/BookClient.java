package com.ms.client.feignclient;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ms.dto.BookDTO;
import com.ms.client.feignclient.fallback.BookClientImpl;

@FeignClient(name="book-api", fallback = BookClientImpl.class)
@LoadBalancerClient(name = "book-api")
public interface BookClient {

	@GetMapping("/books")
	List<BookDTO> getAll();
	
	@GetMapping("/books/{book_id}")
	BookDTO get(@PathVariable("book_id") int id);
	
	@PostMapping("/books")
	BookDTO add(@RequestBody BookDTO book);

	@DeleteMapping("/books/{book_id}")
	String delete(@PathVariable("book_id") int id);
	
	@PutMapping("/books/{book_id}")
	BookDTO update(@PathVariable("book_id") int id, @Valid @RequestBody BookDTO book);
}
