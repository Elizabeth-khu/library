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
public class CreateBookCommand implements Command {

    private static final Logger log = Logger.getLogger(CreateBookCommand.class.getName());

    private final LibraryService libraryService;
    private final BookPrompter prompter;
    private final BookValidator validator;
    private final ConsoleIO io;
    private final Translator translator;

    public CreateBookCommand(LibraryService libraryService, BookPrompter prompter, BookValidator validator, ConsoleIO io, Translator translator) {
        this.libraryService = libraryService;
        this.prompter = prompter;
        this.validator = validator;
        this.io = io;
        this.translator = translator;
    }

    @Override
    public void execute() {
        log.info("create book start");
        BookDraft draft = validator.validated(prompter.promptForCreate());

        Optional<Long> authorIdOpt = Optional.empty();
        while (authorIdOpt.isEmpty()) {
            authorIdOpt = prompter.promptForAuthorId();
        }

        Book book = libraryService.createBook(draft, authorIdOpt.get());

        io.println(translator.translate("books.created", book.getId()));
    }
}