package todoapp.todo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import todoapp.todo.dto.Todo;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class TodoController {

    @Test
    void getTodos() {
        var result = RestAssured.given()
                .when()
                .get("http://localhost:8080/todos")
                .then()
                .statusCode(200).extract().body().asString();

        System.out.println(result);
    }

    @Test
    void postAndDeleteTodo() {
        var id = "111";
        var body = """
                {
                    \"id\": %s,
                    \"text\": \"Eat milk\",
                    \"completed\": false
                }
                """.formatted(id);

        RestAssured.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("http://localhost:8080/todos")
                .then()
                .statusCode(201);

        RestAssured.given()
                .get("http://localhost:8080/todos")
                .then()
                .statusCode(200).extract().body().asString();

        RestAssured.given()
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .delete("http://localhost:8080/todos/" + id)
                .then()
                .statusCode(204);

    }

    @Test
    void deleteAllTodos() throws JsonProcessingException {
        var tasks = RestAssured.given()
                .get("http://localhost:8080/todos")
                .then()
                .statusCode(200).extract().body().asString();
        ObjectMapper mapper = new ObjectMapper();

        List<Todo> todos = mapper.readValue(tasks, new TypeReference<>() {
        });

        List<String> ids = todos.stream().map(t -> t.getId()).toList();

        for (String id : ids) {
            RestAssured.given()
                    .auth()
                    .preemptive()
                    .basic("admin", "admin")
                    .when()
                    .delete("http://localhost:8080/todos/" + id)
                    .then()
                    .statusCode(204);
        }

        var tasksAgain = RestAssured.given()
                .get("http://localhost:8080/todos")
                .then()
                .statusCode(200).extract().body().asString();

        assertThat(tasksAgain).isEqualTo("[]");
    }

    @Test
    void updateTodo() {
        var id = "111";
        var body = """
                {
                    \"id\": %s,
                    \"text\": \"Eat milk\",
                    \"completed\": false
                }
                """.formatted(id);
        var updatedBody = """
                {
                    \"id\": %s,
                    \"text\": \"Eat milk\",
                    \"completed\": true
                }
                """.formatted(id);

        RestAssured.given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("http://localhost:8080/todos")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType("application/json")
                .body(updatedBody)
                .when()
                .put("http://localhost:8080/todos/" + id)
                .then()
                .statusCode(200);
    }
}
