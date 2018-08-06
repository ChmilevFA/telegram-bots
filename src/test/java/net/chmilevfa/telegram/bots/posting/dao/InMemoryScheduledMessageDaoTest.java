package net.chmilevfa.telegram.bots.posting.dao;

import net.chmilevfa.telegram.bots.posting.ScheduledMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Tests for {@link InMemoryScheduledMessageDao}.
 *
 * @author chmilevfa
 * @since 06.08.18
 */
public class InMemoryScheduledMessageDaoTest {

    private ScheduledMessageDao underTest;

    @Before
    public void init() {
        underTest = new InMemoryScheduledMessageDao();
    }

    @Test
    public void saveAndRetrieveSingleMessage() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 9, 8, 11, 12);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(dateTime, ZoneOffset.UTC);
        ScheduledMessage expected = new ScheduledMessage(offsetDateTime, 1234L, "test text");

        underTest.save(expected);
        List<ScheduledMessage> actual =
                underTest.getSince(OffsetDateTime.of(dateTime.minusMonths(2), ZoneOffset.UTC));

        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(expected, actual.get(0));
    }

    @Test
    public void saveAndRetrieveZeroMessages() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 9, 8, 11, 12);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(dateTime, ZoneOffset.UTC);
        ScheduledMessage expected = new ScheduledMessage(offsetDateTime, 1234L, "test text");

        underTest.save(expected);
        List<ScheduledMessage> actual =
                underTest.getSince(OffsetDateTime.of(dateTime.plusMonths(2), ZoneOffset.UTC));

        Assert.assertEquals(0, actual.size());
    }

    @Test
    public void saveAndRetrieveMultiplyMessages() {
        LocalDateTime dateTime = LocalDateTime.of(2018, 9, 8, 11, 12);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(dateTime, ZoneOffset.UTC);
        ScheduledMessage expected1 = new ScheduledMessage(offsetDateTime, 1234L, "test text");
        ScheduledMessage expected2 = new ScheduledMessage(offsetDateTime.plusMonths(3), 2342L, "text3");
        ScheduledMessage expected3 = new ScheduledMessage(offsetDateTime.minusMonths(3), 321L, "text2");

        underTest.save(expected1);
        underTest.save(expected2);
        underTest.save(expected3);
        List<ScheduledMessage> actual =
                underTest.getSince(OffsetDateTime.of(dateTime.minusMonths(2), ZoneOffset.UTC));

        Assert.assertEquals(2, actual.size());
        Assert.assertTrue(actual.contains(expected1));
        Assert.assertTrue(actual.contains(expected2));
        Assert.assertFalse(actual.contains(expected3));
    }
}
