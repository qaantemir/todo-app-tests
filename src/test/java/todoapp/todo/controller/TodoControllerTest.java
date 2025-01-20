package todoapp.todo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todoapp.models.Todo;
import todoapp.requests.TodoService;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class TodoControllerTest extends BaseApiTest {

    private final TodoService todoService = new TodoService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setupTestData() {
        todoService.deleteAllTodos();
    }

    @Test
    void getTodos() {
        var result = todoService.read();
    }

    @Test
    void postTodo() {
        Todo todo = Todo.builder()
                .id(111)
                .text("Eat milk")
                .completed(false)
                .build();

        todoService.create(todo);
        Todo actualTodo = todoService.read().getFirst();

        assertThat(actualTodo).isEqualTo(todo);

    }

    @Test
    void updateTodo() {
        var body = Todo.builder()
                .id(111)
                .text("Eat milk")
                .completed(false)
                .build();


        var updatedBody = Todo.builder()
                .id(111)
                .text("Eat milk")
                .completed(true)
                .build();

        todoService.create(body);
        todoService.update(updatedBody);
        var actualTodo = todoService.read().getFirst();
        assertThat(actualTodo).isEqualTo(updatedBody);
    }
}
