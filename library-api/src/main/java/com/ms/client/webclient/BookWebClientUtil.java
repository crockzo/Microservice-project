package com.ms.client.webclient;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ms.dto.BookDTO;
import com.ms.dto.ExceptionResponse;

@Component
public class BookWebClientUtil {

  private final static String BOOK_SERVICE_BASE_URL = "http://localhost:8082/books";

  private final WebClient bookClient;


  public BookWebClientUtil(WebClient.Builder builder) {
    bookClient = builder.baseUrl(BOOK_SERVICE_BASE_URL).build();
  }


  public BookDTO get(int bookId) {
    Object response = bookClient.get().uri("/{bookId}", bookId).exchangeToMono(resp -> {
      if (resp.statusCode().equals(HttpStatus.OK)) {
        return resp.bodyToMono(BookDTO.class);
      } else {
        return resp.bodyToMono(ExceptionResponse.class);
      }
    }).block();

    checkIfResponseIsException(response);
    return (BookDTO) response;

  }

  public List<BookDTO> getAll() {
    return bookClient.get().retrieve().bodyToFlux(BookDTO.class).collectList().block();
  }

  public BookDTO add(BookDTO book) {
    Object response = bookClient.post().bodyValue(book).exchangeToMono(resp -> {
      if (resp.statusCode().equals(HttpStatus.OK)) {
        return resp.bodyToMono(BookDTO.class);
      } else {
        return resp.bodyToMono(ExceptionResponse.class);
      }
    }).block();

    checkIfResponseIsException(response);
    return (BookDTO) response;
  }

  public String delete(int id) {
    Object response = bookClient.delete().uri("/{id}", id).exchangeToMono(resp -> {
      if (resp.statusCode().equals(HttpStatus.OK)) {
        return resp.bodyToMono(String.class);
      } else {
        return resp.bodyToMono(ExceptionResponse.class);
      }
    }).block();

    checkIfResponseIsException(response);
    return (String) response;
  }

  public BookDTO update(int id, BookDTO book) {
    book.setId(id);
    Object response = bookClient.put().uri("/{id}", id).bodyValue(book).exchangeToMono(resp -> {
      if (resp.statusCode().equals(HttpStatus.OK)) {
        return resp.bodyToMono(BookDTO.class);
      } else {
        return resp.bodyToMono(ExceptionResponse.class);
      }
    }).block();

    checkIfResponseIsException(response);
    return (BookDTO) response;
  }

  private void checkIfResponseIsException(Object response) {
    if (response instanceof ExceptionResponse)
      throw new RuntimeException(((ExceptionResponse) response).getError());
  }

}
