package net.chmilevfa.telegram.bots.posting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

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

    public PostingBot(PostingBotConfig config) {
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleIncomingMessage(update.getMessage());
        }
    }

    private void handleIncomingMessage(Message message) {
        if (message.isUserMessage() || message.getFrom().getId().equals(config.getMasterUserId())) {

        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }
}
