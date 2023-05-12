package com.example.bookstore.util.mapper;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.entity.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorMapperTest {

    private AuthorMapper authorMapper;
    private Date date;
    private Date date1;

    @BeforeEach
    public void setUp() {
        authorMapper = new AuthorMapper();
        date = Date.valueOf("1996-07-04");
        date = Date.valueOf("1976-09-25");
    }

    @Test
    public void testToDTO() {
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setBirthday(date);

        AuthorDTO dto = authorMapper.toDTO(author);

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals(date, dto.getBirthday());
    }

    @Test
    public void testToEntity() {
        AuthorDTO dto = new AuthorDTO();
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setBirthday(date);

        Author author = authorMapper.toEntity(dto);

        assertEquals("Jane", author.getFirstName());
        assertEquals("Smith", author.getLastName());
        assertEquals(date, author.getBirthday());
    }

    @Test
    public void testUpdateEntity() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setBirthday(date);

        AuthorDTO dto = new AuthorDTO();
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setBirthday(date1);

        authorMapper.updateEntity(author, dto);

        assertEquals("Jane", author.getFirstName());
        assertEquals("Smith", author.getLastName());
        assertEquals(date1, author.getBirthday());
    }
}
