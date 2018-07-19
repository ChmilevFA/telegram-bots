package net.chmilevfa.telegram.bots.currency;

import java.util.Arrays;

/**
 * Supported currencies.
 *
 * @author chmilevfa
 * @since 07.07.18
 */
public enum Currencies {

    EUR,
    USD,
    PLN,
    RUB,
    CZK;

    public static boolean containsByName(String name) {
        return Arrays.stream(Currencies.values())
                .anyMatch(currency -> currency.name().equalsIgnoreCase(name));
    }
}
