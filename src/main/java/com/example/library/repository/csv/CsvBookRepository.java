package com.example.library.repository.csv;

import com.example.library.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CsvBookRepository implements BookRepository {

    private final CsvBookReader reader;
    private final CsvBookWriter writer;

    public CsvBookRepository(CsvBookReader reader, CsvBookWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public List<Book> findAll(){
        return reader.readAll();
    }

    @Override
    public Optional<Book> findById(long id){
        return reader.readAll().stream()
                .filter(book -> book.getId() == id)
                .findFirst();
    }

    @Override
    public Book save(Book book) {
        List<Book> books = reader.readAll();
        List<Book> updated = upsert(books,book);
        writer.writeAll(updated);
        return book;
    }

    @Override
    public boolean deleteById(long id) {
        List<Book> books = reader.readAll();
        boolean removed = books.removeIf(b -> b.getId() == id);

        if (!removed) { return false; }

        writer.writeAll(books);
        return true;
    }

    private List<Book> upsert(List<Book> books, Book book) {
        if(replaceExsting(books,book)){
            return books;
        }

        books.add(book);
        return books;
    }

    private boolean replaceExsting(List<Book> books, Book book) {
        for (int i = 0; i< books.size(); i++) {
            if (books.get(i).getId() == book.getId()) {
                books.set(i, book);
                return true;
            }
        }
        return false;
    }
}
