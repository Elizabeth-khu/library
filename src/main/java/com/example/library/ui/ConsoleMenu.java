package com.example.library.ui;

import com.example.library.ui.command.CommandDispatcher;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ConsoleMenu {

    private static final Logger log = Logger.getLogger(ConsoleMenu.class.getName());
    private final CommandDispatcher dispatcher;
    private final ConsoleIO io;

    public ConsoleMenu(ConsoleIO io,CommandDispatcher dispatcher, BookFormatter formatter, BookPrompter prompter) {
        this.io = io;
        this.dispatcher = dispatcher;
    }

    public void run() {
        while (true) {
            printMenu();
            try {
                MenuAction action = readAction();
                if (action == MenuAction.EXIT) {
                    return;
                }
                dispatcher.dispatch(action);
            } catch (RuntimeException e) {
                io.println("Error: " + safeMessage(e));
                log.log(Level.WARNING, "Menu action failed", e);
            }
        }
    }

    private String safeMessage(RuntimeException e) {
        String msg = e.getMessage();
        return (msg == null || msg.isBlank()) ? e.getClass().getSimpleName() : msg;
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