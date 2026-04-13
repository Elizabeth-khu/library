package com.example.library.i18n;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslatorTest {

    @Test
    void translateEnglishByDefault() {
        Translator translator = translatorWithLocale(Locale.ENGLISH);

        assertEquals("Choose option: ", translator.translate("menu.choose"));
        assertEquals("Created book with id=5", translator.translate("books.created", 5));
    }

    @Test
    void translatePolishWhenLocaleIsPolish() {
        Translator translator = translatorWithLocale(new Locale("pl","PL"));

        assertEquals("Wybierz opcję: ", translator.translate("menu.choose"));
        assertEquals("Utworzono książkę o id=5", translator.translate("books.created", 5));
    }

    private Translator translatorWithLocale(Locale locale) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);

        FixedLocaleProvider localeProvider = new FixedLocaleProvider();
        localeProvider.setLocale(locale);

        return new Translator(messageSource, localeProvider);
    }
}
