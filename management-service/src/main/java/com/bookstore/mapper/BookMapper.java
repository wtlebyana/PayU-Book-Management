package com.bookstore.mapper;// src/main/java/com/bookstore/managementservice/mapper/BookMapper.java

import com.bookstore.dto.BookDto;
import com.bookstore.model.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
BookDto toDto(Book book);
    Book toEntity(BookDto dto);
    List<BookDto> toDtoList(List<Book> books);
    List<Book> toEntityList(List<BookDto> dtos);
}
