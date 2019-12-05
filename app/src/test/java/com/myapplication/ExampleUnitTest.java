package com.myapplication;

import org.junit.Test;
import static org.junit.Assert.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void response_isReturned() {
        RequestSpecification httpRequest = RestAssured.given();
        //RestAssured.baseURI = "http://api.acme.international/fortune";
        Response response = httpRequest.get("http://api.acme.international/fortune");

        assertEquals(200, response.getStatusCode());
    }


    @Test
    public void response_hasBody() {
        RequestSpecification httpRequest = RestAssured.given();
       // RestAssured.baseURI = "http://api.acme.international/fortune";
        httpRequest.header("Content-Type", "application/json");
        Response response = httpRequest.get("http://api.acme.international/fortune");
        // Get Response Body
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        // Check if response body has a string
        assertTrue(bodyStringValue.contains("fortune"));


    }

  /*  @Test
    public void response_timeIsCorrect() {
        RequestSpecification httpRequest = RestAssured.given();
        RestAssured.baseURI = "http://api.acme.international/fortune";
        Response response = httpRequest.get();


        assertEquals(10000, response.time());
    }*/
}
