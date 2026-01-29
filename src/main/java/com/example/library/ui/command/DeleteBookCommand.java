package com.example.library.ui.command;

import com.example.library.storage.BooksStorage;
import com.example.library.ui.ConsoleIO;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DeleteBookCommand implements Command {

    private static final Logger log = Logger.getLogger(DeleteBookCommand.class.getName());

    private final BooksStorage booksStorage;
    private final ConsoleIO consoleIO;

    public DeleteBookCommand(BooksStorage booksStorage, ConsoleIO consoleIO) {
        this.booksStorage = booksStorage;
        this.consoleIO = consoleIO;
    }

    @Override
    public void execute() {
        log.info("Delete book");
        try{
            long id = readId("Enter id to delete: ");
            boolean deleted = booksStorage.delete(id);
            consoleIO.println(deleted ? "Deleted book id=" + id : "Book not found: id=" + id);
        }catch(RuntimeException e) {
            consoleIO.println("Error: " + e.getMessage());
            log.warning("Delete book failed: " + e.getMessage());
        }
    }

    private long readId(String prompt) {
        String raw = consoleIO.readLine(prompt);
        try{
            return Long.parseLong(raw.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid id: " + raw, e);
        }
    }

}
