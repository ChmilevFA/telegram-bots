package net.chmilevfa.telegram.bots.currency.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Supported states for user in menus.
 *
 * @author chmilevfa
 * @since 07.07.18
 */
public enum MessageState {

    DEFAULT(0),
    MAIN_MENU(1),
    CHOOSE_CURRENT_RATE_FIRST(2),
    CHOOSE_CURRENT_RATE_SECOND(3),
    SETTINGS(4),
    LANGUAGES(5),
    FEEDBACK(6);

    private final int code;

    MessageState(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static MessageState getMessageStateByCode(int code) {
        return Arrays.stream(MessageState.values())
                .filter(state -> state.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Illegal code for parsing: " + code));
    }
}
