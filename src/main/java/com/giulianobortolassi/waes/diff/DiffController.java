package com.giulianobortolassi.waes.diff;


import com.giulianobortolassi.waes.comparator.Base64Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/diff/{id}")
public class DiffController {

    @Autowired
    private Base64Comparator comparator;

    @Autowired
    private DiffRequestRepository repository;

    /**
     * Set the right side data and persist the DiffRequest.
     * Base64 must be considered as text/plain data
     *
     * - Configured the base64data to be 'optional' so I can deal with the error independent of spring handlers.
     * I prefer to use it in this application to keep things simple. In applications/api with lots of methods
     * it will be best to configure custom Exceptions and Handlers to do the job.
     *
     * @param id the DiffRequest ID. If no request exist with the given ID, a new one is created.
     * @param base64data some text plain data.
     * @return a HTTP 200 if everything went right. An error if something strange happens.
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/left")
    public  ResponseEntity<DiffResponse> leftSide( @PathVariable("id") String id, @RequestBody(required = false) String base64data ){

        if( id == null || base64data == null || base64data.isEmpty() ){
            return new ResponseEntity<>(new DiffResponse("ERROR", "id and body data are mandatory"), HttpStatus.BAD_REQUEST);
        }

        DiffRequest request = repository.getRequest(id);
        if( request == null ) {
            request = new DiffRequest();
            request.setId(id);
        }

        request.setLeft(base64data);

        try {
            repository.saveRequest( request );
        } catch (Exception e) {
            // It should never happen because we check and set the ID.
            // TODO: Log it to alert some monitoring tool
            e.printStackTrace();
            // It should never happen. We check and set the ID.
            return new ResponseEntity<>(new DiffResponse("ERROR", "Internal server error."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Set the right side data and persist the DiffRequest.
     *
     * @param id the DiffRequest ID. If no request exist with the given ID, a new one is created.
     * @param base64data some text plain data.
     * @return a HTTP 200 if everything went right. An error if something strange happens.
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/right")
    public ResponseEntity<DiffResponse> rightSide( @PathVariable("id") String id, @RequestBody(required = false) String base64data ){

        if( id == null || base64data == null || base64data.isEmpty() ){
            return new ResponseEntity<>(new DiffResponse("ERROR", "id and body data are mandatory"), HttpStatus.BAD_REQUEST);
        }

        DiffRequest request = repository.getRequest(id);
        if( request == null ) {
            request = new DiffRequest();
            request.setId(id);
        }

        request.setRight(base64data);

        try {
            repository.saveRequest( request );
        } catch (Exception e) {
            // TODO: Log it to alert some monitoring tool
            e.printStackTrace();
            // It should never happen. We check and set the ID.
            return new ResponseEntity<>(new DiffResponse("ERROR", "Internal server error."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Get the DiffRequest from database and execute the comparision. If the comparision was already executed and no data
     * was changed since the last execution, so the 'cached' result is returned.
     *
     * @param id the DiffRequest id.
     * @return The result of comparision or an error message.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<DiffResponse> compare(@PathVariable("id") String id) {


        DiffRequest request = repository.getRequest(id);
        if( request == null ) {
            return new ResponseEntity<>(
                    new DiffResponse("ERROR","No request found with given ID"),
                    HttpStatus.NOT_FOUND);
        }

        DiffResponse result;
        try {

            boolean wasEvaluated = request.isEvaluated();

            result = request.getResult(comparator);

            /*
             * I think there must be a better way to do that check.
             * It can be handled with some versioning mechanism.
             *
             * I will consider that in the improvement list.
             */
            if (!wasEvaluated) {
                repository.saveRequest(request);
            }

            return new ResponseEntity<>( result, HttpStatus.OK );

        } catch (Exception e) {
            // Deal with presentation logic.
            if( e instanceof IllegalStateException ) {
                return new ResponseEntity<>(
                        new DiffResponse("ERROR","Request is not ready to be processed."),HttpStatus.BAD_REQUEST);

            } else {
                /* Its wise to implement some Logging mechanism to track the unexpected errors. */
                return new ResponseEntity<>(
                        new DiffResponse("ERROR","Unexpected error."),HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
    }

}
