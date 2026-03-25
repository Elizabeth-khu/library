package com.example.library.ui.command;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.domain.BookValidator;
import com.example.library.i18n.Translator;
import com.example.library.service.LibraryService;
import com.example.library.ui.BookPrompter;
import com.example.library.ui.ConsoleIO;
import org.springframework.stereotype.Component;

import java.util.Optional;
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
        log.info("Edit book start");
        long bookId = readId();

        Optional<Book> existingOpt = libraryService.findById(bookId);
        if (existingOpt.isEmpty()) {
            consoleIO.println(translator.translate("books.notFound", bookId));
            return;
        }

        BookDraft draft = bookValidator.validated(bookPrompter.promptForEdit(existingOpt.get()));

        Optional<Long> authorIdOpt = Optional.empty();
        while (authorIdOpt.isEmpty()) {
            authorIdOpt = bookPrompter.promptForAuthorId();
        }

        try {
            libraryService.updateBook(bookId, draft, authorIdOpt.get());
            consoleIO.println(translator.translate("books.updated", bookId));
        } catch (IllegalArgumentException e) {
            consoleIO.println(e.getMessage());
        }
    }

    private long readId() {
        String raw = consoleIO.readLine(translator.translate("prompt.id.edit"));
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException e) {
            consoleIO.println(translator.translate("error.invalidIdFormat"));
            return -1;
        }
    }
}