package com.example.library.storage.jdbc;

import com.example.library.domain.Genre;
import com.example.library.storage.GenresStorage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JdbcGenresStorage implements GenresStorage {

    private final JdbcTemplate jdbc;

    public JdbcGenresStorage(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }


    @Override
    public List<Genre> genres() {
        String sql = "SELECT id, name FROM genres ORDER BY id";
        return jdbc.query(sql, (rs, n) -> new Genre(rs.getLong("id"), rs.getString("name")));
    }

    @Override
    public Optional<Genre> findById(long id) {
        String sql = "SELECT id, name FROM genres WHERE id = ?";
        List<Genre> result = jdbc.query(sql, (rs, n) -> new Genre(rs.getLong("id"), rs.getString("name")), id);
        return result.stream().findFirst();
    }

    @Override
    public Genre create(String name) {
        String cleanName = requireName(name);

        try {
            Long id = jdbc.queryForObject(
                    "INSERT INTO genres(name) VALUES (?) RETURNING id",
                    Long.class, cleanName
            );
            if (id == null) {
                throw new IllegalStateException("Failed to create genre");
            }
            return new Genre(id, cleanName);
        } catch (DuplicateKeyException e) {
            return findByName(cleanName).orElseThrow(() -> e);
        }
    }

    private Optional<Genre> findByName(String name) {
        String sql = "SELECT * FROM genres WHERE name = ?";
        List<Genre> result = jdbc.query(sql, (rs, n) -> new Genre(rs.getLong("id"), rs.getString("name")), name);
        return result.stream().findFirst();
    }

    private String requireName(String raw) {
        String genre = raw == null ? "" : raw.trim();
        if (genre.isBlank()) {
            throw new IllegalArgumentException("Genre must not be blank");
        }
        return genre;
    }

    @Override
    public Optional<Genre> update(long id, String name) {
        String cleanName = requireName(name);

        try {
            int updated = jdbc.update("UPDATE genres SET name = ? WHERE id = ?", cleanName, id);
            return updated == 0 ? Optional.empty() : Optional.of(new Genre(id, cleanName));
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("Genre with name already exists: " + cleanName, e);
        }
    }

    @Override
    public boolean delete(long id) {
        try {
            return jdbc.update("DELETE FROM genres WHERE id = ?", id) > 0;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }
}
