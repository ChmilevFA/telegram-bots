package net.chmilevfa.telegram.bots.currency.state;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
    public void testGetMessageStateByCorrectCode() {
        MessageState expectedState = MessageState.DEFAULT;

        MessageState actualState = MessageState.getMessageStateByCode(expectedState.getCode());

        Assert.assertEquals(expectedState, actualState);
    }

    @Test
    public void testExceptionWhenGetMessageStateByCode() {
        int wrongCode = Integer.MAX_VALUE;
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Illegal code for parsing: " + wrongCode);

        MessageState.getMessageStateByCode(wrongCode);
    }
}
