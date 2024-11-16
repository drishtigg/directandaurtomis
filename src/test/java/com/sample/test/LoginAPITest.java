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

    @Test
    public void testLoginWithOTP() {
        // Step 1: Direct login
        Response loginResponse = given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en-US,en;q=0.9")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8") // Ensure correct charset
                .header("origin", "https://containersqa.verdis.ai:8801")
                .header("referer", "https://containersqa.verdis.ai:8801/")
                .formParam("username", "kamiya@verdis.ai") // Ensure these are valid credentials
                .formParam("password", "Verdis@2040") // Ensure these are valid credentials
                .when()
                .post("/login");

        // Log and verify the login response
        System.out.println("Login Response: ");
        loginResponse.prettyPrint();

        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login API failed!");
        System.out.println("Login successful. Waiting for OTP...");

        // Step 2: Manually enter the OTP sent via SMS
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the OTP sent to your SMS: ");
        String otp = scanner.nextLine();

        // Step 3: Login2FA API to complete the login process
        Response login2faResponse = given()
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en-US,en;q=0.9")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8") // Ensure correct charset
                .header("origin", "https://containersqa.verdis.ai:8801")
                .header("referer", "https://containersqa.verdis.ai:8801/")
                .formParam("smsotp", otp)
                .when()
                .post("/login2fa");

        // Log and verify the 2FA login response
        System.out.println("Login2FA Response: ");
        login2faResponse.prettyPrint();

        Assert.assertEquals(login2faResponse.getStatusCode(), 200, "Login2FA API failed!");
        System.out.println("2FA Login successful!");
    }
}
