package com.example.library.aop;

import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.service.LibraryService;
import com.example.library.storage.AuthorsStorage;
import com.example.library.storage.BookAuthorsStorage;
import com.example.library.storage.BooksStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleCacheAspectTest {

    private AnnotationConfigApplicationContext ctx;

    @AfterEach
    void tearDown() {
        if (ctx != null) {
            ctx.close();
        }
    }

    @Test
    void cachesFindByIdForSameArguments() {
        ctx = new AnnotationConfigApplicationContext(TestConfig.class);

        LibraryService service = ctx.getBean(LibraryService.class);
        CountingStorage storage = ctx.getBean(CountingStorage.class);

        assertTrue(service.findById(1).isPresent());
        assertTrue(service.findById(1).isPresent());

        assertEquals(1, storage.findByIdCalls.get());
    }

    @Test
    void doesNotReuseCacheForDifferentArguments() {
        ctx = new AnnotationConfigApplicationContext(TestConfig.class);

        LibraryService service = ctx.getBean(LibraryService.class);
        CountingStorage storage = ctx.getBean(CountingStorage.class);

        service.findById(1);
        service.findById(2);

        assertEquals(2, storage.findByIdCalls.get());
    }

    @Configuration
    @EnableAspectJAutoProxy
    static class TestConfig {

        @Bean
        CountingStorage booksStorage() {
            return new CountingStorage();
        }

        @Bean
        AuthorsStorage authorsStorage() {
            return new StubAuthorsStorage();
        }

        @Bean
        BookAuthorsStorage bookAuthorsStorage() {
            return new StubBookAuthorsStorage();
        }

        @Bean
        LibraryService libraryService(
                BooksStorage booksStorage,
                AuthorsStorage authorsStorage,
                BookAuthorsStorage bookAuthorsStorage
        ) {
            return new LibraryService(booksStorage, authorsStorage, bookAuthorsStorage);
        }

        @Bean
        SimpleCacheAspect simpleCacheAspect() {
            return new SimpleCacheAspect();
        }
    }

    static class CountingStorage implements BooksStorage {

        final AtomicInteger findByIdCalls = new AtomicInteger();

        @Override
        public List<Book> books() {
            return List.of(new Book(1, "T", "A", "D"));
        }

        @Override
        public Optional<Book> findById(long id) {
            findByIdCalls.incrementAndGet();
            return Optional.of(new Book(id, "T" + id, "A", "D"));
        }

        @Override
        public Book create(BookDraft bookDraft) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Book> update(long id, BookDraft bookDraft) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean delete(long id) {
            throw new UnsupportedOperationException();
        }
    }

    static class StubAuthorsStorage implements AuthorsStorage {

        @Override
        public List<Author> authors() {
            return List.of();
        }

        @Override
        public Optional<Author> findById(long id) {
            return Optional.empty();
        }

        @Override
        public Optional<Author> findByName(String name) {
            return Optional.empty();
        }

        @Override
        public Author create(String name) {
            return new Author(1L, name);
        }

        @Override
        public Optional<Author> update(long id, String name) {
            return Optional.empty();
        }

        @Override
        public boolean delete(long id) {
            return false;
        }
    }

    static class StubBookAuthorsStorage implements BookAuthorsStorage {

        @Override
        public void addAuthorToBook(long bookId, long authorId) {
        }

        @Override
        public boolean removeAuthorFromBook(long bookId, long authorId) {
            return false;
        }

        @Override
        public List<Long> authorIdsForBook(long bookId) {
            return List.of();
        }
    }
}