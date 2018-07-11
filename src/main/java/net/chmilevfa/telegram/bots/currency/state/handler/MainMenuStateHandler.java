package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import net.chmilevfa.telegram.bots.currency.service.StringService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;

import static net.chmilevfa.telegram.bots.currency.service.StringService.*;

/**
 * Implementation of {@link StateHandler} which deals with chosen buttons of
 * main menu. Buttons should be displayed by {@link DefaultStateHandler}.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
@Component
public final class MainMenuStateHandler extends AbstractCurrencyStateHandler implements StateHandler {

    private final StateHandler defaultStateHandler;
    private final Dao dao;

    @Autowired
    public MainMenuStateHandler(StateHandler defaultStateHandler, JsonFileDao dao) {
        this.defaultStateHandler = defaultStateHandler;
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message) {
        SendMessage messageToSend;
        if (message.hasText()) {
            switch (message.getText()) {
                case CURRENT_RATE:
                    messageToSend = onCurrentRateChosen(message);
                    break;
                case SETTINGS:
                    messageToSend = onSettingsChosen(message);
                    break;
                case FEEDBACK:
                    messageToSend = onFeedbackChosen(message);
                    break;
                default:
                    messageToSend =  defaultStateHandler.getMessageToSend(message);
            }
        } else {
            messageToSend =  defaultStateHandler.getMessageToSend(message);
        }
        return messageToSend;
    }

    private SendMessage onCurrentRateChosen(Message message) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.CHOOSE_CURRENT_RATE_FIRST);
        ReplyKeyboardMarkup replyKeyboardMarkup = getCurrenciesKeyboard();
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup, CHOOSE_FIRST_CURRENCY);
    }

    //TODO complete
    private SendMessage onSettingsChosen(Message message) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.SETTINGS);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText("Not implemented yet");
        return sendMessage;
    }

    private SendMessage onFeedbackChosen(Message message) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.FEEDBACK);
        ReplyKeyboardMarkup replyKeyboardMarkup = getFeedbackKeyboard();
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup, WRITE_FEEDBACK);
    }

    private ReplyKeyboardMarkup getFeedbackKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(StringService.GO_TO_MAIN_MENU);

        replyKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardRow));

        return replyKeyboardMarkup;
    }
}
