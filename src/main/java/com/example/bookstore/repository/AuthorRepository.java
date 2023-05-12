package com.example.bookstore.repository;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {

    Page<Author> findAll(Specification<Author> specification, @NonNull Pageable pageable);

    @Query("SELECT DISTINCT a FROM Author a JOIN FETCH a.books b WHERE a.id = :authorId")
    Optional<Author> findByIdWithBooks(@Param("authorId") Long authorId);

    Optional<Author> findByFirstNameAndLastNameAndBirthday(String firstName, String lastName, Date birthday);
}
