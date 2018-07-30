package net.chmilevfa.telegram.bots.schedule.posting;

import net.chmilevfa.telegram.bots.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * TODO description
 *
 * @author chmilevfa
 * @since 30.07.18
 */
@Service("schedulePostsBot")
public class SchedulePostsBot extends TelegramLongPollingBot {

    private static Logger logger = LoggerFactory.getLogger(SchedulePostsBot.class);

    private final BotConfig config;

    public SchedulePostsBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return null;
    }
}
