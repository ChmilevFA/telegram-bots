package net.chmilevfa.telegram.bots.currency.states;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.CurrencyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for default state (probably it should be main menu).
 * TODO now it represents state for choosing particular pair of currencies and should be moved to another StateHandler
 *
 * @author chmilevfa
 * @since 07.07.18
 */
public class DefaultStateHandler {

    private final CurrencyService currencyService = new CurrencyService();

    public SendMessage getMessageDefault(Message message) {
        String messageText = message.getText().trim();
        String responseText = "";
        if (isValidCurrencyCommand(messageText)) {
            String[] currencies = messageText.split("/");
            try {
                float rate = currencyService.getRate(Currencies.valueOf(currencies[0].toUpperCase()), Currencies.valueOf(currencies[1].toUpperCase()));
                responseText = messageText + ": " + String.format("%.2f", rate);
            } catch (IOException e) {
                //TODO log and handle
                e.printStackTrace();
            }
        } else {
            responseText = "Unrecognized command \"" + messageText + "\"";
        }


        ReplyKeyboardMarkup replyKeyboardMarkup = getDefaultKeyboard();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        sendMessage.setText(responseText + "\nChoose currency pair");
        return sendMessage;
    }

    private boolean isValidCurrencyCommand(String messageText) {
        String[] currencies = messageText.split("/");

        if (currencies.length != 2) {
            return false;
        }

        return Currencies.containsByName(currencies[0]) || Currencies.containsByName(currencies[1]);

    }

    //TODO get rid of hardcoded values
    private ReplyKeyboardMarkup getDefaultKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("USD/EUR");
        keyboardFirstRow.add("USD/PLN");
        keyboardFirstRow.add("USD/RUB");
        keyboardFirstRow.add("USD/CZK");
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("EUR/USD");
        keyboardSecondRow.add("EUR/PLN");
        keyboardSecondRow.add("EUR/RUB");
        keyboardSecondRow.add("EUR/CZK");
        keyboard.add(keyboardSecondRow);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("PLN/USD");
        keyboardThirdRow.add("PLN/EUR");
        keyboardThirdRow.add("PLN/RUB");
        keyboardThirdRow.add("PLN/CZK");
        keyboard.add(keyboardThirdRow);

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add("RUB/USD");
        keyboardFourthRow.add("RUB/EUR");
        keyboardFourthRow.add("RUB/PLN");
        keyboardFourthRow.add("RUB/CZK");
        keyboard.add(keyboardFourthRow);

        KeyboardRow keyboardFifthRow = new KeyboardRow();
        keyboardFifthRow.add("CZK/USD");
        keyboardFifthRow.add("CZK/EUR");
        keyboardFifthRow.add("CZK/PLN");
        keyboardFifthRow.add("CZK/RUB");
        keyboard.add(keyboardFifthRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }


}
