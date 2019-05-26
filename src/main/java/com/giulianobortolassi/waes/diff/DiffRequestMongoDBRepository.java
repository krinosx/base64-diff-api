package com.giulianobortolassi.waes.diff;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

/**
 * This class implements a storage mechanism based on DiffRequestRepository interface. Data will be send to a MongoDB
 * cluster using the SpringData MongoDB framework. {@see
 * <a href="https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/">SpringData MongoDB Reference </a>} .
 *
 * MongoDB was choose in order to provide high scalability. Decoupling the persistence and logic tiers we can
 * achieve elasticity in both layers in a independent way.
 *
 * A active spring profile named 'mongodb' is needed in order to use this implementation. I can be achieved by various
 * mechanisms. The suggested one is to pass a command line argument '--spring.profiles.active=mongodb'. This way the
 * application will boot using the application-mongodb.properties file. This file contains the MongoDB cluster connection
 * string a 'spring.data.mongodb.uri' property. This configuration can be overwritten passing a command line argument with
 * the same name: '--spring.data.mongodb.uri=[new uri]'.
 *
 * OBS: The URL contained in packaged application-mongodb.properties file points to a MongoDB cluster hosted in
 * MongoDBAtlas and will not be available forever. If some connection error happens, the first thing to try is to
 * change the 'spring.data.mongodb.uri' parameter to a valid MongDB cluster.
 *
 * - The URL contains a user/password, it is not a best practice to a production ready application. If it was intended
 * to deploy the application in a production environment it must be changed.
 *
 */
@Profile("mongodb")
@Repository
public class DiffRequestMongoDBRepository implements DiffRequestRepository {

    @Autowired
    DiffRequestMongoDBRepositoryProxy proxy;

    /**
     * Save a DiffRequest document into a MongoDB database.
     * @param request DiffRequest object to be saved
     * @return DiffRequest object saved. All the merging logic is implemented by SpringData MongoDB repository (proxy)
     * @throws Exception
     */
    public DiffRequest saveRequest(DiffRequest request) throws Exception {
        DiffRequest save = proxy.save(request);
        return save;
    }

    /**
     * Get a DiffRequest based on given ID. If no request is found, return null
     * @param id DiffRequest id to search
     * @return a DiffRequest or null if no DiffRequest is found with given ID.
     */
    public DiffRequest getRequest(String id) {
        Optional<DiffRequest> byId = proxy.findById(id);
        return byId.isPresent() ? byId.get() : null;
    }
}