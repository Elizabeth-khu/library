package com.example.library.ui.command;

import com.example.library.domain.BookValidator;
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

    public EditBookCommand(BooksStorage booksStorage, BookPrompter bookPrompter, BookValidator bookValidator, ConsoleIO consoleIO) {
        this.booksStorage = booksStorage;
        this.bookPrompter = bookPrompter;
        this.bookValidator = bookValidator;
        this.consoleIO = consoleIO;
    }

    @Override
    public void execute() {
        log.info("Edit book");
        long id = readId();

        var existing = booksStorage.findById(id);
        if (existing.isEmpty()) {
            consoleIO.println("Book not found: id=" + id);
            return;
        }

        var draft = bookValidator.validateAndNormalize(bookPrompter.promptForEdit(existing.get()));
        booksStorage.update(id, draft).orElseThrow(() -> new IllegalStateException("Book not found: id=" + id));
        consoleIO.println("Updated book id=" + id);
    }

    private long readId() {
        String raw = consoleIO.readLine("Enter id to edit: ");
        try {
            return Long.parseLong(raw.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid id: " + raw, e);
        }
    }
}