package net.chmilevfa.telegram.bots.currency.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chmilevfa.telegram.bots.currency.states.MessageState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for {@link InMemoryData}
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
    public void testSerialization() throws JsonProcessingException {
        InMemoryData objectToSerialize = new InMemoryData();
        objectToSerialize.saveState(123, 1231231L, MessageState.MAIN_MENU);
        objectToSerialize.saveState(123123, 12231231L, MessageState.CHOOSE_CURRENCY);
        String expectedJson = "{\"languages\":{},\"states\":{\"123_1231231\":0,\"123123_12231231\":1}}";

        String actualJson = objectMapper.writeValueAsString(objectToSerialize);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testDeserialization () throws IOException {
        String jsonInput = "{\"languages\":{},\"states\":{\"123_1231231\":0,\"123123_12231231\":1}}";
        InMemoryData expectedObject = new InMemoryData();
        expectedObject.saveState(123, 1231231L, MessageState.MAIN_MENU);
        expectedObject.saveState(123123, 12231231L, MessageState.CHOOSE_CURRENCY);

        InMemoryData actualObject = objectMapper.readValue(jsonInput, InMemoryData.class);

        Assert.assertEquals(expectedObject, actualObject);
    }
}
