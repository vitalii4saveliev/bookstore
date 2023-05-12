package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.SearchRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
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
public class BooksControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    private BooksController booksController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        booksController = new BooksController(bookService, bookMapper);
    }

    @Test
    public void testGetFilteredBooks() {
        SearchRequest searchRequest = new SearchRequest();
        BookDTO bookDTO = new BookDTO();
        Page<Book> filteredBooks = new PageImpl<>(List.of(new Book()));
        Page<BookDTO> expectedPage = new PageImpl<>(List.of(bookDTO));

        when(bookService.findBooksByFilterAndPage(searchRequest)).thenReturn(filteredBooks);
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        Page<BookDTO> actualPage = booksController.getFilteredBooks(searchRequest);

        assertEquals(expectedPage, actualPage);

        verify(bookService, times(1)).findBooksByFilterAndPage(searchRequest);
        verify(bookMapper, times(1)).toDTO(any(Book.class));
    }

    @Test
    public void testGetAllBooks() {
        Book book = new Book();
        BookDTO bookDTO = new BookDTO();

        when(bookService.getAllBooks()).thenReturn(List.of(book));
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        List<BookDTO> response = booksController.getAllBooks();

        List<BookDTO> expected = List.of(bookDTO);
        assertEquals(expected, response);

        verify(bookService, times(1)).getAllBooks();
        verify(bookMapper, times(1)).toDTO(any(Book.class));
    }

    @Test
    public void testGetBookById() {
        Book book = new Book();
        BookDTO expected = new BookDTO();

        when(bookService.getBookById(anyLong())).thenReturn(book);
        when(bookMapper.toDTO(any(Book.class))).thenReturn(expected);

        BookDTO actual = booksController.getBookById(1L);

        assertEquals(expected, actual);

        verify(bookService, times(1)).getBookById(1L);
        verify(bookMapper, times(1)).toDTO(any(Book.class));
    }

    @Test
    public void testCreateBook() {
        BookDTO expected = new BookDTO();

        when(bookService.createBook(any(BookDTO.class))).thenReturn(new Book());
        when(bookMapper.toDTO(any(Book.class))).thenReturn(expected);

        BookDTO actual = booksController.createBook(expected);

        assertEquals(expected, actual);

        verify(bookService, times(1)).createBook(any(BookDTO.class));
        verify(bookMapper, times(1)).toDTO(any(Book.class));
    }

    @Test
    public void testUpdateBook() {
        BookDTO expected = new BookDTO();

        when(bookService.updateBook(anyLong(), any(BookDTO.class))).thenReturn(new Book());
        when(bookMapper.toDTO(any(Book.class))).thenReturn(expected);

        BookDTO actual = booksController.updateBook(1L, expected);

        assertEquals(expected, actual);

        verify(bookService, times(1)).updateBook(anyLong(), any(BookDTO.class));
        verify(bookMapper, times(1)).toDTO(any(Book.class));
    }

    @Test
    public void testDeleteBook() {
        booksController.deleteBook(1L);

        verify(bookService, times(1)).deleteBook(1L);
    }

}

