package net.chmilevfa.telegram.bots.currency.states;

import net.chmilevfa.telegram.bots.currency.dao.file.JsonFileDao;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static net.chmilevfa.telegram.bots.currency.service.StringService.HELLO_MESSAGE;
import static net.chmilevfa.telegram.bots.currency.service.StringService.THANKS_FEEDBACK;

/**
 * Feedback implementation of {@link StateHandler}. Just says user "Thank you" for feedback.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
public final class FeedbackStateHandler implements StateHandler {

    //TODO move to DI should be singleton
    private final JsonFileDao dao;

    public FeedbackStateHandler(JsonFileDao dao) {
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message) {
        return onFeedbackSent(message);
    }

    private SendMessage onFeedbackSent(Message message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard();
        return MessageUtils
                .getSendMessageWithKeyboard(message, replyKeyboardMarkup, THANKS_FEEDBACK + HELLO_MESSAGE);
    }
}
