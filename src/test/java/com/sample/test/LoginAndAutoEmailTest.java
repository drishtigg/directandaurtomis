package com.sample.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LoginAndAutoEmailTest {

    private final String baseURI = "https://containersqa.verdis.ai:8801";
    private final String at = "eyJhbGciOiJIUzI1NiJ9.NGIxZGE0YzktM2E2Ny00NjhjLTgwNDgtN2Q1MTY1N2YwNDA4.AlrQHY7kjhpoLHu7dOp7znDg4HMR7Y26scYhgPZk5hk";
    private final String rt = "eyJhbGciOiJIUzI1NiJ9.ZDViZjg2ZjgtMWZmNy00MmJjLTg0Y2EtNzc5MmY5YjBmMmQ1.Oe4UXkYLvjFVf10xlWiGfCuh4tEg5H9TcyF8ce-KBa4";
    private final String cookie = "x9218q34ghq0itv2ii=s%3AGoyF4iS2SxFv3PJ5EMKdVnS8HqgBaPVq.itGoJm2WyJb3su9oVCg29mz2Hhuu1RFxkPY9qWhzKkw; _dd_s=logs=1&id=96834e07-b24b-4f9e-8b69-b0b8f1a606f7&created=1731768665577&expire=1731769674848";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseURI;
    }

    // Data provider to feed different optypes
    @DataProvider(name = "operationTypes")
    public Object[][] operationTypes() {
        return new Object[][]{
            {"deleteAutomail", "e9a4535d-a0a9-49de-9c20-2361c50f970b", "null", "null"},
            {"addAutomail", "null", "new-email@verdis.ai", "i-drishti@verdis.ai"},
            {"updateAutomail", "e9a4535d-a0a9-49de-9c20-2361c50f970b", "updated-email@verdis.ai", "i-drishti@verdis.ai"},
            {"fetchAutomail", "null", "null", "i-drishti@verdis.ai"}
        };
    }

    @Test(dataProvider = "operationTypes")
    public void testAutoEmailDashOperations(String optype, String misId, String email, String username) {
        // Build the request dynamically based on optype
        Response response = given()
            .header("Accept", "application/json, text/plain, */*")
            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            .header("Cookie", cookie)
            .formParam("at", at)
            .formParam("rt", rt)
            .formParam("reqtype", "autoemaildash")
            .formParam("optype", optype)
            .formParam("mis_id", misId)
            .formParam("email", email)
            .formParam("username", username)
            .log().all()
            .when()
            .post("/autoemaildash");

        logResponseDetails(optype, response);

        Assert.assertEquals(response.getStatusCode(), 200, "HTTP Status code mismatch for " + optype);

        // Perform checks based on the optype
        if ("deleteAutomail".equals(optype) || "updateAutomail".equals(optype) || "fetchAutomail".equals(optype)) {
            Assert.assertTrue(response.jsonPath().getBoolean("status"), optype + " operation failed");
        } else if ("addAutomail".equals(optype)) {
            Assert.assertTrue(response.jsonPath().getBoolean("status"), "Add operation failed for " + optype);
        }
    }

    /**
     * Logs details of the response for better debugging
     *
     * @param operation The operation being tested
     * @param response  The response object from the API call
     */
    private void logResponseDetails(String operation, Response response) {
        System.out.println("===== " + operation + " Response Details =====");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asPrettyString());

        if (response.getStatusCode() != 200) {
            System.out.println("Error: Unexpected HTTP Status Code for " + operation);
        } else if (!response.jsonPath().getBoolean("status")) {
            System.out.println("Error: " + response.jsonPath().getString("errorMessage"));
        }
    }
}
