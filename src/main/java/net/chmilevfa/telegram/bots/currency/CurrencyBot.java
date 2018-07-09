package net.chmilevfa.telegram.bots.currency;

import net.chmilevfa.telegram.BotConfig;
import net.chmilevfa.telegram.bots.currency.states.DefaultStateHandler;
import net.chmilevfa.telegram.bots.currency.states.MessageState;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class CurrencyBot extends TelegramLongPollingBot {

    private static final DefaultStateHandler DEFAULT_STATE_HANDLER = new DefaultStateHandler();

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
            case CHOOSE_CURRENCY:
            default:
                sendMessageRequest = DEFAULT_STATE_HANDLER.getMessageDefault(message);
                //TODO
        }
        execute(sendMessageRequest);
    }

    private MessageState getMessageState(Integer UserId, Long chatId) {
        //TODO get actual stored state for user with UserId in chat with chatId
        return MessageState.MAIN_MENU;
    }

    @Override
    public String getBotUsername() {
        return BotConfig.CURRENCY_BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.CURRENCY_BOT_TOKEN;
    }
}
