package net.chmilevfa.telegram.bots.currency;

import net.chmilevfa.telegram.bots.BotConfig;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.*;
import net.chmilevfa.telegram.bots.currency.state.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

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

    private final BotConfig config;

    private final LocalisationService localisationService;

    /** Handlers for all possible bot's states */
    private final List<StateHandler> stateHandlers;

    private final Dao dao;

    public CurrencyBot(
            BotConfig config,
            LocalisationService localisationService,
            List<StateHandler> stateHandlers,
            JsonFileDao dao) {
        this.config = config;
        this.localisationService = localisationService;
        this.stateHandlers = stateHandlers;
        this.dao = dao;
    }

    @Override
    public String getBotUsername() {
        return config.getCurrencyBotName();
    }

    @Override
    public String getBotToken() {
        return config.getCurrencyBotToken();
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

        if (messageState.equals(MessageState.FEEDBACK)) {
            execute(handleFeedback(message, language));
            return;
        }

        SendMessage sendMessageRequest = stateHandlers.stream()
                .filter(handler -> handler.getProcessedMessageState().equals(messageState))
                .findFirst()
                .orElse(getDefaultHandler())
                .getMessageToSend(message, language);
        execute(sendMessageRequest);
    }

    private StateHandler getDefaultHandler() {
        return stateHandlers.stream()
                .filter(handler -> handler.getProcessedMessageState().equals(MessageState.DEFAULT))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to find handler for DEFAULT state"));
    }

    private StateHandler getFeedbackHandler() {
        return stateHandlers.stream()
                .filter(handler -> handler.getProcessedMessageState().equals(MessageState.FEEDBACK))
                .findFirst()
                .orElse(getDefaultHandler());
    }

    private SendMessage handleFeedback(Message message, Language language) throws TelegramApiException {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.MAIN_MENU);

        SendMessage sendMessageRequest;
        if (message.hasText()) {
            switch (UserAnswer.getTypeByString(message.getText(), language, localisationService)) {
                case MAIN_MENU:
                    sendMessageRequest = getDefaultHandler().getMessageToSend(message, language);
                    break;
                default:
                    sendMessageRequest = getFeedbackHandler().getMessageToSend(message, language);
                    sendFeedbackToDeveloper(message, language);
            }
        } else {
            sendMessageRequest = getFeedbackHandler().getMessageToSend(message, language);
            sendFeedbackToDeveloper(message, language);
        }
        return sendMessageRequest;
    }

    private void sendFeedbackToDeveloper(Message message, Language language) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId((long) config.getMasterUserId());
        sendMessage.setText(String.format(
                localisationService.getString("feedbackForDeveloper", language),
                message.getFrom().getUserName(), message.getText()));
        execute(sendMessage);
    }
}
