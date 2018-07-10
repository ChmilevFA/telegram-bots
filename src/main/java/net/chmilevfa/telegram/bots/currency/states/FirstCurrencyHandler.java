package net.chmilevfa.telegram.bots.currency.states;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
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
public final class FirstCurrencyHandler extends AbstractCurrencyStateHandler implements StateHandler {

    //TODO move to DI should be singleton
    private static final StateHandler DEFAULT_STATE_HANDLER = new DefaultStateHandler(JsonFileDao.getInstance());

    //TODO move to DI should be singleton
    private final JsonFileDao dao;

    public FirstCurrencyHandler(JsonFileDao dao) {
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message) {
        if (message.hasText() && Currencies.containsByName(message.getText().trim())) {
            return onCurrentRateChosen(message);
        }
        return DEFAULT_STATE_HANDLER.getMessageToSend(message);
    }

    private SendMessage onCurrentRateChosen(Message message) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.CHOOSE_CURRENT_RATE_SECOND);
        dao.saveFirstUserCurrency(message.getChatId(), Currencies.valueOf(message.getText().trim()));
        ReplyKeyboardMarkup replyKeyboardMarkup = getCurrenciesKeyboard();
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup, CHOOSE_SECOND_CURRENCY);
    }
}
