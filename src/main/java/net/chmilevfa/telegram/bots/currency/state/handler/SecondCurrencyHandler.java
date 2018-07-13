package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import net.chmilevfa.telegram.bots.currency.service.CurrencyService;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.IOException;
import java.util.Objects;

/**
 * Implementation of {@link StateHandler} which deals with second chosen currency.
 * Shows actual currency and shows main menu.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
@Component
public class SecondCurrencyHandler implements StateHandler {

    private static Logger logger = LoggerFactory.getLogger(SecondCurrencyHandler.class);

    private final StateHandler defaultStateHandler;
    private final Dao dao;
    private final CurrencyService currencyService;

    @Autowired
    public SecondCurrencyHandler(StateHandler defaultStateHandler, JsonFileDao dao, CurrencyService currencyService) {
        this.defaultStateHandler = defaultStateHandler;
        this.dao = dao;
        this.currencyService = currencyService;
    }

    @Override
    public SendMessage getMessageToSend(Message message, Language language) {
        if (message.hasText() && Currencies.containsByName(message.getText().trim())) {
            return onCurrentRateChosen(message, language);
        }
        return defaultStateHandler.getMessageToSend(message, language);
    }

    private SendMessage onCurrentRateChosen(Message message, Language language) {
        Currencies firstCurrency = dao.getFirstUserCurrency(message.getChatId());
        if (Objects.isNull(firstCurrency)) {
            defaultStateHandler.getMessageToSend(message, language);
        }
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.MAIN_MENU);
        Currencies secondCurrency = Currencies.valueOf(message.getText().trim());
        float rate = 0;
        try {
            rate = currencyService.getRate(firstCurrency, secondCurrency);
        } catch (IOException e) {
            logger.error("Error during requesting currency rate", e);
        }
        String responseText = String.format(
                LocalisationService.getString("currencyRate", language),
                firstCurrency + "/" + secondCurrency, String.format("%.2f", rate)
        );

        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard(language);
        return MessageUtils
                .getSendMessageWithKeyboard(message, replyKeyboardMarkup, responseText);
    }
}
