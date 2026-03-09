package com.example.library.i18n;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FixedLocaleProvider implements LocaleProvider {
    private Locale locale = Locale.ENGLISH;

    @Override
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
