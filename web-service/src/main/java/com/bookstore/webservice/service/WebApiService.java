package com.bookstore.webservice.service;

import com.bookstore.dto.BookDto;
import java.util.List;
public interface WebApiService {

    List<BookDto> getAllBooks();
    BookDto createBook(BookDto bookDto);
    BookDto updateBook(String isbn, BookDto book);
    boolean deleteBook(String isbn);
    BookDto getBookByIsbn(String isbn);
}
