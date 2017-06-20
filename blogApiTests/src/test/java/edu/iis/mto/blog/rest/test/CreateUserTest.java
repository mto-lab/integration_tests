package edu.iis.mto.blog.rest.test;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ResponseBody;

public class CreateUserTest extends FunctionalTests {

	@Test
	public void postFormWithMalformedRequestDataReturnsBadRequest() {
		JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(jsonObj.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
				.post("/blog/user");
	}

	@Test
	public void createUserShouldHaveUniqueEmailAddress() {
		String email = nextSessionId()+"@test.com";
		JSONObject firstUser = new JSONObject().put("email", email).put("firstname", "Test_1")
				.put("secondname", "");
		JSONObject secondUser = new JSONObject().put("email", email).put("firstname", "Test_2")
				.put("secondname", "");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
		.body(firstUser.toString()).expect().log().all().statusCode(HttpStatus.SC_CREATED).when()
		.post("/blog/user");
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
				.body(secondUser.toString()).expect().log().all().statusCode(HttpStatus.SC_CONFLICT).when()
				.post("/blog/user");
	}

}
