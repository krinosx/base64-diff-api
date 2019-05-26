package com.giulianobortolassi.waes.comparator;

import org.springframework.stereotype.Component;

@Component
public class Base64Comparator {

    /**
     * For this method I will assume the left data is the 'correct' one. I will compare
     * both and assume that the differences, or mismatches will be from the right data.
     *
     *
     * @param left
     * @param right
     * @return
     */
    public ComparasionResult compare(String left, String right) {

        boolean match = false;
        ComparasionResult.ResultType resultType = null;
        int missmatchOffset = -1;
        int missmatchLength = 0;
        char[] difference = null;



        // remove '=' characters from base64 data
        left = left.replaceAll("=","");
        right = right.replaceAll("=","");


        // fail fast...
        if (left.length() != right.length()) {
            match = false;
            resultType = ComparasionResult.ResultType.SIZE_MISMATCH;

        } else if (left.equals(right)) {
            match = true;
            resultType = ComparasionResult.ResultType.MATCH;

        } else {
            match = false;
            resultType = ComparasionResult.ResultType.CONTENT_MISMATCH;
            /* Compare the string.
             * At this point we know both sides have the same size.
             */

            char[] leftArray = left.toCharArray();
            char[] rightArray = right.toCharArray();
            difference = new char[leftArray.length];



            for (int i = 0; i < leftArray.length; i++) {
                // found the difference
                if (leftArray[i] != rightArray[i]) {
                    if (missmatchOffset < 0) {
                        //first one, it will be the start offset
                        missmatchOffset = i;
                    }
                    // register the differences
                    difference[i] = rightArray[i];
                    missmatchLength++;
                }
            }
        }
        return new ComparasionResult(match, resultType, missmatchOffset, missmatchLength, difference);
    }
}
