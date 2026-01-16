package com.example.library.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder({ "id", "title", "author", "description" })
public class Book {

    private long id;
    private String title;
    private String author;
    private String description;

    public Book() {
        // for Jackson
    }

    public Book(long id, String title, String author, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description; // <- ВАЖНО: убедись, что это есть
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; } // <- и это

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}