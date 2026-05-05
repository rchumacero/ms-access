package com.kplian.msaccess.api.service;

import jakarta.enterprise.context.RequestScoped;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@RequestScoped
public class I18nService {
    private static final String BUNDLE_BASE = "i18N/message";
    private static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("es-ES");
    private Locale currentLocale = DEFAULT_LOCALE;

    public void setLocale(Locale locale) {
        this.currentLocale = locale == null ? DEFAULT_LOCALE : locale;
    }

    public void clearLocale() {
        this.currentLocale = DEFAULT_LOCALE;
    }

    public Locale getLocale() {
        return currentLocale;
    }

    public String get(String key, Object... params) {
        Locale locale = getLocale();
        String message = resolveKey(key, locale);
        return MessageFormat.format(message, params);
    }

    public boolean hasKey(String key, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE, locale == null ? DEFAULT_LOCALE : locale);
            return bundle.containsKey(key);
        } catch (MissingResourceException ex) {
            return false;
        }
    }

    private String resolveKey(String key, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE, locale);
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        } catch (MissingResourceException ex) {
            // fallback below
        }
        try {
            ResourceBundle fallback = ResourceBundle.getBundle(BUNDLE_BASE, DEFAULT_LOCALE);
            return fallback.containsKey(key) ? fallback.getString(key) : key;
        } catch (MissingResourceException ex) {
            return key;
        }
    }
}
