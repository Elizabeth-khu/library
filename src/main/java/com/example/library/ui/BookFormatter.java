package com.example.library.ui;

import com.example.library.domain.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookFormatter {

    public String formatList(List<Book> books) {
        if (books.isEmpty()) {
            return "No books found";
        }
        StringBuilder sb = new StringBuilder();
        sb.append( header()).append("\n");
        sb.append(line()).append("\n");
        for (Book b : books) {
            sb.append(row(b)).append("\n");
        }
        return sb.toString();
    }

    private String row(Book b) {
        return String.format("%-5d | %-30s | %-20s | %s",
                b.getId(),
                cut(b.getTitle(),30),
                cut(b.getAuthor(),20),
                safe(b.getDescription()));
    }

    private String safe(String s) {
        return s== null ? "" : s;
    }

    private String cut(String s, int max) {
        String v = safe(s);
        if (v.length() <= max) return v;
        return v.substring(0, max - 1) + "…";
    }

    private String line() {
        return "-".repeat(90);
    }

    private String header() {
        return String.format("%-5s | %-30s | %-20s | %s", "ID", "Title", "Author", "Description");
    }
}
