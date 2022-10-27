package com.outside;

import com.google.common.io.Files;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@org.springframework.stereotype.Service
public class Parser extends Service {

    public Parser() {
        this.initLogger(Parser.class);
    }

    public Map<String, Object> htmlParser(String filepath) {
        try {
            return Map.of("success", true, "content", Files.asCharSource(new File(filepath), StandardCharsets.UTF_8).read());
        } catch (Exception e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
            return Map.of("success", false, "errorMsg",ExceptionUtils.getMessage(e)) ;
        }
    }
}