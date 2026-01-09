package com.example.library.repository.csv;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

@Component
public class CsvSchemaFactory {

    public CsvSchema bookSchema(){
        return CsvSchema.builder()
                .addNumberColumn("id")
                .addColumn("title")
                .addColumn("author")
                .addColumn("description")
                .setUseHeader(true)
                .setQuoteChar('"')
                .build();
    }
}


