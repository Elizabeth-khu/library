package com.example.library.ui.command;

import com.example.library.domain.BookValidator;
import com.example.library.i18n.Translator;
import com.example.library.service.LibraryService;
import com.example.library.ui.BookPrompter;
import com.example.library.ui.ConsoleIO;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class EditBookCommand implements Command {

    private static final Logger log = Logger.getLogger(EditBookCommand.class.getName());

    private final LibraryService libraryService;
    private final BookPrompter bookPrompter;
    private final BookValidator bookValidator;
    private final ConsoleIO consoleIO;
    private final Translator translator;

    public EditBookCommand(LibraryService libraryService, BookPrompter bookPrompter, BookValidator bookValidator, ConsoleIO consoleIO, Translator translator) {
        this.libraryService = libraryService;
        this.bookPrompter = bookPrompter;
        this.bookValidator = bookValidator;
        this.consoleIO = consoleIO;
        this.translator = translator;
    }

    @Override
    public void execute() {
        log.info("Edit book");
        long id = readId();

        var existing = libraryService.findById(id);
        if (existing.isEmpty()) {
            consoleIO.println(translator.translate("books.notFound" , id));
            return;
        }

        var draft = bookValidator.validateAndNormalize(bookPrompter.promptForEdit(existing.get()));
        libraryService.updateBook(id, draft).orElseThrow(() -> new IllegalStateException(translator.translate("books.notFound", id)));
        consoleIO.println(translator.translate("books.updated", id));
    }

    private long readId() {
        String raw = consoleIO.readLine(translator.translate("prompt.id.edit"));
        try {
            return Long.parseLong(raw.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(translator.translate("error.invalidId", raw), e);
        }
    }
}