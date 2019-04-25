package net.chmilevfa.telegram.bots.currency.state.handler;

import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.service.CurrencyService;
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

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link SecondCurrencyHandler}.
 *
 * @author chmilevfa
 * @since 16.07.18
 */
@RunWith(MockitoJUnitRunner.class)
public class SecondCurrencyHandlerTest {

    @Captor
    private ArgumentCaptor<Integer> userIdCaptor;
    @Captor
    private ArgumentCaptor<Long> chatIdCaptor;
    @Captor
    private ArgumentCaptor<MessageState> messageStateCaptor;
    @Captor
    private ArgumentCaptor<Currencies> firstCurrencyCaptor;
    @Captor
    private ArgumentCaptor<Currencies> secondCurrencyCaptor;

    @Mock
    private LocalisationService mockedLocalisationService;
    @Mock
    private StateHandler mockedDefaultStateHandler;
    @Mock
    private Dao mockedDao;
    @Mock
    private CurrencyService mockedCurrencyService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message mockedMessage;

    private SecondCurrencyHandler underTestHandler;

    @Before
    public void setUp() {
        underTestHandler = new SecondCurrencyHandler(
                mockedLocalisationService, mockedDefaultStateHandler, mockedDao, mockedCurrencyService);
    }

    @Test
    public void validMessageToSend() throws IOException {
        Currencies expectedFirstCurrency = Currencies.USD;
        Currencies expectedSecondCurrency = Currencies.EUR;
        Integer expectedUserId = 11111;
        Long expectedChatId = 333333L;
        float expectedRate = 0.11f;
        MessageState expectedState = MessageState.MAIN_MENU;
        String baseCurrencyRate = "currencyRate";
        String argCurrencyRate = baseCurrencyRate + " %s %s %s";
        String expectedText = baseCurrencyRate + " " + expectedFirstCurrency +
                " " + expectedSecondCurrency + " " + expectedRate;
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn(expectedSecondCurrency.name());
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedDao.getFirstUserCurrency(eq(expectedChatId))).thenReturn(expectedFirstCurrency);
        when(mockedCurrencyService.getRate(any(), any())).thenReturn(expectedRate);
        when(mockedLocalisationService.getString(eq(baseCurrencyRate), any())).thenReturn(argCurrencyRate);

        SendMessage actualSendMessage = underTestHandler.getMessageToSend(mockedMessage, Language.ENGLISH);
        verify(mockedDao).saveMessageState(userIdCaptor.capture(), chatIdCaptor.capture(), messageStateCaptor.capture());
        verify(mockedCurrencyService).getRate(firstCurrencyCaptor.capture(), secondCurrencyCaptor.capture());

        assertEquals(expectedUserId, userIdCaptor.getValue());
        assertEquals(expectedChatId, chatIdCaptor.getValue());
        assertEquals(expectedState, messageStateCaptor.getValue());
        assertEquals(expectedFirstCurrency, firstCurrencyCaptor.getValue());
        assertEquals(expectedSecondCurrency, secondCurrencyCaptor.getValue());
        assertEquals(expectedText, actualSendMessage.getText());
    }

    @Test
    public void errorMessageToSend() throws IOException {
        Currencies expectedFirstCurrency = Currencies.USD;
        Currencies expectedSecondCurrency = Currencies.EUR;
        Integer expectedUserId = 11111;
        Long expectedChatId = 333333L;
        MessageState expectedState = MessageState.MAIN_MENU;
        String expectedText = "currencyServiceError";
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn(expectedSecondCurrency.name());
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedDao.getFirstUserCurrency(eq(expectedChatId))).thenReturn(expectedFirstCurrency);
        when(mockedCurrencyService.getRate(any(), any())).thenThrow(new IOException("Imitating IOException from external service"));
        when(mockedLocalisationService.getString(eq(expectedText), any())).thenReturn(expectedText);

        SendMessage actualSendMessage = underTestHandler.getMessageToSend(mockedMessage, Language.ENGLISH);
        verify(mockedDao).saveMessageState(userIdCaptor.capture(), chatIdCaptor.capture(), messageStateCaptor.capture());
        verify(mockedCurrencyService).getRate(firstCurrencyCaptor.capture(), secondCurrencyCaptor.capture());

        assertEquals(expectedUserId, userIdCaptor.getValue());
        assertEquals(expectedChatId, chatIdCaptor.getValue());
        assertEquals(expectedState, messageStateCaptor.getValue());
        assertEquals(expectedFirstCurrency, firstCurrencyCaptor.getValue());
        assertEquals(expectedSecondCurrency, secondCurrencyCaptor.getValue());
        assertEquals(expectedText, actualSendMessage.getText());
    }

    @Test
    public void callDefaultStateHandlerWhenNoText() {
        when(mockedMessage.hasText()).thenReturn(false);
        Language actualLanguage = Language.ENGLISH;

        underTestHandler.getMessageToSend(mockedMessage, actualLanguage);

        verify(mockedDefaultStateHandler, times(1))
                .getMessageToSend(eq(mockedMessage), eq(actualLanguage));
        verify(mockedDao, times(0)).saveMessageState(any(), any(), any());
        verify(mockedLocalisationService, times(0)).getString(any(), any());
    }

    @Test
    public void callDefaultStateHandlerWhenNoFirstUserCurrency() {
        Language actualLanguage = Language.ENGLISH;
        Currencies expectedCurrency = Currencies.USD;
        when(mockedMessage.hasText()).thenReturn(true);
        when(mockedMessage.getText()).thenReturn(expectedCurrency.name());

        underTestHandler.getMessageToSend(mockedMessage, actualLanguage);

        verify(mockedDefaultStateHandler, times(1))
                .getMessageToSend(eq(mockedMessage), eq(actualLanguage));
        verify(mockedDao, times(0)).saveMessageState(any(), any(), any());
        verify(mockedLocalisationService, times(0)).getString(any(), any());
    }

    @Test
    public void getCorrectProcessedMessageState() {
        assertEquals(underTestHandler.getProcessedMessageState(), MessageState.CHOOSE_CURRENT_RATE_SECOND);
    }
}
