package com.example.library.storage;

import com.example.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenresStorage {
    List<Genre> genres();
    Optional<Genre> findById(long id);

    Genre create (String name);
    Optional <Genre> update(long id, String name);

    boolean delete(long id);
}
