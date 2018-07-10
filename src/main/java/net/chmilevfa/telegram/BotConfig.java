package net.chmilevfa.telegram;

//TODO probably it should be better to store it in properties files
public class BotConfig {
    public static String CURRENCY_BOT_TOKEN = "bot_token";
    public static String CURRENCY_BOT_NAME = "bot_name";

    public static int MASTER_ID = 12345; //<- id of bot's owner. Used to send feedback from users.
}