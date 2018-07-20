package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Basic interface for all state handlers that deal with users answers.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
public interface StateHandler {

    /**
     * Creates bot's answer based on incoming message and user's language.
     *
     * @param message incoming message
     * @param language user's language
     * @return bot's answer
     */
    SendMessage getMessageToSend(Message message, Language language);

    /**
     * Returns processed {@link MessageState} by current handler.
     */
    MessageState getProcessedMessageState();
}
