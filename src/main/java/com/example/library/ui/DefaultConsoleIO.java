package com.example.library.ui;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class DefaultConsoleIO implements ConsoleIO {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void println(String string) {
        System.out.println(string);
    }

    @Override
    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
