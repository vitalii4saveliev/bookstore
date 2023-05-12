package com.example.bookstore.service.impl;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.dto.SearchRequest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.specification.AuthorSpecification;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.util.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseAuthorService implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    private final AuthorSpecification authorSpecification;

    @Override
    @Cacheable(value = "authorsFiltered", key = "#searchRequest.hashCode()")
    public Page<Author> findAuthorsByFilterAndPage(SearchRequest searchRequest) {
        Specification<Author> spec = authorSpecification.hasField(searchRequest.getField(), searchRequest.getOperator(), searchRequest.getValue());
        Sort sort = Sort.by(Sort.Direction.fromString(searchRequest.getSortDirection()), searchRequest.getSortField());
        PageRequest pageRequest = PageRequest.of(searchRequest.getPage(), searchRequest.getPageSize(), sort);
        return authorRepository.findAll(spec, pageRequest);
    }

    @Override
    @Cacheable(value = "authors")
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    @Cacheable(value = "books", key = "#authorId")
    public List<Book> findBooksByAuthor(Long authorId) {
        Author author = getAuthorFromDatabaseById(authorId);
        return bookRepository.findAllByAuthor(author);
    }

    @Override
    @Cacheable(value = "authors", key = "#id")
    public Author findAuthorById(Long id) {
        return getAuthorFromDatabaseById(id);
    }

    private Author getAuthorFromDatabaseById(Long id) {
        return authorRepository.findByIdWithBooks(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", String.valueOf(id)));
    }

    @Override
    @Transactional
    public Author createAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);
        return authorRepository.save(author);
    }

    @Override
    @CachePut(value = "authors", key = "#id")
    @Transactional
    public Author updateAuthor(Long id, AuthorDTO authorDTO) {
        Author authorToBeUpdated = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", String.valueOf(id)));
        authorMapper.updateEntity(authorToBeUpdated, authorDTO);
        return authorRepository.save(authorToBeUpdated);
    }

    @Override
    @CacheEvict(value = "authors", key = "#id")
    @Transactional
    public void deleteAuthor(Long id) {
        try {
            authorRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Author", "id", String.valueOf(id));
        }
    }
}
