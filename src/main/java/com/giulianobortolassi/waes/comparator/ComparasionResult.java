package com.giulianobortolassi.waes.comparator;

public class ComparasionResult {

    private boolean match = false;
    public enum ResultType { MATCH, SIZE_MISMATCH, CONTENT_MISMATCH};
    private ResultType type;
    private int mismatchOffset = -1;
    private int matchLength = 0;
    private char[] mismatchChars;


    public ComparasionResult(boolean match, ResultType type, int mismatchOffset, int matchLength, char[] mismatchChars) {
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
