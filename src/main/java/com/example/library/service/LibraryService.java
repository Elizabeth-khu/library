package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.storage.BooksStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LibraryService {
    private final BooksStorage booksStorage;

    public LibraryService(BooksStorage booksStorage) {
        this.booksStorage = booksStorage;
    }

    public List<Book> listBooks() {
        return booksStorage.books();
    }

    public Optional<Book> findById(long id){
        return booksStorage.findById(id);
    }

    public Book createBook(BookDraft draft) {
        return booksStorage.create(draft);
    }

    public Optional<Book> updateBook(long id, BookDraft draft) {
        return booksStorage.update(id, draft);
    }

    public boolean deleteBook(long id) {
        return booksStorage.delete(id);
    }
}
