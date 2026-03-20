package com.example.library.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookValidatorTest {

    private final BookValidator validator = new BookValidator();

    @Test
    void validated_trimsAndKeepsValues() {
        BookDraft draft = new BookDraft("  Title  ", 1L, "  Description  ");

        BookDraft normalized = validator.normalize(draft);

        assertEquals("Title", normalized.title());
        assertEquals(1L, normalized.authorId()); // Исправленная строка 18
        assertEquals("Description", normalized.description());
    }

    @Test
    void validate_shouldThrowException_whenAuthorIdIsZero() {
        BookDraft draft = new BookDraft("Title", 0L, "Description");

        assertThrows(IllegalArgumentException.class, () -> validator.validate(draft));
    }
}