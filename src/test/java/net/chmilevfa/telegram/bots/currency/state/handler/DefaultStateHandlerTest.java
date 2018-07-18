package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link DefaultStateHandler}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultStateHandlerTest {

    @Captor
    private ArgumentCaptor<Integer> userIdCaptor;
    @Captor
    private ArgumentCaptor<Long> chatIdCaptor;
    @Captor
    private ArgumentCaptor<MessageState> messageStateCaptor;

    @Mock
    private Dao mockedDao;
    @Mock
    private LocalisationService mockedLocalisationService;
    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    private Message mockedMessage;

    private DefaultStateHandler underTestHandler;

    @Before
    public void setUp() {
         underTestHandler = new DefaultStateHandler(mockedLocalisationService, mockedDao);
    }

    @Test
    public void validMessageToSend() {
        Integer expectedUserId = 42;
        Long expectedChatId = 123123123L;
        MessageState expectedMessageState  = MessageState.MAIN_MENU;
        Integer expectedMessageId = 142;
        String expectedHelloMessage = "helloMessage";
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getMessageId()).thenReturn(expectedMessageId);
        when(mockedLocalisationService.getString(eq(expectedHelloMessage), any())).thenReturn(expectedHelloMessage);

        SendMessage actualSendMessage = underTestHandler.getMessageToSend(mockedMessage, Language.ENGLISH);
        verify(mockedDao).saveMessageState(userIdCaptor.capture(), chatIdCaptor.capture(), messageStateCaptor.capture());

        assertEquals(expectedUserId, userIdCaptor.getValue());
        assertEquals(expectedChatId, chatIdCaptor.getValue());
        assertEquals(expectedMessageState, messageStateCaptor.getValue());
        assertEquals(expectedChatId.toString(), actualSendMessage.getChatId());
        assertEquals(expectedHelloMessage, actualSendMessage.getText());
        assertEquals(expectedMessageId, actualSendMessage.getReplyToMessageId());
    }
}
