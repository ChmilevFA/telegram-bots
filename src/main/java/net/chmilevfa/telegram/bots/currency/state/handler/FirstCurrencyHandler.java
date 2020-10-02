package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Arrays;

/**
 * An implementation of {@link StateHandler} which deals with first chosen currency.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
@Component
public final class FirstCurrencyHandler extends AbstractCurrencyStateHandler implements StateHandler {

    private static final MessageState PROCESSED_MESSAGE_STATE = MessageState.CHOOSE_CURRENT_RATE_FIRST;

    private final StateHandler defaultStateHandler;
    private final Dao dao;

    public FirstCurrencyHandler(
            LocalisationService localisationService,
            StateHandler defaultStateHandler,
            Dao dao
    ) {
        super(localisationService);
        this.defaultStateHandler = defaultStateHandler;
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message, Language language) {
        if (message.hasText() && Currencies.containsByName(message.getText().trim())) {
            return onCurrentRateChosen(message, language);
        }
        return defaultStateHandler.getMessageToSend(message, language);
    }

    @Override
    public MessageState getProcessedMessageState() {
        return PROCESSED_MESSAGE_STATE;
    }

    private SendMessage onCurrentRateChosen(Message message, Language language) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.CHOOSE_CURRENT_RATE_SECOND);
        Currencies chosenFirstCurrency = Currencies.valueOf(message.getText().trim());
        dao.saveFirstUserCurrency(message.getChatId(), chosenFirstCurrency);
        Currencies[] currenciesWithoutFirstChosen = Arrays.stream(Currencies.values())
                .filter(currency -> !chosenFirstCurrency.equals(currency))
                .toArray(Currencies[]::new);
        ReplyKeyboardMarkup replyKeyboardMarkup = getCurrenciesKeyboard(language, currenciesWithoutFirstChosen);
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup,
                localisationService.getString("chooseSecondCurrency", language));
    }
}
