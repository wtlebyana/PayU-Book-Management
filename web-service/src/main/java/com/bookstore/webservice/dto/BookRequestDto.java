package com.bookstore.webservice.dto;

import com.bookstore.model.BookType;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookRequestDto {

    private String name;
    private LocalDate publishDate;
    private BigDecimal price;
    private BookType bookType;
    private String author;

}
