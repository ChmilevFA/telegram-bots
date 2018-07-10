package net.chmilevfa.telegram.bots.currency.states;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.service.StringService;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Common for Currency implementations of {@link StateHandler}.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
abstract class AbstractCurrencyStateHandler {

    private final static int NUMBER_ELEMENTS_IN_ROW = 2;

    ReplyKeyboardMarkup getCurrenciesKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        Currencies[] currencies = Currencies.values();
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

        keyboardRow.add(StringService.GO_TO_MAIN_MENU);
        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }
}