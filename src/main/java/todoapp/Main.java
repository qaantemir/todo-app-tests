package todoapp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import todoapp.models.Todo;
import todoapp.requests.TodoService;

import java.util.List;

public class Main {
    private static String todoSerialize(Todo todo) {
        var id = todo.getId();
        var text = todo.getText();
        var todoCompleted = todo.getCompleted();

        return """
                {
                    \"id\": %d,
                    \"text\": \"Eat milk\",
                    \"completed\": false
                }
                """.formatted(id, text, todoCompleted);
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Todo todo = Todo.builder()
                .id("111")
                .text("Eat milk")
                .completed(false)
                .build();

        TodoService todoService = new TodoService();
        todoService.create(todo);

        System.out.println(todoService.read());
        todoService.delete("111");


    }
}