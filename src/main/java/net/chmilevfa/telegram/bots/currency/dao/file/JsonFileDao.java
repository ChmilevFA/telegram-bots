package net.chmilevfa.telegram.bots.currency.dao.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chmilevfa.telegram.bots.currency.Currencies;
import net.chmilevfa.telegram.bots.currency.dao.Dao;
import net.chmilevfa.telegram.bots.currency.service.language.Language;
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

    private static final Logger logger = LoggerFactory.getLogger(JsonFileDao.class);

    private final static String FILE_NAME = "data.json";

    private InMemoryData data;

    private JsonFileDao() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            logger.info("Reading data from file {}", FILE_NAME);
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
    public void saveLanguage(Integer userId, Language language) {
        data.saveLanguage(userId, language.getCode());
    }

    @Override
    public Language getLanguage(Integer userId) {
        return Language.getLanguageByCode(data.getLanguage(userId));
    }

    @Override
    public void saveMessageState(Integer userId, Long chatId, MessageState state) {
        data.saveMessageState(userId, chatId, state);
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
            logger.error("Error during writing data to a file storage", e);
            logger.error("Failed to write data: {}", data);
        }
        logger.info("Data writing to file succeed");
    }
}
