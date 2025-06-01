package com.bookstore.service;


import com.bookstore.dto.BookDto;

import java.util.List;


public interface BookService{

    List<BookDto> getAllBooks();
    BookDto saveBook(BookDto bookDto);
    BookDto updateBook(String isbn, BookDto book);
    void deleteBook(String isbn);
    BookDto getBookByIsbn(String isbn);

}
