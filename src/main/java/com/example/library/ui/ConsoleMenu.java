package com.example.library.ui;

import com.example.library.service.LibraryService;
import org.springframework.stereotype.Component;

@Component
public class ConsoleMenu {

    private final ConsoleIO io;
    private final LibraryService service;
    private final BookFormatter formatter;
    private final BookPrompter prompter;

    public ConsoleMenu(ConsoleIO io, LibraryService service, BookFormatter formatter, BookPrompter prompter) {
        this.io = io;
        this.service = service;
        this.formatter = formatter;
        this.prompter = prompter;
    }

    public void run(){
        while(true){
            printMenu();
            MenuAction action = readAction();
            if(action == MenuAction.EXIT) return;
            handle(action);
        }
    }

    private void handle(MenuAction action) {
        try{
            switch (action) {
                case DISPLAY -> display();
                case CREATE -> create();
                case EDIT -> edit();
                case DELETE -> delete();
                default -> { }
            }
        }catch (RuntimeException e){
            io.println("Error: " + e.getMessage());
        }
    }

    private void delete(){
        long id = readId("Enter id to delete: ");
        io.println(service.delete(id)? "Deleted" : "Book not found: id=" + id);
    }

    private void display() {
        io.println(formatter.formatList(service.list()));
    }

    private void create() {
        var data = prompter.promptForCreate();
        var created = service.create(data.title(), data.author(), data.description());
        io.println("Created book with id=" + created.getId());
    }

    private void edit() {
        long id = readId("Enter id to edit: ");

        var existing = service.findById(id);
        if (existing.isEmpty()) {
            io.println("Book not found: id=" + id);
            return;
        }

        var data = prompter.promptForEdit(existing.get());
        var updated = service.edit(id, data.title(), data.author(), data.description());

        if (updated.isEmpty()) {
            io.println("Book not found: id=" + id);
            return;
        }

        io.println("Updated book id=" + id);
    }

    private long readId(String prompt) {
        String s = io.readLine(prompt);
        try{
            return Long.parseLong(s.trim());
        }catch (Exception e){
            throw new IllegalArgumentException("Not a number: " + s);
        }
    }

    private MenuAction readAction() {
        String input = io.readLine("Choose option: ");
        return MenuAction.from(input).orElseThrow(()-> new IllegalArgumentException("Unknown option"));
    }

    private void printMenu() {
        io.println("");
        io.println("====Library====");
        for(MenuAction a : MenuAction.values()){
            io.println(a.code() +") "+ a.label());
        }
    }
}