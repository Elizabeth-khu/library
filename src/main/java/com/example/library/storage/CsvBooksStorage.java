package com.example.library.storage;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.storage.csv.CsvBooksReader;
import com.example.library.storage.csv.CsvBooksWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CsvBooksStorage implements BooksStorage {

    private final CsvBooksReader reader;
    private final CsvBooksWriter writer;

    public CsvBooksStorage(CsvBooksReader reader, CsvBooksWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public List<Book> books() {
        return List.copyOf(reader.readAllBooks());
    }

    @Override
    public Optional<Book> findById(long id) {
        return reader.readAllBooks().stream()
                .filter(b -> b.getId() == id)
                .findFirst();
    }

    @Override
    public Book create(BookDraft bookDraft) {
        List<Book> currentBooks = reader.readAllBooks();
        List<Book> updatedBooks = new ArrayList<>(currentBooks);

        long id = nextId(updatedBooks);
        Book createdBook = new Book(id, bookDraft.title(), bookDraft.author(), bookDraft.description());
        updatedBooks.add(createdBook);
        writer.writeAllBooks(updatedBooks);
        return createdBook;
    }

    private long nextId(List<Book> books) {
        long max = books.stream()
                .mapToLong(Book::getId)
                .max()
                .orElse(0L);
        return max + 1;
    }

    @Override
    public Optional<Book> update(long id, BookDraft bookDraft) {
        var current = reader.readAllBooks();
        var updated = replaced(current, id, bookDraft);

        if (updated.isEmpty()) {
            return Optional.empty();
        }

        writer.writeAllBooks(updated.get());
        return Optional.of(new Book(id, bookDraft.title(), bookDraft.author(), bookDraft.description()));
    }

    @Override
    public boolean delete(long id) {
        List<Book> current = reader.readAllBooks();

        List<Book> updated = current.stream()
                .filter(book -> book.getId() != id)
                .toList();

        if (updated.size() == current.size()) {
            return false;
        }

        writer.writeAllBooks(updated);
        return true;
    }

    private Optional<List<Book>> replaced(List<Book> books, long id, BookDraft bookDraft) {
        var updated = new ArrayList<Book>(books.size());
        boolean found = false;

        for (Book b : books) {
            if (b.getId() == id) {
                updated.add(new Book(id, bookDraft.title(), bookDraft.author(), bookDraft.description()));
                found = true;
            } else {
                updated.add(b);
            }
        }

        return found ? Optional.of(updated) : Optional.empty();
    }
}