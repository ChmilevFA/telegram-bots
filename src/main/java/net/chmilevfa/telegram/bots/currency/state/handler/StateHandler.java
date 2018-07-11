package net.chmilevfa.telegram.bots.currency.state.handler;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Basic interface for all state handler for dealing with users answers.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
public interface StateHandler {

    /**
     * TODO
     * @param message
     * @return
     */
    SendMessage getMessageToSend(Message message);
}
