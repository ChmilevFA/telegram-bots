package net.chmilevfa.telegram.bots.currency.dao;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.state.MessageState;

/**
 * Simple Dao for state of currency bot.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
public interface Dao {

    /**
     *
     * @param userId
     * @param chatId
     * @param state
     */
    void saveMessageState(Integer userId, Long chatId, MessageState state);

    /**
     *
     * @param userId
     * @param chatId
     * @return
     */
    MessageState getState(Integer userId, Long chatId);

    /**
     *
     * @param chatId
     * @param currency
     */
    void saveFirstUserCurrency(Long chatId, Currencies currency);

    /**
     *
     * @param chatId
     * @return
     */
    Currencies getFirstUserCurrency(Long chatId);
}
