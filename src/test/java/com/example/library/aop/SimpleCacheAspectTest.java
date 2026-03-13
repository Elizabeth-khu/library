package com.example.library.aop;

import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.service.LibraryService;
import com.example.library.storage.hibernate.HibernateAuthorsRepository;
import com.example.library.storage.hibernate.HibernateBooksRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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

        assertTrue(service.findById(1).isPresent());
        assertTrue(service.findById(1).isPresent());

        assertEquals(1, booksRepository.findByIdCalls.get());
    }

    @Test
    void doesNotReuseCacheForDifferentArguments() {
        ctx = new AnnotationConfigApplicationContext(TestConfig.class);

        LibraryService service = ctx.getBean(LibraryService.class);
        StubBooksRepository booksRepository = ctx.getBean(StubBooksRepository.class);

        service.findById(1);
        service.findById(2);

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
            return List.of(new Book(1, "T", "A", "D"));
        }

        @Override
        public Optional<Book> findById(long id) {
            findByIdCalls.incrementAndGet();

            Book book = new Book(id, "T" + id, "A", "D");
            book.setAuthors(Set.of(new Author(1L, "A")));

            return Optional.of(book);
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
        public Optional<Author> findByName(String name) {
            return Optional.empty();
        }

        @Override
        public Author save(Author author) {
            return author;
        }

        @Override
        public void delete(Author author) {
        }

        @Override
        public Author getOrCreate(String name) {
            return new Author(1L, name);
        }
    }
}