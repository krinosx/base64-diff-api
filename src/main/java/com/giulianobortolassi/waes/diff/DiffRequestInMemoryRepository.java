package com.giulianobortolassi.waes.diff;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 *  A simple local in memory data store. This class use a HashMap to store DiffRequests.
 *  The DiffRequest.id attribute is used as the hashmap key.
 *
 * Its the default implementation used when the application starts.
 *
 * The intent of this class is to provide a easy way to test the application. If some kind of shared database its
 * possible to use the MongoDB implementation or to implement a new {@link DiffRequestRepository}.
 *
 */
@Profile("default")
@Repository
public class DiffRequestInMemoryRepository implements DiffRequestRepository {

    private HashMap<String, DiffRequest> localDatabase = new HashMap<>();

    /**
     * Save the request to a local HashMap.
     *
     * @param request DiffRequest to be saved
     * @return a persisted version of DiffRequest. If an request with same ID already exists, the merged data will be returned.
     * @throws Exception
     */
    public DiffRequest saveRequest(DiffRequest request) throws Exception {

        if( request.getId() == null ){
            throw new IllegalArgumentException("Request must have an ID!");
        }

        DiffRequest localRequest = localDatabase.get(request.getId());

        if( localRequest == null ) {
            localRequest = request;
        } else {
            // merge data
            if( request.getRight() != null ) {
                localRequest.setRight( request.getRight() );
            }
            if( request.getLeft() != null ) {
                localRequest.setLeft( request.getLeft() );
            }
        }

        // persist
        localDatabase.put(request.getId(), localRequest);

        return request;
    }

    /**
     * Get a DiffRequest based on given ID. If no request is found, return null
     * @param id DiffRequest id to search
     * @return a DiffRequest or null if no DiffRequest is found with given ID.
     */
    public DiffRequest getRequest(String id) {
        return localDatabase.get(id);
    }
}