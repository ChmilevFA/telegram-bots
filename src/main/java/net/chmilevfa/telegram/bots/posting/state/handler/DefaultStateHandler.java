package net.chmilevfa.telegram.bots.posting.state.handler;

import net.chmilevfa.telegram.bots.currency.state.MessageUtils;
import net.chmilevfa.telegram.bots.posting.dao.PostingMessageStateDao;
import net.chmilevfa.telegram.bots.posting.state.PostingMessageState;
import net.chmilevfa.telegram.bots.posting.state.PostingStateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link PostingStateHandler} which shows main menu keyboard.
 *
 * @author chmilevfa
 * @since 09.08.18
 */
@Component
public final class DefaultStateHandler implements PostingStateHandler {

    private static final PostingMessageState PROCESSED_MESSAGE_STATE = PostingMessageState.DEFAULT;

    private final PostingMessageStateDao messageStateDao;

    public DefaultStateHandler(PostingMessageStateDao messageStateDao) {
        this.messageStateDao = messageStateDao;
    }

    @Override
    public SendMessage getMessageToSend(Message message) {
        messageStateDao.saveMessageState(message.getFrom().getId(), message.getChatId(), PostingMessageState.MAIN_MENU);
        return getDefaultSendMessage(message, getMainMenuKeyboard());
    }

    @Override
    public PostingMessageState getProcessedMessageState() {
        return PROCESSED_MESSAGE_STATE;
    }

    private SendMessage getDefaultSendMessage(Message message,
                                              ReplyKeyboardMarkup replyKeyboardMarkup) {
        return MessageUtils.getSendMessageWithKeyboard(message, replyKeyboardMarkup,
                "I'm posting bot, choose what you want");
    }

    /**
     * Creates and returns telegram keyboard which describes bot's main menu.
     *
     * @return main menu keyboard
     */
    private static ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Add new post and set up time for posting"); //TODO move to localization service
        keyboardRows.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }


}
