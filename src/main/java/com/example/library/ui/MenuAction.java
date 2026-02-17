package com.example.library.ui;

import java.util.Arrays;
import java.util.Optional;

public enum MenuAction {
    DISPLAY("1","Display book list"),
    CREATE("2","Create new book"),
    EDIT("3","Edit book"),
    DELETE("4","Delete book"),
    EXIT("0","Exit");

    private final String code;
    private final String label;
    MenuAction(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String code(){
        return code;
    }

    public String label(){
        return label;
    }

    public static Optional<MenuAction> from(String input) {
        String clearInput = input == null ? "" : input.trim();
        return Arrays.stream(values())
                .filter(action -> action.code.equals(clearInput))
                .findFirst();
    }
}