package com.example.library.ui;

import com.example.library.i18n.Translator;
import com.example.library.ui.command.CommandDispatcher;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ConsoleMenu {

    private static final Logger log = Logger.getLogger(ConsoleMenu.class.getName());
    private final CommandDispatcher dispatcher;
    private final ConsoleIO io;
    private final Translator translator;

    public ConsoleMenu(ConsoleIO io, CommandDispatcher dispatcher, Translator translator) {
        this.io = io;
        this.dispatcher = dispatcher;
        this.translator = translator;
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
                io.println(translator.translate("error.prefix", safeMessage(e)));
                log.log(Level.WARNING, "Menu action failed", e);
            }
        }
    }

    private String safeMessage(RuntimeException e) {
        String msg = e.getMessage();
        return (msg == null || msg.isBlank()) ? e.getClass().getSimpleName() : msg;
    }

    private MenuAction readAction() {
        String input = io.readLine(translator.translate("menu.choose"));
        return MenuAction.from(input).orElseThrow(()-> new IllegalArgumentException(translator.translate("error.unknownOption")));
    }

    private void printMenu() {
        io.println("");
        io.println(translator.translate("app.title"));
        io.println(translator.translate("menu.option.display"));
        io.println(translator.translate("menu.option.create"));
        io.println(translator.translate("menu.option.edit"));
        io.println(translator.translate("menu.option.delete"));
        io.println(translator.translate("menu.option.exit"));
    }
}