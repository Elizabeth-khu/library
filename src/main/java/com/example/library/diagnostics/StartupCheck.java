package com.example.library.diagnostics;

import com.example.library.domain.Book;
import com.example.library.service.LibraryService;
import org.springframework.stereotype.Component;

@Component
public class StartupCheck {

    private final LibraryService service;

    public StartupCheck(LibraryService service) {
        this.service = service;
    }

    public void run() {
        int before = service.list().size();

        Book created = service.create("Temp title", "Temp author", "Temp description");
        require(service.list().size() == before + 1, "create failed");

        require(service.edit(created.getId(), "Edited", "Edited", "Edited").isPresent(), "edit failed");
        require(service.delete(created.getId()), "delete failed");

        require(service.list().size() == before, "cleanup failed");
        System.out.println("Startup CRUD check ✅ OK");
    }

    private void require(boolean ok, String message) {
        if (!ok) {
            throw new IllegalStateException(message);
        }
    }
}