package com.example.library.ui.command;

import com.example.library.ui.MenuAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommandDispatcher {

    private final Map<MenuAction, Command> commands;

    public CommandDispatcher(Map<MenuAction, Command> commands) {
        this.commands = commands;
    }

    public void dispatch(MenuAction menuAction) {
        Command command = commands.get(menuAction);
        if (command == null) {
            throw new IllegalArgumentException(String.format("Unknown menu action: %s", menuAction));
        }
        command.execute();
    }
}
