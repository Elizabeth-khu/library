package com.example.library.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookValidatorTest {
    private final BookValidator validator = new BookValidator();

    @Test
    void validated_AcceptsValidDraft() {
        BookDraft draft = new BookDraft("Title", "10", "Description");
        assertDoesNotThrow(() -> validator.validated(draft));
    }

    @Test
    void validate_ThrowsExceptionForInvalidAuthorIdFormat() {
        BookDraft draft = new BookDraft("Title", "abc", "Description");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validate(draft));
        assertTrue(ex.getMessage().contains("must be a valid number"));
    }

    @Test
    void validate_ThrowsExceptionForNegativeAuthorId() {
        BookDraft draft = new BookDraft("Title", "-1", "Description");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(draft));
    }

    @Test
    void normalize_TrimsWhitespace() {
        BookDraft raw = new BookDraft("  Title  ", "  5  ", "  Desc  ");
        BookDraft normalized = validator.normalize(raw);
        assertEquals("Title", normalized.title());
        assertEquals("5", normalized.authorId());
    }
}