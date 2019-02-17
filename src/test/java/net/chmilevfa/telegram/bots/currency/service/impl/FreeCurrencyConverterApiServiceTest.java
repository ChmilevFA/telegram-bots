package net.chmilevfa.telegram.bots.currency.service.impl;

import net.chmilevfa.telegram.bots.currency.Currencies;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

/**
 * Test for {@link FreeCurrencyConverterApiService}
 *
 * @author chmilevfa
 * @since 07.07.18
 */
public class FreeCurrencyConverterApiServiceTest {

    private FreeCurrencyConverterApiService underTest;

    private final String testJsonRate = "{\"EUR_USD\":{\"val\":1.130001}}";

    @Before
    public void init() {
        // apiKey is not needed here anyway
        FreeCurrencyConverterApiService real = new FreeCurrencyConverterApiService("1234");
        underTest = spy(real);
    }

    @Test
    public void getRateTest() throws IOException {
        doReturn(testJsonRate).when(underTest).fetchExternalContent("EUR_USD");
        float actualVal = underTest.getRate(Currencies.EUR, Currencies.USD);
        Assert.assertEquals(1.130001, actualVal, 0.00001);
    }

    @Test
    public void extractCurrencyRateTest() throws IOException {
        float actualVal = underTest.extractCurrencyRate(testJsonRate, "EUR_USD");
        Assert.assertEquals(1.130001, actualVal, 0.00001);
    }
}