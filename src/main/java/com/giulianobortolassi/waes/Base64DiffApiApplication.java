package com.giulianobortolassi.waes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * Base Spring Boot application startup class.
 *
 * MongoDB configuration is disabled so we can change the database implementation based on active Spring profile.
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Base64DiffApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Base64DiffApiApplication.class, args);
    }

}
