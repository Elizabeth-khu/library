package com.example.library.i18n;
import com.example.library.ui.ConsoleIO;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class StartupLocaleSelector {

    private final ConsoleIO consoleIO;
    private final Translator translator;
    private final FixedLocaleProvider fixedLocaleProvider;

    public StartupLocaleSelector(ConsoleIO consoleIO, Translator translator, FixedLocaleProvider fixedLocaleProvider) {
        this.consoleIO = consoleIO;
        this.translator = translator;
        this.fixedLocaleProvider = fixedLocaleProvider;
    }

    public void select(){
        String raw = consoleIO.readLine(translator.translate("locale.choose"));
        Locale locale = toLocale(raw);
        if (locale == null) {
            consoleIO.println(translator.translate("locale.invalid"));
            locale = Locale.ENGLISH;
        }
        fixedLocaleProvider.setLocale(locale);
    }

    private Locale toLocale(String raw) {
        String value = raw == null ? "" : raw.trim().toLowerCase();
        if(value.equals("en")) return Locale.ENGLISH;
        if(value.equals("pl")) return new Locale("pl", "PL");
        return null;
    }
}
