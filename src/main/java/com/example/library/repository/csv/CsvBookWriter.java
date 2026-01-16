package com.example.library.repository.csv;

import com.example.library.domain.Book;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class CsvBookWriter {

    private final CsvMapper mapper;
    private final CsvSchema schema;
    private final CsvFileLocator locator;

    public CsvBookWriter(CsvSchemaFactory schemaFactory, CsvFileLocator locator) {
        this.mapper = new CsvMapper();
        this.schema = schemaFactory.bookSchema();
        this.locator = locator;
    }

    public void writeAll(List<Book> books){
        Path path = locator.booksCsvPath();
        writeTo(path, books);
    }

    private void writeTo(Path path, List<Book> books) {
        try {
            mapper.writer(schema).writeValue(path.toFile(), books);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write CSV: " + path, e);
        }
    }
}
