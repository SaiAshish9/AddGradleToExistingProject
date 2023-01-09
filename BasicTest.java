package com.tekion.ro;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BasicTest {

    public static JSONObject readJSON(){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader("src/test/resources/request/test.json"));
            jsonObject = (JSONObject) obj;
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @BeforeMethod
    public void setUp(){
        readJSON();
        baseURI = "https://reqres.in/api";
        Response resp = null;
        resp = given()
                .get("/users?page=2");
        System.out.println(resp.asString());
    }

    @Test
    public void test1(){
        baseURI = "https://reqres.in/api";
        String request = readJSON().toJSONString();
        given().
                header("Content-Type", "application/json").
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(request).
                when().
                post("/users").
                then().
                statusCode(201).
                assertThat().body(matchesJsonSchemaInClasspath("response/test.json")).
                log().all();
    }

}