package com.bookstore.management_service.service;

import com.bookstore.dto.BookDto;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.model.BookType;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {


    @Mock
    private BookRepository repository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(BookRepository.class);
        bookMapper = mock(BookMapper.class);
        service = new BookServiceImpl(repository, bookMapper);
    }

    @Test
    void getAllBooks_returnsMappedList() {
        Book book = new Book();
        List<Book> books = Arrays.asList(book);

        BookDto dto = new BookDto();
        List<BookDto> dtos = Arrays.asList(dto);

        when(repository.findAll()).thenReturn(books);
        when(bookMapper.toDtoList(books)).thenReturn(dtos);

        List<BookDto> result = service.getAllBooks();
        assertEquals(1, result.size());
        verify(repository).findAll();
        verify(bookMapper).toDtoList(books);
    }


    @Test
    void saveBook_generatesUniqueIsbn_andSavesBook() {
        BookDto inputDto = new BookDto();
        inputDto.setName("Book 1");
        inputDto.setAuthor("Test Author");
        inputDto.setPrice(BigDecimal.TEN);
        inputDto.setPublishDate(LocalDate.of(2025, 9, 2));
        inputDto.setBookType(BookType.HARD_COVER);

        when(repository.existsByIsbn(anyString())).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        BookDto outputDto = new BookDto();
        when(bookMapper.toDto(any(Book.class))).thenReturn(outputDto);

        BookDto result = service.saveBook(inputDto);

        assertNotNull(result);
        verify(repository).save(any(Book.class));
        verify(bookMapper).toDto(any(Book.class));
    }

    @Test
    void updateBook_existingBook_updatesAndReturnsDto() {
        String isbn = "9781617292545";
        Book existing = new Book();
        existing.setIsbn(isbn);

        BookDto dto = new BookDto();

        dto.setIsbn("9781617292545");
        dto.setName("THABANG");
        dto.setAuthor("W.T LEBYANA");
        dto.setPrice(BigDecimal.valueOf(15.5));
        dto.setPublishDate(LocalDate.of(2025, 9, 2));
        dto.setBookType(BookType.EBOOK);

        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        BookDto mappedDto = new BookDto();
        when(bookMapper.toDto(existing)).thenReturn(mappedDto);

        BookDto result = service.updateBook(isbn, dto);

        assertEquals(mappedDto, result);
        assertEquals("THABANG", existing.getName());
        assertEquals("W.T LEBYANA", existing.getAuthor());
        assertEquals(BigDecimal.valueOf(15.5), existing.getPrice());
        verify(repository).save(existing);
    }

    @Test
    void deleteBook_whenNotFound_throwsIllegalArgumentException() {
        String isbn = "0000000000000";
        when(repository.findByIsbn(isbn)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> service.deleteBook(isbn));
    }

}
