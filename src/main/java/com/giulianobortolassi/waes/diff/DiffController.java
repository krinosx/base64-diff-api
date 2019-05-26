package com.giulianobortolassi.waes.diff;


import com.giulianobortolassi.waes.comparator.Base64Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/diff/{id}")
public class DiffController {

    @Autowired
    private Base64Comparator comparator;

    @Autowired
    //@Qualifier("inMemoryDiffRequestRepository")
    @Qualifier("diffRequestMongoDBRepository")
    private DiffRequestRepository repository;


    /**
     *
     *  Base64 must be considered as text/plain data
     *
     * - Configured the base64data to be 'optional' so I can deal with the error independent of spring handlers.
     * I prefer to use it in this application to keep things simple. In applications/api with lots of methods
     * it will be best to configure custom Exceptions and Handlers to do the job.
     *
     *
     * @param id
     * @param base64data
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/left")
    public ResponseEntity leftSide( @PathVariable("id") String id, @RequestBody(required = false) String base64data ){

        if( id == null || base64data == null || base64data.isEmpty() ){
            return new ResponseEntity(new DiffResponse("ERROR", "id and body data are mandatory"), HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity(new DiffResponse("ERROR", "Internal server error."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/right")
    public ResponseEntity<DiffResponse> rightSide( @PathVariable("id") String id, @RequestBody(required = false) String base64data ){

        if( id == null || base64data == null || base64data.isEmpty() ){
            return new ResponseEntity(new DiffResponse("ERROR", "id and body data are mandatory"), HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity(new DiffResponse("ERROR", "Internal server error."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().build();
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<DiffResponse> compare(@PathVariable("id") String id) {


        DiffRequest request = repository.getRequest(id);
        if( request == null ) {
            return new ResponseEntity(
                    new DiffResponse("ERROR","No request found with given ID"),
                    HttpStatus.NOT_FOUND);
        }

        DiffResponse result;
        try {

            boolean wasEvaluated = request.isEvaluated();

            result = request.getResult(comparator);

            /**
             * I think there must be a better way to do that check.
             * It can be handled with some versioning.
             * I will put that in the improvement list.
             */
            if (!wasEvaluated) {
                repository.saveRequest(request);
            }

            return new ResponseEntity<>( result, HttpStatus.OK );

        } catch (Exception e) {

            // Deal with presentation logic.
            if( e instanceof IllegalStateException ) {
                return new ResponseEntity(
                        new DiffResponse("ERROR","Request is not ready to be processed."),HttpStatus.BAD_REQUEST);

            } else {
                return new ResponseEntity(
                        new DiffResponse("ERROR","Unexpected error."),HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
    }

}
