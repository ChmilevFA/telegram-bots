package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * TODO description, complete
 *
 * @author chmilevfa
 * @since 10.07.18
 */
@Component
public class SettingsStateHandler implements StateHandler {

    @Override
    public SendMessage getMessageToSend(Message message, Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard(language);
        return MessageUtils
                .getSendMessageWithKeyboard(message, replyKeyboardMarkup, "Not implemented yet");
    }
}
