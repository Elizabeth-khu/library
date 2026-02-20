package com.example.library.ui.command;

import com.example.library.i18n.Translator;
import com.example.library.service.LibraryService;
import com.example.library.ui.BookFormatter;
import com.example.library.ui.ConsoleIO;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DisplayBooksCommand implements Command {

    private static final Logger log = Logger.getLogger(DisplayBooksCommand.class.getName());

    private final LibraryService libraryService;
    private final BookFormatter bookFormatter;
    private final ConsoleIO consoleIO;
    private final Translator translator;

    public DisplayBooksCommand(LibraryService libraryService, BookFormatter bookFormatter, ConsoleIO consoleIO, Translator translator) {
        this.libraryService = libraryService;
        this.bookFormatter = bookFormatter;
        this.consoleIO = consoleIO;
        this.translator = translator;
    }

    @Override
    public void execute() {
        log.info("Displaying book list");
        var books = libraryService.listBooks();

        if (books.isEmpty()) {
            consoleIO.println(translator.translate("books.none"));
            return;
        }

        consoleIO.println(bookFormatter.formatList(books));
    }
}