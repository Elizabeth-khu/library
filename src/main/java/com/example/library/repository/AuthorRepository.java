package com.example.library.repository;

import com.example.library.domain.Author;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(Long id);

    void delete(Author author);
}