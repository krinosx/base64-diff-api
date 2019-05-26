package com.giulianobortolassi.waes.comparator;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Base64;

public class Base64ComparatorTest {

    @BeforeClass
    public static void initData(){
        document_1_base64 = Base64.getEncoder().encodeToString(document_1.getBytes());
        document_2_base64 = Base64.getEncoder().encodeToString(document_2.getBytes());
        document_3_base64 = Base64.getEncoder().encodeToString(document_3.getBytes());
    }

    Base64Comparator comparator = new DefaultBase64Comparator();

    @Test
    public void documentsMatchTest(){

        ComparisionResult result = comparator.compare(document_1_base64, document_1_base64);

        assertNotNull("Result is expected to not be null.", result );
        assertTrue("Result is expected to be a match.", result.isMatch() );
        assertEquals("", result.getResultType(), ComparisionResult.ResultType.MATCH );
        assertEquals( -1, result.getMismatchOffset() );
        assertEquals(0, result.getMatchLength());
        assertNull(result.getMismatchChars());
    }

    /*
     * Same size, not same content.
     */
    @Test
    public void documentsMismatchTest(){
        ComparisionResult result = comparator.compare(document_1_base64, document_2_base64);

        assertNotNull("Result is expected to not be null.", result );
        assertTrue("Result is expected to be a mismatch.", !result.isMatch() );
        assertEquals("", ComparisionResult.ResultType.CONTENT_MISMATCH, result.getResultType());
        assertEquals( 92, result.getMismatchOffset() );
        assertEquals(3, result.getMatchLength());
        assertNotNull(result.getMismatchChars());
    }

    /*
     * Divergent sizes
     */
    @Test
    public void documentsSizeMismatchTest(){
        ComparisionResult result = comparator.compare(document_1_base64, document_3_base64);

        assertNotNull("Result is expected to not be null.", result );
        assertTrue("Result is expected to be a mismatch.", !result.isMatch() );
        assertEquals("", ComparisionResult.ResultType.SIZE_MISMATCH, result.getResultType());
        assertEquals( -1, result.getMismatchOffset() );
        assertEquals(0, result.getMatchLength());
        assertNull(result.getMismatchChars());
    }


    private static String document_1_base64;
    private static String document_2_base64;
    private static String document_3_base64;

    private static final String document_1 =
            "{ 'document': { "+
                    "   'title': 'My Json Document', "+
                    "   'author': 'giuliano.bortolassi@gmail.com',"+
                    "   'summary': 'This document is a test to compare data in base64',"+
                    "   'content': 'This is a simple content, i will fill it with LoreIpsum LoreIpsum LoreIpsum LoreIpsum LoreIpsum ',"+
                    "   'created':  '2019-05-24'"+
                    "}";

    // Just two character difference in JSON.
    private static final String document_2 =
            "{ 'document': { "+
                    "   'title': 'My Json Document', "+
                    "   'author': 'giuliana.bortolassi@gmail.com',"+
                    "   'summary': 'This document is a test to compare data in base64',"+
                    "   'content': 'This is a simple content, i will fill it with LoreIpsum LoreIpsum LoreIpsum LoreIpsum LoreIpsum ',"+
                    "   'created':  '2019-05-25'"+
                    "}";


    private static final String document_3 =
            "{ 'document': { "+
                    "   'title': 'My Json Document', "+
                    "   'author': 'giulianobortolassi@gmail.com',"+
                    "   'summary': 'This document has no title',"+
                    "   'content': 'This is a simple content',"+
                    "   'created':  '2019-05-24'"+
                    "}";
}
