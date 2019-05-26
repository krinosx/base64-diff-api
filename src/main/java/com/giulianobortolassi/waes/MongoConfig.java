package com.giulianobortolassi.waes;

import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Profile("mongodb")
@Configuration
public class MongoConfig extends MongoAutoConfiguration  {

    public MongoConfig(MongoProperties properties, ObjectProvider<MongoClientOptions> options, Environment environment) {
        super(properties, options, environment);
    }
}
