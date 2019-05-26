package com.giulianobortolassi.waes.diff;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiffRequestMongoDBRepositoryProxy extends MongoRepository<DiffRequest, String> {

}
