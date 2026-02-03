package com.example.library.storage.csv;

import com.example.library.domain.Book;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class CsvBooksReader {

    private final CsvMapper mapper;
    private final Path booksFile;
    private static final CsvSchema HEADER_SCHEMA = CsvSchema.builder()
            .addColumn("id")
            .addColumn("title")
            .addColumn("author")
            .addColumn("description")
            .setUseHeader(true)
            .build();

    public CsvBooksReader(CsvMapper mapper, Path booksFile) {
        this.mapper = mapper;
        this.booksFile = booksFile;
    }

    public List<Book> readAllBooks() {
        try {
            if (Files.notExists(booksFile) || Files.size(booksFile) == 0) {
                return List.of();
            }

            try (var reader = Files.newBufferedReader(booksFile)) {
                MappingIterator<Book> it = mapper
                        .readerFor(Book.class)
                        .with(HEADER_SCHEMA)
                        .readValues(reader);

                return it.readAll();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read books CSV", e);
        }
    }
}