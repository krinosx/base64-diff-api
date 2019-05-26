package com.giulianobortolassi.waes;


import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Basic MongoDB configuration. It will only be enable if spring profile `mongodb` is active.
 *
 * To activate the profile start with '--spring.profiles.active=mongodb' command line parameter
 *
 */
@Profile("mongodb")
@Configuration
public class SpringDataMongoDBConfiguration extends MongoDataAutoConfiguration {

    public SpringDataMongoDBConfiguration(MongoProperties properties) {
        super(properties);
    }
}
