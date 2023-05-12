package com.example.bookstore.util.mapper;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookMapperTest {
    private BookMapper bookMapper;

    @BeforeEach
    public void setUp() {
        AuthorMapper authorMapper = new AuthorMapper();
        bookMapper = new BookMapper(authorMapper);
    }

    @Test
    public void testToDTO() {
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setBirthday(Date.valueOf("1980-05-01"));

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");
        book.setIsbn("1234567890");
        book.setPublicationDate(Date.valueOf("2022-01-01"));
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setAuthor(author);

        BookDTO dto = bookMapper.toDTO(book);

        assertEquals(1L, dto.getId());
        assertEquals("Sample Book", dto.getTitle());
        assertEquals("1234567890", dto.getIsbn());
        assertEquals(Date.valueOf("2022-01-01"), dto.getPublicationDate());
        assertEquals(BigDecimal.valueOf(19.99), dto.getPrice());
        assertEquals(1L, dto.getAuthor().getId());
        assertEquals("John", dto.getAuthor().getFirstName());
        assertEquals("Doe", dto.getAuthor().getLastName());
        assertEquals(Date.valueOf("1980-05-01"), dto.getAuthor().getBirthday());
    }

    @Test
    public void testToEntity() {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setFirstName("Jane");
        authorDTO.setLastName("Smith");
        authorDTO.setBirthday(Date.valueOf("1990-03-15"));

        BookDTO dto = new BookDTO();
        dto.setTitle("Sample Book");
        dto.setIsbn("1234567890");
        dto.setPublicationDate(Date.valueOf("2022-01-01"));
        dto.setPrice(BigDecimal.valueOf(19.99));
        dto.setAuthor(authorDTO);

        Book book = bookMapper.toEntity(dto);

        assertEquals("Sample Book", book.getTitle());
        assertEquals("1234567890", book.getIsbn());
        assertEquals(Date.valueOf("2022-01-01"), book.getPublicationDate());
        assertEquals(BigDecimal.valueOf(19.99), book.getPrice());
        assertEquals("Jane", book.getAuthor().getFirstName());
        assertEquals("Smith", book.getAuthor().getLastName());
        assertEquals(Date.valueOf("1990-03-15"), book.getAuthor().getBirthday());
    }

    @Test
    public void testUpdateEntity() {
        Book book = new Book();
        book.setTitle("Old Book");
        book.setIsbn("9876543210");
        book.setPublicationDate(Date.valueOf("2020-01-01"));
        book.setPrice(BigDecimal.valueOf(9.99));

        BookDTO dto = new BookDTO();
        dto.setTitle("New Book");
        dto.setIsbn("1234567890");
        dto.setPublicationDate(Date.valueOf("2022-01-01"));
        dto.setPrice(BigDecimal.valueOf(19.99));

        bookMapper.updateEntity(book, dto);
        assertEquals("New Book", book.getTitle());
        assertEquals("1234567890", book.getIsbn());
        assertEquals(Date.valueOf("2022-01-01"), book.getPublicationDate());
        assertEquals(BigDecimal.valueOf(19.99), book.getPrice());
    }
}
