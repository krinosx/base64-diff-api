package com.giulianobortolassi.waes.diff;

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
