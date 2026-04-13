package com.example.library.ui;

import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.i18n.Translator;
import com.example.library.service.LibraryService;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class BookPrompter {

    private final ConsoleIO io;
    private final Translator translator;
    private final LibraryService libraryService;

    public BookPrompter(ConsoleIO io, Translator translator, LibraryService libraryService) {
        this.io = io;
        this.translator = translator;
        this.libraryService = libraryService;
    }

    public BookDraft promptForCreate() {
        String title = io.readLine(translator.translate("prompt.title"));
        String description = io.readLine(translator.translate("prompt.description"));
        return new BookDraft(title, "", description);
    }

    public Optional<Long> promptForAuthorId() {
        String input = io.readLine(translator.translate("prompt.author.id"));
        try {
            return Optional.of(Long.parseLong(input.trim()));
        } catch (NumberFormatException e) {
            io.println(translator.translate("error.invalidIdFormat"));
            return Optional.empty();
        }
    }

    public BookDraft promptForEdit(Book existing) {
        String title = io.readLine(translator.translate("prompt.title.edit", existing.getTitle()));
        String description = io.readLine(translator.translate("prompt.description.edit", existing.getDescription()));

        return new BookDraft(
                emptyAsDefault(title, existing.getTitle()),
                "",
                emptyAsDefault(description, existing.getDescription())
        );
    }

    private String emptyAsDefault(String input, String defaultValue) {
        return Optional.ofNullable(input)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElse(defaultValue);
    }
}