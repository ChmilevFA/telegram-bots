package net.chmilevfa.telegram.bots.currency.service.language;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for static methods of {@link Language}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
public class LanguageTest {

    @Test
    public void trueIsLanguageNameSupported() {
        assertTrue(Language.isLanguageNameSupported(Language.ENGLISH.getName()));
    }

    @Test
    public void falseIsLanguageNameSupported() {
        String oddName = Language.ENGLISH.getName() + "rubbish_for_string";

        assertFalse(Language.isLanguageNameSupported(oddName));
    }

    @Test
    public void getLanguageByName() {
        Language expectedLanguage = Language.RUSSIAN;

        Language actualLanguage = Language.getLanguageByName(expectedLanguage.getName());

        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void getDefaultLanguageByName() {
        Language expectedLanguage = Language.ENGLISH;
        String rubbishString = "rubbish_string";

        Language actualLanguage = Language.getLanguageByName(rubbishString);

        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void getLanguageByCode() {
        Language expectedLanguage = Language.RUSSIAN;

        Language actualLanguage = Language.getLanguageByCode(expectedLanguage.getCode());

        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void getDefaultLanguageByCode() {
        Language expectedLanguage = Language.ENGLISH;
        String rubbishString = "rubbish_string";

        Language actualLanguage = Language.getLanguageByCode(rubbishString);

        assertEquals(expectedLanguage, actualLanguage);
    }
}
