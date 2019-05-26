package com.giulianobortolassi.waes.diff;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Profile("default")
@Repository
public class DiffRequestInMemoryRepository implements DiffRequestRepository {

    private HashMap<String, DiffRequest> localDatabase = new HashMap<>();

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

    public DiffRequest getRequest(String id) {
        return localDatabase.get(id);
    }
}