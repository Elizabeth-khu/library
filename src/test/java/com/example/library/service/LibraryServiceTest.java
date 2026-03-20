package com.example.library.service;

import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.repository.hibernate.HibernateAuthorsRepository;
import com.example.library.repository.hibernate.HibernateBooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private HibernateBooksRepository booksRepository;

    @Mock
    private HibernateAuthorsRepository authorsRepository;

    @InjectMocks
    private LibraryService libraryService;

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = new Author(1L, "Joshua Bloch");
    }

    @Test
    void createBook_Success() {
        BookDraft draft = new BookDraft("Effective Java", 1L, "Programming guide");

        when(authorsRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(booksRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = libraryService.createBook(draft);

        assertNotNull(result);
        assertEquals("Effective Java", result.getTitle());
        assertTrue(result.getAuthors().contains(testAuthor));
        verify(authorsRepository).findById(1L);
        verify(booksRepository).save(any(Book.class));
    }

    @Test
    void createBook_AuthorNotFound_ThrowsException() {
        BookDraft draft = new BookDraft("Title", 99L, "Desc");
        when(authorsRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> libraryService.createBook(draft));
        verify(booksRepository, never()).save(any());
    }

    @Test
    void updateBook_Success() {
        long bookId = 10L;
        Book existingBook = new Book(bookId, "Old Title", "Old Desc");
        BookDraft updateDraft = new BookDraft("New Title", 1L, "New Desc");

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(authorsRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(booksRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Book> result = libraryService.updateBook(bookId, updateDraft);

        assertTrue(result.isPresent());
        assertEquals("New Title", result.get().getTitle());
        assertEquals(testAuthor, result.get().getAuthors().iterator().next());
    }

    @Test
    void deleteBook_Success() {
        long bookId = 10L;
        Book bookToDelete = new Book(bookId, "To Delete", "Desc");
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(bookToDelete));

        boolean deleted = libraryService.deleteBook(bookId);

        assertTrue(deleted);
        verify(booksRepository).delete(bookToDelete);
    }
}