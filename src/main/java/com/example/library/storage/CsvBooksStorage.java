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
public class CsvBooksStorage implements BooksStorage{

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
        return books.size() + 1;
    }

    @Override
    public Optional<Book> update(long id, BookDraft bookDraft) {
       List<Book> currentBooks = reader.readAllBooks();
       List<Book> updatedBooks = new ArrayList<>(currentBooks);

       if(!replace(updatedBooks, id, bookDraft)) {
           return Optional.empty();
       }

       writer.writeAllBooks(updatedBooks);
       return Optional.of(new Book(id, bookDraft.title(), bookDraft.author(), bookDraft.description()));

    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    private boolean replace(List<Book> books, long id, BookDraft bookDraft) {
        for(int i = 0; i < books.size(); ++i) {
            if (books.get(i).getId() == id) {
                books.set(i, new Book(id, bookDraft.title(), bookDraft.author(), bookDraft.description()));
                return true;
            }
        }
        return false;
    }
}
