package net.chmilevfa.telegram.bots.currency.state;

import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.service.language.LocalisationService;

import java.util.Arrays;

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
    LANGUAGES("languages"),
    UNKNOWN("");

    UserAnswer(String title) {
        this.title = title;
    }

    private final String title;

    public static UserAnswer getTypeByString(String text, Language language, LocalisationService localisationService) {
        return Arrays.stream(UserAnswer.values())
                .filter(answer -> !answer.equals(UNKNOWN))
                .filter(answer -> text.trim().startsWith(localisationService.getString(answer.title, language)))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
