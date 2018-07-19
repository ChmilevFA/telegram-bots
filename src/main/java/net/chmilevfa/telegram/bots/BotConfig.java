package net.chmilevfa.telegram.bots;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@PropertySource("classpath:application.properties")
public class BotConfig {
    public static String CURRENCY_BOT_TOKEN = "bot_token";
    public static String CURRENCY_BOT_NAME = "bot_name";

    public static int MASTER_ID = 12345; //<- id of bot's owner. Used to send feedback from users.
}