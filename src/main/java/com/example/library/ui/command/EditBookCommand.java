package com.example.library.ui.command;

import com.example.library.domain.BookValidator;
import com.example.library.storage.BooksStorage;
import com.example.library.ui.BookFormatter;
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
        log.info("Enter book");
        try {
            long id = readId("Enter id to edit: ");

            var existing = booksStorage.findById(id);
            if (existing.isEmpty()) {
                consoleIO.println("Book not found: id=" + id);
                return;
            }

            var draft = bookValidator.validateAndNormalize(bookPrompter.promptForEdit(existing.get()));
            var updated = booksStorage.update(id, draft);

            consoleIO.println(updated.isPresent() ? "Updated book id=" + id : "Book not found: id=" + id);
        }catch(RuntimeException e) {
            consoleIO.println("Error: " + e.getMessage());
            log.warning("Edit book failed: " + e.getMessage());
        }
    }

    private long readId(String prompt) {
        String raw = consoleIO.readLine(prompt);
        try{
            return Long.parseLong(raw.trim());
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid id: " + raw);
        }
    }

}
