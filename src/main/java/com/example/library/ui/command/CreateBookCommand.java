package com.example.library.ui.command;

import com.example.library.domain.BookValidator;
import com.example.library.i18n.Translator;
import com.example.library.service.LibraryService;
import com.example.library.ui.BookPrompter;
import com.example.library.ui.ConsoleIO;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class CreateBookCommand implements Command {

    private static final Logger log = Logger.getLogger(CreateBookCommand.class.getName());

    private final LibraryService libraryService;
    private final BookPrompter prompter;
    private final BookValidator validator;
    private final ConsoleIO consoleIO;
    private final Translator translator;

    public CreateBookCommand(LibraryService libraryService, BookPrompter prompter, BookValidator validator, ConsoleIO consoleIO, Translator translator) {
        this.libraryService = libraryService;
        this.prompter = prompter;
        this.validator = validator;
        this.consoleIO = consoleIO;
        this.translator = translator;
    }

    @Override
    public void execute() {
        log.info("Create book");
        var draft = validator.validated(prompter.promptForCreate());        var created = libraryService.createBook(draft);
        consoleIO.println(translator.translate("books.created", created));
    }
}