package com.bookstore.webservice.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.webservice.dto.BookRequestDto;
import com.bookstore.webservice.dto.BookResponseDto;
import com.bookstore.webservice.service.WebApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books")

public class WebServiceController {

    private final WebApiService webApiService;
    @Autowired
    public WebServiceController(WebApiService webApiService) {
        this.webApiService = webApiService;
    }

    @GetMapping("/findAllBooks")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        try {
            List<BookResponseDto> books = webApiService.getAllBooks().stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<BookResponseDto> saveBook(@RequestBody BookRequestDto requestDto) {
        try {
            BookDto bookDto = mapToDto(requestDto);
            BookDto createdBook = webApiService.createBook(bookDto);

            if (createdBook != null) {
                BookResponseDto responseDto = mapToResponseDto(createdBook);
                return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{isbn}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable String isbn,
            @RequestBody BookRequestDto bookRequestDto) {

        BookDto bookDto = mapToDto(bookRequestDto);
        BookDto updatedBook = webApiService.updateBook(isbn, bookDto);

        if (updatedBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("delete/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        try {
            boolean deleted = webApiService.deleteBook(isbn);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    private BookResponseDto mapToResponseDto(BookDto dto) {
        BookResponseDto response = new BookResponseDto();
        response.setName(dto.getName());
        response.setPrice(dto.getPrice());
        response.setIsbn(dto.getIsbn());
        response.setPublishDate(dto.getPublishDate());
        response.setBookType(dto.getBookType());
        response.setAuthor(dto.getAuthor());
        return response;
    }

    private BookDto mapToDto(BookRequestDto dto) {
        BookDto book = new BookDto();
        book.setName(dto.getName());
        book.setPrice(dto.getPrice());
        book.setPublishDate(dto.getPublishDate());
        book.setBookType(dto.getBookType());
        book.setAuthor(dto.getAuthor());
        return book;
    }

}
