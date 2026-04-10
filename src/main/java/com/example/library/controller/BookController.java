package com.example.library.controller;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.service.LibraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final LibraryService libraryService;

    public BookController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return libraryService.listBooks();
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return libraryService.save(book);
    }

    @PostMapping("/author/{authorId}")
    public Book createBookWithAuthor(
            @RequestBody BookDraft draft,
            @PathVariable Long authorId
    ) {
        return libraryService.createBook(draft, authorId);
    }
}