package com.example.library.aop;

import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.service.LibraryService;
import com.example.library.repository.hibernate.HibernateAuthorsRepository;
import com.example.library.repository.hibernate.HibernateBooksRepository;
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
        StubBooksRepository booksRepository = ctx.getBean(StubBooksRepository.class);

        assertTrue(service.findById(1L).isPresent());
        assertTrue(service.findById(1L).isPresent());

        assertEquals(1, booksRepository.findByIdCalls.get());
    }

    @Test
    void doesNotReuseCacheForDifferentArguments() {
        ctx = new AnnotationConfigApplicationContext(TestConfig.class);

        LibraryService service = ctx.getBean(LibraryService.class);
        StubBooksRepository booksRepository = ctx.getBean(StubBooksRepository.class);

        service.findById(1L);
        service.findById(2L);

        assertEquals(2, booksRepository.findByIdCalls.get());
    }

    @Configuration
    @EnableAspectJAutoProxy
    static class TestConfig {

        @Bean
        StubBooksRepository booksRepository() {
            return new StubBooksRepository();
        }

        @Bean
        StubAuthorsRepository authorsRepository() {
            return new StubAuthorsRepository();
        }

        @Bean
        LibraryService libraryService(
                HibernateBooksRepository booksRepository,
                HibernateAuthorsRepository authorsRepository
        ) {
            return new LibraryService(booksRepository, authorsRepository);
        }

        @Bean
        SimpleCacheAspect simpleCacheAspect() {
            return new SimpleCacheAspect();
        }
    }

    static class StubBooksRepository extends HibernateBooksRepository {

        final AtomicInteger findByIdCalls = new AtomicInteger();

        StubBooksRepository() {
            super(null);
        }

        @Override
        public List<Book> findAll() {
            return List.of(new Book(1L, "T", "D"));
        }

        @Override
        public Optional<Book> findById(long id) {
            findByIdCalls.incrementAndGet();
            return Optional.of(new Book(id, "Title", "Desc"));
        }

        @Override
        public Book save(Book book) {
            return book;
        }

        @Override
        public void delete(Book book) {
        }
    }

    static class StubAuthorsRepository extends HibernateAuthorsRepository {

        StubAuthorsRepository() {
            super(null);
        }

        @Override
        public List<Author> findAll() {
            return List.of();
        }

        @Override
        public Optional<Author> findById(long id) {
            return Optional.empty();
        }

        @Override
        public Author save(Author author) {
            return author;
        }
    }
}