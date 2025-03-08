package com.jurkiewicz.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class GithubResourceTest {

    private static final String BASE_PATH = "/github";
    private static final String EXISTING_USER = "torvalds";
    private static final String TEST_USER_WITH_FORKS = "torvalds";
    private static final String NON_EXISTING_USER = "testFakeUser";
    private static final String INVALID_USER = "invalid__user";

    @Test
    public void testGetUserRepositories_Success() {
        given()
                .when()
                .get(BASE_PATH + "/" + EXISTING_USER)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0))
                .body("repositoryName", everyItem(not(emptyOrNullString())))
                .body("ownerLogin", everyItem(equalTo(EXISTING_USER)))
                .body("branches", everyItem(not(empty())));
    }

    @Test
    public void testGetUserRepositories_UserNotFound() {
        given()
                .when()
                .get(BASE_PATH + "/" + NON_EXISTING_USER)
                .then()
                .log().all()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body("status", equalTo(404))
                .body("message", containsString("not found"));
    }

    @Test
    public void testGetUserRepositories_InvalidUsername() {
        given()
                .when()
                .get(BASE_PATH + "/" + INVALID_USER)
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("status", equalTo(400))
                .body("message", containsString("invalid characters"));
    }

    @Test
    public void testGetUserRepositories_IgnoreForks() {
        given()
                .when()
                .get(BASE_PATH + "/" + TEST_USER_WITH_FORKS)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0))
                .body("repositoryName", everyItem(not(emptyOrNullString())))
                .body("ownerLogin", everyItem(equalTo(TEST_USER_WITH_FORKS)))
                .body("branches", everyItem(not(empty())))
                .body("repositoryName", everyItem(not(containsString("fork"))));
    }


}
