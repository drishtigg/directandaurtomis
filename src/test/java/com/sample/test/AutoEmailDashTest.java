package com.sample.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AutoEmailDashTest {

    // Method to send requests with custom form parameters
    public Response sendRequestWithCustomFormParams(Map<String, Object> formParams) {
        // Set the base URI
        RestAssured.baseURI = "https://containersqa.verdis.ai:8801";

        // Prepare the headers and send the request
        return given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en-US,en;q=0.9")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "x9218q34ghq0itv2ii=s%3Au4vS8RQZKGU0nbFkgSzedOfRmWTXU4iz.ce%2FdhKMqyZNcFmjdl%2F6O1GxPcCZ0uZxx%2F2jTMgZrRiw; _dd_s=logs=1&id=bbd4a493-eddc-4432-9778-148e11405ab0&created=1731930206859&expire=1731931468633")
                .header("origin", "https://containersqa.verdis.ai:8801")
                .header("priority", "u=1, i")
                .header("referer", "https://containersqa.verdis.ai:8801/")
                .header("sec-ch-ua", "\"Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"Windows\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                .formParams("at", "eyJhbGciOiJIUzI1NiJ9.OGE0ZjcyZWMtOWViNC00ZmNiLWFjZTEtNDI4MzdiMjFiZmUx.Rj_aF9iMYKxsTxsfJ1sAOmCQQg0zsXUduIQsdAr8H_Y")
                .formParams("rt", "eyJhbGciOiJIUzI1NiJ9.MjI4MDA3ZmUtODgxOC00NmI0LWJmYWUtNTU3NDBlZDJiOTk3.1yPUvB0aa5F6Y1w0-Wmsj25pVTm5eBy4P5kYVERhgCM")
                .formParams(formParams) // Add dynamic form parameters
                .when()
                .post("/autoemaildash");
    }

    // Method to send GET request to /getIPData every 10 minutes to maintain the session
    public void maintainSession() {
        // Define the scheduled task to hit /getIPData every 10 minutes
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            // Prepare the headers and token
            Response getIPResponse = given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en-US,en;q=0.9")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "x9218q34ghq0itv2ii=s%3Au4vS8RQZKGU0nbFkgSzedOfRmWTXU4iz.ce%2FdhKMqyZNcFmjdl%2F6O1GxPcCZ0uZxx%2F2jTMgZrRiw; _dd_s=logs=1&id=bbd4a493-eddc-4432-9778-148e11405ab0&created=1731930206859&expire=1731932927824")
                .header("origin", "https://containersqa.verdis.ai:8801")
                .header("priority", "u=1, i")
                .header("referer", "https://containersqa.verdis.ai:8801/")
                .header("sec-ch-ua", "\"Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"Windows\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36")
                .formParams("at", "eyJhbGciOiJIUzI1NiJ9.OGE0ZjcyZWMtOWViNC00ZmNiLWFjZTEtNDI4MzdiMjFiZmUx.Rj_aF9iMYKxsTxsfJ1sAOmCQQg0zsXUduIQsdAr8H_Y")
                .formParams("rt", "eyJhbGciOiJIUzI1NiJ9.MjI4MDA3ZmUtODgxOC00NmI0LWJmYWUtNTU3NDBlZDJiOTk3.1yPUvB0aa5F6Y1w0-Wmsj25pVTm5eBy4P5kYVERhgCM")
                .when()
                .post("/getIPData");

            // Optionally, log or assert the response if necessary
            System.out.println("Get IP Data Response: " + getIPResponse.asString());
//        }, 0, 10, TimeUnit.MINUTES); // Initial delay of 0, then repeat every 10 minutes
    }

    @Test
    public void testFetchAddUpdateDeleteAutoMail() {
        // Start maintaining the session by calling getIPData every 10 minutes
    	Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
        maintainSession();
    	}, 0, 10, TimeUnit.MINUTES);
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

        // Add: Prepare custom form parameters for 'addAutomail'
        Map<String, Object> addParams = new HashMap<>();
        addParams.put("reqtype", "autoemaildash");
        addParams.put("optype", "addAutomail");
        addParams.put("misId", "38a48a61-df22-4522-a34b-4437d4eba88d");
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

        // Update: Prepare the update params based on current misName
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("reqtype", "autoemaildash");
        updateParams.put("optype", "updateAutomail");
        //updateParams.put("misId", "38a48a61-df22-4522-a34b-4437d4eba88d");
        updateParams.put("email", "i-drishti@verdis.ai");
        updateParams.put("username", "i-drishti@verdis.ai");

        // Dynamically update misName based on currentMisName
        if ("test18".equals(currentMisName)) {
            updateParams.put("misName", "test19");
            updateParams.put("misDescription", "test19");
        } else if ("test19".equals(currentMisName)) {
            updateParams.put("misName", "test18");
            updateParams.put("misDescription", "test18");
        } else {
            // Handle case if misName is neither test18 nor test19
            System.out.println("Unexpected misName: " + currentMisName);
            return; // Exit if the misName is not test18 or test19
        }

        // Send request for updateAutomail
        Response updateResponse = sendRequestWithCustomFormParams(updateParams);
        System.out.println("Update Response: " + updateResponse.asString());
        assertEquals(updateResponse.getStatusCode(), 200, "Expected status code 200");

        // Delete: Prepare custom form parameters for 'deleteAutomail'
        Map<String, Object> customFormParamsDelete = new HashMap<>();
        customFormParamsDelete.put("reqtype", "autoemaildash");
        customFormParamsDelete.put("optype", "deleteAutomail");
       // customFormParamsDelete.put("misId", "38a48a61-df22-4522-a34b-4437d4eba88d");
        customFormParamsDelete.put("email", "i-drishti@verdis.ai");
        customFormParamsDelete.put("username", "i-drishti@verdis.ai");

        // Send request for deleteAutomail
        Response responseDelete = sendRequestWithCustomFormParams(customFormParamsDelete);
        System.out.println("Delete Response: " + responseDelete.asString());
        assertEquals(responseDelete.getStatusCode(), 200, "Expected status code 200");
    }
}
