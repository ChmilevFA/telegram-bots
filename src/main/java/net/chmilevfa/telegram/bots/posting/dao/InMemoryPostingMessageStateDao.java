package net.chmilevfa.telegram.bots.posting.dao;

import net.chmilevfa.telegram.bots.posting.state.PostingMessageState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Simple In-memory implementation of {@link PostingMessageStateDao}.
 *
 * @author chmilevfa
 * @since 08.08.18
 */
@Repository
public class InMemoryPostingMessageStateDao implements PostingMessageStateDao {

    //TODO Maybe Spring's aspects for implementations of DAOs would be better?
    private static Logger logger = LoggerFactory.getLogger(InMemoryPostingMessageStateDao.class);

    private Map<DialogId, PostingMessageState> states = new HashMap<>();

    @Override
    public void saveMessageState(Integer userId, Long chatId, PostingMessageState state) {
        DialogId dialogId = new DialogId(userId, chatId);
        states.put(dialogId, state);
        logger.trace("State {} for userId {} and chatId {} saved", state, userId, chatId);
    }

    @Override
    public PostingMessageState getState(Integer userId, Long chatId) {
        DialogId dialogId = new DialogId(userId, chatId);
        PostingMessageState result = states.getOrDefault(dialogId, PostingMessageState.MAIN_MENU);
        logger.trace("State for userId {} and chatId {} retrieved: {}", userId, chatId, result);
        return result;
    }

    /**
     * ID based on user and chat identifiers.
     */
    private static class DialogId {

        private Integer userId;
        private Long chatId;

        DialogId(Integer userId, Long chatId) {
            this.userId = userId;
            this.chatId = chatId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DialogId dialogId = (DialogId) o;
            return Objects.equals(userId, dialogId.userId) &&
                    Objects.equals(chatId, dialogId.chatId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, chatId);
        }
    }
}
