package net.chmilevfa.telegram.bots.posting.state.handler;

import net.chmilevfa.telegram.bots.posting.dao.PostingMessageStateDao;
import net.chmilevfa.telegram.bots.posting.state.PostingMessageState;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link DefaultStateHandler}.
 *
 * @author chmilevfa
 * @since 10.08.18
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultStateHandlerTest {

    @Captor
    private ArgumentCaptor<Integer> userIdCaptor;
    @Captor
    private ArgumentCaptor<Long> chatIdCaptor;
    @Captor
    private ArgumentCaptor<PostingMessageState> messageStateCaptor;

    @Mock
    private PostingMessageStateDao mockedDao;
    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    private Message mockedMessage;

    private DefaultStateHandler underTestHandler;

    @Before
    public void setUp() {
        underTestHandler = new DefaultStateHandler(mockedDao);
    }

    @Test
    public void validMessageToSend() {
        Integer expectedUserId = 42;
        Long expectedChatId = 123123123L;
        PostingMessageState expectedMessageState = PostingMessageState.MAIN_MENU;
        Integer expectedMessageId = 142;
        when(mockedMessage.getFrom().getId()).thenReturn(expectedUserId);
        when(mockedMessage.getChatId()).thenReturn(expectedChatId);
        when(mockedMessage.getMessageId()).thenReturn(expectedMessageId);

        SendMessage actualSendMessage = underTestHandler.getMessageToSend(mockedMessage);
        verify(mockedDao).saveMessageState(userIdCaptor.capture(), chatIdCaptor.capture(), messageStateCaptor.capture());

        assertEquals(expectedUserId, userIdCaptor.getValue());
        assertEquals(expectedChatId, chatIdCaptor.getValue());
        assertEquals(expectedMessageState, messageStateCaptor.getValue());
        assertEquals(expectedChatId.toString(), actualSendMessage.getChatId());
        assertEquals(expectedMessageId, actualSendMessage.getReplyToMessageId());
    }

    @Test
    public void validPostingMessageStateToProcess() {
        PostingMessageState expectedState = PostingMessageState.DEFAULT;

        PostingMessageState actualState = underTestHandler.getProcessedMessageState();

        assertEquals(expectedState, actualState);
    }
}
