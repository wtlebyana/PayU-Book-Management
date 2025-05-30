package com.bookstore.controller;

import com.bookstore.dto.BookDto;
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

    @PutMapping("/{isbn}")
    public ResponseEntity<BookDto> updateBook(@PathVariable String isbn, @RequestBody BookDto dto) {
        BookDto updatedBook = service.updateBook(isbn, dto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        service.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }

}
