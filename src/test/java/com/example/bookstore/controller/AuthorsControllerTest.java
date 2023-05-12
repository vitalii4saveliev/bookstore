package com.example.bookstore.controller;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.SearchRequest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.util.mapper.AuthorMapper;
import com.example.bookstore.util.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthorsControllerTest {

    @Mock
    private AuthorService authorService;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private BookMapper bookMapper;

    private AuthorsController authorsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authorsController = new AuthorsController(authorService, authorMapper, bookMapper);
    }

    @Test
    public void testGetFilteredAuthors() {
        SearchRequest searchRequest = new SearchRequest();
        AuthorDTO authorDTO = new AuthorDTO();

        List<Author> authors = List.of(new Author());
        Page<Author> filteredAuthors = new PageImpl<>(authors);

        when(authorService.findAuthorsByFilterAndPage(searchRequest)).thenReturn(filteredAuthors);
        when(authorMapper.toDTO(any(Author.class))).thenReturn(authorDTO);

        Page<AuthorDTO> response = authorsController.getFilteredAuthors(searchRequest);

        assertEquals(authorDTO, response.getContent().get(0));

        verify(authorService, times(1)).findAuthorsByFilterAndPage(searchRequest);
        verify(authorMapper, times(1)).toDTO(any(Author.class));
    }

    @Test
    public void testGetAllAuthors() {
        SearchRequest searchRequest = new SearchRequest();
        AuthorDTO expectedDTO = new AuthorDTO();

        List<Author> authors = List.of(new Author());
        Page<Author> filteredAuthors = new PageImpl<>(authors);

        when(authorService.findAuthorsByFilterAndPage(searchRequest)).thenReturn(filteredAuthors);
        when(authorMapper.toDTO(any(Author.class))).thenReturn(expectedDTO);

        Page<AuthorDTO> response = authorsController.getFilteredAuthors(searchRequest);

        assertEquals(expectedDTO, response.getContent().get(0));

        verify(authorService, times(1)).findAuthorsByFilterAndPage(searchRequest);
        verify(authorMapper, times(1)).toDTO(any(Author.class));
    }

    @Test
    public void testGetBooksByAuthor() {
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();

        when(authorService.findBooksByAuthor(anyLong())).thenReturn(List.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        List<BookDTO> response = authorsController.getBooksByAuthor(1L);

        assertEquals(List.of(bookDTO), response);

        verify(authorService, times(1)).findBooksByAuthor(1L);
        verify(bookMapper, times(1)).toDTO(book);
    }

    @Test
    public void testGetAuthorById() {
        Author author = new Author();
        AuthorDTO authorDTO = new AuthorDTO();

        when(authorService.findAuthorById(1L)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        AuthorDTO response = authorsController.getAuthorById(1L);

        assertEquals(authorDTO, response);

        verify(authorService, times(1)).findAuthorById(1L);
        verify(authorMapper, times(1)).toDTO(author);
    }

    @Test
    public void testCreateAuthor() {
        AuthorDTO authorDTO = new AuthorDTO();

        when(authorService.createAuthor(authorDTO)).thenReturn(new Author());
        when(authorMapper.toDTO(any(Author.class))).thenReturn(authorDTO);

        AuthorDTO response = authorsController.createAuthor(authorDTO);

        assertEquals(authorDTO, response);

        verify(authorService, times(1)).createAuthor(authorDTO);
        verify(authorMapper, times(1)).toDTO(any(Author.class));
    }

    @Test
    public void testUpdateAuthor() {
        AuthorDTO authorDTO = new AuthorDTO();

        when(authorService.updateAuthor(1L, authorDTO)).thenReturn(new Author());
        when(authorMapper.toDTO(any(Author.class))).thenReturn(authorDTO);

        AuthorDTO response = authorsController.updateAuthor(1L, authorDTO);

        assertEquals(authorDTO, response);

        verify(authorService, times(1)).updateAuthor(1L, authorDTO);
        verify(authorMapper, times(1)).toDTO(any(Author.class));
    }

    @Test
    public void testDeleteAuthor() {
        authorsController.deleteAuthor(1L);

        verify(authorService, times(1)).deleteAuthor(1L);
    }

}
