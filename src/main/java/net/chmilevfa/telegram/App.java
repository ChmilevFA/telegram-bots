package net.chmilevfa.telegram;

import net.chmilevfa.telegram.bots.currency.CurrencyBot;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class App {

    public static void main(String[] args) {
        ApiContextInitializer.init();

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        CurrencyBot currencyBot = (CurrencyBot) context.getBean("currencyBot");

        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(currencyBot);
        } catch (TelegramApiException e) {
            //TODO log and handle
            e.printStackTrace();
        }
    }
}
