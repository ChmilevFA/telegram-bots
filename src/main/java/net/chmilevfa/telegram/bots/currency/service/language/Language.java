package net.chmilevfa.telegram.bots.currency.service.language;

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

    public static Language getLanguageByCode(String code) {
        for (Language language : Language.values()) {
            if (language.getCode().equals(code)) {
                return language;
            }
        }
        //Default language
        return ENGLISH;
    }
}