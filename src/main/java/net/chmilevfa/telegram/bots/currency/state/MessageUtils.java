package net.chmilevfa.telegram.bots.currency.state;

import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

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
    public static ReplyKeyboardMarkup getMainMenuKeyboard(Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(LocalisationService.getString("currentRate", language));
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(LocalisationService.getString("settings", language));
        keyboard.add(keyboardSecondRow);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(LocalisationService.getString("feedback", language));
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private MessageUtils() {}
}
