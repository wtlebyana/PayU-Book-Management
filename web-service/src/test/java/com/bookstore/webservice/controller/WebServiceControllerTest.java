package com.bookstore.webservice.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.model.BookType;
import com.bookstore.webservice.dto.BookRequestDto;
import com.bookstore.webservice.service.WebApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(WebServiceController.class)
public class WebServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebApiService webApiService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        bookDto = createBookDto();
    }

    @Test
    void getAllBooks_returnsListOfBooks() throws Exception {
        Mockito.when(webApiService.getAllBooks()).thenReturn(Arrays.asList(bookDto));

        mockMvc.perform(get("/api/v1/books/findAllBooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Book"))
                .andExpect(jsonPath("$[0].isbn").value("9781617292545"));
    }

    @Test
    void saveBook_returnsCreatedBook() throws Exception {
        Mockito.when(webApiService.createBook(any(BookDto.class))).thenReturn(bookDto);

        mockMvc.perform(post("/api/v1/books/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequestDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("9781617292545"))
                .andExpect(jsonPath("$.name").value("Test Book"));
    }


    @Test
    void updateBook_returnsUpdatedBook() throws Exception {
        Mockito.when(webApiService.updateBook(eq("9781617292545"), any(BookDto.class))).thenReturn(createBookDto());

        mockMvc.perform(put("/api/v1/books/update/9781617292545")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Book"))
                .andExpect(jsonPath("$.bookType").value("SOFT_COVER"));
    }

    @Test
    void deleteBook_returnsNoContentOnSuccess() throws Exception {
        Mockito.when(webApiService.deleteBook("9781617292545")).thenReturn(true);
        mockMvc.perform(delete("/api/v1/books/delete/9781617292545"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetBookByIsbn_notFound() throws Exception {
        when(webApiService.getBookByIsbn(anyString())).thenReturn(null);

        mockMvc.perform(get("/api/v1/books/1524526256256")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBook_returnsNotFoundWhenBookMissing() throws Exception {
        Mockito.when(webApiService.deleteBook("0000000000000")).thenReturn(false);
        mockMvc.perform(delete("/api/v1/books/delete/0000000000000"))
                .andExpect(status().isNotFound());
    }

    private BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setIsbn("9781617292545");
        bookDto.setName("Test Book");
        bookDto.setBookType(BookType.SOFT_COVER);
        return bookDto;
    }

    private BookDto createBookRequestDto() {
        BookDto bookDto = new BookDto();
        bookDto.setName("Test Book");
        bookDto.setBookType(BookType.SOFT_COVER);
        return bookDto;
    }


}
