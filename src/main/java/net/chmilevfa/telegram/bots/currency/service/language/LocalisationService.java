package net.chmilevfa.telegram.bots.currency.service.language;

import java.util.*;

/**
 * Helps to get specific strings for particular {@link Language}.
 *
 * @author chmilevfa
 * @since 13.07.18
 */
public class LocalisationService {

    private static final String STRINGS_FILE = "strings";

    private static final ResourceBundle defaultLanguage;
    private static final ResourceBundle russianLanguage;

    static {
        defaultLanguage = PropertyResourceBundle.getBundle(STRINGS_FILE, Locale.ROOT);
        russianLanguage =
                PropertyResourceBundle.getBundle(STRINGS_FILE, new Locale(Language.RUSSIAN.getCode()));
    }

    public static String getString(String key, Language language) {
        String result;
        switch (language) {
            case RUSSIAN:
                result = russianLanguage.getString(key);
                break;
            default:
                result = defaultLanguage.getString(key);
        }
        return result;
    }
}
