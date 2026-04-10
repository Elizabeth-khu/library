package com.example.library.controller;


import com.example.library.domain.Author;
import com.example.library.service.LibraryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final LibraryService libraryService;

    public AuthorController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return libraryService.saveAuthor(author);
    }
}
