package com.portal.user;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

@QuarkusTest
class UserResourceTest {

    @Test
    @TestTransaction
    void testSignupAndDuplicate() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"restuser@example.com\",\"password\":\"RestPass123!\"}")
        .when()
            .post("/api/signup")
        .then()
            .statusCode(200)
            .body("message", containsString("Signup OK"));

        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"restusedr@example.com\",\"password\":\"RestPass123!\"}")
        .when()
            .post("/api/signup")
        .then()
            .statusCode(409)
            .body("message", containsString("Email already registered"));
    }

    @Test
    @TestTransaction
    void testLoginFail() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"nouser@example.com\",\"password\":\"badpass\"}")
        .when()
            .post("/api/login")
        .then()
            .statusCode(401)
            .body("message", containsString("Invalid email or password"));
    }
}
