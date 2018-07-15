package net.chmilevfa.telegram.bots.currency.service.language;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of {@link Language} which uses standard Java .properties files to retrieve localized strings.
 *
 * @see PropertyResourceBundle
 *
 * @author chmilevfa
 * @since 13.07.18
 */
@Service
public class PropertyLocalisationService implements LocalisationService {

    private static final String STRINGS_FILE = "strings";

    private static final ResourceBundle defaultLanguage;
    private static final ResourceBundle russianLanguage;

    static {
        defaultLanguage = PropertyResourceBundle.getBundle(STRINGS_FILE, Locale.ROOT);
        russianLanguage =
                PropertyResourceBundle.getBundle(STRINGS_FILE, new Locale(Language.RUSSIAN.getCode()));
    }

    @Override
    public String getString(String key, Language language) {
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
