package net.chmilevfa.telegram.bots.currency.state;

import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;

/**
 * Supported user answers on some of bot's states.
 *
 * @author chmilevfa
 * @since 13.07.18
 */
public enum UserAnswer {
    MAIN_MENU("goToMainMenu"),
    CURRENT_RATE("currentRate"),
    SETTINGS("settings"),
    FEEDBACK("feedback"),
    UNKNOWN("");

    UserAnswer(String title) {
        this.title = title;
    }

    private String title;

    public static UserAnswer getTypeByString(String text, Language language) {
        for (UserAnswer answer : UserAnswer.values()) {
            if (answer.title.isEmpty()) {
                continue;
            }
            if (text.trim().startsWith(LocalisationService.getString(answer.title, language))) {
                return answer;
            }
        }
        return UNKNOWN;
    }
}
