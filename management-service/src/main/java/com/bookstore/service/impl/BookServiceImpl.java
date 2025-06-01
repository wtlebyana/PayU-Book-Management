package com.bookstore.service.impl;

import com.bookstore.dto.BookDto;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import com.bookstore.util.IsbnGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookRepository repository, BookMapper bookMapper) {
        this.repository = repository;
        this.bookMapper = bookMapper;
    }


    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = repository.findAll();

        return bookMapper.toDtoList(books);
    }


    @Override
    public BookDto saveBook(BookDto dto) {
        Book book = mapToEntity(dto);

        String isbn;
        do {
            isbn = IsbnGenerator.generateIsbn();
        } while (repository.existsByIsbn(isbn));

        book.setIsbn(isbn);

        Book savedBook = repository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookDto updateBook(String isbn, BookDto dto) {
        Book existing = repository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));

        validateBookDto(dto);

        existing.setName(dto.getName());
        existing.setAuthor(dto.getAuthor());
        existing.setPrice(dto.getPrice());
        existing.setPublishDate(dto.getPublishDate());
        existing.setBookType(dto.getBookType());

        Book updatedBook = repository.save(existing);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void deleteBook(String isbn) {
        Book existing = repository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
        repository.delete(existing);
    }

    public BookDto getBookByIsbn(String isbn) {
        Optional<Book> bookOptional = repository.findByIsbn(isbn);
        return bookOptional.map(this::convertToDto).orElse(null);
    }


    private void validateBookDto(BookDto dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()
                || dto.getAuthor() == null || dto.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid book: missing name or author");
        }
    }

    private Book mapToEntity(BookDto dto) {
        Book book = new Book();
        book.setName(dto.getName());
        book.setIsbn(dto.getIsbn());
        book.setPublishDate(dto.getPublishDate());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setBookType(dto.getBookType());
        return book;
    }

    private BookDto convertToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setIsbn(book.getIsbn());
        dto.setName(book.getName());
        dto.setAuthor(book.getAuthor());
        dto.setBookType(book.getBookType());
        dto.setPrice(book.getPrice());
        dto.setPublishDate(book.getPublishDate());
        return dto;
    }
}
