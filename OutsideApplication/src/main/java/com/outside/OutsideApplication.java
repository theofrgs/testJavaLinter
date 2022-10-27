package com.outside;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Outside rest api swagger documentation",
        version = "1.0",
        description = "This the documentation of the Outside API, you can find all the routes for Outside Mobile app."
    ),
    servers = {
        @Server(url = "/")
    }
)
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class OutsideApplication {

    private static void configureLoggers() {
        try {
            LogManager.getLogManager()
                    .readConfiguration(new FileInputStream("./src/main/resources/logging.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OutsideApplication.configureLoggers();

        SpringApplication.run(OutsideApplication.class, args);
    }

}
