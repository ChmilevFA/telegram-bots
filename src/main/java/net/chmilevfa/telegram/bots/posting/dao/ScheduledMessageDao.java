package net.chmilevfa.telegram.bots.posting.dao;

import net.chmilevfa.telegram.bots.posting.ScheduledMessage;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DAO interface for objects of {@link ScheduledMessage}.
 *
 * @author chmilevfa
 * @since 06.08.18
 */
public interface ScheduledMessageDao {

    /**
     * Saves message in storage.
     *
     * @param message message to be saved for later usage.
     */
    void save(ScheduledMessage message);

    /**
     * Receives list of stored {@link ScheduledMessage} since specific date and time.
     *
     * @param dateTime specific date and time to filter returnable list of messages.
     */
    List<ScheduledMessage> getSince(OffsetDateTime dateTime);
}
