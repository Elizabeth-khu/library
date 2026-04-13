package com.example.library.aop;

import com.example.library.domain.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.service.LibraryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Optional;

import static org.mockito.Mockito.*;

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
        BookRepository booksRepo = ctx.getBean(BookRepository.class);

        Book testBook = new Book();
        when(booksRepo.findById(1L)).thenReturn(Optional.of(testBook));

        service.findById(1L);
        service.findById(1L);

        verify(booksRepo, times(1)).findById(1L);
    }

    @Configuration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    static class TestConfig {

        @Bean
        public BookRepository booksRepository() {
            return mock(BookRepository.class);
        }

        @Bean
        public AuthorRepository authorRepository() {
            return mock(AuthorRepository.class);
        }

        @Bean
        public LibraryService libraryService(BookRepository b, AuthorRepository a) {
            return new LibraryService(b, a);
        }

        @Bean
        public SimpleCacheAspect simpleCacheAspect() {
            return new SimpleCacheAspect();
        }
    }
}