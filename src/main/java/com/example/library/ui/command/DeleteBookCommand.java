package com.example.library.ui.command;

import com.example.library.i18n.Translator;
import com.example.library.service.LibraryService;
import com.example.library.ui.ConsoleIO;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DeleteBookCommand implements Command {

    private static final Logger log = Logger.getLogger(DeleteBookCommand.class.getName());

    private final LibraryService libraryService;
    private final ConsoleIO consoleIO;
    private final Translator translator;

    public DeleteBookCommand(LibraryService libraryService, ConsoleIO consoleIO, Translator translator) {
        this.libraryService = libraryService;
        this.consoleIO = consoleIO;
        this.translator = translator;
    }

    @Override
    public void execute() {
        log.info("Delete book");
        long id = readId();
        boolean deleted = libraryService.deleteBook(id);
        consoleIO.println(deleted ?
                translator.translate("books.deleted", id) :
                translator.translate("book.notFound", id));
    }

    private long readId() {
        String raw = consoleIO.readLine(translator.translate("prompt.id.delete"));
        try {
            return Long.parseLong(raw.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(translator.translate("error.invalidId", raw), e);
        }
    }

}
