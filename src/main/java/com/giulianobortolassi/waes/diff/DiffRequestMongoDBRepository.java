package com.giulianobortolassi.waes.diff;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

/**
 * Implementing a proxy class to translate from SpringData interface to my
 * independent repository interface. The idea is to reduce coupling with
 * spring data framework.
 *
 */

@Repository
public class DiffRequestMongoDBRepository implements DiffRequestRepository {

    @Autowired
    DiffRequestMongoDBRepositoryProxy proxy;

    private HashMap<String, DiffRequest> localDatabase = new HashMap<>();

    public DiffRequest saveRequest(DiffRequest request) throws Exception {
        DiffRequest save = proxy.save(request);
        return save;
    }

    public DiffRequest getRequest(String id) {
        Optional<DiffRequest> byId = proxy.findById(id);
        return byId.isPresent() ? byId.get() : null;
    }
}