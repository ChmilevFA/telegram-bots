package net.chmilevfa.telegram.bots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ApiContextInitializer.init();
        registerShutdownHooks();

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TelegramLongPollingBot currencyBot = (TelegramLongPollingBot) context.getBean("currencyBot");
        TelegramLongPollingBot schedulePostsBot = (TelegramLongPollingBot) context.getBean("schedulePostsBot");

        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            logger.info("Registering bots...");
            botsApi.registerBot(currencyBot);
            botsApi.registerBot(schedulePostsBot);
            logger.info("All bots registered");
        } catch (TelegramApiException e) {
            logger.error("Error during registering bots", e);
        }
    }

    private static void registerShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> logger.info("Shutting down application...")));
    }
}
