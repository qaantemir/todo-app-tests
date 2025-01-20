package todoapp.todo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todoapp.generators.TestDataGenerator;
import todoapp.models.Todo;
import todoapp.requests.TodoService;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



public class TodoControllerTest extends BaseApiTest {

    private final TodoService todoService = new TodoService();
    Todo body = TestDataGenerator.generateRandomTodo();

    @BeforeEach
    void setupTestData() {
        // some code
    }

    @AfterEach
    void cleanTestData() {
        List<String> todoIds = todoService.read().stream().map(todo -> todo.getId()).toList();
        if (!todoIds.isEmpty()) {
            todoService.getTodoIds().forEach(id -> todoService.delete(id));
        }
    }

    @Test
    void getTodos() {
        todoService.create(body);
        todoService.read();
    }

    @Test
    void postTodo() {
        todoService.create(body);

        Todo actualTodo = todoService.read().getFirst();

        assertThat(actualTodo).isEqualTo(body);

    }

    @Test
    void updateTodo() {
        var id = body.getId();
        var updatedBody = TestDataGenerator.generateRandomTodo(id);

        todoService.create(body);
        todoService.update(updatedBody);
        var actualTodo = todoService.read().getFirst();
        assertThat(actualTodo).isEqualTo(updatedBody);
    }

    @Test
    void updateTodoWithWrongId_NegativeTest() {
        var id = TestDataGenerator.generateRandomId();
        while (id.equals(body.getId())) {
            id = TestDataGenerator.generateRandomId();
        }

        var updatedBody = TestDataGenerator.generateRandomTodo();
        todoService.create(body);
        RestAssured.given()
                .spec(todoService.getUnauthReqSpecs())
                .body(updatedBody)
                .when()
                .put(TODO_URL + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
        var actualTodo = todoService.read().getFirst();
        assertThat(actualTodo).isEqualTo(updatedBody);
    }

    @Test
    void deleteTodo() {
        var id = body.getId();

        todoService.create(body);
        todoService.delete(id);

        var actualTodos = todoService.read();

        assert(actualTodos.isEmpty());
    }

    @Test
    void deleteWithWrongId_NegativeTest() {
        var id = TestDataGenerator.generateRandomId();
        while (id.equals(body.getId())) {
            id = TestDataGenerator.generateRandomId();
        }

        todoService.create(body);

        RestAssured.given()
                .spec(todoService.getAuthReqSpecs())
                .when()
                .delete(TODO_URL + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);


        var actualTodos = todoService.read();

        assert(!actualTodos.isEmpty());
    }

    @Test
    void deleteWithounAuth_NegativeTest() {
        var id = body.getId();
        todoService.create(body);

        RestAssured.given()
                .spec(todoService.getUnauthReqSpecs())
                .when()
                .delete(TODO_URL + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);


        var actualTodos = todoService.read();

        assert(!actualTodos.isEmpty());
    }
}
