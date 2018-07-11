package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static net.chmilevfa.telegram.bots.currency.service.StringService.CHOOSE_SECOND_CURRENCY;

/**
 * Implementation of {@link StateHandler} which deals with first chosen currency.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
@Component
public final class FirstCurrencyHandler extends AbstractCurrencyStateHandler implements StateHandler {

    private final StateHandler defaultStateHandler;
    private final Dao dao;

    @Autowired
    public FirstCurrencyHandler(StateHandler defaultStateHandler, JsonFileDao dao) {
        this.defaultStateHandler = defaultStateHandler;
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message) {
        if (message.hasText() && Currencies.containsByName(message.getText().trim())) {
            return onCurrentRateChosen(message);
        }
        return defaultStateHandler.getMessageToSend(message);
    }

    private SendMessage onCurrentRateChosen(Message message) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.CHOOSE_CURRENT_RATE_SECOND);
        dao.saveFirstUserCurrency(message.getChatId(), Currencies.valueOf(message.getText().trim()));
        ReplyKeyboardMarkup replyKeyboardMarkup = getCurrenciesKeyboard();
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup, CHOOSE_SECOND_CURRENCY);
    }
}
