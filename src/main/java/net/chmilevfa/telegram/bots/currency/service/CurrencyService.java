package net.chmilevfa.telegram.bots.currency.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.chmilevfa.telegram.bots.currency.Currencies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Service to get actual currency rate.
 *
 * @author chmilevfa
 * @since 07.07.18
 */
public class CurrencyService {

    private final static String CURRENCY_CONVERTER_URL = "https://free.currencyconverterapi.com/api/v5/convert?q=%s&compact=y";

    /**
     * Get rate using pair of currencies.
     * @param from currency to convert from.
     * @param to currency to convert to.
     * @return actual rate for from/to.
     */
    public float getRate(Currencies from, Currencies to) throws IOException {
        String currencyArg = from.name() + "_" + to.name();
        String uri = String.format(CURRENCY_CONVERTER_URL, currencyArg);

        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }

        return extractCurrencyRate(content.toString(), currencyArg);
    }

    float extractCurrencyRate(String data, String currencyArg) {
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        return Float.parseFloat(jsonObject.getAsJsonObject(currencyArg).get("val").getAsString());
    }
}
