package com.example.library.service;

import com.example.library.aop.Cached;
import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.hibernate.HibernateBooksRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibraryService {
    private final HibernateBooksRepository booksRepository;
    private final AuthorRepository authorsRepository;

    public LibraryService(
            HibernateBooksRepository booksRepository,
            AuthorRepository authorsRepository
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

    @Transactional(readOnly = true)
    public Optional<Author> findAuthorById(Long id) {
        return authorsRepository.findById(id);
    }

    @Transactional
    public Book save(Book book) {
        return booksRepository.save(book);
    }

    @Transactional
    public boolean deleteBook(long id) {
        return booksRepository.findById(id).map(book -> {
            booksRepository.delete(book);
            return true;
        }).orElse(false);
    }
}