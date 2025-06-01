package com.bookstore.webservice.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.model.BookType;
import com.bookstore.webservice.service.WebApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebPageControllerTest {

    @Mock
    private WebApiService webApiService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private WebPageController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void listBooks_ShouldAddBooksAndBookTypesToModel_AndReturnViewName() {
        List<BookDto> books = Arrays.asList(createSampleBook());
        when(webApiService.getAllBooks()).thenReturn(books);
        String viewName = controller.listBooks(model);
        verify(model).addAttribute("books", books);
        verify(model).addAttribute("bookTypes", BookType.values());
        assertEquals("books", viewName);
    }

    private BookDto createSampleBook() {
        BookDto book = new BookDto();
        book.setName("Test Book Management");
        book.setAuthor("THABANG");
        book.setIsbn("9781617292545");
        book.setPrice(BigDecimal.valueOf(29.99));
        book.setPublishDate(LocalDate.of(2020, 1, 1));
        book.setBookType(BookType.HARD_COVER);
        return book;
    }

}
