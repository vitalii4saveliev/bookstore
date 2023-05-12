package com.example.bookstore.service;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.dto.SearchRequest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {

    Page<Author> findAuthorsByFilterAndPage(SearchRequest searchRequest);

    List<Author> findAllAuthors();

    Author findAuthorById(Long id);

    Author createAuthor(AuthorDTO authorDTO);

    Author updateAuthor(Long id, AuthorDTO authorDTO);

    void deleteAuthor(Long id);

    List<Book> findBooksByAuthor(Long authorId);

}
