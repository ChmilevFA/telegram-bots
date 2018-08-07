package net.chmilevfa.telegram.bots.posting.dao;

import net.chmilevfa.telegram.bots.posting.state.PostingMessageState;

/**
 * DAO interface for objects of {@link PostingMessageState}.
 *
 * @author chmilevfa
 * @since 08.08.18
 */
public interface PostingMessageStateDao {

    /**
     * Saves current user's step in menu in particular chat with bot.
     *
     * @param userId user's identifier
     * @param chatId user's chat with bot identifier
     * @param state current user's step in menu
     */
    void saveMessageState(Integer userId, Long chatId, PostingMessageState state);

    /**
     * Retrieves user's language setting.
     *
     * @param userId user's identifier
     * @param chatId user's chat with bot identifier
     * @return current user's step in menu
     */
    PostingMessageState getState(Integer userId, Long chatId);
}
