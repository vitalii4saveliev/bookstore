package com.example.bookstore.service;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.dto.SearchRequest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.specification.AuthorSpecification;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.impl.DatabaseAuthorService;
import com.example.bookstore.util.mapper.AuthorMapper;
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

@ExtendWith(MockitoExtension.class)
public class DatabaseAuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private AuthorSpecification authorSpecification;

    private DatabaseAuthorService authorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        authorService = new DatabaseAuthorService(authorRepository, bookRepository, authorMapper, authorSpecification);
    }

    @Test
    public void testFindAuthorsByFilterAndPage() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setField("firstName");
        searchRequest.setOperator("equals");
        searchRequest.setValue("John");
        searchRequest.setSortField("lastName");
        searchRequest.setSortDirection("asc");
        searchRequest.setPage(0);
        searchRequest.setPageSize(10);

        List<Author> expectedAuthors = new ArrayList<>();
        expectedAuthors.add(new Author("John", "Doe", Date.valueOf("1990-01-01")));
        expectedAuthors.add(new Author("John", "Smith", Date.valueOf("1985-05-15")));
        Page<Author> expectedPage = new PageImpl<>(expectedAuthors);

        Specification<Author> expectedSpecification = authorSpecification.hasField("firstName", "equals", "John");
        Sort expectedSort = Sort.by(Sort.Direction.ASC, "lastName");
        PageRequest expectedPageRequest = PageRequest.of(0, 10, expectedSort);

        Mockito.when(authorSpecification.hasField("firstName", "equals", "John")).thenReturn(expectedSpecification);
        Mockito.when(authorRepository.findAll(expectedSpecification, expectedPageRequest)).thenReturn(expectedPage);

        Page<Author> resultPage = authorService.findAuthorsByFilterAndPage(searchRequest);

        assertEquals(expectedPage, resultPage);
        verify(authorSpecification, times(2)).hasField("firstName", "equals", "John");
        verify(authorRepository).findAll(expectedSpecification, expectedPageRequest);
    }



    @Test
    public void testFindAllAuthors() {
        List<Author> expectedAuthors = List.of(new Author("Doe", "John", Date.valueOf("1994-09-12")), new Author("Jane", "Smith", Date.valueOf("1884-12-01")));

        Mockito.when(authorRepository.findAll()).thenReturn(expectedAuthors);

        List<Author> resultAuthors = authorService.findAllAuthors();

        assertEquals(expectedAuthors, resultAuthors);
        verify(authorRepository).findAll();
    }

    @Test
    public void testFindBooksByAuthor() {
        Long authorId = 1L;
        Author author = new Author("Doe", "John", Date.valueOf("1875-01-01"));
        List<Book> expectedBooks = List.of(new Book(1L, "Book 1", "ISBN1", Date.valueOf("1777-07-07"), BigDecimal.ONE, author),
                new Book(2L, "Book 2", "ISBN2", Date.valueOf("1777-07-07"), BigDecimal.ONE, author));

        Mockito.when(authorRepository.findByIdWithBooks(authorId)).thenReturn(Optional.of(author));
        Mockito.when(bookRepository.findAllByAuthor(author)).thenReturn(expectedBooks);

        List<Book> resultBooks = authorService.findBooksByAuthor(authorId);

        assertEquals(expectedBooks, resultBooks);
        verify(authorRepository).findByIdWithBooks(authorId);
        verify(bookRepository).findAllByAuthor(author);
    }

    @Test
    public void testFindAuthorById() {
        Long authorId = 1L;
        Author expectedAuthor = new Author("Doe", "John", Date.valueOf("1994-04-04"));

        Mockito.when(authorRepository.findByIdWithBooks(authorId)).thenReturn(Optional.of(expectedAuthor));

        Author resultAuthor = authorService.findAuthorById(authorId);

        assertEquals(expectedAuthor, resultAuthor);
        verify(authorRepository).findByIdWithBooks(authorId);
    }

    @Test
    public void testCreateAuthor() {
        AuthorDTO authorDTO = new AuthorDTO(1L,"John", "Doe", Date.valueOf("1999-09-09"));
        Author expectedAuthor = new Author("John", "Doe", Date.valueOf("1999-09-09"));

        Mockito.when(authorMapper.toEntity(authorDTO)).thenReturn(expectedAuthor);
        Mockito.when(authorRepository.save(expectedAuthor)).thenReturn(expectedAuthor);

        Author resultAuthor = authorService.createAuthor(authorDTO);

        assertEquals(expectedAuthor, resultAuthor);
        verify(authorMapper).toEntity(authorDTO);
        verify(authorRepository).save(expectedAuthor);
    }

    @Test
    public void testUpdateAuthor() {
        Long authorId = 1L;
        AuthorDTO authorDTO = new AuthorDTO(authorId, "John", "Doe", Date.valueOf("1999-09-09"));
        Author existingAuthor = new Author("Jane", "Smith", Date.valueOf("1999-09-09"));
        Author expectedAuthor = new Author("John", "Doe", Date.valueOf("1999-09-09"));

        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        Mockito.when(authorMapper.updateEntity(existingAuthor, authorDTO)).thenReturn(expectedAuthor);
        Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenReturn(expectedAuthor);

        Author resultAuthor = authorService.updateAuthor(authorId, authorDTO);

        assertEquals(expectedAuthor, resultAuthor);
        verify(authorRepository).findById(authorId);
        verify(authorMapper).updateEntity(existingAuthor, authorDTO);
        verify(authorRepository).save(Mockito.any(Author.class));
    }

    @Test
    public void testDeleteAuthor() {
        Long authorId = 1L;

        authorService.deleteAuthor(authorId);

        verify(authorRepository).deleteById(authorId);
    }
}
