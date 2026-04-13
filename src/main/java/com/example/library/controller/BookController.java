package com.example.library.controller;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.service.LibraryService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        return libraryService.findById(bookId)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
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

    @PutMapping("/{bookId}/author/{authorId}")
    public Book updateBook (
            @PathVariable Long bookId,
            @PathVariable Long authorId,
            @RequestBody BookDraft draft
    ){
        return libraryService.updateBook(bookId, draft, authorId);
    }

    @DeleteMapping("/{bookId}")
    public boolean deleteBook(@PathVariable Long bookId) {
        return libraryService.deleteBook(bookId);
    }
}