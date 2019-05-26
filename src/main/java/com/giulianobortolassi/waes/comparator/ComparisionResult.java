package com.giulianobortolassi.waes.comparator;

/**
 * Its part of Base64Comparator 'object api'. It is expected to all implementations of Base64Comparator to return
 * this Object. This way the users of this api only need to understand this object workings and can safe abstract the
 * comparator logic.
 */
public class ComparisionResult {

    /**
     * Indicates if the results Match or do not Match
     */
    private boolean match = false;

    /**
     * The results can be
     * MATCH - Both sides are equals
     * SIZE_MISMATCH - Data do not have the same size, no content comparision will be executed.
     * CONTENT_MISMATCH - Data have the same size, but some characters does not match. The unmatching characters can be
     * found in mismatchChars[] array.
     */
    public enum ResultType { MATCH, SIZE_MISMATCH, CONTENT_MISMATCH};
    private ResultType type;
    /** The starting point where the mismatch started. */
    private int mismatchOffset = -1;
    /** The mismatch character count.*/
    private int matchLength = 0;
    /** An array with the same size as provided data. The index where data matches are set with NULL and the indexes with
     * unmatched data are filled with data from the `right` side. This information can be used to improve the application
     * and provide some data mismatch preview.
     */
    private char[] mismatchChars;


    public ComparisionResult(boolean match, ResultType type, int mismatchOffset, int matchLength, char[] mismatchChars) {
        this.match = match;
        this.type = type;
        this.mismatchOffset = mismatchOffset;
        this.matchLength = matchLength;
        this.mismatchChars = mismatchChars;
    }

    public ResultType getResultType(){
        return this.type;
    }

    public boolean isMatch() {
        return match;
    }

    public int getMismatchOffset() {
        return mismatchOffset;
    }

    public int getMatchLength() {
        return matchLength;
    }

    public char[] getMismatchChars() {
        return mismatchChars;
    }
}
