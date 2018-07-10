package net.chmilevfa.telegram.bots.currency.states;

import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static net.chmilevfa.telegram.bots.currency.service.StringService.*;

/**
 * Default implementation of {@link StateHandler} which actually shows main menu keyboard.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
public final class DefaultStateHandler implements StateHandler {

    private final JsonFileDao dao;

    public DefaultStateHandler(JsonFileDao dao) {
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.MAIN_MENU);
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard();
        return getDefaultSendMessage(message, replyKeyboardMarkup);
    }

    private SendMessage getDefaultSendMessage(Message message, ReplyKeyboardMarkup replyKeyboardMarkup) {
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup, HELLO_MESSAGE);
    }
}
