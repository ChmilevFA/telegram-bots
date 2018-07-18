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
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test for {@link SettingsStateHandler}.
 *
 * @author chmilevfa
 * @since 16.07.18
 */
@RunWith(MockitoJUnitRunner.class)
public class SettingsStateHandlerTest {

    @Captor
    private ArgumentCaptor<Integer> userIdCaptor;
    @Captor
    private ArgumentCaptor<Long> chatIdCaptor;
    @Captor
    private ArgumentCaptor<MessageState> messageStateCaptor;

    @Mock
    private LocalisationService mockedLocalisationService;
    @Mock
    private StateHandler mockedDefaultStateHandler;
    @Mock
    private Dao mockedDao;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message mockedMessage;

    private SettingsStateHandler underTestHandler;

    @Before
    public void setUp() {
        underTestHandler = new SettingsStateHandler(mockedLocalisationService, mockedDefaultStateHandler, mockedDao);
    }

    @Test
    public void validMessageToSend() {
        String userAnswer = "languages";
        Language expectedLanguage = Language.RUSSIAN;
        Integer expectedUserId = 111111;
        Long expectedChatId = 22222L;
        Integer expectedMessageId = 33333;
        MessageState expectedState = MessageState.LANGUAGES;
        String expectedMainMenu = "goToMainMenu";
        String baseChooseLanguage = "chooseLanguage";
        String argChooseLanguage = baseChooseLanguage + " %s";
        String expectedText = baseChooseLanguage + " " + expectedLanguage.getName();
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn(userAnswer);
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getMessageId()).thenReturn(expectedMessageId);
        when(mockedLocalisationService.getString(any(), eq(expectedLanguage))).thenReturn("asdqwef");
        when(mockedLocalisationService.getString(userAnswer, expectedLanguage)).thenReturn(userAnswer);
        when(mockedLocalisationService.getString(baseChooseLanguage, expectedLanguage)).thenReturn(argChooseLanguage);
        when(mockedLocalisationService.getString(expectedMainMenu, expectedLanguage)).thenReturn(expectedMainMenu);

        SendMessage actualSendMessage = underTestHandler.getMessageToSend(mockedMessage, expectedLanguage);
        verify(mockedDao).saveMessageState(userIdCaptor.capture(), chatIdCaptor.capture(), messageStateCaptor.capture());
        Set<String> buttonsText = ((ReplyKeyboardMarkup) actualSendMessage.getReplyMarkup()).getKeyboard().stream()
                .flatMap(List::stream)
                .map(KeyboardButton::getText)
                .collect(Collectors.toSet());

        assertEquals(expectedUserId, userIdCaptor.getValue());
        assertEquals(expectedChatId, chatIdCaptor.getValue());
        assertEquals(expectedState, messageStateCaptor.getValue());
        assertEquals(expectedText, actualSendMessage.getText());
        assertEquals(expectedChatId.toString(), actualSendMessage.getChatId());
        assertEquals(expectedMessageId, actualSendMessage.getReplyToMessageId());
        assertEquals(Language.values().length + 1, buttonsText.size());
        assertTrue(buttonsText.contains(expectedMainMenu));
        for (Language currentLanguage : Language.values()) {
            assertTrue(buttonsText.contains(currentLanguage.getName()));
        }
    }

    @Test
    public void callDefaultStateHandlerWhenUnrecognizedUserAnswer() {
        String wrongText = "qweqwe";
        Language expectedLanguage = Language.RUSSIAN;
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn(wrongText);
        when(mockedMessage.getFrom()).thenReturn(mock(User.class));
        when(mockedLocalisationService.getString(anyString(), any())).thenReturn(wrongText);

        underTestHandler.getMessageToSend(mockedMessage, expectedLanguage);

        verify(mockedDefaultStateHandler, times(1))
                .getMessageToSend(eq(mockedMessage), eq(expectedLanguage));
        verify(mockedDao, times(0)).saveMessageState(any(), any(), any());
    }

    @Test
    public void callDefaultStateHandlerWhenNoText() {
        when(mockedMessage.hasText()).thenReturn(false);
        Language expectedLanguage = Language.RUSSIAN;

        underTestHandler.getMessageToSend(mockedMessage, expectedLanguage);

        verify(mockedDefaultStateHandler, times(1))
                .getMessageToSend(eq(mockedMessage), eq(expectedLanguage));
        verify(mockedDao, times(0)).saveMessageState(any(), any(), any());
    }
}
