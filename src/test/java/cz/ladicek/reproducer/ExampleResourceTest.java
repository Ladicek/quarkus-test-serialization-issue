package cz.ladicek.reproducer;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExampleResourceTest {
    @Test
    @Order(1)
    public void getAll() {
        when()
                .get("/example")
        .then()
                .statusCode(200)
                .body("", hasSize(2));
    }

    @Test
    @Order(2)
    public void get() {
        when()
                .get("/example/1")
        .then()
                .statusCode(200)
                .body("text", equalTo("Hello"));
    }

    @Test
    @Order(3)
    public void create() {
        Example example = new Example();
        example.text = "Salut";

        given()
        .when()
                .contentType(ContentType.JSON)
                .body(example)
                .post("/example")
        .then()
                .statusCode(201)
                .body("id", equalTo(3))
                .body("text", equalTo("Salut"));
    }

    @Test
    @Order(4)
    public void update() {
        Example example = new Example();
        example.id = 3L;
        example.text = "Hallo";

        given()
        .when()
                .contentType(ContentType.JSON)
                .body(example)
                .put("/example/3")
        .then()
                .statusCode(200)
                .body("id", equalTo(3))
                .body("text", equalTo("Hallo"));

        when()
                .get("/example/3")
                .then()
                .statusCode(200)
                .body("text", equalTo("Hallo"));
    }

    @Test
    @Order(5)
    public void delete() {
        when()
                .delete("/example/3")
        .then()
                .statusCode(204);

        when()
                .get("/example/3")
        .then()
                .statusCode(404);
    }
}
