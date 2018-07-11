package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import net.chmilevfa.telegram.bots.currency.service.CurrencyService;
import net.chmilevfa.telegram.bots.currency.service.StringService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
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
    public SendMessage getMessageToSend(Message message) {
        if (message.hasText() && Currencies.containsByName(message.getText().trim())) {
            return onCurrentRateChosen(message);
        }
        return defaultStateHandler.getMessageToSend(message);
    }

    private SendMessage onCurrentRateChosen(Message message) {
        Currencies firstCurrency = dao.getFirstUserCurrency(message.getChatId());
        if (Objects.isNull(firstCurrency)) {
            defaultStateHandler.getMessageToSend(message);
        }
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.MAIN_MENU);
        Currencies secondCurrency = Currencies.valueOf(message.getText().trim());
        float rate = 0;
        try {
            rate = currencyService.getRate(firstCurrency, secondCurrency);
        } catch (IOException e) {
            //TODO log and handle
            e.printStackTrace();
        }
        String responseText = String.format(
                StringService.CURRENCY_RATE, firstCurrency + "/" + secondCurrency, String.format("%.2f", rate));

        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard();
        return MessageUtils
                .getSendMessageWithKeyboard(message, replyKeyboardMarkup, responseText);
    }
}
