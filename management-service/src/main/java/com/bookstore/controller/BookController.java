package com.bookstore.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private final BookService service;

    @GetMapping
    public List<BookDto> listBooks() {
        return service.getAllBooks();
    }

    @PostMapping
    public ResponseEntity<BookDto> saveBook(@RequestBody BookDto dto) {
        BookDto savedBook = service.saveBook(dto);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @PutMapping("/update/{isbn}")
    public ResponseEntity<BookDto> updateBook(@PathVariable String isbn, @RequestBody BookDto dto) {
        if (dto.getIsbn() != null && !isbn.equals(dto.getIsbn())) {
            // ISBN in path and body don't match — respond with Bad Request
            return ResponseEntity.badRequest().build();
        }

        try {
            BookDto updatedBook = service.updateBook(isbn, dto);
            if (updatedBook == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedBook);
        } catch (BookNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@PathVariable String isbn) {
        BookDto bookDto = service.getBookByIsbn(isbn);
        if (bookDto != null) {
            return ResponseEntity.ok(bookDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        service.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }

}
