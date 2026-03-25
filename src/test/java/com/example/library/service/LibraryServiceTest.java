package com.example.library.service;

import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.hibernate.HibernateBooksRepository;
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
    private AuthorRepository authorsRepository;

    @InjectMocks
    private LibraryService libraryService;

    @Test
    void createBook_Success() {
        Long authorId = 1L;
        BookDraft draft = new BookDraft("Clean Code", "", "A handbook of agile software craftsmanship");

        Author author = new Author();
        author.setId(authorId);
        author.setName("Robert C. Martin");

        when(authorsRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(booksRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = libraryService.createBook(draft, authorId);

        assertNotNull(result, "Saved book should not be null");
        assertEquals("Clean Code", result.getTitle());
        assertEquals("A handbook of agile software craftsmanship", result.getDescription());

        assertTrue(result.getAuthors().contains(author), "The book should contain the fetched author");

        verify(authorsRepository, times(1)).findById(authorId);
        verify(booksRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_Success() {
        long bookId = 100L;
        Long newAuthorId = 2L;
        BookDraft draft = new BookDraft("New Title", "", "New Description");

        Book existingBook = new Book();
        existingBook.setTitle("Old Title");
        Author oldAuthor = new Author();
        oldAuthor.setId(1L);
        existingBook.addAuthor(oldAuthor);

        Author newAuthor = new Author();
        newAuthor.setId(newAuthorId);

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(authorsRepository.findById(newAuthorId)).thenReturn(Optional.of(newAuthor));
        when(booksRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = libraryService.updateBook(bookId, draft, newAuthorId);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());

        assertFalse(result.getAuthors().contains(oldAuthor), "Old author should be removed");
        assertTrue(result.getAuthors().contains(newAuthor), "New author should be added");
        assertEquals(1, result.getAuthors().size(), "Book should have exactly one author");

        verify(booksRepository, times(1)).save(existingBook);
    }

    @Test
    void createBook_ThrowsException_WhenAuthorNotFound() {
        BookDraft draft = new BookDraft("Title", "", "Desc");
        Long invalidAuthorId = 999L;

        when(authorsRepository.findById(invalidAuthorId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> libraryService.createBook(draft, invalidAuthorId)
        );

        assertEquals("Author not found", exception.getMessage());
        verify(booksRepository, never()).save(any());
    }
}