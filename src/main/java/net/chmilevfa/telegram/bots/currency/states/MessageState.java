package net.chmilevfa.telegram.bots.currency.states;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Supported states for user in menus.
 *
 * @author chmilevfa
 * @since 07.07.18
 */
public enum MessageState {

    MAIN_MENU(0),
    CHOOSE_CURRENCY(1);

    int code;

    MessageState(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }
}
