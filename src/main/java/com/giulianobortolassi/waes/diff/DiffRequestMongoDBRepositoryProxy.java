package com.giulianobortolassi.waes.diff;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Default MongoDB repository interface.
 *
 * Its a ProxyInterface provided by SpringData API. It will implement the most common CRUD operations based on given
 * template/generics object (DiffRequest)
 * If some custom operation must be implemented we can declare it in this interface and create a concrete class
 * to implement it. (It is not the case in this project)
 */
@Profile("mongodb")
public interface DiffRequestMongoDBRepositoryProxy extends MongoRepository<DiffRequest, String> {

}
