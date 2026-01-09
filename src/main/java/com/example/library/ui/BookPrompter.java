package com.example.library.ui;

import com.example.library.domain.Book;
import org.springframework.stereotype.Component;

@Component
public class BookPrompter {

    private final ConsoleIO io;
    
    public record BookData(String title, String author, String description) {}

    public BookPrompter(ConsoleIO io) {
        this.io = io;
    }
    
    public BookData promptForCreate(){
        return new BookData(
                required("Title: "),
                required("Author: "),
                required("Description: ")
        );
    }
    
    public BookData promptForEdit(Book existing){
        String title = optionalWithDefault("Title", existing.getTitle());
        String author = optionalWithDefault("Author: ", existing.getAuthor());
        String description = optionalWithDefault("Description: ", existing.getDescription());
        return new BookData(title, author, description);
    }

    private String optionalWithDefault(String field, String current) {
        String prompt = field + " [" + safe(current) + "]: ";
        String value = io.readLine(prompt);
        String trimmed = value == null ? "" : value.trim();
        return trimmed.isEmpty() ? safe(current) : trimmed;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String required(String prompt) {
        String value = io.readLine(prompt);
        String trimmed = value == null ? "" : value.trim();
        if(!trimmed.isEmpty()) return trimmed;
        throw new IllegalArgumentException("Field must not be blank");
    }

}
