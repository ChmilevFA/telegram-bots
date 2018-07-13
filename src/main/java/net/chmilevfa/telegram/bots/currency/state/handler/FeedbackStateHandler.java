package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Feedback implementation of {@link StateHandler}. Just says user "Thank you" for feedback.
 *
 * @author chmilevfa
 * @since 10.07.18
 */
@Component
public final class FeedbackStateHandler implements StateHandler {

    @Override
    public SendMessage getMessageToSend(Message message, Language language) {
        return onFeedbackSent(message, language);
    }

    private SendMessage onFeedbackSent(Message message, Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard(language);
        String thanksFeedback = LocalisationService.getString("thanksFeedback", language);
        String helloMessage = LocalisationService.getString("helloMessage", language);
        return MessageUtils
                .getSendMessageWithKeyboard(message, replyKeyboardMarkup, thanksFeedback + helloMessage);
    }
}
