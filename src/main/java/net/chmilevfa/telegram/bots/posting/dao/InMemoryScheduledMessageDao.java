package net.chmilevfa.telegram.bots.posting.dao;

import net.chmilevfa.telegram.bots.posting.ScheduledMessage;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * In memory implementation of {@link ScheduledMessageDao}.
 *
 * @author chmilevfa
 * @since 06.08.18
 */
public class InMemoryScheduledMessageDao implements ScheduledMessageDao {

    private final Queue<ScheduledMessage> storedMessages = new PriorityQueue<>();

    @Override
    public void save(ScheduledMessage message) {
        storedMessages.add(message);
    }

    @Override
    public List<ScheduledMessage> getSince(OffsetDateTime dateTime) {
        return storedMessages.stream()
                .filter(message -> message.getScheduledTime().isAfter(dateTime))
                .collect(Collectors.toList());
    }
}
