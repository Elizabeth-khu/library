package com.example.library.storage;

import com.example.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsStorage {
    List<Author> authors();

    Optional<Author> findById(long id);

    Optional<Author> findByName(String name);

    Author create(String name);

    Optional<Author> update(long id, String name);

    boolean delete(long id);
}