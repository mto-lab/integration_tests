package edu.iis.mto.blog.rest.test;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreatePostTest extends FunctionalTests {
	
	@Test
    public void confirmedUserShouldCreatePost() {
		
		
		JSONObject first = new JSONObject().put("entry", "Test Post"+nextSessionId());	
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(first.toString()).expect().log().all().statusCode(201).when()
                .post("/blog/user/2/post");

    }

}
