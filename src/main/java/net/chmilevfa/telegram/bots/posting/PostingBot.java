package net.chmilevfa.telegram.bots.posting;

import net.chmilevfa.telegram.bots.posting.dao.PostingMessageStateDao;
import net.chmilevfa.telegram.bots.posting.state.PostingMessageState;
import net.chmilevfa.telegram.bots.posting.state.PostingStateHandler;
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
 * TODO description
 *
 * @author chmilevfa
 * @since 30.07.18
 */
@Service("postingBot")
public class PostingBot extends TelegramLongPollingBot {

    private static Logger logger = LoggerFactory.getLogger(PostingBot.class);

    private final PostingBotConfig config;

    /** Handlers for all possible bot's states */
    private final List<PostingStateHandler> stateHandlers;

    private final PostingMessageStateDao messageStateDao;

    public PostingBot(
            PostingBotConfig config,
            List<PostingStateHandler> stateHandlers,
            PostingMessageStateDao messageStateDao) {
        this.config = config;
        this.stateHandlers = stateHandlers;
        this.messageStateDao = messageStateDao;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                handleIncomingMessage(update.getMessage());
            } catch (TelegramApiException e) {
                logger.error("Error during handling telegram message from posting bot", e);
            }
        }
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        PostingMessageState messageState = messageStateDao.getState(message.getFrom().getId(), message.getChatId());

        if (message.isUserMessage() || message.getFrom().getId().equals(config.getMasterUserId())) {
            SendMessage sendMessageRequest = stateHandlers.stream()
                    .filter(handler -> handler.getProcessedMessageState().equals(messageState))
                    .findFirst()
                    .orElse(getDefaultHandler())
                    .getMessageToSend(message);
            execute(sendMessageRequest);
        }
    }

    private PostingStateHandler getDefaultHandler() {
        return stateHandlers.stream()
                .filter(handler -> handler.getProcessedMessageState().equals(PostingMessageState.DEFAULT))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to find handler for DEFAULT state"));
    }
}
