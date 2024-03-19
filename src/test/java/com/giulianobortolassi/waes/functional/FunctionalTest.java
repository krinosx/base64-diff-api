package com.giulianobortolassi.waes.functional;

import com.giulianobortolassi.waes.diff.DiffRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class FunctionalTest {


    @Autowired
    private MockMvc server;

    /*
     * Some black box testing
     */

    @Test
    public void testRightSide() throws Exception {
        int TEST_ID = 1;
        server.perform( put("/v1/diff/{id}/right", TEST_ID).content("Simple Document") )
                .andExpect(status().isOk());

    }

    @Test
    public void testLeftSide() throws Exception {
        int TEST_ID = 2;
        server.perform( put("/v1/diff/{id}/left", TEST_ID).content("Simple Document") )
                .andExpect(status().isOk());
    }


    /*
     * Test the Match cases
     */
    @Test
    public void testMatchResult() throws Exception {
        int TEST_ID = 5;

        server.perform( put("/v1/diff/{id}/left", TEST_ID).content("Simple Document") )
                .andExpect(status().isOk());

        server.perform( put("/v1/diff/{id}/right", TEST_ID).content("Simple Document") )
                .andExpect(status().isOk());

        server.perform( get("/v1/diff/{id}", TEST_ID) )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.message" ).value( DiffRequest.MATCH_MESSAGE ) )
                .andExpect( jsonPath("$.comparisionStatus" ).value( "MATCH" ) );

    }

    @Test
    public void testSizeMismatchResult() throws Exception {
        int TEST_ID = 6;
        server.perform( put("/v1/diff/{id}/left", TEST_ID).content("Simple Document") )
                .andExpect(status().isOk());

        server.perform( put("/v1/diff/{id}/right", TEST_ID).content("Simple Document 123") )
                .andExpect(status().isOk());

        server.perform( get("/v1/diff/{id}", TEST_ID) )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.comparisionStatus").value("SIZE_MISMATCH"))
                .andExpect(jsonPath("$.message").value(DiffRequest.SIZE_MISMATCH_MESSAGE));
    }

    @Test
    public void testContentMismatchResult() throws Exception {
        int TEST_ID = 7;
        server.perform( put("/v1/diff/{id}/left", TEST_ID).content("Simple Document") )
                .andExpect(status().isOk());

        server.perform( put("/v1/diff/{id}/right", TEST_ID).content("Sampra Document") )
                .andExpect(status().isOk());

        server.perform( get("/v1/diff/{id}", TEST_ID) )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.comparisionStatus").value("CONTENT_MISMATCH"))
                .andExpect(content().string(containsString("Content does not match. Difference start:")));
    }

    /*
     * Error scenarios
     */
    @Test
    public void testNoContentLeftSide() throws Exception {
        int TEST_ID = 3;
        server.perform( put("/v1/diff/{id}/left", TEST_ID) )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.comparisionStatus").value("ERROR"))
                .andExpect(jsonPath("$.message").value("id and body data are mandatory"));
    }

    @Test
    public void testNoContentRightSide() throws Exception {
        int TEST_ID = 4;
        server.perform( put("/v1/diff/{id}/right", TEST_ID) )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.comparisionStatus").value("ERROR"))
                .andExpect(jsonPath("$.message").value("id and body data are mandatory"));
    }


    @Test
    public void testBadRequestExceptionResult() throws Exception {
        int TEST_ID = 8;
        server.perform( put("/v1/diff/{id}/left", TEST_ID).content("Simple Document") )
                .andExpect(status().isOk());


        server.perform( get("/v1/diff/{id}", TEST_ID) )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.comparisionStatus").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Request is not ready to be processed."));

    }

    /*
     * Call the GET without setting right or left
     */
    @Test
    public void testNotFoundExceptionResult() throws Exception {
        int TEST_ID = 9;
        server.perform( get("/v1/diff/{id}", TEST_ID) )
                .andExpect(status().isNotFound() )
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.comparisionStatus").value("ERROR"))
                .andExpect(jsonPath("$.message").value("No request found with given ID"));
    }
}