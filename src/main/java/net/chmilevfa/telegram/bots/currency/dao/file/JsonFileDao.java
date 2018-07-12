package net.chmilevfa.telegram.bots.currency.dao.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

/**
 * JSON file implementation of {@link Dao}.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
@Repository("dao")
public class JsonFileDao implements Dao, AutoCloseable {

    private static Logger logger = LoggerFactory.getLogger(JsonFileDao.class);

    private final static String FILE_NAME = "data.json";

    private InMemoryData data;

    private JsonFileDao() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(FILE_NAME);
            if(file.exists() && !file.isDirectory()) {
                this.data = objectMapper.readValue(file, InMemoryData.class);
            }
        } catch (IOException e) {
            logger.error("Error during reading a file storage", e);
        }
        if (this.data == null) {
            data = new InMemoryData();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void saveMessageState(Integer userId, Long chatId, MessageState state) {
        data.saveState(userId, chatId, state);
    }

    @Override
    public MessageState getState(Integer userId, Long chatId) {
        return data.getMessageState(userId, chatId);
    }

    @Override
    public void saveFirstUserCurrency(Long chatId, Currencies currency) {
        data.saveFirstUserCurrency(chatId, currency);
    }

    @Override
    public Currencies getFirstUserCurrency(Long chatId) {
        return data.getFirstUserCurrency(chatId);
    }

    @Override
    public void close() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(FILE_NAME), data);
        } catch (IOException e) {
            logger.error("Error during writing to a file storage", e);
        }
    }
}
