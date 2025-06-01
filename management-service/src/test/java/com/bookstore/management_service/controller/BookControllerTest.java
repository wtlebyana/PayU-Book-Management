package com.bookstore.management_service.controller;

import com.bookstore.controller.BookController;
import com.bookstore.dto.BookDto;
import com.bookstore.model.BookType;
import com.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class BookControllerTest {

    private BookService bookService;
    private BookController bookController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        bookService = mock(BookService.class);
        bookController = new BookController(bookService);
    }

    @Test
    void listBooks_returnsAllBooks() {
        List<BookDto> mockBooks = Arrays.asList(
                new BookDto(
                        "9781617292545",
                        "Book 1",
                        LocalDate.of(2025, 9, 2),
                        BigDecimal.valueOf(150.00) ,
                        BookType.HARD_COVER,
                        "THABANG"),
                new BookDto(
                        "9786543210123",
                        "Book 2",
                        LocalDate.of(2025, 9, 2),
                        BigDecimal.valueOf(100.00),
                        BookType.SOFT_COVER,
                        "LEBYANA")
        );
        when(bookService.getAllBooks()).thenReturn(mockBooks);

        List<BookDto> result = bookController.listBooks();

        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getName());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void saveBook_returnsCreatedResponse() {
        BookDto input = new BookDto(
                "9781617292545",
                "Book 1",
                LocalDate.of(2025, 9, 2),
                BigDecimal.valueOf(150.00) ,
                BookType.HARD_COVER,
                "THABANG");

        BookDto saved = new BookDto(
                "9781617292545",
                "Book 1",
                LocalDate.of(2025, 9, 2),
                BigDecimal.valueOf(150.00) ,
                BookType.HARD_COVER,
                "THABANG");

        when(bookService.saveBook(input)).thenReturn(saved);

        ResponseEntity<BookDto> response = bookController.saveBook(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved, response.getBody());
        verify(bookService, times(1)).saveBook(input);
    }

    @Test
    void updateBook_returnsUpdatedBook() {
        String isbn = "9789987562909";
        BookDto input = new BookDto(
                "9789987562909",
                "Book 1",
                LocalDate.of(2025, 9, 2),
                BigDecimal.valueOf(150.00) ,
                BookType.HARD_COVER,
                "THABANG");

        when(bookService.updateBook(eq(isbn), any(BookDto.class))).thenReturn(input);
        ResponseEntity<BookDto> response = bookController.updateBook(isbn, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(input, response.getBody());
        verify(bookService).updateBook(isbn, input);
    }
    @Test
    void getBookByIsbn_returnsBookDto() {
        String isbn = "9781617292545";

        BookDto bookDto = new BookDto();
        bookDto.setIsbn(isbn);
        bookDto.setName("Test Book");
        bookDto.setAuthor("Test Author");
        bookDto.setBookType(BookType.HARD_COVER);
        bookDto.setPrice(BigDecimal.valueOf(100.0));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("01/06/2025", formatter);

        bookDto.setPublishDate(date);

        when(bookService.getBookByIsbn(isbn)).thenReturn(bookDto);

        ResponseEntity<BookDto> response = bookController.getBookByIsbn(isbn);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(isbn, response.getBody().getIsbn());
        assertEquals("Test Book", response.getBody().getName());

        verify(bookService).getBookByIsbn(isbn);
    }


    @Test
    void deleteBook_returnsNoContent() {
        String isbn = "97816172925455";
        doNothing().when(bookService).deleteBook(isbn);

        ResponseEntity<Void> response = bookController.deleteBook(isbn);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService).deleteBook(isbn);
    }
}
