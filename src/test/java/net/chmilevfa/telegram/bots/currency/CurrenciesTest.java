package net.chmilevfa.telegram.bots.currency;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for static methods of {@link Currencies}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
public class CurrenciesTest {

    @Test
    public void testTrueContainsByUpperCaseName() {
        String nameToCheck = Currencies.EUR.name().toUpperCase();

        Assert.assertTrue(Currencies.containsByName(nameToCheck));
    }

    @Test
    public void testTrueContainsByLowerCaseName() {
        String nameToCheck = Currencies.EUR.name().toLowerCase();

        Assert.assertTrue(Currencies.containsByName(nameToCheck));
    }

    @Test
    public void testFalseContainsByName() {
        String incorrectNameToCheck = "qweasdzxcasdqwe";

        Assert.assertFalse(Currencies.containsByName(incorrectNameToCheck));
    }
}
