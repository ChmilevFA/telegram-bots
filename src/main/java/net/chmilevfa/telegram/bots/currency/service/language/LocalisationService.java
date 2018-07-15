package net.chmilevfa.telegram.bots.currency.service.language;

/**
 * Service to get specific strings for particular {@link Language}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
public interface LocalisationService {

    /**
     * Helps to get specific strings for particular {@link Language}.
     *
     * @param key name of string to be returned.
     * @param language language of string to be returned.
     * @return localized string
     */
    String getString(String key, Language language);
}
