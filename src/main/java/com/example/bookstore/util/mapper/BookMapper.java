package com.example.bookstore.util.mapper;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;

    public BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setPrice(book.getPrice());
        dto.setAuthor(authorMapper.toDTO(book.getAuthor()));
        return dto;
    }

    public Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationDate(dto.getPublicationDate());
        book.setPrice(dto.getPrice());
        AuthorDTO authorDTO = dto.getAuthor();
        Author author = new Author(authorDTO.getFirstName(), authorDTO.getLastName(), authorDTO.getBirthday());
        book.setAuthor(author);
        return book;
    }

    public Book updateEntity(Book book, BookDTO dto) {
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationDate(dto.getPublicationDate());
        book.setPrice(dto.getPrice());
        return book;
    }
}
