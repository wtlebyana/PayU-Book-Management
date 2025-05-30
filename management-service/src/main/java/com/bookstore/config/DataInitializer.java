package com.bookstore.config;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;
import com.bookstore.repository.BookRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer {

    private final BookRepository bookRepository;

    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void init() {
        if (bookRepository.count() == 0) {
            Book book = new Book();
            book.setName("BOOK MANAGEMENT BACKEND 101");
            book.setAuthor("WILLIAM THABANG");
            book.setIsbn("9781617292545");
            book.setPublishDate(LocalDate.parse("2025-03-15"));
            book.setPrice(new BigDecimal("100.00"));
            book.setBookType(BookType.HARD_COVER);
            bookRepository.save(book);
        }
    }
}
