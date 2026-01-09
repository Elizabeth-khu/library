package com.example.library.repository.csv;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public record CsvFileLocator() {

    private static final String BOOKS_CSV = "books.csv";

    public Path booksCsvPath() {
        File file = resolveResourceFile();
        validateWritableFile(file);
        return file.toPath();
    }

    private File resolveResourceFile() {
        try {
            return new ClassPathResource(BOOKS_CSV).getFile();
        } catch (IOException e) {
            throw new IllegalStateException(resourceNotFoundMessage(), e);
        }
    }

    private void validateWritableFile(File file) {
        Path path = file.toPath();

        if (Files.notExists(path)) {
            throw new IllegalStateException(fileNotExistsMessage(path));
        }

        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException(notAFileMessage(path));
        }

        if (!Files.isWritable(path)) {
            throw new IllegalStateException(notWritableMessage(path));
        }
    }

    private String resourceNotFoundMessage() {
        return "books.csv not found in classpath";
    }

    private String fileNotExistsMessage(Path path) {
        return "CSV file does not exist: " + path.toAbsolutePath();
    }

    private String notAFileMessage(Path path) {
        return "CSV path is not a file: " + path.toAbsolutePath();
    }

    private String notWritableMessage(Path path) {
        return "CSV file is not writable: " + path.toAbsolutePath()
                + ". Run from source (IDE/Gradle), not from a packaged JAR.";
    }
}