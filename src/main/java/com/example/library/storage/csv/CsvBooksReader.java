package com.example.library.storage.csv;

import com.example.library.domain.Book;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class CsvBooksReader {

    private final CsvMapper mapper;
    private final CsvSchema schema;
    private final File file;

    public CsvBooksReader(CsvMapper mapper, CsvSchema schema, File file) {
        this.mapper = mapper;
        this.schema = schema;
        this.file = file;
    }

    public List<Object> readAllBooks() {
        try{
            return mapper
                    .readerFor(Book.class)
                    .with(schema)
                    .readValues(file)
                    .readAll();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
