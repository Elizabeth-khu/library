package com.example.library.service;

import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.hibernate.HibernateBooksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private HibernateBooksRepository booksRepository;

    @Mock
    private AuthorRepository authorsRepository;

    @InjectMocks
    private LibraryService libraryService;

    @Test
    void createBook_SavesBookSuccessfully() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        Book book = new Book();
        book.setTitle("Clean Code");
        book.setDescription("Great book");
        book.addAuthor(author);

        when(booksRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = libraryService.save(book);

        assertNotNull(result, "Saved book should not be null");
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Great book", result.getDescription());

        assertNotNull(result.getAuthors());
        assertFalse(result.getAuthors().isEmpty());
        assertTrue(result.getAuthors().contains(author), "The book should contain the specified author");

        verify(booksRepository, times(1)).save(book);
    }
}