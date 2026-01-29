package com.example.library.ui;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import org.springframework.stereotype.Component;

@Component
public class BookPrompter {

    private final ConsoleIO io;

    public BookPrompter(ConsoleIO io) {
        this.io = io;
    }
    
    public BookDraft promptForCreate(){
        String title = io.readLine("Title: ");
        String author = io.readLine("Author: ");
        String description = io.readLine("Description: ");
        return new BookDraft(title, author, description);
    }
    
    public BookDraft promptForEdit(Book existing){
        String title = io.readLine("Title [" + existing.getTitle() + "]: ");
        String author = io.readLine("Author [" + existing.getAuthor() + "]: ");
        String description = io.readLine("Description [" + existing.getDescription() + "]: ");
        return new BookDraft(
                emptyAsDefault(title, existing.getTitle()),
                emptyAsDefault(author, existing.getAuthor()),
                emptyAsDefault(description, existing.getDescription())
        );
    }

    private String emptyAsDefault(String input, String defaultValue) {
        if (input == null) return defaultValue;

        String t = input.trim();
        return t.isEmpty() ? defaultValue : input;
    }
}
