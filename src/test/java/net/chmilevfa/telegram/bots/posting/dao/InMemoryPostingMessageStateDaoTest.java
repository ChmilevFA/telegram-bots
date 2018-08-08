package net.chmilevfa.telegram.bots.posting.dao;

import net.chmilevfa.telegram.bots.posting.state.PostingMessageState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Simple In-memory implementation of {@link InMemoryPostingMessageStateDao}.
 *
 * @author chmilevfa
 * @since 08.08.18
 */
public class InMemoryPostingMessageStateDaoTest {

    private InMemoryPostingMessageStateDao underTest;

    @Before
    public void init() {
        underTest = new InMemoryPostingMessageStateDao();
    }

    @Test
    public void saveAndRetrieveState() {
        Integer userId = 123;
        Long chatId = 321L;
        PostingMessageState expectedState = PostingMessageState.ADD_NEW_POST;

        underTest.saveMessageState(userId, chatId, expectedState);
        PostingMessageState actualState = underTest.getState(userId, chatId);

        assertEquals(expectedState, actualState);
    }

    @Test
    public void retrieveDefaultState() {
        Integer userId = 123;
        Long chatId = 321L;

        PostingMessageState actualState = underTest.getState(userId, chatId);

        assertEquals(PostingMessageState.MAIN_MENU, actualState);
    }
}
