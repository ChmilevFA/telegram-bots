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
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
public class MainMenuStateHandlerTest {

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
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message mockedMessage;
    @Mock
    private StateHandler mockedStateHandler;

    private MainMenuStateHandler underTestHandler;

    @Before
    public void setUp() {
        underTestHandler = new MainMenuStateHandler(mockedLocalisationService, mockedStateHandler, mockedDao);
    }

    @Test
    public void validMessageToSendForCurrentRate() {
        String currentRate = "currentRate";
        Integer expectedUserId = 42;
        Long expectedChatId = 123123123L;
        MessageState expectedMessageState  = MessageState.CHOOSE_CURRENT_RATE_FIRST;
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn(currentRate);
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedLocalisationService.getString(any(), any())).thenReturn("otherString");
        when(mockedLocalisationService.getString(eq(currentRate), any())).thenReturn(currentRate);

        underTestHandler.getMessageToSend(mockedMessage, Language.ENGLISH);
        verify(mockedDao).saveMessageState(userIdCaptor.capture(), chatIdCaptor.capture(), messageStateCaptor.capture());

        assertEquals(expectedUserId, userIdCaptor.getValue());
        assertEquals(expectedChatId, chatIdCaptor.getValue());
        assertEquals(expectedMessageState, messageStateCaptor.getValue());
    }

    @Test
    public void correctMessageToSendForSettings() {
        String settings = "settings";
        String expectedText = "chooseAnOption";
        String expectedLanguages = "languages";
        String expectedMainMenu = "goToMainMenu";
        Integer expectedUserId = 42;
        Long expectedChatId = 123123123L;
        Integer expectedMessageId = 11111;
        MessageState expectedMessageState  = MessageState.SETTINGS;
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn(settings);
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getMessageId()).thenReturn(expectedMessageId);
        when(mockedLocalisationService.getString(any(), any())).thenReturn("otherString");
        when(mockedLocalisationService.getString(eq(settings), any())).thenReturn(settings);
        when(mockedLocalisationService.getString(eq(expectedText), any())).thenReturn(expectedText);
        when(mockedLocalisationService.getString(eq(expectedLanguages), any())).thenReturn(expectedLanguages);
        when(mockedLocalisationService.getString(eq(expectedMainMenu), any())).thenReturn(expectedMainMenu);

        SendMessage actualMessage = underTestHandler.getMessageToSend(mockedMessage, Language.ENGLISH);
        verify(mockedDao).saveMessageState(userIdCaptor.capture(), chatIdCaptor.capture(), messageStateCaptor.capture());
        ReplyKeyboardMarkup keyboardMarkup = (ReplyKeyboardMarkup) actualMessage.getReplyMarkup();
        Set<String> buttonsText = keyboardMarkup.getKeyboard().stream()
                .flatMap(List::stream)
                .map(KeyboardButton::getText)
                .collect(Collectors.toSet());

        assertEquals(expectedUserId, userIdCaptor.getValue());
        assertEquals(expectedChatId, chatIdCaptor.getValue());
        assertEquals(expectedMessageState, messageStateCaptor.getValue());
        assertEquals(expectedText, actualMessage.getText());
        assertEquals(expectedChatId.toString(), actualMessage.getChatId());
        assertEquals(expectedMessageId, actualMessage.getReplyToMessageId());
        assertEquals(2, buttonsText.size());
        assertTrue(buttonsText.contains(expectedLanguages));
        assertTrue(buttonsText.contains(expectedMainMenu));
    }

    @Test
    public void correctMessageToSendForFeedback() {
        String feedback = "feedback";
        String expectedText = "writeFeedback";
        String expectedMainMenu = "goToMainMenu";
        Integer expectedUserId = 42;
        Long expectedChatId = 123123123L;
        Integer expectedMessageId = 11111;
        MessageState expectedMessageState  = MessageState.FEEDBACK;
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn(feedback);
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getMessageId()).thenReturn(expectedMessageId);
        when(mockedLocalisationService.getString(any(), any())).thenReturn("otherString");
        when(mockedLocalisationService.getString(eq(feedback), any())).thenReturn(feedback);
        when(mockedLocalisationService.getString(eq(expectedText), any())).thenReturn(expectedText);
        when(mockedLocalisationService.getString(eq(expectedMainMenu), any())).thenReturn(expectedMainMenu);

        SendMessage actualMessage = underTestHandler.getMessageToSend(mockedMessage, Language.ENGLISH);
        verify(mockedDao).saveMessageState(userIdCaptor.capture(), chatIdCaptor.capture(), messageStateCaptor.capture());
        ReplyKeyboardMarkup keyboardMarkup = (ReplyKeyboardMarkup) actualMessage.getReplyMarkup();
        Set<String> buttonsText = keyboardMarkup.getKeyboard().stream()
                .flatMap(List::stream)
                .map(KeyboardButton::getText)
                .collect(Collectors.toSet());

        assertEquals(expectedUserId, userIdCaptor.getValue());
        assertEquals(expectedChatId, chatIdCaptor.getValue());
        assertEquals(expectedMessageState, messageStateCaptor.getValue());
        assertEquals(expectedText, actualMessage.getText());
        assertEquals(expectedChatId.toString(), actualMessage.getChatId());
        assertEquals(expectedMessageId, actualMessage.getReplyToMessageId());
        assertEquals(1, buttonsText.size());
        assertTrue(buttonsText.contains(expectedMainMenu));
    }

    @Test
    public void callDefaultStateHandlerWhenUnknownUserAnswer() {
        Language actualLanguage = Language.ENGLISH;
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn("AnyUnknownText");
        when(mockedLocalisationService.getString(any(), any())).thenReturn("anotherDifferentString");

        underTestHandler.getMessageToSend(mockedMessage, actualLanguage);

        verify(mockedStateHandler, times(1))
                .getMessageToSend(eq(mockedMessage), eq(actualLanguage));
        verify(mockedDao, times(0)).saveMessageState(any(), any(), any());
    }

    @Test
    public void callDefaultStateHandlerWhenNoText() {
        when(mockedMessage.hasText()).thenReturn(false);
        Language actualLanguage = Language.ENGLISH;

        underTestHandler.getMessageToSend(mockedMessage, actualLanguage);

        verify(mockedStateHandler, times(1))
                .getMessageToSend(eq(mockedMessage), eq(actualLanguage));
        verify(mockedDao, times(0)).saveMessageState(any(), any(), any());
    }
}
