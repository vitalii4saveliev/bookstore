package com.example.bookstore.service;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.SearchRequest;
import com.example.bookstore.entity.Book;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {

    Page<Book> findBooksByFilterAndPage(SearchRequest searchRequest);
    List<Book> getAllBooks();

    Book getBookById(Long id);

    Book createBook(BookDTO Book);

    Book updateBook(Long id, BookDTO Book);

    void deleteBook(Long id);
}
