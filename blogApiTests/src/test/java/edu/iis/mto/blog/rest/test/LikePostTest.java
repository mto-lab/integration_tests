package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class LikePostTest extends FunctionalTests {

	@Test
	public void confirmedUserShouldLikePost() {

		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
				.log().all().statusCode(HttpStatus.SC_OK).when().post("/blog/user/2/like/1");
	}

	@Test
	public void userShouldNotLikeOwnPost() {

		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
				.log().all().statusCode(HttpStatus.SC_BAD_REQUEST).when().post("/blog/user/1/like/1");

	}

	@Test
	public void likeSamePostShouldNotChangeStatus() {
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
				.log().all().statusCode(HttpStatus.SC_OK).body(Matchers.equalTo("false")).when().post("/blog/user/2/like/2");
	}
}
