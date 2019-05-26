package com.giulianobortolassi.waes.diff;


/**
 * It represents a simple class to be sent as a response for a comparision request. Spring Web framework
 * make it easier to marshal/unmarshal simple POJO objects, so I use this one to generate the JSON response.
 */
public class DiffResponse {

    String comparisionStatus;
    String message;

    public DiffResponse(String comparisionStatus, String message) {
        this.comparisionStatus = comparisionStatus;
        this.message = message;
    }

    public String getComparisionStatus() {
        return comparisionStatus;
    }

    public String getMessage() {
        return message;
    }
}
