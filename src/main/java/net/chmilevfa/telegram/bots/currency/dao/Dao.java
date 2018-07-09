package net.chmilevfa.telegram.bots.currency.dao;

import net.chmilevfa.telegram.bots.currency.states.MessageState;

/**
 * Simple Dao for state of currency bot.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
public interface Dao {

    public void saveMessageState(Integer userId, Long chatId, MessageState state);

    public MessageState getState(Integer userId, Long chatId);
}
