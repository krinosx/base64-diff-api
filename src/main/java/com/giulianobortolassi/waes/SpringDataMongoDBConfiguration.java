package com.giulianobortolassi.waes;


import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("mongodb")
@Configuration
public class SpringDataMongoDBConfiguration extends MongoDataAutoConfiguration {

    public SpringDataMongoDBConfiguration(MongoProperties properties) {
        super(properties);
    }
}
