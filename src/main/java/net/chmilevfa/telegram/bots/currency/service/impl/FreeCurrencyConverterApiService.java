package net.chmilevfa.telegram.bots.currency.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.service.CurrencyService;
import org.glassfish.jersey.internal.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Service for getting an up-to-date currency rate.
 *
 * @author chmilevfa
 * @since 07.07.18
 */
@Service("currencyService")
@PropertySource("classpath:currencyservice.properties")
public class FreeCurrencyConverterApiService implements CurrencyService {

    private static Logger logger = LoggerFactory.getLogger(FreeCurrencyConverterApiService.class);

    /** URL of exchange rate service */
    private final static String CURRENCY_CONVERTER_URL =
            "https://free.currencyconverterapi.com/api/v6/convert?q=%s&compact=y&apiKey=%s";

    /** Mapper for reading JSON */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** ApiKey for external service */
    @Value("${freecurrencyconverter.apiKey}")
    private String apiKey;

    /**
     * Get rate for pair of currencies.
     * @param from currency to convert from.
     * @param to currency to convert to.
     * @return current rate for from/to.
     */
    @Override
    public float getRate(Currencies from, Currencies to) throws IOException {
        String currencyArg = from.name() + "_" + to.name();
        return extractCurrencyRate(fetchExternalContent(currencyArg), currencyArg);
    }

    /**
     *
     * @param currencyArg currencies names joined by "_"
     * @return content from external website
     */
    String fetchExternalContent(String currencyArg) throws IOException {
        String uri = String.format(CURRENCY_CONVERTER_URL, currencyArg, apiKey);

        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        logger.info("Requesting currency rate for {} pair", currencyArg);

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                contentBuilder.append(inputLine);
            }
        }

        return contentBuilder.toString();
    }

    float extractCurrencyRate(String data, String currencyArg) throws IOException {
        String textValue = MAPPER.readTree(data).at("/" + currencyArg + "/val").asText();
        return Float.parseFloat(textValue);
    }
}
