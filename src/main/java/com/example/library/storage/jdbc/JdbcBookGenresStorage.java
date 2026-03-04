package com.example.library.storage.jdbc;

import com.example.library.storage.BookGenresStorage;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcBookGenresStorage implements BookGenresStorage {

    private final JdbcTemplate jdbc;

    public JdbcBookGenresStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addGenreToBook(long bookId, long genreId) {
        try {
            jdbc.update(
                    "INSERT INTO book_genres(book_id, genre_id) VALUES (?, ?)",
                    bookId, genreId
            );
        } catch (DuplicateKeyException ignored) { }
    }

    @Override
    public boolean removeGenreFromBook(long bookId, long genreId) {
        return jdbc.update(
                "DELETE FROM book_genres WHERE book_id = ? AND genre_id = ?",
                bookId, genreId
        ) > 0;
    }

    @Override
    public List<Long> genreIdsForBook(long bookId) {
        return jdbc.query(
                "SELECT genre_id FROM book_genres WHERE book_id = ? ORDER BY genre_id",
                (rs, n) -> rs.getLong("genre_id"),
                bookId
        );
    }
}