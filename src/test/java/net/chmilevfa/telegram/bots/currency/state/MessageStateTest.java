package net.chmilevfa.telegram.bots.currency.state;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Tests for static methods of {@link MessageState}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
public class MessageStateTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getMessageStateByCorrectCode() {
        MessageState expectedState = MessageState.DEFAULT;

        MessageState actualState = MessageState.getMessageStateByCode(expectedState.getCode());

        assertEquals(expectedState, actualState);
    }

    @Test
    public void exceptionWhenGetMessageStateByInvalidCode() {
        int wrongCode = Integer.MAX_VALUE;
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Illegal code for parsing: " + wrongCode);

        MessageState.getMessageStateByCode(wrongCode);
    }
}
