package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Default implementation of {@link StateHandler} which actually shows main menu keyboard.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
@Component
public final class DefaultStateHandler implements StateHandler {

    private final Dao dao;

    @Autowired
    public DefaultStateHandler(JsonFileDao dao) {
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message, Language language) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.MAIN_MENU);
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard(language);
        return getDefaultSendMessage(message, replyKeyboardMarkup, language);
    }

    private SendMessage getDefaultSendMessage(Message message,
                                              ReplyKeyboardMarkup replyKeyboardMarkup,
                                              Language language) {
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup,
                LocalisationService.getString("helloMessage", language));
    }
}
