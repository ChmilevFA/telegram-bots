package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link LanguagesStateHandler}.
 *
 * @author chmilevfa
 * @since 16.07.18
 */
@RunWith(MockitoJUnitRunner.class)
public class LanguagesStateHandlerTest {

    @Captor
    private ArgumentCaptor<Integer> userIdCaptor;
    @Captor
    private ArgumentCaptor<Long> chatIdCaptor;
    @Captor
    private ArgumentCaptor<MessageState> messageStateCaptor;
    @Captor
    private ArgumentCaptor<Integer> languageUserIdCaptor;
    @Captor
    private ArgumentCaptor<Language> languageCaptor;

    @Mock
    private Dao mockedDao;
    @Mock
    private LocalisationService mockedLocalisationService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message mockedMessage;
    @Mock
    private StateHandler mockedStateHandler;

    private LanguagesStateHandler underTestHandler;

    @Before
    public void setUp() {
        underTestHandler = new LanguagesStateHandler(mockedLocalisationService, mockedStateHandler, mockedDao);
    }

    @Test
    public void validMessageToSend() {
        Integer expectedUserId = 42;
        Long expectedChatId = 123123123L;
        MessageState expectedMessageState  = MessageState.MAIN_MENU;
        Language expectedLanguage = Language.ENGLISH;
        String baseHelloMessage = "languageChosen";
        String argHelloMessage = baseHelloMessage + ":%s";
        String expectedHelloMessage = baseHelloMessage + ":" + expectedLanguage.getName();
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn(expectedLanguage.getName());
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedLocalisationService.getString(eq(baseHelloMessage), any())).thenReturn(argHelloMessage);

        SendMessage actualSendMessage = underTestHandler.getMessageToSend(mockedMessage, expectedLanguage);
        verify(mockedDao).saveMessageState(userIdCaptor.capture(), chatIdCaptor.capture(), messageStateCaptor.capture());
        verify(mockedDao).saveLanguage(languageUserIdCaptor.capture(), languageCaptor.capture());

        assertEquals(expectedUserId, userIdCaptor.getValue());
        assertEquals(expectedChatId, chatIdCaptor.getValue());
        assertEquals(expectedMessageState, messageStateCaptor.getValue());
        assertEquals(expectedUserId, languageUserIdCaptor.getValue());
        assertEquals(expectedLanguage, languageCaptor.getValue());
        assertEquals(expectedHelloMessage, actualSendMessage.getText());
    }

    @Test
    public void callDefaultStateHandlerWhenMessageDoesNotContainText() {
        when(mockedMessage.hasText()).thenReturn(false);
        Language actualLanguage = Language.ENGLISH;

        underTestHandler.getMessageToSend(mockedMessage, actualLanguage);

        verify(mockedStateHandler, times(1))
                .getMessageToSend(eq(mockedMessage), eq(actualLanguage));
        verify(mockedDao, times(0)).saveMessageState(any(), any(), any());
        verify(mockedDao, times(0)).saveLanguage(any(), any());
        verify(mockedLocalisationService, times(0)).getString(any(), any());
    }

    @Test
    public void callDefaultStateHandlerWhenMessageContainsIncorrectText() {
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn("IncorrectText");
        when(mockedMessage.getFrom().getId()).thenReturn(123);
        Language actualLanguage = Language.ENGLISH;

        underTestHandler.getMessageToSend(mockedMessage, actualLanguage);

        verify(mockedStateHandler, times(1))
                .getMessageToSend(eq(mockedMessage), eq(actualLanguage));
        verify(mockedDao, times(0)).saveMessageState(any(), any(), any());
        verify(mockedDao, times(0)).saveLanguage(any(), any());
        verify(mockedLocalisationService, times(0)).getString(any(), any());
    }

    @Test
    public void getCorrectProcessedMessageState() {
        assertEquals(underTestHandler.getProcessedMessageState(), MessageState.LANGUAGES);
    }
}
