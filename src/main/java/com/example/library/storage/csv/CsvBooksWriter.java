package com.example.library.storage.csv;

import com.example.library.domain.Book;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class CsvBooksWriter {

    private static final CsvSchema HEADER_SCHEMA = CsvSchema.builder()
            .addColumn("id")
            .addColumn("title")
            .addColumn("author")
            .addColumn("description")
            .setUseHeader(true)
            .build();

    private final CsvMapper mapper;
    private final Path booksFile;

    public CsvBooksWriter(CsvMapper mapper, Path booksFile) {
        this.mapper = mapper;
        this.booksFile = booksFile;
    }

    public void writeAllBooks(List<Book> books) {
        try {
            Files.createDirectories(booksFile.getParent());

            try (var writer = Files.newBufferedWriter(booksFile)) {
                mapper.writerFor(Book.class)
                        .with(HEADER_SCHEMA)
                        .writeValues(writer)
                        .writeAll(books);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write books CSV", e);
        }
    }
}