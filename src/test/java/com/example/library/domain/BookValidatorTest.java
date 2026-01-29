package com.example.library.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class BookValidatorTest {

    private final BookValidator bookValidator = new BookValidator();

    @Test
    void validateAndNormalize_trimsAndKeepsValues() {
        BookDraft draft = new BookDraft("    Title  ", "  Author  ", "  Description  ");
        BookDraft normalized = bookValidator.validateAndNormalize(draft);

        Assertions.assertEquals("Title", normalized.title());
        Assertions.assertEquals("Author", normalized.author());
        Assertions.assertEquals("Description", normalized.description());
    }

    @Test
    void validateAndNormalize_rejectsBlankTitle() {
        BookDraft draft = new BookDraft("   ", "A", "D");
        Assertions.assertThrows(IllegalArgumentException.class, () -> bookValidator.validateAndNormalize(draft));
    }
}
