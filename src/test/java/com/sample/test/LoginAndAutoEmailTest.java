package com.sample.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Timer;
import java.util.TimerTask;

import static io.restassured.RestAssured.given;

public class LoginAndAutoEmailTest {

    private final String baseURI = "https://containersqa.verdis.ai:8801";
    private String at="eyJhbGciOiJIUzI1NiJ9.ZDVlNWZkOTAtOWY2MC00NGJlLWEwYTAtM2NkNDZlMWFhOWI2.awzM-Mfa3DlXcbBLCMFKzdPgc4_eYF8KwWtac6AB1kM";
    private String rt="eyJhbGciOiJIUzI1NiJ9.YzFjOTBjZWUtMWI1Yi00OTJiLWJiNmItYTA5MTM4ZTlkMWRi.QZdd0nBhHurvCweegNlwZ-6x-erTHhlP83QfvYAMteI";
    private String cookie="x9218q34ghq0itv2ii=s%3Au4vS8RQZKGU0nbFkgSzedOfRmWTXU4iz.ce%2FdhKMqyZNcFmjdl%2F6O1GxPcCZ0uZxx%2F2jTMgZrRiw; _dd_s=logs=1&id=c9882fe4-64bc-4867-b5f6-57757bd74d76&created=1731919035642&expire=1731922380905";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://containersqa.verdis.ai:8801/";

        // Initial call to get session data
        refreshSession();

        // Schedule token refresh every 10 minutes
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshSession();
            }
        }, 10 * 60 * 1000, 10 * 60 * 1000); // 10 minutes in milliseconds
    }

    private void refreshSession() {
        Response response = given()
            .header("accept", "application/json, text/plain, */*")
            .header("accept-language", "en-US,en;q=0.9")
            .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
            .header("origin", baseURI)
            .header("referer", baseURI + "/")
            .formParam("username", "i-drishti@verdis.ai") // Ensure valid credentials
            .formParam("password", "Verdis@2044") // Ensure valid credentials
            .when()
            .post("/getIPData");

        logResponseDetails("refreshSession", response);

        Assert.assertEquals(response.getStatusCode(), 200, "Session refresh failed");

        // Update session tokens and cookie
        this.at = response.jsonPath().getString("at");
        this.rt = response.jsonPath().getString("rt");
        this.cookie = response.header("Set-Cookie"); // Update cookie if returned
    }

    @DataProvider(name = "operationTypes")
    public Object[][] operationTypes() {
        return new Object[][]{
//            {"deleteAutomail", "2531cf4e-d529-435f-8b6d-cf8490c23421", "null", "null"},
//            {"addAutomail", "null", "i-drishti@verdis.ai", "i-drishti@verdis.ai"},
//            {"updateAutomail", "2531cf4e-d529-435f-8b6d-cf8490c23421", "i-drishti@verdis.ai", "i-drishti@verdis.ai"},
            {"fetchAutomail", "null", "null", "i-drishti@verdis.ai"}
        };
    }

    @Test(dataProvider = "operationTypes")
    public void testAutoEmailDashOperations(String optype, String misId, String email, String username) {
        Response response = given()
            .header("Accept", "application/json, text/plain, */*")
            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            .header("Cookie", cookie)
            .formParam("at", at)
            .formParam("rt", rt)
            .formParam("reqtype", "autoemaildash")
            .formParam("optype", optype)
//            .formParam("mis_id", misId)
//            .formParam("email", email)
            .formParam("username", username)
            .log().all()
            .when()
            .post("/autoemaildash");

        logResponseDetails(optype, response);

        Assert.assertEquals(response.getStatusCode(), 200, "HTTP Status code mismatch for " + optype);

        // Validate response
        Assert.assertTrue(response.jsonPath().getBoolean("status"), optype + " operation failed");
    }

    private void logResponseDetails(String operation, Response response) {
        System.out.println("===== " + operation + " Response Details =====");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asPrettyString());
    }
}
