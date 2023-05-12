package com.example.bookstore.util.mapper;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorDTO toDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setBirthday(author.getBirthday());
        return dto;
    }

    public Author toEntity(AuthorDTO authorDTO) {
        return new Author(authorDTO.getFirstName(), authorDTO.getLastName(), authorDTO.getBirthday());
    }

    public Author updateEntity(Author author, AuthorDTO authorDTO){
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        author.setBirthday(authorDTO.getBirthday());
        return author;
    }
}
