package com.giulianobortolassi.waes.diff;


/**
 * Proxy to underlying data store mechanism.
 *
 * As this application must be scalable I decided to add one abstraction layer
 * in oder to leverage different data mechanism.
 *
 * If its intended to deploy in different Cloud Providers, different data stores may be used.
 * We can hide it from the 'business logic' and use a kind of config mechanism to decide what
 * is the best option for a given host.
 *
 * It will also make easier to mock the data store mechanism when we do functional testing or
 * unit testing.
 *
 * This api version ships with two implementations
 *  - DiffRequestMongoDBRepository: A MongoDB repository using SpringDataMongoDB API.
 *      See {@link DiffRequestMongoDBRepository} for details on usage
 *  - DiffRequestInMemoryRepository: A simple implementation using a HashMap to store data.
 *      Its intended to be used for test purposes.
 */
public interface DiffRequestRepository {

    DiffRequest saveRequest(DiffRequest request) throws Exception;

    DiffRequest getRequest(String id);
}
