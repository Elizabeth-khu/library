package com.example.library.storage;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;

import java.util.List;
import java.util.Optional;

public interface BooksStorage {
    List<Book> books();

    Optional<Book> findById(long id);

    Book create(BookDraft bookDraft);

    Optional<Book> update(long id, BookDraft bookDraft);

    boolean delete(long id);
}
