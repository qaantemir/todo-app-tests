package todoapp.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import org.apache.http.HttpStatus;
import todoapp.models.Todo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class TodoService implements TodoCrudInterface {
    private final static String BASE_URL = "http://localhost:8080";
    private final static String TODO_ENDPOINT = "/todos";
    private final static String TODO_URL = BASE_URL + TODO_ENDPOINT;

    private final ObjectMapper mapper = new ObjectMapper();

    private List<String> todoIds = new ArrayList<>();


    private final RequestSpecification unauthReqSpecs = RestAssured.given()
            .contentType(ContentType.JSON);
    private final RequestSpecification authReqSpecs = RestAssured.given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin");

    @Override
    public void create(Todo todo) {
        var body = this.todoSerialize(todo);
        RestAssured.given()
                .spec(unauthReqSpecs)
                .body(body)
                .when()
                .post(TODO_URL)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
        todoIds.add(todo.getId());
    }

    @Override
    public ArrayList<Todo> read() {
        List<Todo> todoList = RestAssured.given()
                .spec(unauthReqSpecs)
                .when()
                .get(TODO_URL)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().jsonPath().getList("", Todo.class);
        return new ArrayList<>(todoList);
    }

    @Override
    public void update(Todo todo) {
        var id = todo.getId();
        var body = this.todoSerialize(todo);
        RestAssured.given()
                .spec(unauthReqSpecs)
                .body(body)
                .when()
                .put(TODO_URL + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Override
    public void delete(String id) {
        RestAssured.given()
                .spec(authReqSpecs)
                .when()
                .delete(TODO_URL + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    public void deleteAllTodos() {
        var todos = this.read();
//        if (todos != null && !todos.isEmpty()) todos.forEach(todo -> this.delete(String.valueOf(todo.getId())));
        System.out.println(todos);
        System.out.println(todos != null);
        System.out.println(!todos.isEmpty());
        if (todos != null && !todos.isEmpty()) {
            for (var todo : todos) {
                this.delete(todo.getId());
            }
        }

    }

    private String todoSerialize(Todo todo) {
        var id = todo.getId();
        var text = todo.getText();
        var todoCompleted = todo.getCompleted();

        return """
                {
                    \"id\": %s,
                    \"text\": \"%s\",
                    \"completed\": %s
                }
                """.formatted(id, text, todoCompleted);
    }

    private Todo todoDeserialize(String body) throws JsonProcessingException {
        return mapper.readValue(body, Todo.class);
    }
}
