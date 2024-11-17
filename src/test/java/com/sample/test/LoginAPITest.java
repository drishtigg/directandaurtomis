package com.sample.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

import java.util.Scanner;

public class LoginAPITest {

    static {
        RestAssured.baseURI = "https://containersqa.verdis.ai:8801";
    }

    @Test
    public void testDirectLogin() {
        // Debugging: Log request information
        System.out.println("Sending direct login request...");

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

        // Assertions
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Direct Login failed! Status code mismatch.");
        System.out.println("Direct Login successful!");
    }

   
}
