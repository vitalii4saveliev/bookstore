package com.example.bookstore.service.impl;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.SearchRequest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.specification.BookSpecification;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import com.example.bookstore.util.mapper.AuthorMapper;
import com.example.bookstore.util.mapper.BookMapper;
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
public class DatabaseBookService implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final BookSpecification bookSpecification;


    @Override
    @Cacheable(value = "booksFiltered", key = "#searchRequest.hashCode()")
    public Page<Book> findBooksByFilterAndPage(SearchRequest searchRequest) {
        Specification<Book> spec = bookSpecification.hasField(searchRequest.getField(), searchRequest.getOperator(), searchRequest.getValue());
        Sort sort = Sort.by(Sort.Direction.fromString(searchRequest.getSortDirection()), searchRequest.getSortField());
        PageRequest pageRequest = PageRequest.of(searchRequest.getPage(), searchRequest.getPageSize(), sort);
        return bookRepository.findAll(spec, pageRequest);
    }

    @Override
    @Cacheable(value = "books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(id)));
    }

    @Override
    @Transactional
    public Book createBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        Author author = resolveAuthorForBook(bookDTO);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Override
    @CachePut(value = "books", key = "#id")
    @Transactional
    public Book updateBook(Long id, BookDTO bookDTO) {
        Book bookToBeUpdated = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(id)));
        Book book = bookMapper.updateEntity(bookToBeUpdated, bookDTO);
        Author author = resolveAuthorForBook(bookDTO);
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    private Author resolveAuthorForBook(BookDTO bookDTO) {
        AuthorDTO bookAuthorDTO = bookDTO.getAuthor();
        return authorRepository.findByFirstNameAndLastNameAndBirthday(
                        bookAuthorDTO.getFirstName(), bookAuthorDTO.getLastName(), bookAuthorDTO.getBirthday())
                .orElseGet(() -> authorRepository.save(authorMapper.toEntity(bookAuthorDTO)));
    }

    @Override
    @CacheEvict(value = "books", key = "#id")
    @Transactional
    public void deleteBook(Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Author", "id", String.valueOf(id));
        }
    }
}
