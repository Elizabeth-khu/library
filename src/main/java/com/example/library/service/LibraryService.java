package com.example.library.service;

import com.example.library.aop.Cached;
import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.storage.AuthorsStorage;
import com.example.library.storage.BookAuthorsStorage;
import com.example.library.storage.BooksStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LibraryService {
    private final BooksStorage booksStorage;
    private final AuthorsStorage authorsStorage;
    private final BookAuthorsStorage bookAuthorsStorage;

    public LibraryService(BooksStorage booksStorage, AuthorsStorage authorsStorage, BookAuthorsStorage bookAuthorsStorage) {
        this.booksStorage = booksStorage;
        this.authorsStorage = authorsStorage;
        this.bookAuthorsStorage = bookAuthorsStorage;
    }

    public List<Book> listBooks() {
        return booksStorage.books();
    }

    @Cached
    public Optional<Book> findById(long id){
        return booksStorage.findById(id);
    }

    public Book createBook(BookDraft draft) {
        Author author = authorsStorage.findByName(draft.author())
                .orElseGet(() -> authorsStorage.create(draft.author()));

        Book created = booksStorage.create(draft);
        bookAuthorsStorage.addAuthorToBook(created.getId(), author.id());

        return created;
    }

    public Optional<Book> updateBook(long id, BookDraft draft) {
        Optional<Book> updated = booksStorage.update(id, draft);
        if (updated.isEmpty()) {
            return Optional.empty();
        }

        Author author = authorsStorage.findByName(draft.author())
                .orElseGet(() -> authorsStorage.create(draft.author()));

        for (Long authorId : bookAuthorsStorage.authorIdsForBook(id)) {
            bookAuthorsStorage.removeAuthorFromBook(id, authorId);
        }

        bookAuthorsStorage.addAuthorToBook(id, author.id());

        return updated;    }

    public boolean deleteBook(long id) {
        return booksStorage.delete(id);
    }
}
