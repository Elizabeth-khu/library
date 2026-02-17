package com.example.library.i18n;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class Translator {

    private final MessageSource messageSource;
    private final LocaleProvider localeProvider;
    public Translator(MessageSource messageSource, LocaleProvider localeProvider) {
        this.messageSource = messageSource;
        this.localeProvider = localeProvider;
    }

    public String translate(String key, Object... params) {
        return messageSource.getMessage(key, params, localeProvider.getLocale());
    }
}
