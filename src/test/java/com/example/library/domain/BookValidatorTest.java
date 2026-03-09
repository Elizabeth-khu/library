package com.example.library.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookValidatorTest {

    private final BookValidator bookValidator = new BookValidator();

    @Test
    void validated_trimsAndKeepsValues() {
        BookDraft draft = new BookDraft("  Title  ", " Author ", "  Description ");

        BookDraft normalized = bookValidator.validated(draft);

        assertEquals("Title", normalized.title());
        assertEquals("Author", normalized.author());
        assertEquals("Description", normalized.description());
    }

    @Test
    void validated_throwsWhenAnyFieldBlank() {
        BookDraft draft = new BookDraft("  ", "Author", "Description");

        assertThrows(IllegalArgumentException.class, () -> bookValidator.validated(draft));
    }
}