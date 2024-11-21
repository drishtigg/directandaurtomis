package com.sample.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LoginAPITest {

    static {
        // Setting the base URI for the API
        RestAssured.baseURI = "https://containersqa.verdis.ai:8801";
    }

    @Test
    public void testDirectLogin() {
        // Debugging: Log request information
        System.out.println("Sending direct login request...");

        // Send POST request to the /login endpoint
        Response response = given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en-US,en;q=0.9")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8") // Ensure correct charset
                .header("origin", "https://containersqa.verdis.ai:8801")
                .header("referer", "https://containersqa.verdis.ai:8801/")
                .formParam("username", "i-drishti@verdis.ai") // Ensure these are valid credentials
                .formParam("password", "Verdis@2044") // Ensure these are valid credentials
                .when()
                .post("/login");

        // Log response for debugging
        System.out.println("Response for Direct Login: ");
        response.prettyPrint();

        // Assertions to validate the login response
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Direct Login failed! Status code mismatch.");

        // Check if the "status" field in the response is true
        boolean loginStatus = response.jsonPath().getBoolean("status");
        Assert.assertTrue(loginStatus, "Login was not successful!");
       
        // Extract the 'at' and 'rt' tokens from the response
        String atToken = response.jsonPath().getString("at");  // Extract 'at' token
        String rtToken = response.jsonPath().getString("rt");  // Extract 'rt' token

        // Print both tokens for debugging
        System.out.println("AT Token: " + atToken);
        System.out.println("RT Token: " + rtToken);

        System.out.println("Direct Login successful!");
    }
}
