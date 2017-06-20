package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class SearchUserTest extends FunctionalTests {

	@Test
	public void searchRemovedUserPostShouldNotFindAny() throws Exception {
		RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").expect()
				.log().all().statusCode(HttpStatus.SC_NOT_FOUND).when().get("/blog/user/4/post");
	}


	@Test
	     public void correctLikeNumber() throws Exception {
	         RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
	                 .expect().log().all().statusCode(HttpStatus.SC_OK).when()
	                .post("/blog/user/3/like/4");
	         int likes = RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
	                 .expect().log().all().statusCode(HttpStatus.SC_OK).when()
	                 .get("/blog/user/6/post").then().extract().jsonPath().getInt("likesCount[0]");
	                 System.out.println(likes);
	         MatcherAssert.assertThat(likes, Matchers.equalTo(1));
	     }
}
