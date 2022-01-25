package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.dao.Dao;
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

/**
 * An implementation of {@link StateHandler} which deals with user's answers in the
 * language menu. Switches user language settings if correct {@link Language} was chosen.
 *
 * @author chmilevfa
 * @since 14.07.18
 */
@Component
public final class LanguagesStateHandler implements StateHandler {

    private static final MessageState PROCESSED_MESSAGE_STATE = MessageState.LANGUAGES;

    private static final Logger logger = LoggerFactory.getLogger(LanguagesStateHandler.class);

    private final LocalisationService localisationService;
    private final StateHandler defaultStateHandler;
    private final Dao dao;

    public LanguagesStateHandler(
            LocalisationService localisationService,
            StateHandler defaultStateHandler,
            Dao dao) {
        this.localisationService = localisationService;
        this.defaultStateHandler = defaultStateHandler;
        this.dao = dao;
    }

    @Override
    public SendMessage getMessageToSend(Message message, Language language) {
        if (message.hasText()) {
            String messageText = message.getText().trim();

            logger.trace("Received message: \"{}\" from userId: {} from chatId: {}",
                    messageText, message.getFrom().getId(), message.getChatId());

            if (Language.isLanguageNameSupported(messageText)) {
                return onLanguageChosen(message, Language.getLanguageByName(messageText));
            }
        }
        return defaultStateHandler.getMessageToSend(message, language);
    }

    @Override
    public MessageState getProcessedMessageState() {
        return PROCESSED_MESSAGE_STATE;
    }

    private SendMessage onLanguageChosen(Message message, Language language) {
        dao.saveMessageState(message.getFrom().getId(), message.getChatId(), MessageState.MAIN_MENU);
        dao.saveLanguage(message.getFrom().getId(), language);

        String responseText = String.format(
                localisationService.getString("languageChosen", language),
                language.getName()
        );

        ReplyKeyboardMarkup replyKeyboardMarkup = MessageUtils.getMainMenuKeyboard(language, localisationService);
        return MessageUtils
                .getSendMessageWithKeyboard(message, replyKeyboardMarkup, responseText);
    }
}
