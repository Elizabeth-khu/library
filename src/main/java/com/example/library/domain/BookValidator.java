package com.example.library.domain;

import org.springframework.stereotype.Component;

@Component
public class BookValidator {

    public BookDraft normalize(BookDraft draft) {
        return new BookDraft(
                safeTrim(draft.title()),
                draft.authorId(),
                safeTrim(draft.description())
        );
    }

    public void validate(BookDraft draft) {
        requireNotBlank(draft.title(), "title");
        if (draft.authorId() <= 0) {
            throw new IllegalArgumentException("authorId must be greater than 0");
        }
        requireNotBlank(draft.description(), "description");
    }

    public BookDraft validated(BookDraft raw) {
        BookDraft normalized = normalize(raw);
        validate(normalized);
        return normalized;
    }

    private void requireNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}