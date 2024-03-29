package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Common class for Currency implementations of {@link StateHandler}.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
abstract class AbstractCurrencyStateHandler {

    private final static int NUMBER_ELEMENTS_IN_ROW = 2;

    LocalisationService localisationService;

    public AbstractCurrencyStateHandler(LocalisationService localisationService) {
        this.localisationService = localisationService;
    }

    ReplyKeyboardMarkup getCurrenciesKeyboard(Language language, Currencies[] currencies) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        for (int i = 0; i < currencies.length; i++) {
            if (i > 0 && i % NUMBER_ELEMENTS_IN_ROW == 0) {
                keyboardRows.add(keyboardRow);
                keyboardRow = new KeyboardRow();
            }
            keyboardRow.add(currencies[i].name());
        }

        if (currencies.length % NUMBER_ELEMENTS_IN_ROW == 0) {
            keyboardRows.add(keyboardRow);
            keyboardRow = new KeyboardRow();
        }

        keyboardRow.add(localisationService.getString("goToMainMenu", language));
        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }
}
