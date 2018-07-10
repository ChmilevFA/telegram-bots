package net.chmilevfa.telegram.bots.currency;

import net.chmilevfa.telegram.BotConfig;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import net.chmilevfa.telegram.bots.currency.service.CurrencyService;
import net.chmilevfa.telegram.bots.currency.service.StringService;
import net.chmilevfa.telegram.bots.currency.states.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import static net.chmilevfa.telegram.bots.currency.service.StringService.GO_TO_MAIN_MENU;

public class CurrencyBot extends TelegramLongPollingBot {

    //TODO move to DI should be singleton
    private static final StateHandler DEFAULT_STATE_HANDLER = new DefaultStateHandler(JsonFileDao.getInstance());
    private static final StateHandler MAIN_MENU_STATE_HANDLER = new MainMenuStateHandler(JsonFileDao.getInstance());
    private static final StateHandler FIRST_CURRENCY_HANDLER = new FirstCurrencyHandler(JsonFileDao.getInstance());
    private static final StateHandler SECOND_CURRENCY_HANDLER =
            new SecondCurrencyHandler(JsonFileDao.getInstance(), new CurrencyService());
    private static final StateHandler SETTINGS_STATE_HANDLER = new SettingsStateHandler();
    private static final StateHandler FEEDBACK_STATE_HANDLER = new FeedbackStateHandler(JsonFileDao.getInstance());

    //TODO move to DI should be singleton
    private JsonFileDao dao = JsonFileDao.getInstance();

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
                //TODO logger
                e.printStackTrace();
            }
        }
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        MessageState messageState = getMessageState(message.getFrom().getId(), message.getChatId());

        SendMessage sendMessageRequest;
        switch (messageState) {
            case MAIN_MENU:
                sendMessageRequest = MAIN_MENU_STATE_HANDLER.getMessageToSend(message);
                break;
            case CHOOSE_CURRENT_RATE_FIRST:
                sendMessageRequest = FIRST_CURRENCY_HANDLER.getMessageToSend(message);
                break;
            case CHOOSE_CURRENT_RATE_SECOND:
                sendMessageRequest = SECOND_CURRENCY_HANDLER.getMessageToSend(message);
                break;
            case FEEDBACK:
                sendMessageRequest = handleFeedback(message);
                break;
            case SETTINGS:
            case DEFAULT:
            default:
                sendMessageRequest = DEFAULT_STATE_HANDLER.getMessageToSend(message);
        }
        execute(sendMessageRequest);
    }

    private SendMessage handleFeedback(Message message) throws TelegramApiException {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.MAIN_MENU);

        SendMessage sendMessageRequest;
        if (message.hasText()) {
            switch (message.getText()) {
                case GO_TO_MAIN_MENU:
                    sendMessageRequest = DEFAULT_STATE_HANDLER.getMessageToSend(message);
                    break;
                default:
                    sendMessageRequest = FEEDBACK_STATE_HANDLER.getMessageToSend(message);
                    sendFeedbackToDeveloper(message);
            }
        } else {
            sendMessageRequest = FEEDBACK_STATE_HANDLER.getMessageToSend(message);
            sendFeedbackToDeveloper(message);
        }
        return sendMessageRequest;
    }

    private void sendFeedbackToDeveloper(Message message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId((long) BotConfig.MASTER_ID);
        sendMessage.setText(
                String.format(
                        StringService.FEEDBACK_FOR_DEVELOPER,
                        message.getFrom().getUserName()
                ) + message.getText());
        execute(sendMessage);
    }

    private MessageState getMessageState(Integer userId, Long chatId) {
        return dao.getState(userId, chatId);
    }
}
