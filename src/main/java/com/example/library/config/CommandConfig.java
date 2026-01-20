package com.example.library.config;

import com.example.library.ui.MenuAction;
import com.example.library.ui.command.Command;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class CommandConfig {

    @Bean
    public Map<MenuAction, Command> commands() {
        return new EnumMap<>(MenuAction.class);
    }
}
