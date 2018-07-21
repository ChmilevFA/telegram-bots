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
 * Collection of methods that help building bot's answers.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
public final class MessageUtils {

    /**
     * Based on incoming message, already configured keyboard and text of bot's answer,
     * creates a message to be sent from bot.
     *
     * @param message incoming message
     * @param replyKeyboardMarkup configured keyboard
     * @param textMessage text of bot's answer
     * @return message to be sent from bot
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
     * Creates and returns telegram keyboard which describes bot's main menu.
     *
     * @return main menu keyboard
     */
    public static ReplyKeyboardMarkup getMainMenuKeyboard(Language language, LocalisationService localisationService) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(localisationService.getString("currentRate", language));
        keyboardRows.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(localisationService.getString("settings", language));
        keyboardRows.add(keyboardSecondRow);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(localisationService.getString("feedback", language));
        keyboardRows.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    private MessageUtils() {}
}
