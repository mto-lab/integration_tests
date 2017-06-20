package edu.iis.mto.blog.rest.test;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.junit.BeforeClass;

import com.jayway.restassured.RestAssured;

public class FunctionalTests {

    @BeforeClass
    public static void setup() {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = Integer.valueOf(8080);
        } else {
            RestAssured.port = Integer.valueOf(port);
        }

        String basePath = System.getProperty("server.base");
        if (basePath == null) {
            basePath = "";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;

    }
    protected String nextSessionId() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }
}
