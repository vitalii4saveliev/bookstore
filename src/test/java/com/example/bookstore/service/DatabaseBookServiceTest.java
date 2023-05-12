package com.example.bookstore.service;


import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.SearchRequest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.specification.BookSpecification;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.impl.DatabaseBookService;
import com.example.bookstore.util.mapper.AuthorMapper;
import com.example.bookstore.util.mapper.BookMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DatabaseBookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private BookSpecification bookSpecification;

    private DatabaseBookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new DatabaseBookService(bookRepository, authorRepository, bookMapper, authorMapper, bookSpecification);
    }

    @Test
    public void testFindBooksByFilterAndPage() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setField("title");
        searchRequest.setOperator("equals");
        searchRequest.setValue("Title");
        searchRequest.setSortDirection("ASC");
        searchRequest.setSortField("publicationDate");
        searchRequest.setPage(0);
        searchRequest.setPageSize(10);

        Specification<Book> expectedSpecification = bookSpecification.hasField("title", "equals", "Title");

        List<Book> expectedBooks = new ArrayList<>();
        Author author = new Author("John", "Doe", Date.valueOf("1990-01-01"));
        Book book1 = new Book(1L, "Title", "ISBN123456789", Date.valueOf("2022-01-01"), BigDecimal.valueOf(10.99), author);
        Book book2 = new Book(2L, "Title", "ISBN987654321", Date.valueOf("2021-12-31"), BigDecimal.valueOf(9.99), author);
        expectedBooks.add(book1);
        expectedBooks.add(book2);

        Page<Book> expectedPage = new PageImpl<>(expectedBooks);

        Mockito.when(bookSpecification.hasField("title", "equals", "Title")).thenReturn(expectedSpecification);
        Mockito.when(bookRepository.findAll(expectedSpecification, PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "publicationDate")))).thenReturn(expectedPage);

        Page<Book> resultPage = bookService.findBooksByFilterAndPage(searchRequest);

        assertEquals(expectedPage, resultPage);
        verify(bookSpecification, times(2)).hasField("title", "equals", "Title");
        verify(bookRepository).findAll(expectedSpecification, PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "publicationDate")));
    }


    @Test
    public void testGetAllBooks() {
        List<Book> expectedBooks = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.getAllBooks();

        assertEquals(expectedBooks, actualBooks);
        verify(bookRepository).findAll();
    }

    @Test
    public void testGetBookById_Found() {
        Long bookId = 1L;
        Book expectedBook = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));

        Book actualBook = bookService.getBookById(bookId);

        assertEquals(expectedBook, actualBook);
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void testGetBookById_NotFound() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(bookId));
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void testCreateBook() {
        AuthorDTO authorDTO = new AuthorDTO(1L, "test", "test", Date.valueOf("1994-04-12"));
        BookDTO bookDTO = new BookDTO(1L, "title", "isbn", Date.valueOf("1788-11-09"), BigDecimal.ONE, authorDTO);
        Author expectedAuthor = new Author("test", "test", Date.valueOf("1994-04-12"));
        Book expectedBook = new Book(1L, "title", "isbn", Date.valueOf("1788-11-09"), BigDecimal.ONE, expectedAuthor);

        when(bookMapper.toEntity(bookDTO)).thenReturn(expectedBook);
        when(authorRepository.findByFirstNameAndLastNameAndBirthday(expectedAuthor.getFirstName(), expectedAuthor.getLastName(), expectedAuthor.getBirthday())).thenReturn(Optional.of(expectedAuthor));
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);

        Book actual = bookService.createBook(bookDTO);
        assertEquals(expectedBook, actual);
        assertEquals(expectedAuthor, actual.getAuthor());

        verify(bookMapper).toEntity(bookDTO);
        verify(authorRepository).findByFirstNameAndLastNameAndBirthday(expectedAuthor.getFirstName(), expectedAuthor.getLastName(), expectedAuthor.getBirthday());
        verify(bookRepository).save(expectedBook);
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO(1L, "Updated Title", "Updated ISBN", Date.valueOf("2000-01-01"), BigDecimal.TEN, new AuthorDTO(1L, "Test", "Author", Date.valueOf("1990-01-01")));
        Book existingBook = new Book(bookId, "Old Title", "Old ISBN", Date.valueOf("1990-01-01"), BigDecimal.ONE, new Author("Test", "Author", Date.valueOf("1990-01-01")));
        Book updatedBook = new Book(bookId, "Updated Title", "Updated ISBN", Date.valueOf("2000-01-01"), BigDecimal.TEN, new Author("Test", "Author", Date.valueOf("1990-01-01")));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookMapper.updateEntity(existingBook, bookDTO)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        Book actual = bookService.updateBook(bookId, bookDTO);

        assertEquals(updatedBook, actual);
        assertEquals(updatedBook.getAuthor(), actual.getAuthor());

        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateEntity(existingBook, bookDTO);
        verify(bookRepository).save(updatedBook);
    }

}
