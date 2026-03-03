package com.example.library.storage.jdbc;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.storage.BooksStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcBooksStorage implements BooksStorage {

    private final JdbcTemplate jdbc;

    public JdbcBooksStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Book> books() {
        String sql = """
                SELECT b.id, b.title, b.description, a.name AS author
                FROM books b
                JOIN book_authors ba ON ba.book_id = b.id
                JOIN authors a ON a.id = ba.author_id
                ORDER BY b.id
                """;

        return jdbc.query(sql, (rs, rowNum) ->
                new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("description")
                )
        );
    }

    @Override
    public Optional<Book> findById(long id) {
        String sql = """
                SELECT b.id, b.title, b.description, a.name AS author
                FROM books b
                JOIN book_authors ba ON ba.book_id = b.id
                JOIN authors a ON a.id = ba.author_id
                WHERE b.id = ?
                """;

        List<Book> result = jdbc.query(sql, (rs, rowNum) ->
                new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("description")
                ), id
        );

        return result.stream().findFirst();
    }

    @Override
    public Book create(BookDraft draft) {
        long authorId = ensureAuthorId(draft.author());
        long bookId = insertBook(draft.title(), draft.description());
        linkBookAuthor(bookId, authorId);
        return new Book(bookId, draft.title(), draft.author(), draft.description());
    }

    @Override
    public Optional<Book> update(long id, BookDraft draft) {
        int updated = jdbc.update(
                "UPDATE books SET title = ?, description = ? WHERE id = ?",
                draft.title(), draft.description(), id
        );
        if (updated == 0) {
            return Optional.empty();
        }

        long authorId = ensureAuthorId(draft.author());
        jdbc.update("DELETE FROM book_authors WHERE book_id = ?", id);
        linkBookAuthor(id, authorId);

        return Optional.of(new Book(id, draft.title(), draft.author(), draft.description()));
    }

    @Override
    public boolean delete(long id) {
        return jdbc.update("DELETE FROM books WHERE id = ?", id) > 0;
    }

    private long ensureAuthorId(String name) {
        jdbc.update("INSERT INTO authors(name) VALUES (?) ON CONFLICT (name) DO NOTHING", name);
        return jdbc.queryForObject("SELECT id FROM authors WHERE name = ?", Long.class, name);
    }

    private long insertBook(String title, String description) {
        String sql = "INSERT INTO books(title, description) VALUES (?, ?) RETURNING id";
        Long id = jdbc.queryForObject(sql, Long.class, title, description);

        if (id == null) {
            throw new IllegalStateException("Failed to get generated book id");
        }
        return id;
    }

    private void linkBookAuthor(long bookId, long authorId) {
        jdbc.update(
                "INSERT INTO book_authors(book_id, author_id) VALUES (?, ?)",
                bookId, authorId
        );
    }
}