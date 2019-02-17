package net.chmilevfa.telegram.bots.currency.service;

import net.chmilevfa.telegram.bots.currency.Currencies;

import java.io.IOException;

/**
 * An interface for getting an up-to-date currency rate.
 *
 * @author Katenkka
 * @since 17.02.19
 */
public interface CurrencyService {

    /**
     * Gets rate for pair of currencies.
     * @param from currency to convert from.
     * @param to currency to convert to.
     * @return current rate for from/to.
     */
    float getRate(Currencies from, Currencies to) throws IOException;
}
