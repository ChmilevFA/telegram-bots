package net.chmilevfa.telegram.bots.currency.service.language;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for static methods of {@link Language}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
public class LanguageTest {

    @Test
    public void testTrueIsLanguageNameSupported() {
        Assert.assertTrue(Language.isLanguageNameSupported(Language.ENGLISH.getName()));
    }

    @Test
    public void testFalseIsLanguageNameSupported() {
        String oddName = Language.ENGLISH.getName() + "rubbish_for_string";

        Assert.assertFalse(Language.isLanguageNameSupported(oddName));
    }

    @Test
    public void testGetLanguageByName() {
        Language expectedLanguage = Language.RUSSIAN;

        Language actualLanguage = Language.getLanguageByName(expectedLanguage.getName());

        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGetDefaultLanguageByName() {
        Language expectedLanguage = Language.ENGLISH;
        String rubbishString = "rubbish_string";

        Language actualLanguage = Language.getLanguageByName(rubbishString);

        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGetLanguageByCode() {
        Language expectedLanguage = Language.RUSSIAN;

        Language actualLanguage = Language.getLanguageByCode(expectedLanguage.getCode());

        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGetDefaultLanguageByCode() {
        Language expectedLanguage = Language.ENGLISH;
        String rubbishString = "rubbish_string";

        Language actualLanguage = Language.getLanguageByCode(rubbishString);

        Assert.assertEquals(expectedLanguage, actualLanguage);
    }
}
