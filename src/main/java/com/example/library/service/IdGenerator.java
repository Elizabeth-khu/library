package com.example.library.service;

import com.example.library.domain.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IdGenerator {

    public long nextId(List<Book> books) {
        return maxId(books) + 1;
    }

    private long maxId(List<Book> books) {
        long max = 0;
        for (Book book : books) {
            max = Math.max(max, book.getId());
        }
        return max;
    }
}
