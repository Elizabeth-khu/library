package com.example.library.repository.csv;

import com.example.library.domain.Book;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvBookReader {

    private final CsvMapper mapper;
    private final CsvSchema schema;
    private final CsvFileLocator locator;

    public CsvBookReader(CsvSchemaFactory schemaFactory, CsvFileLocator locator) {
        this.mapper = new CsvMapper();
        this.schema = schemaFactory.bookSchema();
        this.locator = locator;
    }

    public List<Book> readAll(){
        Path path = locator.booksCsvPath();
        return readFrom(path);
    }

    private List<Book> readFrom(Path path) {
        try (MappingIterator<Book> it = iterator(path)) {
            return collect(it);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read CSV: " + path, e);

        }
    }

    private MappingIterator<Book> iterator(Path path) throws IOException {
        return mapper.readerFor(Book.class).with(schema).readValues(path.toFile());
    }

    private List<Book> collect(MappingIterator<Book> it) throws IOException {
        List<Book> books = new ArrayList<>();
        while (it.hasNext()) {
            books.add(it.next());
        }
        return books;
    }
}
