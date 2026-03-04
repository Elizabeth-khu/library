package com.example.library.storage;

import java.util.List;

public interface BookGenresStorage {
    void addGenreToBook(long bookId, long genreId);
    boolean removeGenreFromBook(long bookId, long genreId);
    List<Long> genreIdsForBook(long bookId);
}