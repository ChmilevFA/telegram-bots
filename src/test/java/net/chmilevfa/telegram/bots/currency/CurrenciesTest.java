package net.chmilevfa.telegram.bots.currency;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for static methods of {@link Currencies}.
 *
 * @author chmilevfa
 * @since 15.07.18
 */
public class CurrenciesTest {

    @Test
    public void containsByUpperCaseNameFound() {
        String nameToCheck = Currencies.EUR.name().toUpperCase();

        assertTrue(Currencies.containsByName(nameToCheck));
    }

    @Test
    public void containsByLowerCaseNameFound() {
        String nameToCheck = Currencies.EUR.name().toLowerCase();

        assertTrue(Currencies.containsByName(nameToCheck));
    }

    @Test
    public void containsByNameNotFound() {
        String incorrectNameToCheck = "qweasdzxcasdqwe";

        assertFalse(Currencies.containsByName(incorrectNameToCheck));
    }
}
