package com.example.library;

import com.example.library.ui.ConsoleMenu;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext("com.example.library")) {
            ctx.getBean(ConsoleMenu.class).run();
        }
    }
}