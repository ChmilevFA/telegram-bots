package net.chmilevfa.telegram.bots.currency.state;

import com.fasterxml.jackson.annotation.JsonValue;

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
    FEEDBACK(5);

    int code;

    MessageState(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    //TODO add @JsonCreator
}
