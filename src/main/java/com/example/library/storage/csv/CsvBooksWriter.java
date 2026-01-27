package com.example.library.storage.csv;

import com.example.library.domain.Book;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CsvBooksWriter {

    private final CsvMapper mapper;
    private final CsvSchema schema;
    private final File file;

    public CsvBooksWriter(CsvMapper mapper, CsvSchema schema, File file) {
        this.mapper = mapper;
        this.schema = schema;
        this.file = file;
    }

    public void writeAll(List<Book> books){
        try{
            mapper
                    .writerFor(Book.class)
                    .with(schema)
                    .writeValues(file)
                    .writeAll(books);
        }catch (IOException e){
            throw new RuntimeException("Failed to write books to CSV" + e);
        }
    }
}
