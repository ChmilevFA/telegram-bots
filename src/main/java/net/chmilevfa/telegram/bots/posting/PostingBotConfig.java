package net.chmilevfa.telegram.bots.posting;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@PropertySource("classpath:application.properties")
public class PostingBotConfig {

    private final String botName;
    private final String botToken;
    private final Integer masterUserId;

    public PostingBotConfig(Environment env) {
        this.botName = env.getProperty("bot.posting.name");
        this.botToken = Objects.requireNonNull(env.getProperty("bot.posting.token"));
        this.masterUserId = Integer.parseInt(Objects.requireNonNull(env.getProperty("master.user.id")));
    }

    String getBotName() {
        return botName;
    }

    String getBotToken() {
        return botToken;
    }

    Integer getMasterUserId() {
        return masterUserId;
    }
}
