package com.outside;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

@org.springframework.stereotype.Service
public class ConfigProperties {

    private static ConfigProperties instance = null;
    static Properties properties = null;
    public static final Logger LOGGER = LogManager.getLogger(ConfigProperties.class);

    protected static void initProperties() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream("services.properties")) {
            if (resourceStream == null) {
                throw new FileNotFoundException("services.properties");
            }
            properties.load(resourceStream);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getMessage(e));
        }
    }

    public static ConfigProperties getInstance() {
        if (instance == null) {
            initProperties();
            instance = new ConfigProperties();
        }
        return instance;
    }

    public static Properties getProperties() {
        if (instance == null) {
            initProperties();
            instance = new ConfigProperties();
        }
        return properties;
    }

}
