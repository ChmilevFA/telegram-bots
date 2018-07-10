package net.chmilevfa.telegram.bots.currency.states;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * TODO description
 *
 * @author chmilevfa
 * @since 10.07.18
 */
public class SettingsStateHandler implements StateHandler {

    @Override
    public SendMessage getMessageToSend(Message message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard();
        return MessageUtils
                .getSendMessageWithKeyboard(message, replyKeyboardMarkup, "Not implemented yet");
    }
}
