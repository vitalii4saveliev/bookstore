package com.example.bookstore.controller;

import com.example.bookstore.dto.AuthorDTO;
import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.SearchRequest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.util.mapper.AuthorMapper;
import com.example.bookstore.util.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorsController implements SecuredRestController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;


    @PostMapping("/filter")
    public Page<AuthorDTO> getFilteredAuthors(@RequestBody SearchRequest searchRequest) {
        Page<Author> filteredBooks = authorService.findAuthorsByFilterAndPage(searchRequest);
        return filteredBooks.map(authorMapper::toDTO);
    }

    @GetMapping
    public List<AuthorDTO> getAllAuthors() {
        return authorService.findAllAuthors().stream().map(authorMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{authorId}/books")
    public List<BookDTO> getBooksByAuthor(@PathVariable Long authorId) {
        List<Book> authorBooks = authorService.findBooksByAuthor(authorId);
        return authorBooks.stream().map(bookMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AuthorDTO getAuthorById(@PathVariable Long id) {
        Author author = authorService.findAuthorById(id);
        return authorMapper.toDTO(author);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO createAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
        return authorMapper.toDTO(authorService.createAuthor(authorDTO));
    }

    @PutMapping("/{id}")
    public AuthorDTO updateAuthor(@PathVariable Long id, @RequestBody @Valid AuthorDTO authorDTO) {
        return authorMapper.toDTO(authorService.updateAuthor(id, authorDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

}
