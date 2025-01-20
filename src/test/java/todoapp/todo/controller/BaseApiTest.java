package todoapp.todo.controller;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {
    private final static String BASE_URL = "http://localhost:8080";
    private final static String TODO_ENDPOINT = "/todos";
    private final static String TODO_URL = BASE_URL + TODO_ENDPOINT;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.basePath = TODO_ENDPOINT;
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }
}
