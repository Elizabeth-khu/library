package com.example.library.aop;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.service.LibraryService;
import com.example.library.storage.BooksStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleCacheAspectTest {

    private AnnotationConfigApplicationContext ctx;

    @AfterEach
    void tearDown() {
        if (ctx != null) ctx.close();
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
        LibraryService libraryService(BooksStorage storage) {
            return new LibraryService(storage);
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
}