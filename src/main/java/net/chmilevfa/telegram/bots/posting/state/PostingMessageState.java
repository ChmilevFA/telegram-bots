package net.chmilevfa.telegram.bots.posting.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Represents menus state of posting bot.
 *
 * @author chmilevfa
 * @since 07.08.18
 */
public enum  PostingMessageState {

    DEFAULT(0),
    MAIN_MENU(1),
    ADD_NEW_POST(2),
    SET_DATE_TIME_FOR_NEW_POST(3);

    private final int code;

    PostingMessageState(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static PostingMessageState getByCode(int code) {
        return Arrays.stream(PostingMessageState.values())
                .filter(state -> state.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Illegal code for parsing: " + code));
    }
}
