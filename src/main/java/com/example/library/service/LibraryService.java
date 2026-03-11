package com.example.library.service;

import com.example.library.aop.Cached;
import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.storage.BooksStorage;
import com.example.library.storage.jdbc.JdbcAuthorsRepository;
import com.example.library.storage.jdbc.JdbcBookAuthorsRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class LibraryService {

    private final BooksStorage booksStorage;
    private final JdbcAuthorsRepository authorsRepository;
    private final JdbcBookAuthorsRepository bookAuthorsRepository;

    public LibraryService(
            BooksStorage booksStorage,
            JdbcAuthorsRepository authorsRepository,
            JdbcBookAuthorsRepository bookAuthorsRepository
    ) {
        this.booksStorage = booksStorage;
        this.authorsRepository = authorsRepository;
        this.bookAuthorsRepository = bookAuthorsRepository;
    }

    public List<Book> listBooks() {
        return booksStorage.books();
    }

    @Cached
    public Optional<Book> findById(long id) {
        return booksStorage.findById(id);
    }

    @Transactional
    public Book createBook(BookDraft draft) {
        Author author = authorsRepository.findByName(draft.author())
                .orElseGet(() -> authorsRepository.create(draft.author()));

        Book created = booksStorage.create(draft);
        bookAuthorsRepository.addAuthorToBook(created.getId(), author.id());

        return created;
    }

    @Transactional
    public Optional<Book> updateBook(long id, BookDraft draft) {
        Optional<Book> updated = booksStorage.update(id, draft);
        if (updated.isEmpty()) {
            return Optional.empty();
        }

        Author author = authorsRepository.findByName(draft.author())
                .orElseGet(() -> authorsRepository.create(draft.author()));

        for (Long authorId : bookAuthorsRepository.authorIdsForBook(id)) {
            bookAuthorsRepository.removeAuthorFromBook(id, authorId);
        }

        bookAuthorsRepository.addAuthorToBook(id, author.id());

        return updated;
    }

    @Transactional
    public boolean deleteBook(long id) {
        return booksStorage.delete(id);
    }
}