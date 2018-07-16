package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import net.chmilevfa.telegram.bots.currency.state.UserAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link StateHandler} which deals with user's answers in the settings menu.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
@Component
public final class SettingsStateHandler implements StateHandler {

    private static Logger logger = LoggerFactory.getLogger(SettingsStateHandler.class);

    private final LocalisationService localisationService;
    private final StateHandler defaultStateHandler;
    private final Dao dao;

    @Autowired
    public SettingsStateHandler(
            LocalisationService localisationService,
            StateHandler defaultStateHandler,
            JsonFileDao dao) {
        this.localisationService = localisationService;
        this.defaultStateHandler = defaultStateHandler;
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message, Language language) {
        SendMessage messageToSend;
        if (message.hasText()) {
            String messageText = message.getText();

            logger.trace("Received message: \"{}\" from userId: {} from chatId: {}",
                    messageText, message.getFrom().getId(), message.getChatId());

            switch (UserAnswer.getTypeByString(messageText, language, localisationService)) {
                case LANGUAGES:
                    messageToSend = onLanguagesChosen(message, language);
                    break;
                default:
                    messageToSend =  defaultStateHandler.getMessageToSend(message, language);
            }
        } else {
            messageToSend =  defaultStateHandler.getMessageToSend(message, language);
        }
        return messageToSend;
    }

    private SendMessage onLanguagesChosen(Message message, Language language) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.LANGUAGES);
        ReplyKeyboardMarkup replyKeyboardMarkup = getLanguagesKeyboard(language);

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        String replyText =
                String.format(localisationService.getString("chooseLanguage", language), language.getName());
        sendMessage.setText(replyText);
        return sendMessage;
    }

    private ReplyKeyboardMarkup getLanguagesKeyboard(Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (Language supportedLanguage : Language.values()) {
            KeyboardRow currentRow = new KeyboardRow();
            currentRow.add(supportedLanguage.getName());
            keyboardRows.add(currentRow);
        }

        KeyboardRow currentRow = new KeyboardRow();
        currentRow.add(localisationService.getString("goToMainMenu", language));
        keyboardRows.add(currentRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }
}
