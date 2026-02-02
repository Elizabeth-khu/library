package com.example.library.config;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Path;

@Configuration
public class CsvConfig {
    @Bean
    public CsvMapper csvMapper() {
        return new CsvMapper();
    }

    @Bean
    public CsvSchema bookCsvSchema() {
        return CsvSchema.builder()
                .addNumberColumn("id")
                .addColumn("title")
                .addColumn("author")
                .addColumn("description")
                .setUseHeader(true)
                .build();
    }

    @Bean
    public File booksCsvFile(){
        return new File ("src/main/resources/books.csv");
    }

    @Bean
    public Path booksFile() {
        return Path.of("src/main/resources/books.csv");
    }
}
