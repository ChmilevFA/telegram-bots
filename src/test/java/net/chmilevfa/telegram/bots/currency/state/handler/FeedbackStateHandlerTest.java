package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Test for {@link FeedbackStateHandler}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
@RunWith(MockitoJUnitRunner.class)
public class FeedbackStateHandlerTest {

    @Mock
    private LocalisationService mockedLocalisationService;
    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    private Message mockedMessage;

    private FeedbackStateHandler underTestHandler;

    @Before
    public void setUp() {
        underTestHandler = new FeedbackStateHandler(mockedLocalisationService);
    }

    @Test
    public void validMessageToSend() {
        Long expectedChatId = 123123123L;
        Integer expectedMessageId = 142;
        String expectedHelloMessage = "helloMessage";
        String expectedThanksFeedback = "thanksFeedback";
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getMessageId()).thenReturn(expectedMessageId);
        when(mockedLocalisationService.getString(eq(expectedHelloMessage), any())).thenReturn(expectedHelloMessage);
        when(mockedLocalisationService.getString(eq(expectedThanksFeedback), any())).thenReturn(expectedThanksFeedback);

        SendMessage actualSendMessage = underTestHandler.getMessageToSend(mockedMessage, Language.ENGLISH);

        assertEquals(expectedChatId.toString(), actualSendMessage.getChatId());
        assertEquals(expectedMessageId, actualSendMessage.getReplyToMessageId());
        assertTrue(actualSendMessage.getText().contains(expectedHelloMessage));
        assertTrue(actualSendMessage.getText().contains(expectedThanksFeedback));
    }

    @Test
    public void getCorrectProcessedMessageState() {
        assertEquals(underTestHandler.getProcessedMessageState(), MessageState.FEEDBACK);
    }
}
