package com.example.library.config;

import com.example.library.ui.MenuAction;
import com.example.library.ui.command.Command;
import com.example.library.ui.command.DisplayBooksCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class CommandConfig {

    @Bean
    public Map<MenuAction, Command> commands(DisplayBooksCommand displayBooksCommand) {
        Map<MenuAction, Command> map = new EnumMap<>(MenuAction.class);
        map.put(MenuAction.DISPLAY, displayBooksCommand);
        return map;
    }
}
