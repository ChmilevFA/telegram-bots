package net.chmilevfa.telegram.bots.currency.state;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static net.chmilevfa.telegram.bots.currency.service.StringService.CURRENT_RATE;
import static net.chmilevfa.telegram.bots.currency.service.StringService.FEEDBACK;
import static net.chmilevfa.telegram.bots.currency.service.StringService.SETTINGS;

/**
 * Collection of helper method for building bot's answers.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
public final class MessageUtils {

    /**
     * TODO
     * @param message
     * @param replyKeyboardMarkup
     * @param textMessage
     * @return
     */
    public static SendMessage getSendMessageWithKeyboard(
            Message message,
            ReplyKeyboardMarkup replyKeyboardMarkup,
            String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        sendMessage.setText(textMessage);
        return sendMessage;
    }

    /**
     * TODO
     * @return
     */
    public static ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(CURRENT_RATE);
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(SETTINGS);
        keyboard.add(keyboardSecondRow);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(FEEDBACK);
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private MessageUtils() {}
}
