package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.repository.csv.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private final BookRepository repository;
    private final IdGenerator idGenerator;

    public LibraryService(BookRepository repository, IdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    public List<Book> list() {
        return repository.findAll();
    }

    public Book create(String title, String author, String description) {
        String t = trimmedRequired(title, "title");
        String a = trimmedRequired(author, "author");
        String d = trimmedRequired(description, "description");

        Book book = new Book(newId(), t, a, d);
        return repository.save(book);
    }

    public Optional<Book> edit(long id, String title, String author, String description) {
        validateId(id);
        if (repository.findById(id).isEmpty()) {
            return Optional.empty();
        }

        String t = trimmedRequired(title, "title");
        String a = trimmedRequired(author, "author");
        String d = trimmedRequired(description, "description");

        Book updated = new Book(id, t, a, d);
        repository.save(updated);
        return Optional.of(updated);
    }

    public boolean delete(long id) {
        validateId(id);
        return repository.deleteById(id);
    }

    private long newId() {
        return idGenerator.nextId(repository.findAll());
    }

    private void validate(Book book) {
        requireNotBlank(book.getTitle(), "title");
        requireNotBlank(book.getAuthor(), "author");
        requireNotBlank(book.getDescription(), "description");
    }

    private void validateId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }
    }

    private void requireNotBlank(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim();
    }

    private String trimmedRequired(String value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return trimmed;
    }


    public Optional<Book> findById(long id) {
        validateId(id);
        return repository.findById(id);
    }
}

