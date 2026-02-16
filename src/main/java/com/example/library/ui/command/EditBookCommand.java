package com.example.library.ui.command;

import com.example.library.domain.BookValidator;
import com.example.library.i18n.Translator;
import com.example.library.storage.BooksStorage;
import com.example.library.ui.BookPrompter;
import com.example.library.ui.ConsoleIO;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class EditBookCommand implements Command {

    private static final Logger log = Logger.getLogger(EditBookCommand.class.getName());

    private final BooksStorage booksStorage;
    private final BookPrompter bookPrompter;
    private final BookValidator bookValidator;
    private final ConsoleIO consoleIO;
    private final Translator translator;

    public EditBookCommand(BooksStorage booksStorage, BookPrompter bookPrompter, BookValidator bookValidator, ConsoleIO consoleIO, Translator translator) {
        this.booksStorage = booksStorage;
        this.bookPrompter = bookPrompter;
        this.bookValidator = bookValidator;
        this.consoleIO = consoleIO;
        this.translator = translator;
    }

    @Override
    public void execute() {
        log.info("Edit book");
        long id = readId();

        var existing = booksStorage.findById(id);
        if (existing.isEmpty()) {
            consoleIO.println(translator.translate("books.notFound" , id));
            return;
        }

        var draft = bookValidator.validateAndNormalize(bookPrompter.promptForEdit(existing.get()));
        booksStorage.update(id, draft).orElseThrow(() -> new IllegalStateException(translator.translate("books.notFound", id)));
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