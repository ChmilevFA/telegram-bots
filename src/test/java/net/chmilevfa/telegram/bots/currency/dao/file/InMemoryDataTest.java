package net.chmilevfa.telegram.bots.currency.dao.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link InMemoryData}.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
public class InMemoryDataTest {

    private ObjectMapper objectMapper;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void serialization() throws JsonProcessingException {
        InMemoryData objectToSerialize = new InMemoryData();
        objectToSerialize.saveMessageState(123, 1231231L, MessageState.MAIN_MENU);
        objectToSerialize.saveMessageState(123123, 12231231L, MessageState.CHOOSE_CURRENT_RATE_FIRST);
        objectToSerialize.saveFirstUserCurrency(123123L, Currencies.EUR);
        objectToSerialize.saveLanguage(123123, Language.RUSSIAN.getCode());
        String expectedJson = "{\"languages\":{\"123123\":\"ru\"},\"states\":{\"123_1231231\":1,\"123123_12231231\":2},\"firstUserCurrency\":{\"123123\":\"EUR\"}}";

        String actualJson = objectMapper.writeValueAsString(objectToSerialize);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void deserialization () throws IOException {
        String jsonInput = "{\"languages\":{\"123123\":\"ru\"},\"states\":{\"123_1231231\":1,\"123123_12231231\":2},\"firstUserCurrency\":{\"123123\":\"EUR\"}}";
        InMemoryData expectedObject = new InMemoryData();
        expectedObject.saveMessageState(123, 1231231L, MessageState.MAIN_MENU);
        expectedObject.saveMessageState(123123, 12231231L, MessageState.CHOOSE_CURRENT_RATE_FIRST);
        expectedObject.saveFirstUserCurrency(123123L, Currencies.EUR);
        expectedObject.saveLanguage(123123, Language.RUSSIAN.getCode());

        InMemoryData actualObject = objectMapper.readValue(jsonInput, InMemoryData.class);

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void retrieveDataAfterSerializationDeserialization() throws IOException {
        MessageState expectedUserState = MessageState.MAIN_MENU;
        Currencies expectedCurrency = Currencies.EUR;
        String expectedLanguageCode = Language.RUSSIAN.getCode();
        int userId = 123123;
        long chatId = 1231231L;
        InMemoryData expectedData = new InMemoryData();
        expectedData.saveMessageState(userId, chatId, expectedUserState);
        expectedData.saveFirstUserCurrency(chatId, expectedCurrency);
        expectedData.saveLanguage(userId, expectedLanguageCode);

        String actualJson = objectMapper.writeValueAsString(expectedData);
        InMemoryData actualData = objectMapper.readValue(actualJson, InMemoryData.class);

        assertEquals(expectedUserState, actualData.getMessageState(userId, chatId));
        assertEquals(expectedCurrency, actualData.getFirstUserCurrency(chatId));
        assertEquals(expectedLanguageCode, actualData.getLanguage(userId));
    }
}
