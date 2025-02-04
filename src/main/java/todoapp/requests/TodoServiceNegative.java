package todoapp.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import todoapp.models.Todo;
import todoapp.utils.TodoUtils;

import java.util.List;

public class TodoServiceNegative implements TodoCrudInterface {
    private final static String TODO_URL = "http://localhost:8080/todos";


    @Override
    public void create(Todo todo) {
        var body = TodoUtils.todoSerialize(todo);
        RestAssured.given()
                .spec(ReqSpecs.unauthReqSpecs)
                .body(body)
                .when()
                .post(TODO_URL);
    }

    @Override
    public List<Todo> read() {
        return List.of();
    }

    @Override
    public List<Todo> read(Integer offset, Integer limit) {
        return List.of();
    }

    @Override
    public void update(String id, Todo todo) {
        var body = TodoUtils.todoSerialize(todo);

        RestAssured.given()
                .spec(ReqSpecs.unauthReqSpecs)
                .body(body)
                .when()
                .put(TODO_URL + "/" + id);
    }

    @Override
    public void delete(String id) {
        RestAssured.given()
                .spec(ReqSpecs.authReqSpecs)
                .when()
                .delete(TODO_URL + "/" + id);
    }
}
