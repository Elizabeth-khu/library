package com.example.library.ui;

import com.example.library.domain.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookFormatter {

    private static final int ID_WIDTH = 5;
    private static final int TITLE_WIDTH = 30;
    private static final int AUTHOR_WIDTH = 20;
    private static final int LINE_WIDTH = 90;

    private static final String ROW_FORMAT = "%-" + ID_WIDTH + "s | %-" + TITLE_WIDTH + "s | %-" + AUTHOR_WIDTH + "s | %s";
    private static final String ELLIPSIS = "…";

    public String formatList(List<Book> books) {
        StringBuilder sb = new StringBuilder();
        sb.append(header()).append('\n');
        sb.append(line()).append('\n');

        for (Book b : books) {
            sb.append(row(b)).append('\n');
        }
        return sb.toString();
    }

    private String row(Book b) {
        return String.format(
                ROW_FORMAT,
                b.getId(),
                cut(b.getTitle(), TITLE_WIDTH),
                cut(b.getAuthorsAsString(), AUTHOR_WIDTH),
                safeTrim(b.getDescription())
        );
    }

    private String header() {
        return String.format(ROW_FORMAT, "ID", "Title", "Author", "Description");
    }

    private String line() {
        return "-".repeat(LINE_WIDTH);
    }

    private String cut(String s, int max) {
        String v = safeTrim(s);
        if (v.length() <= max) {
            return v;
        }
        return v.substring(0, max - 1) + ELLIPSIS;
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}