package com.giulianobortolassi.waes.diff;


import com.giulianobortolassi.waes.comparator.Base64Comparator;
import com.giulianobortolassi.waes.comparator.ComparasionResult;
import org.springframework.data.annotation.Id;

public class DiffRequest {
    @Id
    private String id;
    private String left;
    private String right;

    private String resultStatus;
    private String resultMessage;

    // State control variable
    private boolean isEvaluated = false;

    // Return message
    public static final String MATCH_MESSAGE = "Documents match!";
    public static final String SIZE_MISMATCH_MESSAGE = "Document size does not match.";
    public static final String CONTENT_MISMATCH_MESSAGE = "Content does not match. Difference start: %d and keep for %d characters ";
    public static final String ILLEGAL_STATE_MESSAGE = "Left and Right side must be set prior to call getResult()!";


    public DiffRequest(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
        isEvaluated = false;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
        isEvaluated = false;
    }

    public DiffResponse getResult(Base64Comparator comparator) throws Exception {
        // compare

        if(!isEvaluated) {
            if( left != null && right != null ) {

                ComparasionResult compareResult = comparator.compare(this.left, this.right);

                if( compareResult.isMatch() ) {
                    // build the response
                    this.resultStatus = "MATCH";
                    this.resultMessage = MATCH_MESSAGE;


                } else if( compareResult.getResultType() == ComparasionResult.ResultType.SIZE_MISMATCH){
                    this.resultStatus = "SIZE_MISMATCH";
                    this.resultMessage = SIZE_MISMATCH_MESSAGE;
                } else {
                    this.resultStatus = "CONTENT_MISMATCH";
                    this.resultMessage = String.format(
                                        DiffRequest.CONTENT_MISMATCH_MESSAGE,
                                        compareResult.getMismatchOffset(),
                                        compareResult.getMatchLength());
                }

                isEvaluated = true;
            } else {
                throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
            }
        }

        return new DiffResponse(this.resultStatus, this.resultMessage);
    }

    public boolean isEvaluated() {
        return isEvaluated;
    }
}
