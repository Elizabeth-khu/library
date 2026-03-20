package com.example.library.ui;

import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.i18n.Translator;
import com.example.library.storage.hibernate.HibernateAuthorsRepository; // Добавляем репозиторий
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookPrompter {

    private final ConsoleIO io;
    private final Translator translator;
    private final HibernateAuthorsRepository authorsRepository;

    public BookPrompter(ConsoleIO io, Translator translator, HibernateAuthorsRepository authorsRepository) {
        this.io = io;
        this.translator = translator;
        this.authorsRepository = authorsRepository;
    }

    public BookDraft promptForCreate() {
        String title = io.readLine(translator.translate("prompt.title"));

        long authorId = promptForAuthorId();

        String description = io.readLine(translator.translate("prompt.description"));

        return new BookDraft(title, authorId, description);
    }

    public BookDraft promptForEdit(Book existing) {
        String titleInput = io.readLine(translator.translate("prompt.title.edit", existing.getTitle()));
        String title = emptyAsDefault(titleInput, existing.getTitle());

        io.print(translator.translate("prompt.author.current", existing.getAuthorsAsString()));
        long authorId = promptForAuthorId();

        String descInput = io.readLine(translator.translate("prompt.description.edit", existing.getDescription()));
        String description = emptyAsDefault(descInput, existing.getDescription());

        return new BookDraft(title, authorId, description);
    }

    private long promptForAuthorId() {
        List<Author> authors = authorsRepository.findAll();

        io.print(translator.translate("prompt.author.select_list"));

        authors.forEach(author ->
                io.print(String.format("[%d] %s", author.getId(), author.getName()))
        );

        while (true) {
            String input = io.readLine(translator.translate("prompt.author.enter_id"));
            try {
                long id = Long.parseLong(input);
                if (authors.stream().anyMatch(a -> a.getId() == id)) {
                    return id;
                }
                io.print(translator.translate("error.author.not_found"));
            } catch (NumberFormatException e) {
                io.print(translator.translate("error.invalid.number"));
            }
        }
    }

    private String emptyAsDefault(String input, String defaultValue) {
        return Optional.ofNullable(input)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElse(defaultValue);
    }
}