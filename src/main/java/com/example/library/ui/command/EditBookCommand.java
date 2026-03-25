package com.example.library.ui.command;

import com.example.library.domain.Author;
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
        long id = readId();

        Optional<Book> bookOpt = libraryService.findById(id);
        if (bookOpt.isEmpty()) {
            consoleIO.println(translator.translate("books.notFound", id));
            return;
        }

        Book book = bookOpt.get();

        BookDraft draft = bookValidator.validated(bookPrompter.promptForEdit(book));

        Author author = bookPrompter.promptForAuthor();

        book.setTitle(draft.title());
        book.setDescription(draft.description());

        book.getAuthors().clear();
        book.addAuthor(author);

        libraryService.save(book);

        consoleIO.println(translator.translate("books.updated", id));
    }

    private long readId() {
        String raw = consoleIO.readLine(translator.translate("prompt.id.edit"));
        try {
            return Long.parseLong(raw.trim());
        } catch (Exception e) {
            consoleIO.println(translator.translate("error.invalidId", raw));
            return -1;
        }
    }
}