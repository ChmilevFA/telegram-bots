package net.chmilevfa.telegram.bots.posting.state;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Basic interface for all state handlers for posting bot that deal with users answers.
 *
 * @author chmilevfa
 * @since 09.08.18
 */
public interface PostingStateHandler {

    /**
     * Creates bot's answer based on incoming message and user's language.
     *
     * @param message incoming message
     * @return bot's answer
     */
    SendMessage getMessageToSend(Message message);

    /**
     * Returns processed {@link PostingMessageState} by current handler.
     */
    PostingMessageState getProcessedMessageState();
}
