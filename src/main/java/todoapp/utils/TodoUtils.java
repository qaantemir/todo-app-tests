package todoapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import todoapp.models.Todo;

public class TodoUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String todoSerialize(Todo todo) {
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

    public static Todo todoDeserialize(String body) throws JsonProcessingException {
        return mapper.readValue(body, Todo.class);
    }
}
