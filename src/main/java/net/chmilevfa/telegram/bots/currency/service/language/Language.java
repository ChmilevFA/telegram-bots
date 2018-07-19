package net.chmilevfa.telegram.bots.currency.service.language;

import java.util.Arrays;

/**
 * Currently supported languages.
 *
 * @author chmilevfa
 * @since 13.07.18
 */
public enum Language {

    ENGLISH("English", "en"),
    RUSSIAN("Russian", "ru");

    private final String name;
    private final String code;

    Language(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    /**
     * Checks if language can be found by passed argument.
     *
     * @param name possible name of language
     * @return returns true if supported language was found by parameter and false otherwise.
     */
    public static boolean isLanguageNameSupported(String name) {
        return Arrays.stream(Language.values())
                .anyMatch(language -> language.getName().equals(name));
    }

    /**
     * Returns one of supported {@link Language} by passed string name.
     * Returns {@link Language#ENGLISH} as default if the language was not found.
     *
     * @param name possible name of language
     * @return returns {@link Language} by passed string name
     */
    public static Language getLanguageByName(String name) {
        return Arrays.stream(Language.values())
                .filter(language -> language.getName().equals(name))
                .findFirst()
                .orElse(ENGLISH);
    }

    /**
     * Returns one of supported {@link Language} by passed string code.
     * Returns {@link Language#ENGLISH} as default if language was not found.
     *
     * @param code possible code of language
     * @return returns {@link Language} by passed string code
     */
    public static Language getLanguageByCode(String code) {
        return Arrays.stream(Language.values())
                .filter(language -> language.getCode().equals(code))
                .findFirst()
                .orElse(ENGLISH);
    }
}