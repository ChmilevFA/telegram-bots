package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import net.chmilevfa.telegram.bots.currency.state.UserAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.List;

/**
 * An implementation of {@link StateHandler} which deals with user's answers in the
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
    public MainMenuStateHandler(
            LocalisationService localisationService,
            StateHandler defaultStateHandler,
            Dao dao
    ) {
        super(localisationService);
        this.defaultStateHandler = defaultStateHandler;
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message, Language language) {
        SendMessage messageToSend;
        if (message.hasText()) {
            switch (UserAnswer.getTypeByString(message.getText(), language, localisationService)) {
                case CURRENT_RATE:
                    messageToSend = onCurrentRateChosen(message, language);
                    break;
                case SETTINGS:
                    messageToSend = onSettingsChosen(message, language);
                    break;
                case FEEDBACK:
                    messageToSend = onFeedbackChosen(message, language);
                    break;
                default:
                    messageToSend =  defaultStateHandler.getMessageToSend(message, language);
            }
        } else {
            messageToSend =  defaultStateHandler.getMessageToSend(message, language);
        }
        return messageToSend;
    }

    private SendMessage onCurrentRateChosen(Message message, Language language) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.CHOOSE_CURRENT_RATE_FIRST);
        ReplyKeyboardMarkup replyKeyboardMarkup = getCurrenciesKeyboard(language);
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup,
                localisationService.getString("chooseFirstCurrency", language));
    }

    private SendMessage onSettingsChosen(Message message, Language language) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.SETTINGS);
        ReplyKeyboardMarkup replyKeyboardMarkup = getSettingsKeyboard(language);

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        sendMessage.setText(localisationService.getString("chooseAnOption", language));
        return sendMessage;
    }

    private ReplyKeyboardMarkup getSettingsKeyboard(Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow firstKeyboardRow = new KeyboardRow();
        firstKeyboardRow.add(localisationService.getString("languages", language));

        KeyboardRow secondKeyboardRow = new KeyboardRow();
        secondKeyboardRow.add(localisationService.getString("goToMainMenu", language));

        replyKeyboardMarkup.setKeyboard(List.of(firstKeyboardRow, secondKeyboardRow));

        return replyKeyboardMarkup;
    }

    private SendMessage onFeedbackChosen(Message message, Language language) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.FEEDBACK);
        ReplyKeyboardMarkup replyKeyboardMarkup = getFeedbackKeyboard(language);
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup,
                localisationService.getString("writeFeedback", language));
    }

    private ReplyKeyboardMarkup getFeedbackKeyboard(Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(localisationService.getString("goToMainMenu", language));

        replyKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardRow));

        return replyKeyboardMarkup;
    }
}
