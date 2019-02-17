package net.chmilevfa.telegram.bots.currency.service;

import net.chmilevfa.telegram.bots.currency.Currencies;

import java.io.IOException;

public interface CurrencyService {
    float getRate(Currencies from, Currencies to) throws IOException;
}
