package com.example.library.service;

import com.example.library.aop.Cached;
import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibraryService {

    private final BookRepository booksRepository;
    private final AuthorRepository authorsRepository;

    public LibraryService(
            BookRepository booksRepository,
            AuthorRepository authorsRepository
    ) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> listBooks() {
        return booksRepository.findAll();
    }

    @Transactional
    public List<Author> listAuthors() {
        return authorsRepository.findAll();
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

    public Book save(Book book) {
        return booksRepository.save(book);
    }

    public boolean deleteBook(long id) {
        return booksRepository.findById(id).map(book -> {
            booksRepository.delete(book);
            return true;
        }).orElse(false);
    }

    public Book createBook(BookDraft draft, Long authorId) {
        Author author = authorsRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Book book = new Book();
        book.setTitle(draft.title());
        book.setDescription(draft.description());
        book.addAuthor(author);

        return booksRepository.save(book);
    }

    public Book updateBook(long bookId, BookDraft draft, Long authorId) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        Author author = authorsRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        book.setTitle(draft.title());
        book.setDescription(draft.description());

        book.getAuthors().clear();
        book.addAuthor(author);

        return booksRepository.save(book);
    }

    public Author saveAuthor(Author author) {
        return authorsRepository.save(author);
    }
}