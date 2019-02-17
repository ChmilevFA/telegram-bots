package net.chmilevfa.telegram.bots.currency.service;

import net.chmilevfa.telegram.bots.AppConfig;
import net.chmilevfa.telegram.bots.currency.Currencies;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * Test for {@link CurrencyService}
 *
 * @author chmilevfa
 * @since 07.07.18
 */
public class CurrencyServiceTest {

    private CurrencyService underTest;

    @Before
    public void setUp() {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        underTest = (CurrencyService) context.getBean("currencyService");
    }

    @Test
    public void extractCurrencyRateTest() {
        float actualVal = 0;

        try {
            actualVal = underTest.getRate(Currencies.USD, Currencies.CZK);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertNotEquals(0, actualVal, 0.00001);
    }
}
