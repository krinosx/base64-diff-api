package com.giulianobortolassi.waes.comparator;

import org.springframework.stereotype.Component;

@Component
public class DefaultBase64Comparator implements Base64Comparator {

    /**
     * For this method I will assume the left data is the 'correct' one. I will compare
     * both and assume that the differences, or mismatches will be from the right data.
     *
     *
     * @param left left side data to be compared
     * @param right right side data to be compared
     * @return A {@link ComparisionResult} object
     */
    public ComparisionResult compare(String left, String right) {

        boolean match = false;
        ComparisionResult.ResultType resultType = null;
        int mismatchOffset = -1;
        int mismatchLength = 0;
        char[] difference = null;


        // remove '=' characters from base64 data
        left = left.replaceAll("=","");
        right = right.replaceAll("=","");


        // fail fast...
        if (left.length() != right.length()) {
            match = false;
            resultType = ComparisionResult.ResultType.SIZE_MISMATCH;

        } else if (left.equals(right)) {
            match = true;
            resultType = ComparisionResult.ResultType.MATCH;

        } else {
            match = false;
            resultType = ComparisionResult.ResultType.CONTENT_MISMATCH;
            /* Compare the string.
             * At this point we know both sides have the same size.
             */

            char[] leftArray = left.toCharArray();
            char[] rightArray = right.toCharArray();
            difference = new char[leftArray.length];



            for (int i = 0; i < leftArray.length; i++) {
                // found the difference
                if (leftArray[i] != rightArray[i]) {
                    if (mismatchOffset < 0) {
                        //first one, it will be the start offset
                        mismatchOffset = i;
                    }
                    // register the differences
                    difference[i] = rightArray[i];
                    mismatchLength++;
                }
            }
        }
        return new ComparisionResult(match, resultType, mismatchOffset, mismatchLength, difference);
    }
}
