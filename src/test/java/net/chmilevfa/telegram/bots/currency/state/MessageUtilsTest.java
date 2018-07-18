package net.chmilevfa.telegram.bots.currency.state;

import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Tests for static methods of {@link MessageUtils}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageUtilsTest {

    @Mock
    private Message mockedMessage;
    @Mock
    private LocalisationService mockedLocalisationService;

    @Test
    public void validSendMessageWithKeyboard() {
        Long expectedChatId = 123123123L;
        Integer expectedMessageId = 42;
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getMessageId()).thenReturn(expectedMessageId);
        ReplyKeyboardMarkup expectedKeyboard = new ReplyKeyboardMarkup();
        String expectedTextMessage = "Test text";

        SendMessage actualSendMessage =
                MessageUtils.getSendMessageWithKeyboard(mockedMessage, expectedKeyboard, expectedTextMessage);

        assertEquals(expectedChatId.toString(), actualSendMessage.getChatId());
        assertEquals(expectedMessageId, actualSendMessage.getReplyToMessageId());
        assertEquals(expectedKeyboard, actualSendMessage.getReplyMarkup());
        assertEquals(expectedTextMessage, actualSendMessage.getText());

    }

    @Test
    public void correctMainMenuKeyboard() {
        String expectedCurrentRate = "currentRate";
        String expectedSettings = "settings";
        String expectedFeedback = "feedback";
        when(mockedLocalisationService.getString(eq(expectedCurrentRate), any())).thenReturn(expectedCurrentRate);
        when(mockedLocalisationService.getString(eq(expectedSettings), any())).thenReturn(expectedSettings);
        when(mockedLocalisationService.getString(eq(expectedFeedback), any())).thenReturn(expectedFeedback);

        ReplyKeyboardMarkup actualKeyboard =
                MessageUtils.getMainMenuKeyboard(Language.ENGLISH, mockedLocalisationService);

        assertEquals(expectedCurrentRate, actualKeyboard.getKeyboard().get(0).get(0).getText());
        assertEquals(expectedSettings, actualKeyboard.getKeyboard().get(1).get(0).getText());
        assertEquals(expectedFeedback, actualKeyboard.getKeyboard().get(2).get(0).getText());
    }
}
