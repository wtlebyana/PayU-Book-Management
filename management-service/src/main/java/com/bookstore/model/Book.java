package com.bookstore.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String author;
    @Column(unique = true, nullable = false, length = 13)
    private String isbn;
    private LocalDate publishDate;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private BookType bookType;}
