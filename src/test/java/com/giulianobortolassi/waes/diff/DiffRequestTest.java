package com.giulianobortolassi.waes.diff;

import com.giulianobortolassi.waes.comparator.Base64Comparator;
import com.giulianobortolassi.waes.comparator.ComparasionResult;

import org.junit.Test;

import static org.junit.Assert.*;

public class DiffRequestTest {

    private static String document_1_base64 = "Some base64 data... it is not relevant fot this unit test";
    private static String document_2_base64 = "Some base64 data... it is not relevant fot that unit test";
    private static String document_3_base64 = "Some base64 data... ";


    @Test
    public void testDiffMatchRequest(){
        /* Creating a mock class to isolate the tests. Comparator will have a separated test to
         * check it inner working.
         * As this test is pretty simple, I decided to no use an Mock framework like Mockito or
         * other ones. If it get complex, we can replace the inner class with an Mockito proxy.
         */
        class MyMockComparator extends Base64Comparator {
            @Override
            public ComparasionResult compare(String left, String right) {
                return new ComparasionResult(true, ComparasionResult.ResultType.MATCH,-1,-1,null);
            }
        }

        DiffRequest request = new DiffRequest();
        request.setLeft(document_1_base64);
        request.setRight(document_1_base64);

        DiffResponse result = null;
        try {
            result = request.getResult(new MyMockComparator());
        } catch (Exception e) {
            fail("No exception expected. Got one. " + e.getMessage() );
        }
        assertNotNull("A result message is expected.",result);
        assertEquals("Equal documents must match!", DiffRequest.MATCH_MESSAGE, result.getMessage() );
    }

    @Test
    public void testDiffDoesNotMatchRequest(){
        class MyMockComparator extends Base64Comparator {
            @Override
            public ComparasionResult compare(String left, String right) {
                return new ComparasionResult(false, ComparasionResult.ResultType.CONTENT_MISMATCH,
                        10,20,new char[document_1_base64.length()]);
            }
        }

        MyMockComparator comparator = new MyMockComparator();

        DiffRequest request = new DiffRequest();
        request.setLeft(document_1_base64);
        request.setRight(document_2_base64);

        DiffResponse result = null;

        try {
            result = request.getResult( comparator );
        } catch (Exception e) {
            fail("No exception expected. Got one. " + e.getMessage() );
        }
        assertNotNull("A result message is expected.", result );

        ComparasionResult compare = comparator.compare(document_1_base64, document_2_base64);
        String expectedMessage =
                String.format(DiffRequest.CONTENT_MISMATCH_MESSAGE,
                        compare.getMismatchOffset(),
                        compare.getMatchLength());

        assertEquals("Got wrong message!", expectedMessage ,result.getMessage());
    }

    @Test
    public void testDiffWrongSizeRequest(){
        class MyMockComparator extends Base64Comparator {
            @Override
            public ComparasionResult compare(String left, String right) {
                return new ComparasionResult(false, ComparasionResult.ResultType.SIZE_MISMATCH,
                        -1,-1,null);
            }
        }

        DiffRequest request = new DiffRequest();
        request.setLeft(document_1_base64);
        request.setRight(document_3_base64);

        DiffResponse result = null;
        try {
            result = request.getResult( new MyMockComparator() );
        } catch ( Exception e ) {
            fail("No exception expected. Got one. " + e.getMessage() );
        }
        assertNotNull("A result message is expected.",result);
        assertEquals("Got wrong message!", DiffRequest.SIZE_MISMATCH_MESSAGE, result.getMessage() );
    }

    @Test
    public void testNoRepeatProccessingIfDone(){

        /**
         *  Mock class to compare "cached" comparision
         */
        class MyMockComparator extends Base64Comparator {
            int comparatorRunsCount = 0;

            public int getComparatorRunsCount() {
                return this.comparatorRunsCount;
            }

            @Override
            public ComparasionResult compare(String left, String right) {
                comparatorRunsCount++;
                return super.compare(left, right);
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

        assertNotNull( "A result message is expected.",result);
        assertEquals( "Got wrong message!", DiffRequest.SIZE_MISMATCH_MESSAGE, result.getMessage() );
        assertEquals( "Wrong comparator count.",1, mockComparator.getComparatorRunsCount() );

        // Now lets change the content
        request.setLeft( document_2_base64 );
        try {
            result = request.getResult( mockComparator );
        } catch ( Exception e ) {
            fail("No exception expected. Got one. " + e.getMessage() );
        }

        assertNotNull( "A result message is expected.", result );
        assertEquals( "Got wrong message!", DiffRequest.SIZE_MISMATCH_MESSAGE, result.getMessage() );
        assertEquals( "Wrong comparator count.",2, mockComparator.getComparatorRunsCount() );

    }

    @Test
    public void testDiffBadRequest(){
        class MyMockComparator extends Base64Comparator {
            @Override
            public ComparasionResult compare(String left, String right) {
                fail("the compare method must no be called! It must fail before that");
                return null;
            }
        }

        //Do not set the parameters and try to call getResult.
        DiffRequest request = new DiffRequest();

        DiffResponse result = null;
        try {
            result = request.getResult( new MyMockComparator() );
        } catch ( Exception e ) {
            assertEquals( e.getMessage(), DiffRequest.ILLEGAL_STATE_MESSAGE );
        }
        assertNull( "A result message is NOT expected.", result);
    }
}