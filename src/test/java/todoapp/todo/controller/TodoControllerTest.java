package todoapp.todo.controller;

import org.junit.jupiter.api.*;
import todoapp.generators.TestDataGenerator;
import todoapp.models.Todo;
import todoapp.requests.TodoService;
import todoapp.requests.TodoServiceNegative;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



public class TodoControllerTest extends BaseApiTest {

    private final TodoService todoService = new TodoService();
    private final TodoServiceNegative todoServiceNegative = new TodoServiceNegative();
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
    void getTodosWithQueryParams() {
        for (int i = 0; i < 5; i++) {
            todoService.create(TestDataGenerator.generateRandomTodo());
        }
        todoService.read(2, 2);
    }

    @Test
    void postTodo() {
        todoService.create(body);

        Todo actualTodo = todoService.read().getFirst();

        assertThat(actualTodo).isEqualTo(body);

    }

    @Test
    void postTodoWithZeroId() {
        body.setId("0");
        todoService.create(body);

        Todo actualTodo = todoService.read().getFirst();

        assertThat(actualTodo).isEqualTo(body);

    }

    @Test
    void postTodoWithMaxId() {
        body.setId("18446744073709551615");
        todoService.create(body);

        Todo actualTodo = todoService.read().getFirst();

        assertThat(actualTodo).isEqualTo(body);

    }

    @Test
    void postTodoWithEmptyTextId() {
        body.setText("");
        todoService.create(body);

        Todo actualTodo = todoService.read().getFirst();

        assertThat(actualTodo).isEqualTo(body);

    }

    @Test
    void updateTodo() {
        var id = body.getId();
        var updatedBody = TestDataGenerator.generateRandomTodo(id);

        todoService.create(body);
        todoService.update(id, updatedBody);
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
    void updateTodoWithWrongId_NegativeTest() {
        var id = TestDataGenerator.generateRandomId();
        while (id.equals(body.getId())) {
            id = TestDataGenerator.generateRandomId();
        }

        var updatedBody = TestDataGenerator.generateRandomTodo();

        todoService.create(body);

        todoServiceNegative.update(id, updatedBody);

        var actualTodo = todoService.read().getFirst();
        assertThat(actualTodo).isNotEqualTo(updatedBody);
    }

    @Test
    void deleteWithWrongId_NegativeTest() {
        var id = TestDataGenerator.generateRandomId();
        while (id.equals(body.getId())) {
            id = TestDataGenerator.generateRandomId();
        }

        todoService.create(body);
        todoServiceNegative.delete(id);

        var actualTodos = todoService.read();

        assert(!actualTodos.isEmpty());
    }


    @Test
    void postWithMinusId_NegativeTest() {
        var body = TestDataGenerator.generateRandomTodo();
        body.setId("-1");

        todoServiceNegative.create(body);

        var actualTodo = todoService.read();

        assert (actualTodo.isEmpty());
    }

    @Test
    void postWithOverId_NegativeTest() {
        body.setId("18446744073709551616");
        todoServiceNegative.create(body);

        var actualTodo = todoService.read();

        assert (actualTodo.isEmpty());

    }

    @Test
    void postWithSymbolId_NegativeTest() {
        body.setId("abc");
        todoServiceNegative.create(body);

        var actualTodo = todoService.read();

        assert (actualTodo.isEmpty());

    }

    @Test
    void postWithFloatId_NegativeTest() {
        body.setId("1.1");
        todoServiceNegative.create(body);

        var actualTodo = todoService.read();

        assert (actualTodo.isEmpty());

    }


}
