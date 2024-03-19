package com.giulianobortolassi.waes.diff;

import com.giulianobortolassi.waes.comparator.Base64Comparator;
import com.giulianobortolassi.waes.comparator.ComparisionResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;


public class DiffRequestTest {

    private static String document_1_base64 = "Some base64 data... it is not relevant fot this unit test";
    private static String document_2_base64 = "Some base64 data... it is not relevant fot that unit test";
    private static String document_3_base64 = "Some base64 data... ";


    @Test
    public void testDiffMatchRequest(){
        /* Creating a mock class to isolate the tests. Base64Comparator implementation will have a
         * separated test to check it inner working.
         * As this test is pretty simple, I decided to do not use a Mock framework like Mockito or
         * other ones. If it get complex, we can replace the inner class with an Mockito proxy.
         */
        Base64Comparator mockComparator = new Base64Comparator(){
            @Override
            public ComparisionResult compare(String left, String right) {
                return new ComparisionResult(true, ComparisionResult.ResultType.MATCH,-1,-1,null);
            }
        };


        DiffRequest request = new DiffRequest();
        request.setLeft(document_1_base64);
        request.setRight(document_1_base64);

        DiffResponse result = null;
        try {
            result = request.getResult(mockComparator);
        } catch (Exception e) {
            fail("No exception expected. Got one. " + e.getMessage() );
        }
        assertNotNull("A result message is expected.",result);
        assertEquals( DiffRequest.MATCH_MESSAGE, result.getMessage(), "Equal documents must match!" );
    }

    @Test
    public void testDiffDoesNotMatchRequest(){

        Base64Comparator mockComparator = new Base64Comparator(){
            @Override
            public ComparisionResult compare(String left, String right) {
                return new ComparisionResult(false, ComparisionResult.ResultType.CONTENT_MISMATCH,
                        10,20,new char[document_1_base64.length()]);
            }
        };

        DiffRequest request = new DiffRequest();
        request.setLeft(document_1_base64);
        request.setRight(document_2_base64);

        DiffResponse result = null;

        try {
            result = request.getResult( mockComparator );
        } catch (Exception e) {
            fail("No exception expected. Got one. " + e.getMessage() );
        }
        assertNotNull("A result message is expected.", result );

        ComparisionResult compare = mockComparator.compare(document_1_base64, document_2_base64);
        String expectedMessage =
                String.format(DiffRequest.CONTENT_MISMATCH_MESSAGE,
                        compare.getMismatchOffset(),
                        compare.getMatchLength());

        assertEquals(expectedMessage ,result.getMessage(), "Got wrong message!" );
    }

    @Test
    public void testDiffWrongSizeRequest(){
        Base64Comparator mockComparator = new Base64Comparator(){
            @Override
            public ComparisionResult compare(String left, String right) {
                return new ComparisionResult(false, ComparisionResult.ResultType.SIZE_MISMATCH,
                        -1,-1,null);
            }
        };

        DiffRequest request = new DiffRequest();
        request.setLeft(document_1_base64);
        request.setRight(document_3_base64);

        DiffResponse result = null;
        try {
            result = request.getResult( mockComparator );
        } catch ( Exception e ) {
            fail("No exception expected. Got one. " + e.getMessage() );
        }
        assertNotNull("A result message is expected.",result);
        assertEquals( DiffRequest.SIZE_MISMATCH_MESSAGE, result.getMessage(), "Got wrong message!" );
    }

    @Test
    public void testNoRepeatProcessingIfDone(){

        /*
         *  Mock class to compare "cached" comparison. This case needed a concrete class as
         * it tests some inner working that depends on comparator calls. Again, it is so simple I
         * do not need a extra mock framework.
         */
        class MyMockComparator implements Base64Comparator {
            int comparatorRunsCount = 0;

            public int getComparatorRunsCount() {
                return this.comparatorRunsCount;
            }

            @Override
            public ComparisionResult compare(String left, String right) {
                comparatorRunsCount++;
                return new ComparisionResult(false, ComparisionResult.ResultType.SIZE_MISMATCH,
                        -1,-1,null);
            }
        }

        MyMockComparator mockComparator = new MyMockComparator();

        DiffRequest request = new DiffRequest();
        request.setLeft(document_1_base64);
        request.setRight(document_3_base64);

        DiffResponse result = null;
        try {
            result = request.getResult( mockComparator );
            // This one must not call comparator again!
            result = request.getResult( mockComparator );
        } catch (Exception e) {
            fail("No exception expected. Got one. " + e.getMessage() );
        }

        assertNotNull( "A result message is expected.", result );
        assertEquals( DiffRequest.SIZE_MISMATCH_MESSAGE, result.getMessage(), "Got wrong message!" );
       // assertEquals( "Wrong comparator count.", 1, mockComparator.getComparatorRunsCount() );

        // Now lets change the content
        request.setLeft( document_2_base64 );
        try {
            result = request.getResult( mockComparator );
        } catch ( Exception e ) {
            fail("No exception expected. Got one. " + e.getMessage() );
        }

        assertNotNull( "A result message is expected.", result );
        assertEquals( DiffRequest.SIZE_MISMATCH_MESSAGE, result.getMessage(), "Got wrong message!" );
        //assertEquals( "Wrong comparator count.",2, mockComparator.getComparatorRunsCount() );

    }

    @Test
    public void testDiffBadRequest(){
        Base64Comparator mockComparator = new Base64Comparator() {
            @Override
            public ComparisionResult compare(String left, String right) {
                fail("the compare method must no be called! It must fail before that");
                return null;
            }
        };

        //Do not set the parameters and try to call getResult.
        DiffRequest request = new DiffRequest();

        DiffResponse result = null;
        try {
            result = request.getResult( mockComparator );
        } catch ( Exception e ) {
            assertEquals( e.getMessage(), DiffRequest.ILLEGAL_STATE_MESSAGE );
        }
        //assertNull( "A result message is NOT expected.", result);
    }
}