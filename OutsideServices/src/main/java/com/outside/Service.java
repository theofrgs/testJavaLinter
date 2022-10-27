package com.outside;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@org.springframework.stereotype.Service
public abstract class Service {
    public Properties properties = ConfigProperties.getProperties();
    protected Logger LOGGER = LogManager.getLogger(Service.class);

    public void initLogger(Class<?> _class) {
        this.LOGGER = LogManager.getLogger(_class);
    }

}
