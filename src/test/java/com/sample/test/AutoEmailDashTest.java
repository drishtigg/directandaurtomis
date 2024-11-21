package com.sample.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

public class AutoEmailDashTest {

    private String atToken;
    private String rtToken;
    private String misId;  // Store the misId dynamically

    // Set base URI for API
    static {
        RestAssured.baseURI = "https://containersqa.verdis.ai:8801";
    }

    // Perform login once and save the tokens for the session
    @BeforeClass
    public void login() {
        // Send POST request to the /login endpoint
        Response response = given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en-US,en;q=0.9")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("origin", "https://containersqa.verdis.ai:8801")
                .header("referer", "https://containersqa.verdis.ai:8801/")
                .formParam("username", "i-drishti@verdis.ai")
                .formParam("password", "Verdis@2044")
                .when()
                .post("/login");

        // Debugging: Log the response for debugging
        System.out.println("Login Response: ");
        response.prettyPrint();

        // Assertions to validate login response
        Assert.assertEquals(response.getStatusCode(), 200, "Login failed! Status code mismatch.");
        Assert.assertTrue(response.jsonPath().getBoolean("status"), "Login was not successful!");

        // Extract tokens
        this.atToken = response.jsonPath().getString("at");
        this.rtToken = response.jsonPath().getString("rt");

        // Debugging: Log extracted tokens
        System.out.println("Access Token (AT): " + this.atToken);
        System.out.println("Refresh Token (RT): " + this.rtToken);
    }

    // Method to send requests with dynamic form parameters
    private Response sendRequestWithCustomFormParams(Map<String, Object> formParams) {
        return given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en-US,en;q=0.9")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "x9218q34ghq0itv2ii=s%3Af1rs_d-8IBj4_H3bGXHSEm-Y6gK_Gbwq.vjerdTVOt4YZAAu5H%2BDr%2BnDh4Y5DAkEOOnMsXev77XQ; _dd_s=logs=1&id=197c028e-7c0f-4cc8-bec8-c3813f9ff48a&created=1732123320904&expire=1732126340705")
                .header("origin", "https://containersqa.verdis.ai:8801")
                .header("referer", "https://containersqa.verdis.ai:8801/")
                .formParam("at", this.atToken) // Use dynamic Access Token (AT)
                .formParam("rt", this.rtToken) // Use dynamic Refresh Token (RT)
                .formParams(formParams) // Add dynamic form parameters
                .when()
                .post("/autoemaildash");
    }

    @Test
    public void testFetchAddUpdateDeleteAutoMail() {
        // Fetch: Prepare custom form parameters for 'fetchAutomail'
        Map<String, Object> customFormParamsFetch = new HashMap<>();
        customFormParamsFetch.put("reqtype", "autoemaildash");
        customFormParamsFetch.put("optype", "fetchAutomail");
        customFormParamsFetch.put("username", "i-drishti@verdis.ai");

        // Send request for fetchAutomail
        Response responseFetch = sendRequestWithCustomFormParams(customFormParamsFetch);
        System.out.println("Fetch Response: " + responseFetch.asString());
        assertEquals(responseFetch.getStatusCode(), 200, "Expected status code 200");

        // Extract the 'misName' from the fetch response
        String currentMisName = responseFetch.jsonPath().getString("misName");
        String fetchedMisId = responseFetch.jsonPath().getString("misId");  // Store the misId for later

        if (fetchedMisId != null && !fetchedMisId.isEmpty()) {
            this.misId = fetchedMisId;  // Store the misId for future operations
        }

        // Add: Prepare custom form parameters for 'addAutomail'
        Map<String, Object> addParams = new HashMap<>();
        addParams.put("reqtype", "autoemaildash");
        addParams.put("optype", "addAutomail");
        addParams.put("misId", this.misId); // Use dynamically fetched misId
        addParams.put("email", "i-drishti@verdis.ai");
        addParams.put("username", "i-drishti@verdis.ai");
        addParams.put("misName", "test18");
        addParams.put("misDescription", "test18");
        addParams.put("schedulerProperties[0][type]", "custom");
        addParams.put("schedulerProperties[0][on]", "18");
        addParams.put("schedulerProperties[0][at]", "16:40 - 16:45");
        addParams.put("emailProperties[to][]", "i-drishti@verdis.ai");
        addParams.put("emailProperties[subject]", "test18");
        addParams.put("emailProperties[mailContent]", "test");

        // Send request for addAutomail
        Response addResponse = sendRequestWithCustomFormParams(addParams);
        System.out.println("Add Response: " + addResponse.asString());
        assertEquals(addResponse.getStatusCode(), 200, "Expected status code 200");

        // Dynamically prepare the updateParams based on added misName
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("reqtype", "autoemaildash");
        updateParams.put("optype", "updateAutomail");
        updateParams.put("misId", this.misId); // Use the dynamic misId for update
        updateParams.put("misName", "test19");
        updateParams.put("misDescription", "test19");

        // Send request for updateAutomail
        Response updateResponse = sendRequestWithCustomFormParams(updateParams);
        System.out.println("Update Response: " + updateResponse.asString());
        assertEquals(updateResponse.getStatusCode(), 200, "Expected status code 200");

        // Prepare custom form parameters for 'deleteAutomail'
        Map<String, Object> customFormParamsDelete = new HashMap<>();
        customFormParamsDelete.put("reqtype", "autoemaildash");
        customFormParamsDelete.put("optype", "deleteAutomail");
        customFormParamsDelete.put("misId", this.misId);  // Use the dynamic misId for delete
        customFormParamsDelete.put("email", "i-drishti@verdis.ai");
        customFormParamsDelete.put("username", "i-drishti@verdis.ai");

        // Send request for deleteAutomail
        Response responseDelete = sendRequestWithCustomFormParams(customFormParamsDelete);
        System.out.println("Delete Response: " + responseDelete.asString());
        assertEquals(responseDelete.getStatusCode(), 200, "Expected status code 200");
    }
}
