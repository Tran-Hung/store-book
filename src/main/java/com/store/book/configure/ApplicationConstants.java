package com.store.book.configure;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ApplicationConstants {
    public static final List<Locale> LOCALES = Arrays.asList(new Locale("en"),
            new Locale("es"),
            new Locale("fr"),
            new Locale("es", "MX"),
            new Locale("zh"),
            new Locale("ja"));
}
