package net.chmilevfa.telegram.bots.currency.state;

import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

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
    public void testGetSendMessageWithKeyboard() {
        Long expectedChatId = 123123123L;
        Integer expectedMessageId = 42;
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getMessageId()).thenReturn(expectedMessageId);
        ReplyKeyboardMarkup expectedKeyboard = new ReplyKeyboardMarkup();
        String expectedTextMessage = "Test text";

        SendMessage actualSendMessage =
                MessageUtils.getSendMessageWithKeyboard(mockedMessage, expectedKeyboard, expectedTextMessage);

        Assert.assertEquals(expectedChatId.toString(), actualSendMessage.getChatId());
        Assert.assertEquals(expectedMessageId, actualSendMessage.getReplyToMessageId());
        Assert.assertEquals(expectedKeyboard, actualSendMessage.getReplyMarkup());
        Assert.assertEquals(expectedTextMessage, actualSendMessage.getText());

    }

    @Test
    public void testGetMainMenuKeyboard() {
        String expectedCurrentRate = "currentRate";
        String expectedSettings = "settings";
        String expectedFeedback = "feedback";
        when(mockedLocalisationService.getString(eq(expectedCurrentRate), any())).thenReturn(expectedCurrentRate);
        when(mockedLocalisationService.getString(eq(expectedSettings), any())).thenReturn(expectedSettings);
        when(mockedLocalisationService.getString(eq(expectedFeedback), any())).thenReturn(expectedFeedback);

        ReplyKeyboardMarkup actualKeyboard =
                MessageUtils.getMainMenuKeyboard(Language.ENGLISH, mockedLocalisationService);

        Assert.assertEquals(expectedCurrentRate, actualKeyboard.getKeyboard().get(0).get(0).getText());
        Assert.assertEquals(expectedSettings, actualKeyboard.getKeyboard().get(1).get(0).getText());
        Assert.assertEquals(expectedFeedback, actualKeyboard.getKeyboard().get(2).get(0).getText());
    }
}
