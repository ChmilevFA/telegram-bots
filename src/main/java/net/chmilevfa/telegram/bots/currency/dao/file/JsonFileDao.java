package net.chmilevfa.telegram.bots.currency.dao.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.state.MessageState;
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

    private final static String FILE_NAME = "data.json";
    private static volatile JsonFileDao instance;

    private InMemoryData data;

    private JsonFileDao() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(FILE_NAME);
            if(file.exists() && !file.isDirectory()) {
                this.data = objectMapper.readValue(file, InMemoryData.class);
            }
        } catch (IOException e) {
            //TODO log and handle
            e.printStackTrace();
        }
        if (this.data == null) {
            data = new InMemoryData();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * Get Singleton instance
     *
     * @return instance of the class
     */
    public static JsonFileDao getInstance() {
        if (instance == null) {
            synchronized (JsonFileDao.class) {
                if (instance == null) {
                    instance = new JsonFileDao();
                }
            }
        }
        return instance;
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
            //TODO log and handle
            e.printStackTrace();
        }
    }
}
