package com.example.library.domain;

import org.springframework.stereotype.Component;

@Component
public class BookValidator {

    public BookDraft validateAndNormalize(BookDraft bookDraft) {
        return new BookDraft(
                requireClean(bookDraft.title(), "title"),
                requireClean(bookDraft.author(), "author"),
                requireClean(bookDraft.description(), "description")
        );
    }

    private String requireClean(String value, String field) {
        String cleaned = safeTrim(value);
        if(cleaned.isEmpty()) {
            throw new IllegalArgumentException(field + " must not be empty");
        }
        return cleaned;
    }

    private String safeTrim(String s){
        return s == null ? "" : s.trim();
    }
}
