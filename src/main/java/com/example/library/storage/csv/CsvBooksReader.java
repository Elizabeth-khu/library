package com.example.library.storage.csv;

import com.example.library.domain.Book;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CsvBooksReader {

    private final CsvMapper mapper;
    private final Path booksFile;
    private final CsvSchema schema;

    public CsvBooksReader(CsvMapper mapper, Path booksFile) {
        this.mapper = mapper;
        this.booksFile = booksFile;
        this.schema = CsvSchema.emptySchema().withHeader();
    }

    public List<Book> readAllBooks() {
        if (Files.notExists(booksFile)) {
            return Collections.emptyList();
        }

        try (var reader = Files.newBufferedReader(booksFile)) {
            MappingIterator<Book> it =
                    mapper.readerFor(Book.class)
                            .with(schema)
                            .readValues(reader);

            return it.readAll();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read books CSV", e);
        }
    }
}