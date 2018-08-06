package net.chmilevfa.telegram.bots.posting;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Describes scheduled message to be posted to telegram.
 * Supports only text messages.
 *
 * @author chmilevfa
 * @since 06.08.18
 */
public class ScheduledMessage implements Comparable<ScheduledMessage> {

    private OffsetDateTime scheduledTime;
    private long chatId;
    private String message;

    public ScheduledMessage(OffsetDateTime scheduledTime, long chatId, String message) {
        this.scheduledTime = scheduledTime;
        this.chatId = chatId;
        this.message = message;
    }

    public long getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getScheduledTime() {
        return scheduledTime;
    }

    @Override
    public int compareTo(ScheduledMessage o) {
        return scheduledTime.compareTo(o.scheduledTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledMessage that = (ScheduledMessage) o;
        return chatId == that.chatId &&
                Objects.equals(message, that.message) &&
                Objects.equals(scheduledTime, that.scheduledTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, message, scheduledTime);
    }
}
