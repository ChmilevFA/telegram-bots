package net.chmilevfa.telegram.bots.currency;

import net.chmilevfa.telegram.BotConfig;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.*;
import net.chmilevfa.telegram.bots.currency.state.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Implements currency bot behaviour.
 * Delegates handling particular behaviour of every possible bot
 * state {@link MessageState} to implementations of {@link StateHandler}.
 *
 * @see MessageState
 * @see StateHandler
 *
 * @author chmilevfa
 * @since 08.07.18
 */
@Service("currencyBot")
public class CurrencyBot extends TelegramLongPollingBot {

    private static Logger logger = LoggerFactory.getLogger(CurrencyBot.class);

    /** Handlers for all possible bot's states */
    private final StateHandler defaultStateHandler;
    private final StateHandler mainMenuStateHandler;
    private final StateHandler firstCurrencyHandler;
    private final StateHandler secondCurrencyHandler;
    private final StateHandler settingsStateHandler;
    private final StateHandler languagesStateHandler;
    private final StateHandler feedbackStateHandler;

    private final JsonFileDao dao;

    @Autowired
    public CurrencyBot(
            StateHandler defaultStateHandler,
            StateHandler mainMenuStateHandler,
            StateHandler firstCurrencyHandler,
            StateHandler secondCurrencyHandler,
            StateHandler settingsStateHandler,
            StateHandler languagesStateHandler,
            StateHandler feedbackStateHandler,
            JsonFileDao dao) {
        this.defaultStateHandler = defaultStateHandler;
        this.mainMenuStateHandler = mainMenuStateHandler;
        this.firstCurrencyHandler = firstCurrencyHandler;
        this.secondCurrencyHandler = secondCurrencyHandler;
        this.settingsStateHandler = settingsStateHandler;
        this.languagesStateHandler = languagesStateHandler;
        this.feedbackStateHandler = feedbackStateHandler;
        this.dao = dao;
    }

    @Override
    public String getBotUsername() {
        return BotConfig.CURRENCY_BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.CURRENCY_BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                handleIncomingMessage(update.getMessage()); // Call method to send the message
            } catch (TelegramApiException e) {
                logger.error("Error during handling telegram message", e);
            }
        }
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        MessageState messageState = dao.getState(message.getFrom().getId(), message.getChatId());
        Language language = dao.getLanguage(message.getFrom().getId());

        SendMessage sendMessageRequest;
        switch (messageState) {
            case MAIN_MENU:
                sendMessageRequest = mainMenuStateHandler.getMessageToSend(message, language);
                break;
            case CHOOSE_CURRENT_RATE_FIRST:
                sendMessageRequest = firstCurrencyHandler.getMessageToSend(message, language);
                break;
            case CHOOSE_CURRENT_RATE_SECOND:
                sendMessageRequest = secondCurrencyHandler.getMessageToSend(message, language);
                break;
            case FEEDBACK:
                sendMessageRequest = handleFeedback(message, language);
                break;
            case SETTINGS:
                sendMessageRequest = settingsStateHandler.getMessageToSend(message, language);
                break;
            case LANGUAGES:
                sendMessageRequest = languagesStateHandler.getMessageToSend(message, language);
                break;
            case DEFAULT:
            default:
                sendMessageRequest = defaultStateHandler.getMessageToSend(message, language);
        }
        execute(sendMessageRequest);
    }

    private SendMessage handleFeedback(Message message, Language language) throws TelegramApiException {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.MAIN_MENU);

        SendMessage sendMessageRequest;
        if (message.hasText()) {
            switch (UserAnswer.getTypeByString(message.getText(), language)) {
                case MAIN_MENU:
                    sendMessageRequest = defaultStateHandler.getMessageToSend(message, language);
                    break;
                default:
                    sendMessageRequest = feedbackStateHandler.getMessageToSend(message, language);
                    sendFeedbackToDeveloper(message, language);
            }
        } else {
            sendMessageRequest = feedbackStateHandler.getMessageToSend(message, language);
            sendFeedbackToDeveloper(message, language);
        }
        return sendMessageRequest;
    }

    private void sendFeedbackToDeveloper(Message message, Language language) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId((long) BotConfig.MASTER_ID);
        sendMessage.setText(
                String.format(
                        LocalisationService.getString("feedbackForDeveloper", language),
                        message.getFrom().getUserName()
                ) + System.lineSeparator() + message.getText());
        execute(sendMessage);
    }
}
