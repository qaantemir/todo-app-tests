package todoapp.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import org.apache.http.HttpStatus;
import todoapp.models.Todo;
import todoapp.utils.TodoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class TodoService implements TodoCrudInterface {
    private final static String TODO_URL = "http://localhost:8080/todos";

    private List<String> todoIds = new ArrayList<>();


    @Override
    public void create(Todo todo) {
        var body = TodoUtils.todoSerialize(todo);
        RestAssured.given()
                .spec(ReqSpecs.unauthReqSpecs)
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
                .spec(ReqSpecs.unauthReqSpecs)
                .when()
                .get(TODO_URL)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().jsonPath().getList("", Todo.class);
        return new ArrayList<>(todoList);
    }

    @Override
    public ArrayList<Todo> read(Integer offset, Integer limit) {
        List<Todo> todoList = RestAssured.given()
                .spec(ReqSpecs.unauthReqSpecs)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(TODO_URL)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().jsonPath().getList("", Todo.class);
        return new ArrayList<>(todoList);
    }

    @Override
    public void update(String id,Todo todo) {
        var body = TodoUtils.todoSerialize(todo);
        RestAssured.given()
                .spec(ReqSpecs.unauthReqSpecs)
                .body(body)
                .when()
                .put(TODO_URL + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Override
    public void delete(String id) {
        RestAssured.given()
                .spec(ReqSpecs.authReqSpecs)
                .when()
                .delete(TODO_URL + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
