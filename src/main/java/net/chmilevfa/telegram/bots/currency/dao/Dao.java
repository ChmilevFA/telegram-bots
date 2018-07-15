package net.chmilevfa.telegram.bots.currency.dao;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.state.MessageState;

/**
 * Simple Dao for state of currency bot.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
public interface Dao {

    /**
     * Saves user's language setting.
     *
     * @param userId user's identifier
     * @param language chosen user's language
     */
    void saveLanguage(Integer userId, Language language);

    /**
     * Retrieves user's language setting.
     *
     * @param userId user's identifier
     * @return saved user's language
     */
    Language getLanguage(Integer userId);

    /**
     * Saves current user's step in menu in particular chat with bot.
     *
     * @param userId user's identifier
     * @param chatId user's chat with bot identifier
     * @param state current user's step in menu
     */
    void saveMessageState(Integer userId, Long chatId, MessageState state);

    /**
     * Retrieves user's language setting.
     *
     * @param userId user's identifier
     * @param chatId user's chat with bot identifier
     * @return current user's step in menu
     */
    MessageState getState(Integer userId, Long chatId);

    /**
     * Saves first user's chosen currency in currency menu.
     * First user's chosen currency needs to be saved to
     * remember which first one from pair for currency rate was chosen.
     *
     * @param chatId user's chat with bot identifier
     * @param currency user's chosen currency
     */
    void saveFirstUserCurrency(Long chatId, Currencies currency);

    /**
     * Retrieves first user's chosen currency in currency menu.
     * Should be returned and deleted.
     *
     * @param chatId user's chat with bot identifier
     * @return first user's chosen currency in currency menu
     */
    Currencies getFirstUserCurrency(Long chatId);
}
