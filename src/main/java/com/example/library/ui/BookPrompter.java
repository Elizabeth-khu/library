package com.example.library.ui;

import com.example.library.domain.Book;
import com.example.library.domain.BookDraft;
import com.example.library.i18n.Translator;
import org.springframework.stereotype.Component;

@Component
public class BookPrompter {

    private final ConsoleIO io;
    private final Translator translator;

    public BookPrompter(ConsoleIO io, Translator translator) {
        this.io = io;
        this.translator = translator;
    }
    
    public BookDraft promptForCreate(){
        String title = io.readLine(translator.translate("prompt.title"));
        String author = io.readLine( translator.translate("prompt.author"));
        String description = io.readLine(translator.translate("prompt.description"));
        return new BookDraft(title, author, description);
    }
    
    public BookDraft promptForEdit(Book existing){
        String title = io.readLine(translator.translate("prompt.title.edit", existing.getTitle()));
        String author = io.readLine(translator.translate("prompt.author.edit", existing.getAuthor()));
        String description = io.readLine(translator.translate("prompt.description.edit", existing.getDescription()));
        return new BookDraft(
                emptyAsDefault(title, existing.getTitle()),
                emptyAsDefault(author, existing.getAuthor()),
                emptyAsDefault(description, existing.getDescription())
        );
    }

    private String emptyAsDefault(String input, String defaultValue) {
        if (input == null) return defaultValue;

        String t = input.trim();
        return t.isEmpty() ? defaultValue : t;
    }
}
