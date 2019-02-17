package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.service.CurrencyService;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.IOException;
import java.util.Objects;

/**
 * An implementation of {@link StateHandler} which deals with second chosen currency.
 * Shows up-to-date currency rate and shows main menu.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
@Component
public final class SecondCurrencyHandler implements StateHandler {

    private static final MessageState PROCESSED_MESSAGE_STATE = MessageState.CHOOSE_CURRENT_RATE_SECOND;

    private static Logger logger = LoggerFactory.getLogger(SecondCurrencyHandler.class);

    private final LocalisationService localisationService;
    private final StateHandler defaultStateHandler;
    private final Dao dao;
    private final CurrencyService currencyService;

    public SecondCurrencyHandler(
            LocalisationService localisationService,
            StateHandler defaultStateHandler,
            Dao dao,
            CurrencyService currencyService) {
        this.localisationService = localisationService;
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

    @Override
    public MessageState getProcessedMessageState() {
        return PROCESSED_MESSAGE_STATE;
    }

    private SendMessage onCurrentRateChosen(Message message, Language language) {
        Currencies firstCurrency = dao.getFirstUserCurrency(message.getChatId());
        if (Objects.isNull(firstCurrency)) {
            defaultStateHandler.getMessageToSend(message, language);
        }
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.MAIN_MENU);
        Currencies secondCurrency = Currencies.valueOf(message.getText().trim());

        String responseText;
        try {
            float rate = currencyService.getRate(firstCurrency, secondCurrency);
            responseText = String.format(
                    localisationService.getString("currencyRate", language),
                    firstCurrency, secondCurrency, String.format("%.2f", rate)
            );
        } catch (IOException e) {
            logger.error("Error during requesting currency rate", e);
            responseText = localisationService.getString("currencyServiceError", language);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard(language, localisationService);
        return MessageUtils
                .getSendMessageWithKeyboard(message, replyKeyboardMarkup, responseText);
    }
}
