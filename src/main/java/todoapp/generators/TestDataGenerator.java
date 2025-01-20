package todoapp.generators;

import todoapp.models.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDataGenerator {
    private static final Random RANDOM = new Random();

    public static Todo generateRandomTodo() {
        String id = String.valueOf((long) RANDOM.nextInt(1000)); // Генерация случайного ID
        String text = "Todo " + id; // Генерация заголовка
        boolean completed = RANDOM.nextBoolean(); // Случайное значение для completed
        return new Todo(id, text, completed);
    }

    public static Todo generateRandomTodo(String id) {
        String text = "Todo " + id; // Генерация заголовка
        boolean completed = RANDOM.nextBoolean(); // Случайное значение для completed
        return new Todo(id, text, completed);
    }

    public static List<Todo> generateRandomTodos(int count) {
        List<Todo> todos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            todos.add(generateRandomTodo());
        }
        return todos;
    }
}
