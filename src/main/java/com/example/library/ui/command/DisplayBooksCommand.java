package com.example.library.ui.command;

import com.example.library.i18n.Translator;
import com.example.library.storage.BooksStorage;
import com.example.library.ui.BookFormatter;
import com.example.library.ui.ConsoleIO;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DisplayBooksCommand implements Command {

    private static final Logger log = Logger.getLogger(DisplayBooksCommand.class.getName());

    private final BooksStorage booksStorage;
    private final BookFormatter bookFormatter;
    private final ConsoleIO consoleIO;
    private final Translator translator;

    public DisplayBooksCommand(BooksStorage booksStorage, BookFormatter bookFormatter, ConsoleIO consoleIO, Translator translator) {
        this.booksStorage = booksStorage;
        this.bookFormatter = bookFormatter;
        this.consoleIO = consoleIO;
        this.translator = translator;
    }

    @Override
    public void execute() {
        log.info("Displaying book list");
        var books = booksStorage.books();

        if (books.isEmpty()) {
            consoleIO.println(translator.translate("books.none"));
            return;
        }

        consoleIO.println(bookFormatter.formatList(books));
    }
}