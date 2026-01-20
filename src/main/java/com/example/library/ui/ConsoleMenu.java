package com.example.library.ui;

import com.example.library.ui.command.CommandDispatcher;
import org.springframework.stereotype.Component;

@Component
public class ConsoleMenu {

    private final CommandDispatcher dispatcher;
    private final ConsoleIO io;

    public ConsoleMenu(ConsoleIO io,CommandDispatcher dispatcher, BookFormatter formatter, BookPrompter prompter) {
        this.io = io;
        this.dispatcher = dispatcher;
    }

    public void run(){
        while(true){
            printMenu();
            MenuAction action = readAction();
            if(action == MenuAction.EXIT) return;
            dispatcher.dispatch(action);
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