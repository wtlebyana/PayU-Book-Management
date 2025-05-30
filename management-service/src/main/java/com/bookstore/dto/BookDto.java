package com.bookstore.dto;

import com.bookstore.model.BookType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookDto {
    private String isbn;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;
    private BigDecimal price;
    private BookType bookType;
    private String author;

}
