package net.chmilevfa.telegram.bots;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@PropertySource("classpath:application.properties")
public class BotConfig {

    private final String currencyBotName;
    private final String currencyBotToken;
    private final Integer masterUserId;

    public BotConfig(Environment env) {
        this.currencyBotName = env.getProperty("bot.name");
        this.currencyBotToken = env.getProperty("bot.token");
        this.masterUserId = Integer.parseInt(Objects.requireNonNull(env.getProperty("master.user.id")));
    }

    public String getCurrencyBotName() {
        return currencyBotName;
    }

    public String getCurrencyBotToken() {
        return currencyBotToken;
    }

    public int getMasterUserId() {
        return masterUserId;
    }
}