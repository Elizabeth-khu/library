package com.example.library.service;

import com.example.library.aop.Cached;
import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.storage.hibernate.HibernateAuthorsRepository;
import com.example.library.storage.hibernate.HibernateBooksRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LibraryService {

    private final HibernateBooksRepository booksRepository;
    private final HibernateAuthorsRepository authorsRepository;

    public LibraryService(
            HibernateBooksRepository booksRepository,
            HibernateAuthorsRepository authorsRepository
    ) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> listBooks() {
        return booksRepository.findAll();
    }

    @Cached
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return booksRepository.findById(id);
    }

    @Transactional
    public Book createBook(BookDraft draft) {
        Author author = authorsRepository.findById(draft.authorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found with ID: " + draft.authorId()));

        Book book = new Book(draft.title(), draft.description());
        book.setAuthors(Set.of(author));

        return booksRepository.save(book);
    }

    @Transactional
    public Optional<Book> updateBook(long id, BookDraft draft) {
        return booksRepository.findById(id).map(book -> {
            Author author = authorsRepository.findById(draft.authorId())
                    .orElseThrow(() -> new IllegalArgumentException("Author not found with ID: " + draft.authorId()));

            book.setTitle(draft.title());
            book.setDescription(draft.description());
            book.setAuthors(Set.of(author));

            return booksRepository.save(book);
        });
    }

    @Transactional
    public boolean deleteBook(long id) {
        return booksRepository.findById(id).map(book -> {
            booksRepository.delete(book);
            return true;
        }).orElse(false);
    }
}