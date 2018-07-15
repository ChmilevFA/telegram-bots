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

    private LocalisationService localisationService;

    public FeedbackStateHandler(LocalisationService localisationService) {
        this.localisationService = localisationService;
    }

    @Override
    public SendMessage getMessageToSend(Message message, Language language) {
        return onFeedbackSent(message, language);
    }

    private SendMessage onFeedbackSent(Message message, Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard(language, localisationService);
        String answer =
                localisationService.getString("thanksFeedback", language) +
                System.lineSeparator() + System.lineSeparator() +
                localisationService.getString("helloMessage", language);
        return MessageUtils
                .getSendMessageWithKeyboard(message, replyKeyboardMarkup, answer);
    }
}
