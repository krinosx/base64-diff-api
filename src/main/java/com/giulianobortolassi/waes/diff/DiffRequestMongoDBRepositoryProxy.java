package com.giulianobortolassi.waes.diff;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

@Profile("mongodb")
public interface DiffRequestMongoDBRepositoryProxy extends MongoRepository<DiffRequest, String> {

}
