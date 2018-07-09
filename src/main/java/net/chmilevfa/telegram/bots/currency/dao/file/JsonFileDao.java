package net.chmilevfa.telegram.bots.currency.dao.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.dao.InMemoryData;
import net.chmilevfa.telegram.bots.currency.states.MessageState;

import java.io.File;
import java.io.IOException;

/**
 * JSON file implementation of {@link Dao}.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
public class JsonFileDao implements Dao, AutoCloseable {

    private final static String FILE_NAME = "data.json";

    private InMemoryData data;

    @Override
    public void saveMessageState(Integer userId, Long chatId, MessageState state) {
        data.saveState(userId, chatId, state);
    }

    @Override
    public MessageState getState(Integer userId, Long chatId) {
        return data.getMessageState(userId, chatId);
    }

    public void init() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        data = objectMapper.readValue(new File(FILE_NAME), InMemoryData.class);
    }

    @Override
    public void close() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(FILE_NAME), data);
    }
}
