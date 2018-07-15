package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.Currencies;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link FirstCurrencyHandler}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
@RunWith(MockitoJUnitRunner.class)
public class FirstCurrencyHandlerTest {

    @Captor
    private ArgumentCaptor<Integer> messageStateUserIdCaptor;
    @Captor
    private ArgumentCaptor<Long> messageStateChatIdCaptor;
    @Captor
    private ArgumentCaptor<MessageState> messageStateCaptor;
    @Captor
    private ArgumentCaptor<Long> CurrencyChatIdCaptor;
    @Captor
    private ArgumentCaptor<Currencies> currencyCaptor;

    @Mock
    private Dao mockedDao;
    @Mock
    private LocalisationService mockedLocalisationService;
    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    private Message mockedMessage;
    @Mock
    private StateHandler mockedStateHandler;

    private FirstCurrencyHandler underTestHandler;

    @Before
    public void setUp() {
        underTestHandler = new FirstCurrencyHandler(mockedLocalisationService, mockedStateHandler, mockedDao);
    }

    @Test
    public void testGetMessageToSend() {
        Integer expectedUserId = 42;
        Long expectedChatId = 123123123L;
        MessageState expectedMessageState  = MessageState.CHOOSE_CURRENT_RATE_SECOND;
        Integer expectedMessageId = 142;
        String expectedGoToMainMenu = "goToMainMenu";
        String expectedChooseCurrency = "chooseSecondCurrency";
        Currencies expectedCurrency = Currencies.EUR;
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getMessageId()).thenReturn(expectedMessageId);
        when(mockedMessage.getText()).thenReturn(expectedCurrency.name());
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedLocalisationService.getString(eq(expectedGoToMainMenu), any())).thenReturn(expectedGoToMainMenu);
        when(mockedLocalisationService.getString(eq(expectedChooseCurrency), any())).thenReturn(expectedChooseCurrency);

        SendMessage actualSendMessage = underTestHandler.getMessageToSend(mockedMessage, Language.ENGLISH);
        verify(mockedDao).saveMessageState(
                messageStateUserIdCaptor.capture(), messageStateChatIdCaptor.capture(), messageStateCaptor.capture());
        verify(mockedDao).saveFirstUserCurrency(CurrencyChatIdCaptor.capture(), currencyCaptor.capture());
        ReplyKeyboardMarkup keyboardMarkup = (ReplyKeyboardMarkup) actualSendMessage.getReplyMarkup();
        Set<String> buttonsText = keyboardMarkup.getKeyboard().stream()
                .flatMap(List::stream)
                .map(KeyboardButton::getText)
                .collect(Collectors.toSet());

        assertEquals(expectedUserId, messageStateUserIdCaptor.getValue());
        assertEquals(expectedChatId, messageStateChatIdCaptor.getValue());
        assertEquals(expectedMessageState, messageStateCaptor.getValue());
        assertEquals(expectedChatId, CurrencyChatIdCaptor.getValue());
        assertEquals(expectedCurrency, currencyCaptor.getValue());
        assertTrue(buttonsText.contains(expectedGoToMainMenu));
        for (Currencies currency : Currencies.values()) {
            assertTrue(buttonsText.contains(currency.name()));
        }
    }
}
