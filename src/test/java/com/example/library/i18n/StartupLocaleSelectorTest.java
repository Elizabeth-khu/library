package com.example.library.i18n;

import com.example.library.ui.ConsoleIO;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartupLocaleSelectorTest {

    @Test
    void selectsPolishWhenUserTypesPl() {
        TestConsoleIO io = new TestConsoleIO("pl");
        FixedLocaleProvider localeProvider = new FixedLocaleProvider();

        StartupLocaleSelector selector = selector(io, localeProvider);
        selector.select();

        assertEquals("pl", localeProvider.getLocale().getLanguage());
    }

    @Test
    void selectsEnglishWhenUserTypesEn() {
        TestConsoleIO io = new TestConsoleIO("en");
        FixedLocaleProvider localeProvider = new FixedLocaleProvider();

        selector(io, localeProvider).select();

        assertEquals("en", localeProvider.getLocale().getLanguage());
    }

    @Test
    void fallsBackToEnglishOnInvalidInputAndPrintsMessage() {
        TestConsoleIO io = new TestConsoleIO("xx");
        FixedLocaleProvider localeProvider = new FixedLocaleProvider();

        selector(io, localeProvider).select();

        assertEquals("en", localeProvider.getLocale().getLanguage());
        assertTrue(io.printed().contains("Unknown language") || io.printed().contains("Nieznany język"));
    }

    private StartupLocaleSelector selector(TestConsoleIO io, FixedLocaleProvider localeProvider) {
        Translator translator = translator(localeProvider);
        return new StartupLocaleSelector(io, translator, localeProvider);
    }

    private Translator translator(FixedLocaleProvider localeProvider) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);

        return new Translator(messageSource, localeProvider);
    }

    private static final class TestConsoleIO implements ConsoleIO {

        private final Deque<String> inputs = new ArrayDeque<>();
        private final StringBuilder out = new StringBuilder();

        private TestConsoleIO(String... lines) {
            for (String s : lines) {
                inputs.addLast(s);
            }
        }

        @Override
        public void println(String string) {
            out.append(string).append("\n");
        }

        @Override
        public String readLine(String prompt) {
            out.append(prompt);
            return inputs.isEmpty() ? "" : inputs.removeFirst();
        }

        String printed() {
            return out.toString();
        }
    }
}