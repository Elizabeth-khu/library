package com.example.library.storage.jdbc;

import com.example.library.domain.Author;
import com.example.library.storage.AuthorsStorage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JdbcAuthorsStorage implements AuthorsStorage {

    private final JdbcTemplate jdbc;

    public JdbcAuthorsStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Author> authors() {
        String sql = "SELECT id, name FROM authors ORDER BY id";
        return jdbc.query(sql, (rs, n) -> new Author(rs.getLong("id"), rs.getString("name")));
    }

    @Override
    public Optional<Author> findById(long id) {
        String sql = "SELECT id, name FROM authors WHERE id = ?";
        List<Author> result = jdbc.query(sql, (rs, n) -> new Author(rs.getLong("id"), rs.getString("name")), id);
        return result.stream().findFirst();
    }

    @Override
    public Author create(String name) {
        String clean = requireName(name);

        try {
            Long id = jdbc.queryForObject(
                    "INSERT INTO authors(name) VALUES (?) RETURNING id",
                    Long.class,
                    clean
            );
            if (id == null) {
                throw new IllegalStateException("Failed to create author");
            }
            return new Author(id, clean);
        } catch (DuplicateKeyException e) {
            return findByName(clean).orElseThrow(() -> e);
        }
    }

    @Override
    public Optional<Author> update(long id, String name) {
        String clean = requireName(name);

        try {
            int updated = jdbc.update("UPDATE authors SET name = ? WHERE id = ?", clean, id);
            return updated == 0 ? Optional.empty() : Optional.of(new Author(id, clean));
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("Author with name already exists: " + clean, e);
        }
    }

    @Override
    public boolean delete(long id) {
        try {
            return jdbc.update("DELETE FROM authors WHERE id = ?", id) > 0;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public Optional<Author> findByName(String name) {
        String clean = requireName(name);
        String sql = "SELECT id, name FROM authors WHERE name = ?";
        List<Author> result = jdbc.query(sql, (rs, n) -> new Author(rs.getLong("id"), rs.getString("name")), clean);
        return result.stream().findFirst();
    }

    private String requireName(String raw) {
        String name = raw == null ? "" : raw.trim();
        if (name.isBlank()) {
            throw new IllegalArgumentException("author name must not be blank");
        }
        return name;
    }
}