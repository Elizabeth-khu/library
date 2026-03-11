package com.example.library.storage.jdbc;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcBookAuthorsRepository {

    private final JdbcTemplate jdbc;

    public JdbcBookAuthorsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void addAuthorToBook(long bookId, long authorId) {
        try {
            jdbc.update(
                    "INSERT INTO book_authors(book_id, author_id) VALUES (?, ?)",
                    bookId, authorId
            );
        } catch (DuplicateKeyException e) {
        }
    }

    public boolean removeAuthorFromBook(long bookId, long authorId) {
        return jdbc.update(
                "DELETE FROM book_authors WHERE book_id = ? AND author_id = ?",
                bookId, authorId
        ) > 0;
    }

    public List<Long> authorIdsForBook(long bookId) {
        return jdbc.query(
                "SELECT author_id FROM book_authors WHERE book_id = ? ORDER BY author_id",
                (rs, n) -> rs.getLong("author_id"),
                bookId
        );
    }
}
